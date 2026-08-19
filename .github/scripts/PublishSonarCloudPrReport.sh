#!/usr/bin/env bash
set -euo pipefail

SONAR_API_URL="${SONAR_HOST_URL%/}"
REPORT_TASK_FILE="$(find . -type f -path '*/target/sonar/report-task.txt' -print -quit)"
if [[ -z "${REPORT_TASK_FILE}" ]]; then
  REPORT_TASK_FILE="$(find . -type f -name report-task.txt -print -quit)"
fi
REPORT_MARKER='<!-- sonarcloud-pr-report -->'

if [[ -z "${REPORT_TASK_FILE}" ]]; then
  echo "::error::SonarCloud report-task.txt was not found."
  exit 1
fi

get_report_property() {
  local property_name="$1"
  awk -F= -v property_name="${property_name}" '
    $1 == property_name { print substr($0, index($0, "=") + 1); exit }
  ' "${REPORT_TASK_FILE}"
}

SONAR_CE_TASK_URL="$(get_report_property "ceTaskUrl")"
SONAR_DASHBOARD_URL="$(get_report_property "dashboardUrl")"

if [[ -z "${SONAR_CE_TASK_URL}" ]]; then
  echo "::error::ceTaskUrl is missing from ${REPORT_TASK_FILE}."
  exit 1
fi

SONAR_ANALYSIS_ID=""
for attempt in $(seq 1 60); do
  TASK_JSON="$(curl --fail-with-body --silent --show-error --retry 3 \
    --retry-delay 2 --user "${SONAR_TOKEN}:" "${SONAR_CE_TASK_URL}")"
  SONAR_TASK_STATUS="$(jq -r '.task.status // empty' <<<"${TASK_JSON}")"

  case "${SONAR_TASK_STATUS}" in
    SUCCESS)
      SONAR_ANALYSIS_ID="$(jq -r '.task.analysisId // empty' <<<"${TASK_JSON}")"
      break
      ;;
    FAILED|CANCELED)
      echo "::error::SonarCloud analysis task ended with ${SONAR_TASK_STATUS}."
      jq -r '.task.errorMessage // empty' <<<"${TASK_JSON}" >&2
      exit 1
      ;;
    PENDING|IN_PROGRESS)
      echo "Waiting for SonarCloud analysis (${attempt}/60, status=${SONAR_TASK_STATUS})."
      sleep 10
      ;;
    *)
      echo "::error::Unexpected SonarCloud task status: ${SONAR_TASK_STATUS:-empty}."
      exit 1
      ;;
  esac
done

if [[ -z "${SONAR_ANALYSIS_ID}" ]]; then
  echo "::error::Timed out waiting for the SonarCloud analysis task."
  exit 1
fi

QUALITY_GATE_JSON="$(curl --fail-with-body --silent --show-error --retry 3 \
  --retry-delay 2 --user "${SONAR_TOKEN}:" --get \
  --data-urlencode "analysisId=${SONAR_ANALYSIS_ID}" \
  "${SONAR_API_URL}/api/qualitygates/project_status")"
ISSUES_JSON="$(curl --fail-with-body --silent --show-error --retry 3 \
  --retry-delay 2 --user "${SONAR_TOKEN}:" --get \
  --data-urlencode "componentKeys=${SONAR_PROJECT_KEY}" \
  --data-urlencode "pullRequest=${PR_NUMBER}" \
  --data-urlencode "resolved=false" \
  --data-urlencode "ps=100" \
  "${SONAR_API_URL}/api/issues/search")"

QUALITY_GATE_STATUS="$(jq -r '.projectStatus.status // "UNKNOWN"' <<<"${QUALITY_GATE_JSON}")"
ISSUE_TOTAL="$(jq -r '.total // 0' <<<"${ISSUES_JSON}")"
ISSUE_SEVERITIES="$(jq -r '
  [.issues[]? | (.severity // "UNKNOWN")]
  | group_by(.)
  | map("\(.[0]): \(length)")
  | join(", ")
' <<<"${ISSUES_JSON}")"
ISSUE_SEVERITIES="${ISSUE_SEVERITIES:-none}"
SONAR_DASHBOARD_URL="${SONAR_DASHBOARD_URL:-${SONAR_API_URL}/dashboard?id=${SONAR_PROJECT_KEY}&pullRequest=${PR_NUMBER}}"

CONDITION_ROWS="$(jq -r '
  .projectStatus.conditions[]?
  | "| `\(.metricKey)` | \(.status // "-") | \(.actualValue // "-") | \(.errorThreshold // "-") |"
' <<<"${QUALITY_GATE_JSON}")"
CONDITION_ROWS="${CONDITION_ROWS:-| - | - | - | - |}"

ISSUE_ROWS="$(jq -r --arg project "${SONAR_PROJECT_KEY}" '
  .issues[0:100][]?
  | .key as $key
  | (.component | sub("^" + $project + ":"; "")) as $path
  | (.textRange.startLine // .line // "-") as $line
  | (.severity // (.impacts[0].severity // "UNKNOWN")) as $severity
  | (.message | gsub("[\r\n]+"; " ") | gsub("\\|"; "\\\\|") | .[0:240]) as $message
  | "| [`\($key)`](https://sonarcloud.io/project/issues?id=\($project)&open=\($key)) | `\(.rule)` | \($severity) | `\($path):\($line)` | \($message) |"
' <<<"${ISSUES_JSON}")"
if [[ -z "${ISSUE_ROWS}" ]]; then
  ISSUE_ROWS="| - | - | - | - | No open issues found for this pull request. |"
fi

ISSUE_LIMIT_NOTE=""
if (( ISSUE_TOTAL > 100 )); then
  ISSUE_LIMIT_NOTE="Only the first 100 issues are shown to keep the GitHub comment within its size limit."
fi

REPORT_FILE="$(mktemp)"
trap 'rm -f "${REPORT_FILE}"' EXIT
cat >"${REPORT_FILE}" <<EOF
${REPORT_MARKER}
## SonarQube Cloud

**Quality Gate:** **${QUALITY_GATE_STATUS}**  
**Issues:** ${ISSUE_TOTAL} (${ISSUE_SEVERITIES})  
**Analysis:** [View details](${SONAR_DASHBOARD_URL})

### Quality Gate Conditions

| Condition | Status | Actual | Threshold |
| --- | --- | ---: | ---: |
${CONDITION_ROWS}

### Pull Request Issues

| Issue | Rule | Severity | Location | Message |
| --- | --- | --- | --- | --- |
${ISSUE_ROWS}

${ISSUE_LIMIT_NOTE}

_This comment is updated by the SonarCloud workflow after each PR analysis._
EOF

GITHUB_HEADERS=(
  --header "Accept: application/vnd.github+json"
  --header "Authorization: Bearer ${GITHUB_TOKEN}"
  --header "X-GitHub-Api-Version: 2022-11-28"
)
COMMENTS_URL="${GITHUB_API_URL}/repos/${GITHUB_REPOSITORY}/issues/${PR_NUMBER}/comments"
COMMENT_ID=""
for page in $(seq 1 10); do
  COMMENTS_JSON="$(curl --fail-with-body --silent --show-error --retry 3 \
    "${GITHUB_HEADERS[@]}" "${COMMENTS_URL}?per_page=100&page=${page}")"
  COMMENT_ID="$(jq -r --arg marker "${REPORT_MARKER}" '
    [.[] | select((.body // "") | contains($marker))][0].id // empty
  ' <<<"${COMMENTS_JSON}")"
  if [[ -n "${COMMENT_ID}" ]]; then
    break
  fi
  COMMENT_COUNT="$(jq 'length' <<<"${COMMENTS_JSON}")"
  if (( COMMENT_COUNT < 100 )); then
    break
  fi
done
REPORT_PAYLOAD="$(jq -n --rawfile body "${REPORT_FILE}" '{body: $body}')"

if [[ -n "${COMMENT_ID}" ]]; then
  curl --fail-with-body --silent --show-error --retry 3 \
    "${GITHUB_HEADERS[@]}" --request PATCH \
    --header "Content-Type: application/json" \
    --data "${REPORT_PAYLOAD}" \
    "${GITHUB_API_URL}/repos/${GITHUB_REPOSITORY}/issues/comments/${COMMENT_ID}" \
    >/dev/null
  echo "Updated SonarCloud PR comment ${COMMENT_ID}."
else
  curl --fail-with-body --silent --show-error --retry 3 \
    "${GITHUB_HEADERS[@]}" --request POST \
    --header "Content-Type: application/json" \
    --data "${REPORT_PAYLOAD}" \
    "${COMMENTS_URL}" \
    >/dev/null
  echo "Created SonarCloud PR comment."
fi

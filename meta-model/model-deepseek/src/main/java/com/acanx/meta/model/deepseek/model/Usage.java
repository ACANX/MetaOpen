package com.acanx.meta.model.deepseek.model;

public class Usage {

    /**
     * 序列化后的字段：prompt_tokens
     */
    private Integer promptTokens;

    /**
     * 序列化后的字段：completion_tokens
     */
    private Integer completionTokens;

    /**
     * 序列化后的字段：reasoning_content
     */
    private Integer totalTokens;

    /**
     * 序列化后的字段：prompt_cache_hit_tokens
     */
    private Integer promptCacheHitTokens;

    /**
     * 序列化后的字段：prompt_cache_miss_tokens
     */
    private Integer promptCacheMissTokens;

    public Usage() {
    }



    public Integer getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(Integer promptTokens) {
        this.promptTokens = promptTokens;
    }

    public Integer getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(Integer completionTokens) {
        this.completionTokens = completionTokens;
    }

    public Integer getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }

    public Integer getPromptCacheHitTokens() {
        return promptCacheHitTokens;
    }

    public void setPromptCacheHitTokens(Integer promptCacheHitTokens) {
        this.promptCacheHitTokens = promptCacheHitTokens;
    }

    public Integer getPromptCacheMissTokens() {
        return promptCacheMissTokens;
    }

    public void setPromptCacheMissTokens(Integer promptCacheMissTokens) {
        this.promptCacheMissTokens = promptCacheMissTokens;
    }
}

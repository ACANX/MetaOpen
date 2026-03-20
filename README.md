# MetaOpen

Meta Open Source Components




# 编译

```aiignore
mvn clean install -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 --add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED

# 编译时的VM参数
-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 --add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED

```
# Referance
- [Meta-Open Overview - Sonatype](https://central.sonatype.com/artifact/com.acanx.meta/meta-open/overview)
- [meta-open - MvnRepository](https://mvnrepository.com/artifact/com.acanx.meta/meta-open)
- [os-dependencies - MvnRepository](https://mvnrepository.com/artifact/com.acanx.meta/os-dependencies)

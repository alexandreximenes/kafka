# ---------------- Build Stage ----------------
FROM bellsoft/liberica-runtime-container:jdk-21-musl AS build
WORKDIR /app

# Instala ferramentas necessárias
RUN apk add --no-cache maven bash curl unzip git coreutils

# Copia o POM para cache de dependências
COPY pom.xml .

# Baixa dependências sem falhar caso ainda não haja nada
RUN mvn dependency:go-offline -B

# Copia o código-fonte
COPY src ./src

# Build do JAR sem rodar testes
RUN mvn clean install -DskipTests -B

# ---------------- Runtime Stage ----------------
FROM bellsoft/liberica-runtime-container:jre-21-slim-musl
WORKDIR /app

# Cria usuário não-root
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Copia o JAR final
COPY --from=build /app/target/*.jar app.jar

USER spring

ENTRYPOINT ["java", "-Duser.timezone=America/Sao_Paulo", "-Dfile.encoding=UTF-8", \
 "-XX:HeapDumpPath=/log/dumps", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:+UseG1GC", \
 "-Xms128m", "-Xmx512m", "-XX:MetaspaceSize=128m", "-XX:MaxMetaspaceSize=256m", \
 "-XX:MaxRAMPercentage=75.0", "-XX:InitialRAMPercentage=50.0", "-jar", "app.jar"]
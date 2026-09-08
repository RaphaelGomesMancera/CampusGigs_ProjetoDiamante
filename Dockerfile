# -----------------------------------------------------------------------------
# Dockerfile
# -----------------------------------------------------------------------------
# Build multi-stage: a imagem final não carrega o Maven nem o código-fonte,
# só o jar já compilado — mais leve e mais rápido para subir.
# -----------------------------------------------------------------------------

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Copia só o pom.xml primeiro para o Docker cachear as dependências
# e não baixar tudo de novo a cada alteração de código.
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

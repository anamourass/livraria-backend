# =========================
# Etapa de build (compilação com Maven)
# =========================
FROM eclipse-temurin:17-jdk-jammy AS build

# Define diretório de trabalho
WORKDIR /app

# Copia arquivos de configuração do Maven
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw .

# Dá permissão de execução para o Maven wrapper
RUN chmod +x mvnw

# Baixa dependências (para aproveitar cache)
RUN ./mvnw dependency:resolve

# Copia o código-fonte
COPY src ./src

# Compila e empacota a aplicação (sem rodar testes)
RUN ./mvnw clean package -DskipTests


# =========================
# Etapa de runtime (execução)
# =========================
FROM eclipse-temurin:17-jre-jammy

# Cria um usuário não-root para segurança
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Define diretório de trabalho
WORKDIR /app

# Copia o jar gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Define permissões
RUN chown -R spring:spring /app

# Troca para o usuário não-root
USER spring

# Expõe a porta do app
EXPOSE 8080

# Health check (Render ignora, mas é bom ter localmente)
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de execução da aplicação
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]

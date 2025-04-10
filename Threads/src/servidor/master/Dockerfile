# Etapa 1: Compilação
FROM openjdk:17-slim AS build
LABEL authors="Pedro"
WORKDIR /app

# Copia o código-fonte
COPY . .

# Compila o código
RUN javac Master.java -d out/

# Etapa 2: Execução
FROM openjdk:17-slim
WORKDIR /app

# Copia os arquivos compilados da etapa de build
COPY --from=build /app/out/ ./out/

# Expõe a porta do mestre
EXPOSE 5000

# Executa o servidor
CMD ["java", "-cp", "out", "master.Master"]

FROM openjdk:17-slim AS build
WORKDIR /app

COPY SlaveNumber.java slaveNumbers/SlaveNumber.java

RUN javac slaveNumbers/SlaveNumber.java

FROM openjdk:17-slim
WORKDIR /app

COPY --from=build /app/slaveNumbers/ slaveNumbers/

EXPOSE 8002

CMD ["java", "slaveNumbers.SlaveNumber"]

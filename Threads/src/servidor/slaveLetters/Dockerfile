FROM openjdk:17-slim AS build
WORKDIR /app

COPY SlaveLetter.java slaveLetters/SlaveLetter.java

RUN javac slaveLetters/SlaveLetter.java

FROM openjdk:17-slim
WORKDIR /app

COPY --from=build /app/slaveLetters/ slaveLetters/

EXPOSE 8001

CMD ["java", "slaveLetters.SlaveLetter"]

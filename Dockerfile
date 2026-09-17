FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY Jogo.java .

RUN javac Jogo.java

CMD ["java", "Jogo"]

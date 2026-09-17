FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY Servidor.java .

RUN javac Servidor.java

CMD ["java", "Servidor"]

FROM openjdk:23-jdk-slim
WORKDIR /app
COPY build/libs/JobPortal-0.0.1.jar /app/app.jar
COPY ./wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
EXPOSE 8080
CMD ["/app/wait-for-it.sh", "db:5432", "--", "java", "-jar", "/app/app.jar"]

FROM openjdk:23-jdk-slim
WORKDIR /app
COPY build/libs/JobPortal-0.9.jar /app/JobPortal-0.9.jar
COPY ./wait-for-it.sh /app/wait-for-it.sh
RUN chmod 0744 /app/wait-for-it.sh
EXPOSE 8080
CMD ["/app/wait-for-it.sh", "db:5432", "--", "java", "-jar", "/app/JobPortal-0.9.jar"]

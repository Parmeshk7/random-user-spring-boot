FROM openjdk:17
COPY /var/lib/jenkins/workspace/SpringBoot-CI-CD/target/.*jar app.jar
CMD ["java", "-jar", "app.jar"]

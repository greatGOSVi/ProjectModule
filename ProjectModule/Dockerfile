FROM openjdk:11
EXPOSE 8090
ADD target/project-module.jar project-module.jar
ENTRYPOINT ["java", "-jar", "/project-module.jar"]

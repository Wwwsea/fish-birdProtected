# Docker 镜像构建
FROM openjdk:17 as builder
WORKDIR /app

COPY birdProducted-backend-0.0.1-SNAPSHOT.jar /app/birdProducted-backend-0.0.1-SNAPSHOT.jar

# Run the web service on container startup.
CMD ["java","-Duser.timezone=GMT+8","-jar","/app/user-center-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]

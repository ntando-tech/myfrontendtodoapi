FROM eclipse-temurin:21-alpine

#COPY . .
#RUN mvn clean package -DskipTests

##create a nonroot user and group
RUN addgroup -S spring && adduser -S spring -G spring

##set the nonroot user as the default user
USER spring:spring

##set the working directory
WORKDIR /opt

COPY target/*.jar api.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","api.jar"]

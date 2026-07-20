#FROM eclipse-temurin:21-alpine
#
##COPY . .
##RUN mvn clean package -DskipTests
#
###create a nonroot user and group
#RUN addgroup -S spring && adduser -S spring -G spring
#
###set the nonroot user as the default user
#USER spring:spring
#
###set the working directory
#WORKDIR /opt
#
#COPY target/*.jar api.jar
#EXPOSE 8081
#ENTRYPOINT ["java","-jar","api.jar"]


# Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app
COPY . .
RUN ./mvnw package

# Run stage
FROM eclipse-temurin:21-jdk-alpine AS runner

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]
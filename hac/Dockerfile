
#FROM paketobuildpacks/builder:latest as builder
FROM openjdk:17

WORKDIR /workspace

COPY . .

USER root
RUN mkdir -p /uploads

# Set permissions on the uploads directory
RUN chmod 777 /uploads


#RUN mkdir -p /uploads
#RUN chmod 777 /uploads

# Build your application
RUN ./mvnw package

# Run the application
CMD ["java", "-jar", "target/hac-0.0.1-SNAPSHOT.jar"]

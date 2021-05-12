FROM openjdk:8-jdk-alpine
ADD ./target/demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar
EXPOSE 8080
#CMD ["java","-jar","demo-0.0.1-SNAPSHOT.jar"]
ADD src/main/resources/netflix_titles1.csv netflix_titles1.csv
ENTRYPOINT ["java","-jar","demo-0.0.1-SNAPSHOT.jar"]

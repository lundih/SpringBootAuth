FROM azul/zulu-openjdk-alpine:11
VOLUME /tmp
COPY /build/libs/auth-server.jar /app.jar
EXPOSE 8091
ENTRYPOINT ["java","-jar", "/app.jar"]
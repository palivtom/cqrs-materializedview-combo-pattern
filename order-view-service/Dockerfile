FROM alpine:latest

# Add open JDK 17
RUN apk update && apk upgrade && apk --update add openjdk17 tini && rm -rf /tmp/* /var/cache/apk/*

# Add user for the application
RUN addgroup spring && adduser -D -u 666 -G spring spring

# Copy jar and entrypoint shell script into docker image
ARG JAR_FILE=build/libs/*.jar
COPY --chown=spring:spring ${JAR_FILE} /app.jar
COPY --chown=spring:spring entrypoint.sh /entrypoint.sh

# Add execution rights to entrypoint
RUN chmod +x /entrypoint.sh

USER spring:spring
CMD ["/sbin/tini", "/entrypoint.sh"]
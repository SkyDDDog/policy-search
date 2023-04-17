FROM java:8

MAINTAINER Sky_Dog<362664609@qq.com>

COPY *.jar /app.jar

VOLUME ["/log"]

CMD ["--server.port=4000"]

EXPOSE 12345

ENTRYPOINT ["java","-jar","/app.jar"]
FROM  mskj/java8-base

ENV AppName="smart-job" JARGcLogPath="/data/logs/${AppName}"  Target="smart-job-web/target"  JarName="smart-job-1.0.0.jar"

#captcher 字体包
RUN set -xe \
&& apk add ttf-dejavu fontconfig

WORKDIR /data

COPY docker-entrypoint.sh .

COPY ${Target}/${JarName} .

CMD ["sh","+x","docker-entrypoint.sh"]

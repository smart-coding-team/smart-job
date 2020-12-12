FROM  mskj/java8-base

ENV AppName="smart-job" JARGcLogPath="/data/logs/${AppName}"  Target="smart-job-web/target"  JarName="${AppName}-1.0.0.jar"

WORKDIR /springboot

COPY docker-entrypoint.sh .

COPY ${Target}/${JarName} .

CMD ["sh","+x","docker-entrypoint.sh"]

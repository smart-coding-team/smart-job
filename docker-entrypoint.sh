#!/bin/bash
PID=springboot.pid
JAVA_OPTS=" -Duser.timezone=Asia/Shanghai -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"
JAVA_OPTS="${JAVA_OPTS} -verbose:gc -Xloggc:${GcLogPath}/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:ErrorFile=${GcLogPath}/hs_err_pid_%p.log"
JAVA_MEM_OPTS=" -XX:+UseG1GC -Xms${JAVA_XMX-512m} -Xmx${JAVA_XMX-512m} -Xmn${JAVA_XMN-256m} -XX:+AlwaysPreTouch -XX:-UseBiasedLocking -XX:G1HeapRegionSize=16m"
JAVA_OPTS="$JAVA_OPTS $JAVA_MEM_OPTS"
SPRING_OPTS=" --spring.profiles.active=${ACTIVE_PROFILE-prod}"

function echoPlus(){
 # 增强显示
 # @param color (31|91 红 32|92绿 33|93黄 34|94蓝 35|95紫 36|96天蓝)
 # @param content
 local colorCode=$1
 local content=$2

 echo -e "\e[${colorCode}m${content}\e[0m"
}


echoPlus 33 "[check] 即将启动Springboot......"


java ${JAVA_OPTS} -jar ${JarName} ${SPRING_OPTS}

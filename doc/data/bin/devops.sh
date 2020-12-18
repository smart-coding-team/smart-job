#!/bin/bash

#===============================================================================
#   SYSTEM REQUIRED:  Linux Centos7
#   DESCRIPTION:  docker 容器部署
#   AUTHOR: wuque
#===============================================================================

stty erase '^H'

function echoPlus(){
  # 增强显示
  # @param color  (31|91 红  32|92绿  33|93黄 34|94蓝 35|95紫 36|96天蓝)
  # @param content
  local colorCode=$1
  local content=$2

  echo -e "\e[${colorCode}m${content}\e[0m"
}

function log_error() {
    echo -e "\033[31m [ERROR] $@ \033[0m"
}

function log_info() {
    echo -e "\033[32m [INFO] $@ \033[0m"
}

#加载env
SHHOME=$(pwd)
BASE_DIR=$(cd `dirname $0`/..; pwd)
cp -f ${BASE_DIR}/bin/.env ${BASE_DIR}/docker-compose/.env
if [ -r "${BASE_DIR}/docker-compose/.env" ]; then
    . ${BASE_DIR}/docker-compose/.env
else
    log_error "${BASE_DIR}/docker-compose/.env not found"
    exit 1 ;
fi

#加载check环境监测脚本
SHHOME=$(pwd)
BASE_DIR=$(cd `dirname $0`/..; pwd)
if [ -r "${SHHOME}/check.sh" ]; then
    . ${SHHOME}/check.sh
else
    log_error "${SHHOME}/check.sh not found"
    exit 1 ;
fi


function Devops() {

  case "$1" in

    *)
             (UpdateImages $@)
        ;;

  esac

}


function updateImages() {
  if [ $# -gt 0 ] ;then
    for imgName in $@; do
            ${BASE_DIR}/docker-compose/docker-compose -f ${BASE_DIR}/docker-compose/docker-java.yml stop ${imgName}
            ${BASE_DIR}/docker-compose/docker-compose -f ${BASE_DIR}/docker-compose/docker-java.yml rm -f ${imgName}
            ${BASE_DIR}/docker-compose/docker-compose -f ${BASE_DIR}/docker-compose/docker-java.yml up -d ${imgName}
    done
  fi
}

function smart-job-ui (){
  cd ${BASE_DIR}/nginx/html/job-html/
  ls ${BASE_DIR}/nginx/html/job-html/  | xargs rm -r -f
  [ ! -f ${BASE_DIR}/nginx/html/job-html/smart-job-ui.tar.gz ] && log_error "指定文件不存在:${BASE_DIR}/nginx/html/job-html/smart-job-ui.tar.gz" && exit 1 ;
  tar zxvf ${BASE_DIR}/nginx/html/job-html/smart-job-ui*.tar.gz -C ${BASE_DIR}/nginx/html/job-html/
}

case "$1" in

    smart-job-ui)
            (smart-job-ui)
        ;;
    *)
             (updateImages $@)
        ;;

esac
if [ $# -eq 0 ]; then
cat << EOF
+-------------------------------------------------+
                  容器更新
请按需加载服务，例如：
    *  前端应用： smart-job-ui
    *  后端应用： smart-job
    *  中间件：nginx  redis  mysql

+-------------------------------------------------+
EOF
    echo -n "请输入:"
    read module
fi

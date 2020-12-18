#!/bin/bash
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


function log_error() {
    echo -e "\033[31m [ERROR] $@ \033[0m"
}

function log_info() {
    echo -e "\033[32m [INFO] $@ \033[0m"
}

docker images > /dev/null 2>&1
if [ $? -ne 0 ];then
    log_error "执行Docker命令失败"
    log_error "[可能原因1] Docker未启动， systemctl start docker.service"
    log_error "[可能原因2] 权限不够，使用sudo 或者 将该用户加入docker用户组 sudo gpasswd -a \${USER} docker"
    exit 1
fi

function load() {
  if [ $# -gt 0 ] ;then
    for app in $@; do
        for imgName in `find $BASE_DIR/docker-images -name "$app.tar"`
        do
            echo "load $imgName"
            docker load --input $imgName
        done
        #docker load --input $BASE_DIR/images/$app.tar
    done
  else
    for imgName in `find $BASE_DIR/docker-images -name "*.tar"`
    do
        echo "load $imgName"
        docker load --input $imgName
    done
  fi
}

if [ $# -gt 0 ]; then
    load $@
else
cat << EOF
+-------------------------------------------------+
                  容器初始化
请按需加载服务，例如：
    * 加载单独服务 则输入对应镜像名 比如 smart-job


+-------------------------------------------------+
EOF
    echo -n "请输入:"
    read module
    load $module
fi

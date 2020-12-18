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


function check() {
   docker exec -it nginx /bin/bash -c 'cd sbin/  && ./nginx -t'
}

function load() {

   docker exec -it nginx /bin/bash -c 'cd sbin/  && ./nginx -s reload'
}
case "$1" in

    -t)
             (check )
              ;;
    -s)
             (load )
        ;;

esac
if [ $# -eq 0 ]; then
cat << EOF
+-------------------------------------------------+
                  nginx
  nginx 语法检查： -t
  nginx 配置重新加载： -s

+-------------------------------------------------+
EOF
    echo -n "请输入:"


fi



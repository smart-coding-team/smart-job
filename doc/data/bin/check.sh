#/bin/bash

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

function log_warn() {
    echo -e "\033[33m [WARN] $@ \033[0m"
}

docker ps > /dev/null 2>&1
if [ $? -ne 0 ];then
    log_error "执行Docker命令失败"
    log_error "[可能原因1] Docker未启动， systemctl start docker.service"
    log_error "[可能原因2] 权限不够，使用sudo 或者 将该用户加入docker用户组 sudo gpasswd -a \${USER} docker"
else
    log_info "docker 服务正常"
fi

SELINUX=`/usr/sbin/sestatus -v | awk '{printf $3}'`
if [ "$SELINUX" = disabled ];then
    log_info "SELINUX = Disabled"
else
    log_error "[可能原因1] SELINUX = permissive"
    log_error "[可能原因2] SELINUX = enforcing"
    echoPlus 93 "临时关闭 setenforce 0"
    echoPlus 93 "永久关闭 vim /etc/selinux/config"
fi

FIREWALLD_STATUS=`systemctl status firewalld | grep Active | awk '{print $2}'`
if [ "$FIREWALLD_STATUS" = active ];then
    log_error "[防火墙开启状态] 关闭命令: systemctl stop firewalld && systemctl disable firewalld"
else
    log_info "[防火墙关闭状态]"
fi

VMCOUNT=`sysctl vm.max_map_count | awk '{print $3}'`
if [ ${VMCOUNT} -lt 524288 ];then
    log_error "sysctl -w vm.max_map_count=524288"
else
    log_info "vm.max_map_count=${VMCOUNT}"
fi

shn=`ulimit -SHn`
if [ $shn -lt 655350 ]; then
    log_warn "系统文件描述符配置较低(ulimit -Sn) $shn < 655350, 请参考如下配置(vim /etc/profile)系统级优化"
    echoPlus 93 "ulimit -SHn 655350"
    echoPlus 93 "ulimit -SHd unlimited"
    echoPlus 93 "ulimit -SHm unlimited"
    echoPlus 93 "ulimit -SHs unlimited"
    echoPlus 93 "ulimit -SHt unlimited"
    echoPlus 93 "ulimit -SHv unlimited"
fi

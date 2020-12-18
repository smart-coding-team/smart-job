#!/bin/bash

## sql文件拷贝到容器中
SHHOME=$(pwd)
BASE_DIR=$(cd `dirname $0`/..; pwd)
docker cp ${BASE_DIR}/config/smart-job.sql  mysql:/tmp/


## 登录mysql
docker exec -it mysql bash -c 'mysql -uroot -pxwyXNlwVflXHl2pyR1SY'

## 执行执行初始化sql
source /tmp/smart-job.sql



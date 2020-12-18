## smart-job 应用部署指南

 smart-job/doc/data的目录上到服务器root目录下面

>  ├── bin              ## 部署的脚本
>  ├── config           ##应用的配置文件
>  ├── docker           ##docker  
>  ├── docker-compose   ##容器编排docker-compose
>  ├── docker-images    ##docker 镜像
>  ├── mysql_data       ##mysql存储的目录
>  ├── nginx            ##负载均衡nginx 配置目录（包含前端的html）
>  └── software         ##系统优化应用
>

### docker环境安装(如果已完成可以忽略)

1、系统优化
解压服务器性能优化包，并执行优化脚本

`tar xvf /root/data/software/larks.tar.gz -C /usr/local/`

`cd /usr/local/larks/scripts/system && ./initial_system && ./sysconfig && sudo bash && source /etc/profile`

2、安装docker 
注：使用root用户操作，新建普通用户使用系统默认权限就可，所有应用程序都在该用户家目录

解压至系统/目录（root用户操作）

`cd /data/docker && tar zxf docker-19.03.9.tgz && mv docker.service /etc/systemd/system/ && mv docker/* /usr/bin/ && systemctl daemon-reload && systemctl enable docker && systemctl start docker`

docker对磁盘的占用较大，请执行以下命令预先设置docker root dir为大磁盘路径

修改docker配置文件，
` cat << EOF >> /etc/docker/daemon.json
{
"data-root":"/data/docker-data"
} 
EOF`

重启docker以使配置生效
`systemctl restart docker`


### 应用部署

1、中间件部署,启动mysql, redis, nginx 

   `cd  /data/bin && ./devops.sh mysql redis nginx`

   查看容器是否启动成功

   `docker ps ` 
    
2、初始化smart-job应用的数据库

  sql拷贝到mysql容器内

  `docker cp /data/config/smart-job.sql  mysql:/tmp/`

  进入mysql 容器并登陆mysql

  `docker exec -it mysql bash -c 'mysql -uroot -pxwyXNlwVflXHl2pyR1SY'`

  执行初始化smart-job的数据库脚本

   `source /tmp/smart-job.sql`

3、smart-job 后端应用部署  

`cd  /data/bin && ./devops.sh smart-job` 

4、smart-job-ui 前端应用部署

  下载前端代码 https://github.com/smart-coding-team/smart-job-ui/releases/

 `cd  /data/nginx/html/job-html && wget https://github.com/smart-coding-team/smart-job-ui/releases/download/v1.0.0/smart-job-ui-v1.0.0.tar.gz` 

 部署前端代码

 `cd  /data/bin && ./devops.sh smart-job-ui` 

5、nginx 配置 

`vi  /data/nginx/conf.d/job.xxx.com.conf ` 

修改xxx.xx.com域名

```nginx
server {
    listen      80;
    server_name  xxx.xx.com;
   # ssl_certificate   /usr/local/nginx/conf.d/ssl_keys/xxx.cc.pem;
   # ssl_certificate_key    /usr/local/nginx/conf.d/ssl_keys/xxx.cc.key;
   # ssl_session_timeout 5m;
   # ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
   # ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
   # ssl_prefer_server_ciphers on;
    proxy_buffering off;
    ignore_invalid_headers off;
    access_log  "pipe:rollback /usr/local/nginx/logs/access_apm.log interval=24h baknum=2 maxsize=2G"  access;

    location / {
        root     /usr/local/html/job-html;
        index    index.html;
        try_files $uri /index.html;
        expires  0;
        add_header Cache-Control no-cache;
        add_header Cache-Control private;
    }

    location /api {
        proxy_pass http://127.0.0.1:7040;
        proxy_set_header Host   $host;
        proxy_set_header X-Real-IP    $remote_addr;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-For    $proxy_add_x_forwarded_for;
        proxy_http_version 1.1;
        proxy_set_header Connection "";
    }

}
```
 检查nginx 语法

 `docker exec -it nginx /bin/bash -c 'cd sbin/  && ./nginx -t'`

 重新加载nginx 配置

 `docker exec -it nginx /bin/bash -c 'cd sbin/  && ./nginx -s reload'`

6、浏览器地址输入地址 xxx.xx.com  默认管理员账号和密码: admin/admin123


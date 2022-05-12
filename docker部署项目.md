# docker部署项目

### 安装mysql

创建容器，设置端口映射、目录映射

~~~shell
mkdir /mnt/docker/mysql
cd /mnt/docker/mysql
~~~

~~~shell
docker run -id \
-p 3307:3306 \
--name=mysql \
-v /mnt/docker/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=root \
mysql:5.7
~~~

![1652348100240](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1652348100240.png)

 我们已经将宿主机`/mnt/docker/mysql/data`目录和docker容器的`/var/lib/mysql`地址之间实现了映射，因此，我们如果将blog.sql文件导入宿主机的该目录下，blog.sql同样也会出现在docker的对应目录下 

```shell
接下来在docker中执行该sql。
mysql -uroot -p ,输入密码，进入docker中的数据库
创建数据库create database blog;
退出回到容器exit
将文件导入数据库mysql -uroot -p blog < blog.sql;
切换数据库use blog;
```

### 用Dockerfile 制作镜像 

### 部署后端 

制作后端镜像，在linux中新建一个文件夹blog，新建一个Dockerfile文件 

![](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1652347117797.png)

#### Dockerfile文件 

```shell
FROM java:8
VOLUME /tmp
ADD blog_api.jar blog_api.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","/blog_api.jar"]
```

Dockerfile就是把项目打包成镜像 

```shell
打包镜像命令 
docker build -t app . 
```

运行镜像命令

```shell
docker run -d -p 8888:8888 --name app 镜像id 
```

可以把打包镜像命令和运行命令写到sh脚本中

```shell
jingxiang.sh 文件 
docker build -t app . 
```

```shell
run.sh 文件 
docker run -d -p 8888:8888 --name app 镜像id 
```

### 部署前端

制作后端镜像，在linux中新建一个文件夹vueblog ，新建一个Dockerfile文件 

![1652347180228](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1652347180228.png)

把前端代码打包放到文件夹dist中 

新建一个nginx的 default.conf文件 

```shell
server {
 
    listen  80;
 
    listen  [::]:80;
 
    server_name www.hechunyu.xyz  hechunyu.xyz;
 
    location / {
 
        root   /usr/share/nginx/html;
 
        index  index.html index.htm;
 
	try_files $uri $uri/ /index.html;    # 这句一定要加 不然会刷新的时候报404
 
    }
 
    location /api/{
 
      proxy_set_header Host $http_host;
 
      proxy_set_header X-Real-IP $remote_addr;
 
      proxy_set_header REMOTE-HOST $remote_addr;
 
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

      proxy_pass http://124.223.163.111:8888/; #这里一定要换成你要部署的服务器的地址
 
  }
 
    error_page   500 502 503 504  /50x.html;
 
    location = /50x.html {
 
        root   /usr/share/nginx/html;
 
    }
}
```

编写Dockerfile文件 

```shell
FROM nginx       这里指定的是基于nginx如果没有安装nginx会自动安装最新版本

COPY  dist/   /usr/share/nginx/html/  把文件挂载到docker中nginx目录中 
 							  
RUN chmod -R 755 /usr/share/nginx/html

COPY  default.conf  /etc/nginx/conf.d/default.conf  把配置文件挂载到docker中nginx目录中 
                                       
```

Dockerfile就是把项目打包成镜像 

```shell
打包镜像命令 
docker build -t blogvue . 
```

运行镜像命令

```shell
docker run -d -p 80:80 --name app 镜像id 
```

可以把打包镜像命令和运行命令写到sh脚本中

```shell
jingxiang.sh 文件 
docker build -t app . 
```

```shell
run.sh 文件 
docker run -d -p 80:80 --name app 镜像名称
```


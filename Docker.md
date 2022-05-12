## Docker

docker三要素 ：镜像（image）、容器（container）、仓库（repository）

​							Book b1=new Book ();

![1646887590265](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1646887590265.png)

​							镜像就是一个只读的模板 ，可以创建docker容器

​     仓库集中存放镜像的地方 

​	 镜像是打包好的运行环境，一个项目导出来算一个镜像，容器相当于这个对象的实例

![1646888792860](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1646888792860.png)

###### 安装gcc

yum -y install gcc 

###### 安装c++

yum -y install gcc-c++

docker官网https://docs.docker.com/engine/install/centos/

执行yum install -y yum-utils下载仓库 

执行

```yum
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

重新建立索引 ： yum makecache fast

安装Docker Engine 引擎

```linux
yum install docker-ce docker-ce-cli containerd.io
```

启动 Docker。

```
$  systemctl start docker
```

通过运行映像来验证 Docker 引擎是否已正确安装。

docker   run +镜像 

```
$ sudo docker run hello-world
```

docker version 

## 卸载 Docker 引擎

1. 卸载 Docker 引擎、CLI 和 Containerd 软件包：

   ```
   $ sudo yum remove docker-ce docker-ce-cli containerd.io
   ```

2. 主机上的映像、容器、卷或自定义配置文件不会自动删除。要删除所有映像、容器和卷：

   ```
   $ sudo rm -rf /var/lib/docker
   $ sudo rm -rf /var/lib/container
   ```

## 阿里云镜像加速

阿里云容器镜像服务

镜像加速器地址 （阿里云自己注册的）：https://ycydvj1v.mirror.aliyuncs.com

##  配置镜像加速器

针对Docker客户端版本大于 1.10.0 的用户

您可以通过修改daemon配置文件/etc/docker/daemon.json来使用加速器

```
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://ycydvj1v.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

开始在本机找镜像 ，如果没有去远处拉镜像， 

docker利用宿主机

#### 帮助启动类命令 

![1646896964831](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1646896964831.png)

docker   cp  --help  :查看特定的命令帮助 

### 镜像命令

显示开启的所有镜像 ：docker  images  

![1646897297670](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1646897297670.png)

搜索远程库是否有镜像：docker search +镜像名

![1646897522871](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1646897522871.png)

下载镜像 (TAG版本号)  ：docker      pull   +镜像名字   

​					    指定版本：docker      pull     redis:6.0.8

查看docker装了多少             docker system  df  

删除镜像：   docker   rmi    +名字 

​					   docker  rmi   hello-world  

 强制删除 ： docker rmi  -f   +镜像Id  

#### 谈谈docker虚悬镜像是什么 ？

仓库名和     标签TGA都是 none

![1646898549884](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1646898549884.png)

## 容器命令

![1646899022746](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1646899022746.png)

docker里面安装Ubuntu系统

#### 启动容器：

docker   run  +镜像 

--name=   起一个名字 

-d

 `firewall-cmd --add-port=9506/tcp  `--permanent` 

-t:为容器重新分配一个伟输入终端 通常与 -i 使用

 启动交互式容器  ：docker  run  -it 

端口映射 ： -p  指定端口映射

​                     -P   系统自动分配 

![1646900572889](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1646900572889.png)



#### 启动交互式容器 

i : interactive 

t: tty  伪终端

docker   run  -it     ubuntu：18（最新不用写版本）

docker中启动ubuntu        进入docker集装箱中干活 

docker run -it ubuntu  /bin/bash

![1646900857740](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1646900857740.png) 

  退出终端   exit 

列出所有容器 ： docker  ps 

 docker run -it  --name  myu1 ubuntu /bin/bash 

自己定义的容器名称 

列出所有运行过的容器：  docker ps   -a  

退出容器 ：exit       退出但不完全退出 ctrl +p+q

启动已经停止的容器 ：

删除容器+ id 或者名字 ： docker  rm    

停止容器 ：  docker   stop +容器id

###  启动守护式容器 （后台行 ）：

-d 指定 后台运行 

前台交互式运行 docker -it  run redis

后台交互式运行 docker   run  -d  redis  /bin/bash 

查看容器信息： docker inspect +容器id

##### 重新 进入正在运行的容器 并且与容器交互 ：docker  exec --it +id /bin/bash 

​																			 docker  attach +容器id     

​															（推荐） exec是重新打开终端 ，exit的时候不会退出 

​																		     attach 不会启动新的进程 ，exit退出的时候会停止容器  

备份容器到主机 ：

docker cp +容器 id   :+容器中的路径  +宿主机中的路径

![1646911791060](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1646911791060.png)

##### 导入和导出容器 ：

 把整个容器导出 ： docker export +容器id > abcde.tar 

 把容器导入docker  ：cat abc.tar | docker import - hechunyu/ubuntu:3.3

 镜像是分层的： 方便共享 ，每个镜像的每一层都可以共享 ，镜像是只读的，容器层是可写的 

在镜像中生成一个新的镜像 ：

apt -get update 

docker commit -m ="提交的描述信息"  -a ="作者"  容器id   要创建的目标镜像名/标签名 :1.3

docker commit -m="test" -a="hechunyu" e4c3525c4087  hechunyu/myubuntu:1.3

本地镜像发布到阿里云 ：

![1646918862280](C:\Users\75898\AppData\Roaming\Typora\typora-user-images\1646918862280.png)

### Docker容器卷 

Docker挂载    ：相当于v-model

在挂载目录多加一个 --privileged =true  相当于root权限 

容器卷的设置目的就是数据的持久化，独立于容器的生存周期 

docker run -it --privileged=true-v /宿主绝对路径

#### 软件安装：

搜索 docker search

docker  pull  

docker run redis

安装 tomcat

小p 自己定义端口 

大P随机分配端口 

运行 ： docker run -it  -p  8080:8080  tomcat 

 docker run -it  -p  8080:8080    - - name   tomcat1   tomcat

 后台启动tomcat  ：   docker run -d  -p 8080:8080 --name t2  fb5657adc892

进入tomcat中 ： docker exec -it d50f2e0ecea6 /bin/bash 

把webapps.list改成webapps ： mv webapps.list    webapps 

## Mysql 如何运行 

使用mysql镜像 ：

docker run --p 3306:3306  --name mysql1  -e MYSQL_ROOT_PASSWORD=123456   -d mysql:tag

docker exec -it e639a955cb1d /bin/bash

mysql -uroot -p 

docker 容器中mysql中文乱码 ： SHOW VARIABLES LIKE  'character%'；

创建容器新加卷 

docker run -d -p 3306:3306 --privileged=true -v /opt/mysql/log:/var/log/mysql -v /opt/mysql/data:/var/lib/mysql -v /opt/mysql/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456  --name mysql mysql:5.7

在conf目录中 新建my.cnf 文件 

里面加入 

[client]

default_character_set=utf8

[mysqld]

collation_server = utf8_general_ci

character_set_server = utf8

重启mysql容器 ： docker restart mysql 

docker exec -it mysql  /bin/bash 

#### 安装redis 

新建一个宿主机文件夹 /app/redis      mkdir -p  /app/redis ,里面放一个最原始的redis.conf文件 

 启动

docker run  -p 6379:6379 --name myr3 --privileged=true -v /app/redis/redis.conf:/etc/redis/redis.con -v /app/redis/data:/data -d redis:6.0.8 

redis-server /etc/redis/redis.conf	

宿主机配置文件修改了，docker文件也跟着修改 

 停用全部运行中的容器: docker stop $(docker ps -a)
2.删除全部容器: docker rm $(docker ps -aq)
3.一条命令实现停用并删除容器: docker stop $(docker ps -a) & docker rm -f $(docker ps -aq) 

docker run -d --name myMysql -p 9506:3306 -v /data/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 mysql:5.7.31





docker build -t blog_api .

docker run -d -p 8888:8888 --name  blog_api-8888 authority
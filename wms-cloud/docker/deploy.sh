#!/bin/sh

# 使用说明，用来提示输入参数
usage() {
	echo "Usage: sh 执行脚本.sh [port|base|up|stop|rm|build]"
	exit 1
}

# 开启所需端口
# 如果是部署在华为云，不需要开启端口，华为云的端口开启需要在控制台
port(){
	firewall-cmd --add-port=80/tcp --permanent
	firewall-cmd --add-port=8080/tcp --permanent
	firewall-cmd --add-port=8848/tcp --permanent
	firewall-cmd --add-port=9848/tcp --permanent
	firewall-cmd --add-port=9849/tcp --permanent
	firewall-cmd --add-port=6379/tcp --permanent
	firewall-cmd --add-port=3306/tcp --permanent
	firewall-cmd --add-port=9100/tcp --permanent
	firewall-cmd --add-port=9200/tcp --permanent
	firewall-cmd --add-port=9201/tcp --permanent
	firewall-cmd --add-port=9202/tcp --permanent
	firewall-cmd --add-port=9203/tcp --permanent
	firewall-cmd --add-port=9300/tcp --permanent
	service firewalld restart
}



# 启动基础环境（必须）
base(){
	docker-compose up -d wms-mysql wms-redis wms-minio wms-nacos
}

# 启动程序模块（必须）
up(){
	docker-compose up -d  wms-nginx wms-gateway wms-auth wms-modules-system wms-modules-file master-data storage-in bin-in reservation product
}

# 关闭所有环境/模块
stop(){
	docker-compose stop wms-nginx wms-gateway wms-auth wms-modules-system wms-modules-file master-data storage-in bin-in reservation product
}

# 删除所有环境/模块
rm(){
	docker-compose rm wms-nginx wms-gateway wms-auth wms-modules-system wms-modules-file master-data storage-in bin-in reservation product
}

# 模块编译
build(){
	docker-compose build wms-nginx wms-gateway wms-auth wms-modules-system wms-modules-file master-data storage-in bin-in reservation product
}

# 根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
"port")
	port
;;
"base")
	base
;;
"up")
	up
;;
"stop")
	stop
;;
"rm")
	rm
;;
"build")
	build
;;
*)
	usage
;;
esac

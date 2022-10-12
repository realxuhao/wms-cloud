#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
cp ../sql/ry_20220814.sql ./mysql/db
cp ../sql/ry_config_20220510.sql ./mysql/db

# copy html
echo "begin copy html "
cp -r ../ruoyi-ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy wms-gateway "
cp ../wms-gateway/target/wms-gateway.jar ./wms/gateway/jar

echo "begin copy wms-auth "
cp ../wms-auth/target/wms-auth.jar ./wms/auth/jar

echo "begin copy ruoyi-visual "
cp ../ruoyi-visual/ruoyi-monitor/target/ruoyi-visual-monitor.jar  ./wms/visual/monitor/jar

echo "begin copy wms-modules-system "
cp ../wms-modules/wms-system/target/wms-modules-system.jar ./wms/modules/system/jar

echo "begin copy wms-modules-file "
cp ../wms-modules/wms-file/target/wms-modules-file.jar ./wms/modules/file/jar

echo "begin copy wms-modules-job "
cp ../wms-modules/wms-job/target/wms-modules-job.jar ./wms/modules/job/jar

echo "begin copy wms-modules-gen "
cp ../wms-modules/wms-gen/target/wms-modules-gen.jar ./wms/modules/gen/jar


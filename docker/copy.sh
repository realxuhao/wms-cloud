#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}



# copy html
echo "begin copy html "
cp -r ../wms-ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy wms-gateway "
cp ../wms-gateway/target/wms-gateway.jar ./wms/gateway/jar

echo "begin copy wms-auth "
cp ../wms-auth/target/wms-auth.jar ./wms/auth/jar

echo "begin copy wms-modules-system "
cp ../wms-modules/wms-system/target/wms-modules-system.jar ./wms/modules/system/jar

echo "begin copy wms-modules-file "
cp ../wms-modules/wms-file/target/wms-modules-file.jar ./wms/modules/file/jar

echo "begin copy master-data "
cp ../wms-modules/master-data/target/master-data.jar ./wms/modules/masterdata/jar

echo "begin copy storage-in "
cp ../wms-modules/storage-in/target/storage-in.jar ./wms/modules/storagein/jar

echo "begin copy bin-in "
cp ../wms-modules/bin-in/target/bin-in.jar ./wms/modules/binin/jar






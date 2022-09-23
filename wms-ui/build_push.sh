#!/bin/bash

tag=$1
if [ -z $tag ]; then
  echo "please input the tag"
  exit 1
fi

echo "build docker image with tag: template/ui_minerva:$tag"

docker build -t template/ui_minerva:$tag .
docker tag template/ui_minerva:$tag swr.cn-north-4.myhuaweicloud.com/meinenghua/template/ui_minerva:$tag
docker push swr.cn-north-4.myhuaweicloud.com/meinenghua/template/ui_minerva:$tag
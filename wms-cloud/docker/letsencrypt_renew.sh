#!/bin/bash
certbot renew --quiet
docker kill -s SIGHUP wms-nginx
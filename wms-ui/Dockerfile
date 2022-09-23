FROM node:10.16.0-alpine

RUN npm config set unsafe-perm true
RUN npm install -g serve
RUN apk add --no-cache bash

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

EXPOSE 8000

CMD ["./build_serve.sh"]

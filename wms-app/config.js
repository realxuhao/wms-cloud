const baseApi = {
	development: "http://192.168.10.199:8080", // "http://192.168.85.161:8080",//本地开发环境地址
	production: "http://192.168.10.199:8080" //正式环境地址
}

const NODE_ENV = process.env.NODE_ENV
if (!NODE_ENV) {
	console.error("获取运行环境失败!");
}

console.log('env--------', NODE_ENV)

const config = {
	apiBaseUrl: baseApi[NODE_ENV]
};

export default config;

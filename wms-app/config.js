const baseApi = {
	// 正式环境地址
	development: "https://digital-warehouse.nutriciachina.com/api", // "http://192.168.85.161:8080",//本地开发环境地址
	production: "https://digital-warehouse.nutriciachina.com/api", //正式环境地址

	// 华为云地址
	// development: "https://digital-warehouse.nutriciachina.com/api", // "http://192.168.85.161:8080",//本地开发环境地址
	// production: "https://www.nutricia-home.com/api", //正式环境地址


	// https://digital-warehouse.nutriciachina.com/api
	// 测试环境
	// development: "http://10.200.178.66/api", // "http://192.168.85.161:8080",//本地开发环境地址
	// production: "http://10.200.178.66/api" //正式环境地址
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
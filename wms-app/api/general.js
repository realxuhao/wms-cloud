import config from "../config";
import { getToken } from '@/utils/storage'
import store from '../store'

const request = (options) => {
	// 拼接完整的接口路径
	options.url = `${config.apiBaseUrl}${options.url}`

	//判断是都携带参数
	if (!options.data) {
		options.data = {};
	}

	// 请求头
	const headers = {
		...(options.header || {}),
		'Authorization': 'Bearer ' + getToken(),
	}
	options.header = headers

	// 超时时间
	options.timeout = 600 * 1000

	const promise = new Promise(function (resolve, reject) {
		uni.request(options).then(responses => {
			// 异常
			if (responses[0]) {
				reject({ message: "网络超时" });
			} else {
				let { data } = responses[1]; // 如果返回的结果是data.data的，嫌麻烦可以用这个，return res,这样只返回一个data

				if (data.code === 401) {
					store.dispatch('Logout')
					reject({ message: '身份认证已失效，即将返回登录' });
				}

				if (data.code !== 200) {
					reject({ message: data.msg||'异常错误请联系管理员',code:data.code });
				}

				resolve(data);
			}
		}).catch(error => {
			reject(error);
		})
	})

	return promise;
};

export default request;

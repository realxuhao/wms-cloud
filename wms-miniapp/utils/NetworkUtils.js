// https://segmentfault.com/a/1190000014789969

const baseUrl = 'http://localhost:8080/';
// const baseUrl = 'http://121.37.188.213:8080/';

const http = ({
    url = '',
    param = {},
    ...other
} = {}) => {

    wx.showLoading({
        title: '请求中，请耐心等待..'
    });

    //读取session
    let timeStart = Date.now();
    return new Promise((resolve, reject) => {

        var sessionId = wx.getStorageSync('sessionId')
        wx.request({
            url: getUrl(url),
            data: param,
            header: {
                'Cookie': sessionId,
                'content-type': 'application/json', // 默认值 ,另一种是 "content-type": "application/x-www-form-urlencoded"
                'Authorization': wx.getStorageSync("token") == undefined ? "" :  'Bearer ' + wx.getStorageSync("token")
            },
            ...other,
            success: (res) => {
                //cookie
                var session = res.header["Set-Cookie"];
                if (session != undefined) {
                    wx.setStorage({
                        key: 'sessionId',
                        data: session,
                        success: function (res) {
                          console.log(res)
                        }
                    })
                }

                console.log(`耗时${Date.now() - timeStart}`);
                if (res.statusCode >= 200 && res.statusCode < 300) {
                    resolve(res.data)
                } else if (res.statusCode == 403) {
                    reject(res)
                    wx.reLaunch({
                        url: '../pages/login/login'
                    })
                } else {
                    reject(res)
                }
            },
            fail:(res) =>{
                reject(res)
            },complete:(res) =>{
                wx.hideLoading({
                  success: (res) => {},
                })
            }
        })
    })
}

const getUrl = (url) => {
    if (url.indexOf('://') == -1) {
        url = baseUrl + url;
    }
    return url
}

// get方法
const _get = (url, param = {}) => {
    return http({
        url,
        param
    })
}

const _post = (url, param = {}) => {
    return http({
        url,
        param,
        method: 'post'
    })
}

const _put = (url, param = {}) => {
    return http({
        url,
        param,
        method: 'put'
    })
}

const _delete = (url, param = {}) => {
    return http({
        url,
        param,
        method: 'delete'
    })
}

const _uploadFiles = (url, files, key, param = {}, method = 'post') => {
    var that = this;
    var base64DataArray = [];
    for (let i = 0; i < files.length; i++) {
        var fileName = files[i]
        var base64Str = wx.getFileSystemManager().readFileSync(fileName, 'base64')
        
        base64Str = _getBase64DataPrefix(fileName) + base64Str
        base64DataArray.push(base64Str)

    }

    param[key] = base64DataArray;

    console.log(param)

    return http({
        url,
        param,
        method: method
    })
}

//根据文件名 返回base64的头
const _getBase64DataPrefix = (fileName) => {
    var extensionArr = ['txt', 'doc', 'docx', 'xls', 'xlsx', 'pdf', 'pptx', 'ppt', 'png', 'jp', 'gif', 'svg', 'ico', 'bmp']
    var base64HeaderArr = ['data:text/plain;base64,',
        'data:application/msword;base64,',
        'data:application/vnd.openxmlformats-officedocument.wordprocessingml.document;base64,',
        'data:application/vnd.ms-excel;base64,',
        'data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,',
        'data:application/pdf;base64,',
        'data:application/vnd.openxmlformats-officedocument.presentationml.presentation;base64,',
        'data:application/vnd.ms-powerpoint;base64,',
        'data:image/png;base64,',
        'data:image/jpeg;base64,',
        'data:image/gif;base64,',
        'data:image/svg+xml;base64,',
        'data:image/x-icon;base64,',
        'data:image/bmp;base64, '
    ]

    for (var i = 0; i < extensionArr.length; i++) {
        if (fileName.indexOf(extensionArr[i]) != -1) {
            return base64HeaderArr[i]
        }
    }
}

// base64 data类型 https://www.cnblogs.com/wang0020/p/10153447.html
const _uploadFiles2 = (url, files, key, param = {}, method = 'post') => {

    var that = this;
    var fileJSONArray = [];

    for (let i = 0; i < files.length; i++) {
        var file = files[i]
        var base64Str = wx.getFileSystemManager().readFileSync(file.path, 'base64')
        var fileName = file.name

        base64Str = _getBase64DataPrefix(fileName) + base64Str

        var dotIndex = fileName.lastIndexOf('.')
        var suffix = fileName.substring(dotIndex + 1)

        console.log(suffix)

        var tmpObj = {
            "attachmentFile": base64Str,
            "attachmentName": fileName,
            "attachmentSuffix": suffix,
            "attachmentOthers":file.other
        }

        fileJSONArray.push(tmpObj)
    }

    param[key] = fileJSONArray;

    console.log(param)

    return http({
        url,
        param,
        method: method
    })
}

module.exports = {
    baseUrl,
    _get,
    _post,
    _put,
    _delete,
    _uploadFiles,
    _uploadFiles2
}
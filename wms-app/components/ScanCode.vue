<template></template>
<script>
var main, receiver, filter,settingFilter;
var _codeQueryTag = false;
export default {
    data() {
        return {
            scanCode: ''
        };
    },
   created() {
   	this.initScan();
	this.startScan()
   },
   
   onLoad() {
   	uni.$on('stopScan',function(){
   		_this.stopScan();
   	})  
   },
   
    destroyed: function() {
        this.stopScan();
    },

    methods: {
        initScan() {
            let _this = this;
            main = plus.android.runtimeMainActivity(); //获取activity
			
            var IntentFilter = plus.android.importClass('android.content.IntentFilter');
            filter = new IntentFilter();
            filter.addAction("com.android.server.scannerservice.broadcast");//
			// 你的广播动作
            receiver = plus.android.implements('io.dcloud.feature.internal.reflect.BroadcastReceiver', {
                onReceive: function(context, intent) {
					console.log(1111)
                    plus.android.importClass(intent);
                    let code = intent.getStringExtra('scannerdata'); // 换你的广播标签
					console.log('code',code)
                    _this.queryCode(code);
                }
            });
			
        },
		setScannerEnabled(enable){
			var Intent = plus.android.importClass('android.content.Intent');
			const intent = new Intent("com.android.scanner.ENABLED")
			intent.putExtra("enabled",enable)
			main.sendBroadcast(intent)
		},
        startScan() {
            main.registerReceiver(receiver, filter);
        },
        stopScan() {
            main.unregisterReceiver(receiver);
        },
        queryCode: function(code) {
            //防重复
            if (_codeQueryTag) return false;
            _codeQueryTag = true;
            setTimeout(function() {
                _codeQueryTag = false;
            }, 150);
            var id = code;
				
			// this.setScannerEnabled(false)
			
            uni.$emit('scancodedate', { code: id });
        }
    }
};
</script>
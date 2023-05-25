import Vue from "vue";

import App from "./App";
import store from "./store";

Vue.prototype.$store = store;

Vue.config.productionTip = false;

App.mpType = "app";

import './directive/permission/index'


const app = new Vue({
	store,
	...App,
});
app.$mount();

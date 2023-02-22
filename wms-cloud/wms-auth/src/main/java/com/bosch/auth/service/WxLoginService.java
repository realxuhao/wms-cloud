package com.bosch.auth.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bosch.system.api.model.LoginUser;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.utils.JwtUtils;
import com.ruoyi.common.core.utils.uuid.IdUtils;
import com.ruoyi.common.security.service.TokenService;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class WxLoginService
{
    @Autowired
    private TokenService tokenService;

    public String WxGetOpenId(String code) throws IOException {
        Map<String,Object> rtnMap = new HashMap<String,Object>();
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        url += "?appid=wxdbed75d266b71b47";//自己的appid
        url += "&secret=f16485d3c9097df9fa4eb4286b5cfe14";//密匙
        url += "&js_code=" + code;
        url += "&grant_type=authorization_code";
        byte[] res = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            res = IOUtils.toByteArray(response.getEntity().getContent());
        } catch (Exception e) {
            throw e;
        } finally {
            if (httpget != null) {
                httpget.abort();
            }
            httpclient.getConnectionManager().shutdown();
        }
        JSONObject jo = JSON.parseObject(new String(res, "utf-8"));
        String openid = jo.getString("openid");
        return openid;
    }

    public Map<String, Object> wxCreateToken(String openid)
    {
        String token = IdUtils.fastUUID();

        LoginUser loginUser = new LoginUser();
        loginUser.setToken(token);
        tokenService.refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, 0l);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, "wx-miniapp");

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("open_id", openid);
        return rspMap;
    }

}

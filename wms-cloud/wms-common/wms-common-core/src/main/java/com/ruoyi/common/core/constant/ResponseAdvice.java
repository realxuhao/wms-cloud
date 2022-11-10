//package com.ruoyi.common.core.constant;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ruoyi.common.core.domain.R;
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//
//@RestControllerAdvice
//public class ResponseAdvice implements ResponseBodyAdvice<Object> {
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
//        return true;
//    }
//    @SneakyThrows
//    @Override
//    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<?
//            extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
//                                  ServerHttpResponse response) {
//        if(body instanceof String){
//            return objectMapper.writeValueAsString(R.ok(body));
//        }
//        if(body instanceof R){
//            if(((R<?>) body).isSuccess()){
//                return ((R<?>) body).getData();
//            }
//            return objectMapper.writeValueAsString(((R<?>) body).getData());
//        }
//        return R.ok(body);
//    }
//
//
//
//}
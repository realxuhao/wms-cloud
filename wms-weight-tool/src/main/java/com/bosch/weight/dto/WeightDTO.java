package com.bosch.weight.dto;

import java.util.Date;

/**
 * @program: broadcasttest
 * @description:
 * @author: xuhao
 * @create: 2023-02-14 10:58
 **/
public class WeightDTO {

    private String ip;
    private Integer port;
    private Double totalWeight;
    private Date uploadTime;



    public WeightDTO(String ip, Integer port, Double totalWeight,Date uploadTime) {
        this.ip = ip;
        this.port = port;
        this.totalWeight = totalWeight;
        this.uploadTime = uploadTime;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }
}
package com.bosch.product.api.domain.dto;

import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.ShippingTask;

import java.util.List;

public class ListPair {
    public List<ShippingTask> shippingTasks;
    public List<ShippingPlan> shippingPlans;

    public ListPair(List<ShippingTask> shippingTask, List<ShippingPlan> shippingPlan) {
        this.shippingTasks = shippingTask;
        this.shippingPlans = shippingPlan;
    }
}
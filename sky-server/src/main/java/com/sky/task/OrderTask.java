package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void processTimeoutOrder() {
        log.info("定时处理超时订单： {}", LocalDateTime.now());
        LocalDateTime now = LocalDateTime.now().plusMinutes(-15);
        List<Orders> orders = orderMapper.getBySttusAndOrderTimeLT(Orders.PENDING_PAYMENT, now);
        if (orders != null && orders.size() > 0) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("支付超时");
                order.setCancelTime(now);
                orderMapper.update(order);
            }
        }
    }
    //一直派送的状态处理
    @Scheduled(cron = "0 0 1 * * ?")
    private void processDeliverOrder() {
        log.info("定时处理一直派送的订单 {}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusHours(-1);
        List<Orders> orders = orderMapper.getBySttusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if (orders != null && orders.size() > 0) {
            for (Orders order : orders) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }
}

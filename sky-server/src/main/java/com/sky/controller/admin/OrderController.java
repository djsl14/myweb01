package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜素")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单搜素：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @ApiOperation("订单统计")
    public Result<OrderStatisticsVO> statistics(){
        log.info("订单统计");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    @GetMapping("/details/{id}")
    @ApiOperation("订单详情")
    public Result<OrderVO> details(@PathVariable String id){
        log.info("订单详情：{}", id);
        OrderVO orderVO = orderService.getByIdWithOrderDetail(Long.valueOf(id));
        return Result.success(orderVO);
    }

    @PutMapping("/confirm")
    @ApiOperation("接单相关接口")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        Long id = ordersConfirmDTO.getId();
        log.info("接单相关接口：{}", id);
        orderService.confirm(id);
        return Result.success("接单成功");
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation("派送相关接口")
    public Result delivery(@PathVariable String id){
        Long orderId = Long.valueOf(id);
        log.info("派送id:{}", orderId);
        orderService.delivery(orderId);
        return Result.success("开始派送");
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable String id){
        Long orderId = Long.valueOf(id);
        log.info("完成订单id:{}", orderId);
        orderService.complete(orderId);
        return Result.success("订单完成");
    }

    @PutMapping("/rejection")
    @ApiOperation("拒单相关接口")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception{
        log.info("拒单：{}", ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success("拒单成功");
    }
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        log.info("取消订单：{}", ordersCancelDTO);
        orderService.cancel(ordersCancelDTO);
        return Result.success("订单取消成功");
    }
}

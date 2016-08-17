package com.taotao.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.order.pojo.Order;
import com.taotao.order.service.OrderService;
/**
 * 订单服务
 * 接收一个json的字符串，使用@RequestBody注解，
 * 需要一个pojo接收参数，让框架把字符串转换成java对象
 * TbOrder TbOrderItem TbOrderShipping三个pojo
 * 创建一个新的pojo继承TbOrder，把其他两个的pojo加入
 * @author Haochenxu
 *
 */
@Controller
public class OrderController {

		@Autowired
		private OrderService orderService;
		
		@RequestMapping(value="/create",method=RequestMethod.POST)
		@ResponseBody
		public TaotaoResult createOrder(@RequestBody Order order){
			try {
				//调用service
				TaotaoResult result = orderService.createOrder(
						order, order.getOrderItems(), order.getOrderShipping());
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
			}
		}
		
		@RequestMapping("/info/{orderId}")
		@ResponseBody
		public TaotaoResult getOrderByOrderId(@PathVariable String orderId){
			try {
				//调用service
				TaotaoResult result = orderService.getOrderByOrderId(orderId);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
			}
		}

}

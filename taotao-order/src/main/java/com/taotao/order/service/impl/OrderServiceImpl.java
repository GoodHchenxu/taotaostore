package com.taotao.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.dao.JedisClient;
import com.taotao.order.mapper.OrderMapper;
import com.taotao.order.pojo.Order;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderItemExample;
import com.taotao.pojo.TbOrderItemExample.Criteria;
import com.taotao.pojo.TbOrderShipping;
/**
 * 订单服务
 * @author Haochenxu
 *
 */
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ORDER_GEN_KEY}")
	private String ORDER_GEN_KEY;
	@Value("${ORDER_INIT_ID}")
	private String ORDER_INIT_ID;
	@Value("${ORDER_DETAIL_GEN}")
	private String ORDER_DETAIL_GEN;
	@Autowired
	private OrderMapper oMapper;
	
	/**
	 *订单生成
	 */
	public TaotaoResult createOrder(TbOrder order, List<TbOrderItem> itemList, TbOrderShipping orderShipping) {
		//先向订单表中插入记录
		//获得订单号，补全pojo的属性，然后插入
		//首先应该判断redis中是否存在默认key,如果没有需要设置，否则从1开始自增
		String string = jedisClient.get(ORDER_GEN_KEY);
		if(StringUtils.isBlank(string)){
			jedisClient.set(ORDER_GEN_KEY, ORDER_INIT_ID);
		}
		long orderId = jedisClient.incr(ORDER_GEN_KEY);
		order.setStatus(1);
		order.setOrderId(orderId + "");
		order.setCloseTime(new Date());
		order.setUpdateTime(new Date());
		order.setBuyerRate(0);//0 未评价 1 已评价
		//向订单表中插入
		orderMapper.insert(order);
		//插入订单明细
		//循环插入
		for(TbOrderItem orderItem : itemList){
			//补全订单明细
			long orderItemId = jedisClient.incr(ORDER_DETAIL_GEN);
			orderItem.setId(orderItemId + "");
			orderItem.setOrderId(orderId + "");
			//插入记录
			orderItemMapper.insert(orderItem);
		}
		//插入物流表
		//补全物流表的属性
		orderShipping.setOrderId(orderId + "");
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		return TaotaoResult.ok(orderId);
	}

	/**
	 * 根据订单Id查询订单，返回TaotaoResult
	 */
	public TaotaoResult getOrderByOrderId(String orderId) {
		//根据id查询订单
		TbOrder tbOrder = orderMapper.selectByPrimaryKey(orderId);
		//查询出订单详情列表
		TbOrderItemExample example = new TbOrderItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andOrderIdEqualTo(orderId);
		List<TbOrderItem> list = orderItemMapper.selectByExample(example);
		//查询出物流表
		TbOrderShipping orderShipping = orderShippingMapper.selectByPrimaryKey(orderId);
		//返回一个特定的pojo格式
		Order order = new Order();
		//将订单中的内容添加到要返回的pojo中
		order.setOrderShipping(orderShipping);
		order.setOrderItems(list);
		order.setOrderId(orderId);
		order.setPayment(tbOrder.getPayment());
		order.setPaymentType(tbOrder.getPaymentType());
		order.setStatus(tbOrder.getStatus());
		order.setCreateTime(tbOrder.getCreateTime());
		order.setPostFee(tbOrder.getPostFee());
		order.setUserId(tbOrder.getUserId());
		order.setBuyerMessage(tbOrder.getBuyerMessage());
		order.setBuyerNick(tbOrder.getBuyerNick());
		return TaotaoResult.ok(order);
	}





}

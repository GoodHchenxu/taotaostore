package com.taotao.portal.service.impl;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.portal.pojo.Order;
/**
 * 订单处理service
 * @author Haochenxu
 *
 */
@Service
public class OrderService implements com.taotao.portal.service.OrderService {

	@Value("${ORDER_BASE_URL}")
	private String ORDER_BASE_URL;
	@Value("${ORDER_CREATE_URL}")
	private String ORDER_CREATE_URL;
	
	public String createOrder(Order order) {
		//调用taotao-order的服务
		String json = HttpClientUtil.doPostJson(
				ORDER_BASE_URL + ORDER_CREATE_URL, JsonUtils.objectToJson(order));
		//把json转换成taotaoResult
		TaotaoResult taotaoResult = TaotaoResult.format(json);
		if(taotaoResult.getStatus() == 200){
			Integer orderId = (Integer) taotaoResult.getData();
			return orderId.toString();
		}
		return "";
	}

}

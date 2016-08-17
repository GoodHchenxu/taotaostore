package com.taotao.order.mapper;

import java.util.List;

import com.taotao.pojo.TbOrder;

public interface OrderMapper {

	List<TbOrder> getOrderList(String userId);
}

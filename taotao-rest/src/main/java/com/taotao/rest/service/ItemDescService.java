package com.taotao.rest.service;

import com.taotao.common.pojo.TaotaoResult;

public interface ItemDescService {
	
	TaotaoResult getItemDesc(long itemId);
	TaotaoResult getItemParam(long itemId);
}

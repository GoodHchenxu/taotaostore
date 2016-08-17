package com.taotao.service;



import com.taotao.common.pojo.EUDataGriResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	
	EUDataGriResult getContentByCategoryId(int page,int rows,Long categoryId);
	TaotaoResult insertContent(TbContent tbContent);
	
}

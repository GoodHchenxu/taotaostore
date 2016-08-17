package com.taotao.rest.controller;

import java.awt.PageAttributes.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.utils.JsonUtils;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;

/**
 * 查询商品分类列表
 * 接收页面的参数，就是一个方法的名称
 * 返回一个json数据，把数据装成一个js代码
 * 返回一个字符串
 * @author Haochenxu
 *
 */
@Controller
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping(value="/itemcat/list1", 
			produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	@ResponseBody
	public String getItemCatList(String callback){
		CatResult result = itemCatService.getItemCatList();
		//需要把这个对象转换成一个字符串
		String json = JsonUtils.objectToJson(result);
		//拼装返回值
		String catResult = callback + "(" + json + ");";
		return catResult;
	}
	/**
	 * 第二种方法把json转成一个字符串
	 * @param callback
	 * @return
	 */
	@RequestMapping("/itemcat/list")
	@ResponseBody
	public Object getItemCatList1(String callback) {
		CatResult catResult = itemCatService.getItemCatList();
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(catResult);
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}

}

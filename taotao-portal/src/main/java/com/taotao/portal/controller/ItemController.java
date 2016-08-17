package com.taotao.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.portal.pojo.ItemInfo;
import com.taotao.portal.service.ItemService;
/**
 * 接收页面传递的商品id
 * 调用service查询商品的基本信息
 * 传递给jsp，返回逻辑视图
 * 展示详情页面
 * @author Haochenxu
 *
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public String getItemById(@PathVariable long itemId,Model model){
		ItemInfo item = itemService.getItemById(itemId);
		//向页面传递值
		model.addAttribute("item", item);
		return "item";
	}
	
	/**
	 * 获取商品描述信息
	 */
	@RequestMapping(value="/item/desc/{itemId}",produces=MediaType.TEXT_HTML_VALUE+";charset=utf-8")
	@ResponseBody
	public String getItemDescById(@PathVariable long itemId){
		String string = itemService.getItemDescById(itemId);
		return string;
	}
	
	/**
	 * 获取商品规格参数
	 */
	@RequestMapping(value="/item/param/{itemId}", produces=MediaType.TEXT_HTML_VALUE+";charset=utf-8")
	@ResponseBody
	public String getItemParam(@PathVariable Long itemId) {
		String string = itemService.getItemParamById(itemId);
		return string;
	}

	
}

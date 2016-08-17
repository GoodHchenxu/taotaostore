package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDataGriResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

/**
 * 商品管理
 * @author Haochenxu
 *
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	/**
	 * 通过商品id查询商品
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable long itemId){
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	/**
	 * 查询所有商品，并分页显示
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EUDataGriResult getItemList(Integer page,Integer rows){
		EUDataGriResult result = 
				itemService.getItemList(page, rows);
		return result;
	}
	/**
	 * 添加新商品,并添加新的规格参数
	 * @param item
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createItem(TbItem item,String desc,String itemParams) throws Exception{
		TaotaoResult result = 
				itemService.createItem(item,desc,itemParams);
		return result;
	}
}

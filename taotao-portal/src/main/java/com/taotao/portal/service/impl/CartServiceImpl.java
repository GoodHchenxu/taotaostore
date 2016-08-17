package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
/**
 * 购物车服务service
 * @author Haochenxu
 *
 */
@Service
public class CartServiceImpl implements CartService {

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;
	
	/**
	 * 添加购物车商品
	 */
	public TaotaoResult addCartItem(long itemId, int num, 
			HttpServletRequest request,HttpServletResponse response) {
		//取商品信息
		CartItem cartItem = null;
		//从购物车中获取商品信息列表
		List<CartItem> list = getCartItemList(request);
		//对商品列表中商品进行判断，是否存在需要添加的商品
		for(CartItem cItem : list){
			//如果存在此商品
			if(cItem.getId() == itemId){
				//增加商品数量
				cItem.setNum(cItem.getNum() + num);
				cartItem = cItem;
				break;
			}
		}
		if(cartItem == null){
			cartItem = new CartItem();
			//通过id调用rest服务获取商品信息
			String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
			//将字符串转换成java对象
			TaotaoResult result = TaotaoResult.formatToPojo(json, TbItem.class);
			if(result.getStatus() == 200){
				TbItem item = (TbItem) result.getData();
				cartItem.setId(item.getId());
				//这个图片是一个列表，需要取第一张，判断是否为空，不为空则用逗号分隔取第一张
				cartItem.setImage(item.getImage() == null ? "":item.getImage().split(",")[0]);
				cartItem.setNum(num);
				cartItem.setPrice(item.getPrice());
				cartItem.setTitle(item.getTitle());
			}
			//添加到购物车列表
			list.add(cartItem);
		}
		//将购物车列表存入Cookie中
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list),true);

		return TaotaoResult.ok();
	}
	
	/**
	 * 从cookie中取商品列表
	 * @return
	 */
	private List<CartItem> getCartItemList(HttpServletRequest request){
		//从cookie中获取商品列表是个字符串
		String cartJson = CookieUtils.getCookieValue(request, "TT_CART",true);
		if(cartJson == null){
			return new ArrayList<>();
		}
		try {
			//把json转换成商品列表
			List<CartItem> list = JsonUtils.jsonToList(cartJson, CartItem.class);
			return list;
		} catch (Exception e) {
			e.printStackTrace();	
		}
		return new ArrayList<>();
	}

	/**
	 * 展现购物车列表
	 */
	public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {
		List<CartItem> list = getCartItemList(request);
		return list;
	}

	/**
	 * 删除购物车列表
	 */
	public TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response) {
		//从cookie中取出购物车列表取出
		List<CartItem> list = getCartItemList(request);
		//循环list,找到要删除的商品id
		for(CartItem cartItem : list){
			if(cartItem.getId() == itemId){
				list.remove(cartItem);
				break;
			}
		}
		//把购物车列表重新写入cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), true);
		return TaotaoResult.ok();
	}
}

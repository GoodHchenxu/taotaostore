package com.taotao.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
/**
 * 查询出商品分类，以自定义CatResult的格式返回
 * @author Haochenxu
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_CAT_REDIS_KEY}")
	private String ITEM_CAT_REDIS_KEY;
	
	public CatResult getItemCatList() {
		//从缓存中获取分类内容
		try {
			String result = jedisClient.hget(ITEM_CAT_REDIS_KEY,"");
			if(!StringUtils.isBlank(result)){
				CatResult result2 = JsonUtils.jsonToPojo(result, CatResult.class);
				System.out.println("------------------------------");
				return result2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//使用递归查询分类列表
		CatResult catResult = new CatResult();
		catResult.setData(getCatList(0));
		//向缓存中存储内容
		try {
			String cachString = JsonUtils.objectToJson(catResult);
			jedisClient.hset(ITEM_CAT_REDIS_KEY,"", cachString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return catResult;
	}
	/**
	 * 查询分类列表的方法
	 * @param parentId
	 * @return
	 */
	private List<?> getCatList(long parentId){
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//构建返回值list
		List resultList = new ArrayList<>();
		//向list中添加节点
		//加一个计数器
		int count = 0;
		for(TbItemCat tbItemCat : list){
			//判断是否为父节点
			if (tbItemCat.getIsParent()) {
				CatNode catNode = new CatNode();
				if (parentId == 0) {
					catNode.setName("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
				} else {
					catNode.setName(tbItemCat.getName());
				}
				catNode.setUrl("/products/"+tbItemCat.getId()+".html");
				catNode.setItem(getCatList(tbItemCat.getId()));
				
				resultList.add(catNode);
				count++;
				//第一级只取14条记录
				if(parentId == 0 && count >= 14){
					break;
				}
			//如果是叶子节点
			} else {
				resultList.add("/products/"+tbItemCat.getId()+".html|" + tbItemCat.getName());
			}

		}
		return resultList;
	}
}

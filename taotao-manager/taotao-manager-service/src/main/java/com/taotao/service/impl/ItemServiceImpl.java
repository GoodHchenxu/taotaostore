package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGriResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
/**
 * 商品管理service
 * @author Haochenxu
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	
	public TbItem getItemById(long itemId) {
		//TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//使用example进行查询
		TbItemExample example = new TbItemExample();
		//添加查询条件
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			TbItem item = list.get(0);
			return item;
		}
		return null;
	}
	
	/**
	 * 商品列表查詢
	 */
	public EUDataGriResult getItemList(int page, int rows) {
		//查詢商品列表
		TbItemExample example = new TbItemExample();
		//分页处理
		PageHelper.startPage(page, rows);
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回对象
		EUDataGriResult result = new EUDataGriResult();
		result.setRows(list);
		//获取总的记录数
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	/**
	 * 添加新商品
	 * @throws Exception 
	 */
	public TaotaoResult createItem(TbItem item,String desc,String itemParam) throws Exception {
		//把item内容补全
		//生成商品id
		long itemId = IDUtils.genItemId();
		item.setId(itemId);
		//商品的状态   1 正常 2 下架 3 删除
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//把商品信息插入到数据库
		itemMapper.insert(item);
		//添加商品描述
		TaotaoResult result = 
				insertItemDesc(itemId, desc);
		if(result.getStatus() != 200)throw new Exception();
		//添加规格参数
		result = insertItemParamItem(itemId, itemParam);
		if(result.getStatus() != 200)throw new Exception();
		return TaotaoResult.ok();
	}
	/**
	 * 添加商品描述
	 */
	public TaotaoResult insertItemDesc(Long itemId,String desc){
		TbItemDesc desc2 = new TbItemDesc();
		desc2.setItemId(itemId);
		desc2.setItemDesc(desc);
		desc2.setCreated(new Date());
		desc2.setUpdated(new Date());
		itemDescMapper.insert(desc2);
		return TaotaoResult.ok();
	}
	/**
	 * 添加规格参数
	 * @param itemId
	 * @param itemParam
	 * @return
	 */
	private TaotaoResult insertItemParamItem(long itemId,String itemParam){
		//创建一个pojo
		TbItemParamItem tbItemParamItem = new TbItemParamItem();
		tbItemParamItem.setCreated(new Date());
		tbItemParamItem.setUpdated(new Date());
		tbItemParamItem.setItemId(itemId);
		tbItemParamItem.setParamData(itemParam);
		//向表中插入数据
		itemParamItemMapper.insert(tbItemParamItem);
		return TaotaoResult.ok();
	}
}

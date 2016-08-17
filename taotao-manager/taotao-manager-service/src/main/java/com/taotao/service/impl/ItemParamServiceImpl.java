package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.pojo.TbItemParamExample.Criteria;
import com.taotao.service.ItemParamService;
/**
 * 商品规格参数模板
 * @author Haochenxu
 *
 */
@Service
public class ItemParamServiceImpl implements ItemParamService {
	
	@Autowired
	private TbItemParamMapper itemParamMapper;
	/**
	 * 通过商品分类id查询是否存在模板
	 */
	public TaotaoResult getItemParamByCid(long cid) {
		TbItemParamExample example = new TbItemParamExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(cid);
		List<TbItemParam> list = 
				itemParamMapper.selectByExampleWithBLOBs(example);
		if(list != null && list.size()>0){
			return TaotaoResult.ok(list.get(0));
		}
		return TaotaoResult.ok();
	}

	/**
	 * 创建新的模板保存到数据库中
	 */
	public TaotaoResult insertItemParam(TbItemParam tbItemParam) {
		tbItemParam.setCreated(new Date());
		tbItemParam.setUpdated(new Date());
		itemParamMapper.insert(tbItemParam);
		return TaotaoResult.ok();
	}

}

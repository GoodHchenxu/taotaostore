package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

import com.taotao.service.ContentCategoryService;

/**
 * 内容分类服务管理查询
 * @author Haochenxu
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	public List<EUTreeNode> getContentCategory(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EUTreeNode> result = new ArrayList<>();
		for(TbContentCategory tbContentCategory : list){
			EUTreeNode node = new EUTreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			result.add(node);
		}
		return result;
	}

	/**
	 * 新增分类
	 */
	public TaotaoResult insertContentCategory(Long parentId, String name) {
		//创建一个pojo
		TbContentCategory contentCategory = new TbContentCategory();
		//设置Id，因为Id是自增长，需要使用主键返回，在Mapper中配置
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		contentCategory.setIsParent(false);
		contentCategory.setName(name);
		contentCategory.setSortOrder(1);
		contentCategory.setStatus(1);
		contentCategory.setParentId(parentId);
		contentCategoryMapper.insert(contentCategory);
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		//判断父节点的isParent是否为true，如果不是，则改为true
		if(!tbContentCategory.getIsParent()){
			tbContentCategory.setIsParent(true);
			//更新父节点
			contentCategoryMapper.updateByPrimaryKey(tbContentCategory);
		}
		return TaotaoResult.ok(contentCategory);
	}

	/**
	 * 删除内容分类
	 */
	public TaotaoResult deleteContentCategory(Long parentId, Long id) {
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		contentCategoryMapper.deleteByPrimaryKey(id);
		List<EUTreeNode> list = getContentCategory(parentId);
		if(list.isEmpty()){
			tbContentCategory.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(tbContentCategory);
		}
		return TaotaoResult.ok();
	}

	/**
	 * 重命名
	 */
	public TaotaoResult updateContentCategory(Long id, String name) {
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		tbContentCategory.setName(name);
		contentCategoryMapper.updateByPrimaryKey(tbContentCategory);
		return TaotaoResult.ok();
	}



}

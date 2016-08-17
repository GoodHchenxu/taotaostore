package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ContentCategoryService;

/**
 * 接收页面传过来的parentId
 * 根据id来查询分类节点列表
 * 返回一个list<EUTreeNode>
 * 需要相应json数据
 * @author Haochenxu
 *
 */
@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	/**
	 * 此处参数并不是parentId,而是一个不同的id，并且默认值是0，所以使用注解解决
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<EUTreeNode> getContentListByParentId(@RequestParam(value="id",defaultValue="0")long parentId){
		List<EUTreeNode> list = contentCategoryService.getContentCategory(parentId);
		return list;
	}
	/**
	 * 新增内容分类，调用service实现
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public TaotaoResult insertContentCategory(Long parentId,String name){
		TaotaoResult result = contentCategoryService.insertContentCategory(parentId, name);
		return result;
	}
	/**
	 * 删除内容分类
	 * @param parentId
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public TaotaoResult deleteContentCategory(Long parentId,Long id){
		TaotaoResult result = contentCategoryService.deleteContentCategory(parentId, id);
		return  result;
	}
	/**
	 * 重命名
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public TaotaoResult updateContentCategory(Long id,String name){
		TaotaoResult result = contentCategoryService.updateContentCategory(id, name);
		return result;
	}
	
}

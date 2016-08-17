package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDataGriResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;

/**
 * 通过点击内容列表展现内容
 * @author Haochenxu
 *
 */
@Controller
@RequestMapping("/content")
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	/**
	 * 通过页面传来的Id进行查询内容
	 * @param page
	 * @param rows
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/query/list")
	@ResponseBody
	public EUDataGriResult getContentByCategoryId(Integer page,Integer rows,Long categoryId){
		EUDataGriResult result = contentService.getContentByCategoryId(page, rows, categoryId);
		return result;
	}
	/**
	 * 通过点击提交按钮，新增内容
	 * 上传数据到数据库
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult insertContent(TbContent tbContent){
		TaotaoResult result = contentService.insertContent(tbContent);
		return result;
	}

}

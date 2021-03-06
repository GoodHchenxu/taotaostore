package com.taotao.common.pojo;
/**
 * easyUI树形控件节点格式
 * 用来封装数据
 * @author Haochenxu
 *
 */
public class EUTreeNode {
	
	private long id;
	private String text;
	private String state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}

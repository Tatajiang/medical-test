package com.jiang.medical.util;

public class RetInfo {
	
	/**
	 * 返回代码
	 */
	private Integer code;
	
	/**
	 * 返回消息
	 */
	private String message;

	/**
	 * 返回数据
	 */
	private String data;
	
	/**
	 * 返回对象数据
	 */
	private Object object;

	/**
	 * 当前页
	 */
	private Integer page;
	
	/**
	 * 总页数
	 */
	private Integer pageSize;
	
	/**
	 * 总页数
	 */
	private Integer totalPage;
	
	/**
	 * 总记录数
	 */
	private Integer totalCount;
	
	
	public static final Integer FAILURE = -1;
	
	public static final Integer SUCCESS = 0;
	
	public static final Integer NOLOGIN = 1;
	
	/** 
	 * @return code
	 */
	public Integer getCode() {
		return code;
	}

	/** 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/** 
	 * @return data
	 */
	public String getData() {
		return data;
	}

	/** 
	 * @return page
	 */
	public Integer getPage() {
		return page;
	}

	/** 
	 * @return pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/** 
	 * @return totalPage
	 */
	public Integer getTotalPage() {
		return totalPage;
	}

	/** 
	 * @return totalCount
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * <p>Title: </p> 
	 * <p>Description: </p> 
	 * @param code			返回代码
	 * @param message		返回消息
	 * @param data			返回数据
	 * @param page			当前页
	 * @param totalPage		总页数
	 * @param pageSize		页面大小	
	 * @param totalCount	总记录数
	 */
	public RetInfo(Integer code,String message,String data,Integer page,Integer totalPage,Integer pageSize,Integer totalCount){
		this.code = code;
		this.message = message;
		this.data = data;
		this.page = page;
		this.totalPage = totalPage;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
	}
	
	/**
	 * <p>Title: </p> 
	 * <p>Description: </p> 
	 * @param code			返回代码
	 * @param message		返回消息
	 * @param data			返回数据
	 */
	public RetInfo(Integer code,String message,String data){
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	/**
	 * <p>Title: </p> 
	 * <p>Description: </p> 
	 * @param code			返回代码
	 * @param message		返回消息
	 */
	public RetInfo(Integer code,String message){
		this.code = code;
		this.message = message;
		this.data = null;
	}
	
	/**
	 * <p>Title: </p> 
	 * <p>Description: </p> 
	 * @param code		返回代码
	 * @param obj		返回对象
	 */
	public RetInfo(Integer code, Object obj){
		this.code = code;
		this.object = obj;
	}
	
	/**
	 * <p>Title: </p> 
	 * <p>Description: </p> 
	 * @param code			返回代码
	 */
	public RetInfo(Integer code){
		this.code = code;
	}
	
	/**
	 * 新增直接返回对象数据
	 * @param code返回代码
	 * @param message		返回消息
	 * @param object		返回数据
	 * @author chenjingyuan
	 * @date 2018-01-23
	 */
	public RetInfo(Integer code,String message,Object object){
		this.code = code;
		this.message = message;
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}

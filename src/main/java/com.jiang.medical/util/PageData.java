package com.jiang.medical.util;

import java.util.ArrayList;
import java.util.List;

public class PageData {
	/**
	 * 默认开始位置（从下标0开始）
	 */
	public static final int START_SIZE = 0;
	/**
	 * 默认页大小（每页20条数据）
	 */
	public static final int LIMIT_SIZE = 20;
	/**
	 * 对数据进行分页
	 * @param list	源数据集
	 * @param startSize	开始位置
	 * @param limit	页面大小
	 * @return items 目标数据集
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List list(List<?> list, Integer startSize, Integer limit) {
		List items = new ArrayList();
		if (null == list || list.size() == 0) return items;
		
		if (null == startSize || startSize < 0) startSize = START_SIZE;
		if (null == limit || limit < 0) limit = LIMIT_SIZE;
		
		int num = 0;	//页开始
		for (int i=startSize; i<list.size(); i++) {
			if (num >= limit) break;//当页数量达到指定大小时退出
			items.add(list.get(i));
			num++;
		}
		return items;
	}
}

package com.rui.pro1.modules.sys.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rui.pro1.common.bean.page.Query;
import com.rui.pro1.common.bean.page.QueryResult;
import com.rui.pro1.modules.sys.entity.Dict;
import com.rui.pro1.modules.sys.exception.ObjectExistException;
import com.rui.pro1.modules.sys.mapper.DictMapper;
import com.rui.pro1.modules.sys.service.IDictService;

import freemarker.template.utility.StringUtil;

@Service
public class DictService implements IDictService {
	@Autowired
	private DictMapper dictMapper;

	@Override
	public QueryResult<Dict> getList(int page, int pagesize, Dict dict) {
		Query query = new Query();
		query.setBean(dict);
		query.setPageIndex(page);
		query.setPageSize(pagesize);

		// 组合分页信息
		QueryResult<Dict> queryResult = new QueryResult<Dict>();
		Long count = dictMapper.getCount(query);
		List<Dict> list = dictMapper.queryPages(query);
		
		// 总页数 和 取多少条
		queryResult.setCurrentPage(page);
		queryResult.setPages(count, pagesize);
		queryResult.setItems(list);
		return queryResult;
	}

	@Override
	public Dict get(int id) {
		return dictMapper.selectByPrimaryKey(id);
	}

	@Override
	public int del(int id) {
		return dictMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int add(Dict dict) throws Exception {
		
		if(dict==null||StringUtils.isBlank(dict.getType())||StringUtils.isBlank(dict.getName())||StringUtils.isBlank(dict.getValue())){
			return 0;
		}
		List<Dict> dicts=dictMapper.getByType(dict.getType());
		if(dicts!=null&&dicts.size()>0)
		{
			for(Dict d:dicts){
				if(dict.getName().equals(d.getName())||dict.getValue().equals(d.getValue())){
					throw new ObjectExistException(dict.getType()+"名称和值已存在！");
				}
			}
		}
		
		
		
		
		return dictMapper.insertSelective(dict);
	}

	@Override
	public int update(Dict dict) throws ObjectExistException {
		
		if(dict==null||StringUtils.isBlank(dict.getType())||StringUtils.isBlank(dict.getName())||StringUtils.isBlank(dict.getValue())){
			return 0;
		}
		List<Dict> dicts=dictMapper.getByType(dict.getType());
		if(dicts!=null&&dicts.size()>0)
		{
			for(Dict d:dicts){
				if(dict.getId()==d.getId()){
					continue;
				}
				if(dict.getName().equals(d.getName())||dict.getValue().equals(d.getValue())){
					throw new ObjectExistException(dict.getType()+"名称和值已存在！");
				}
			}
		}
		
		return dictMapper.updateByPrimaryKeySelective(dict);
	}

	@Override
	public List<Dict> getByType(String type) {
		return dictMapper.getByType(type);
	}

}

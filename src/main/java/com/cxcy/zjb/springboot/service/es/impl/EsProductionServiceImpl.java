/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ProductionServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.es.impl;

import com.cxcy.zjb.springboot.domain.*;
import com.cxcy.zjb.springboot.domain.es.EsProduction;
import com.cxcy.zjb.springboot.repository.CatagoryRepository;
import com.cxcy.zjb.springboot.repository.es.EsProductionRepository;
import com.cxcy.zjb.springboot.service.es.EsProductionService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.SearchParseException;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
@Service
public class EsProductionServiceImpl implements EsProductionService {

    @Autowired
    private EsProductionRepository esProductionRepository;

    @Autowired
    private CatagoryRepository catagoryRepository;


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private static final Pageable TOP_5_PAGEABLE = new PageRequest(0, 5);
    private static final String EMPTY_KEYWORD = "";


    @Override
    public EsProduction save(EsProduction esProduction) {
        return esProductionRepository.save(esProduction);
    }

    @Override
    public EsProduction findByPId(Long pId) {
        return esProductionRepository.findByProductionId(pId);
    }

    @Override
    public void deleteByPId(String id) {
        esProductionRepository.delete(id);
    }


    @Override
    public Page<EsProduction> listHotestEsProductions(String keyword, Pageable pageable) {
        Sort sort = new Sort(DESC,"readSize","commentSize","voteSize","createTime");
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        return esProductionRepository.findDistinctEsProductionByTitleContainingOrSummaryContaining(keyword, keyword, pageable);
    }
    @Override
    public Page<EsProduction> listEsProductions(Pageable pageable) {
        return esProductionRepository.findAll(pageable);
    }



}
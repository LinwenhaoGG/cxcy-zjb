package com.cxcy.zjb.springboot.repository.es;

import com.cxcy.zjb.springboot.domain.es.EsProduction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 *Production 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface EsProductionRepository extends ElasticsearchRepository<EsProduction,String> {

    /**
     * 模糊查询（去重）
     * @param title
     * @param summary
     * @param pageable
     * @return
     */
    Page<EsProduction> findDistinctEsProductionByTitleContainingOrSummaryContaining(String title, String summary, Pageable pageable);

    EsProduction findByProductionId(Long pId);
}

package com.cxcy.zjb.springboot.domain.es;

import com.cxcy.zjb.springboot.domain.Production;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Production 作品ES实体
 */
@Document(indexName = "production",type = "production")
@XmlRootElement// MediaType 转为 XML
@Data
public class EsProduction implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;                  //作品主键

    @Field(index = FieldIndex.not_analyzed)
    private Long productionId; // production 的 id

    private String title;             //作品标题

    private String summary;            //作品简介

    @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Timestamp uploadTime;     //作品上传时间

    @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段)
    private Integer pCheck = 1;            //审核情况标识，0表示已审核，1表示未审核，2表示审核未通过

    @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Integer voteSize = 0;     //点赞量

    @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Integer commentSize = 0;    //评论量

    @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Integer readSize = 0;       //访问、阅读量

    @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Long catagorys;          //作品类型,ca_id作为外键

    @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Long user;

    protected EsProduction() {  // JPA 的规范要求无参构造函数；设为 protected 防止直接使用
    }

    public EsProduction(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }

    public EsProduction(Long productionId, String title, String summary, Timestamp uploadTime, Integer pCheck, Integer voteSize, Integer commentSize, Integer readSize, Long catagorys, Long user) {
        this.productionId = productionId;
        this.title = title;
        this.summary = summary;
        this.uploadTime = uploadTime;
        this.pCheck = pCheck;
        this.voteSize = voteSize;
        this.commentSize = commentSize;
        this.readSize = readSize;
        this.catagorys = catagorys;
        this.user = user;
    }

    public EsProduction(Production production) {
        this.productionId = productionId;
        this.title = title;
        this.summary = summary;
        this.uploadTime = uploadTime;
        this.pCheck = pCheck;
        this.voteSize = voteSize;
        this.commentSize = commentSize;
        this.readSize = readSize;
        this.catagorys = catagorys;
        this.user = user;
    }

    public void update(Production production) {
        this.productionId = productionId;
        this.title = title;
        this.summary = summary;
        this.uploadTime = uploadTime;
        this.pCheck = pCheck;
        this.voteSize = voteSize;
        this.commentSize = commentSize;
        this.readSize = readSize;
        this.catagorys = catagorys;
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, title='%s', summary='%s']",
                productionId, title, summary);
    }
}

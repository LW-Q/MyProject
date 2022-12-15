package com.lw.project.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.lw.common.to.SkuEsModel;
import com.lw.project.search.config.LwElasticSearchConfig;
import com.lw.project.search.constant.EsConstant;
import com.lw.project.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        // 1、给es中建立索引，product，建立好映射关系

        // 2、给es中保存这些数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel model : skuEsModels) {
            IndexRequest request = new IndexRequest(EsConstant.PRODUCT_INDEX);
            request.id(model.getSkuId().toString());
            String s = JSON.toJSONString(model);
            request.source(s, XContentType.JSON);
            bulkRequest.add(request);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, LwElasticSearchConfig.COMMON_OPTIONS);

        // TODO 如果批量错误
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.info("商品上架完成：{},返回数据:{}",collect,bulk.toString());
        return b;
    }
}

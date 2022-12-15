package com.lw.project.search;

import com.alibaba.fastjson.JSON;
import com.lw.project.search.config.LwElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class LwSearchApplicationTests {
	@Autowired
	private RestHighLevelClient client;

	@Test
	void searchData() throws IOException {
		// 创建检索请求
		SearchRequest request = new SearchRequest();
		// 指定检索索引
		request.indices("bank");
		// 指定dsl语句， 检索条件
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		request.source(searchSourceBuilder);

		// 执行检索
		// 使用结果
		SearchResponse response = client.search(request, LwElasticSearchConfig.COMMON_OPTIONS);

	}
	@Data
	class User{
		private String userName;
		private String where;
		private Integer age;
	}
	@Test
	void indexData() throws IOException {
		IndexRequest request = new IndexRequest("test");
		request.id("1");
		User user = new User();
		user.setUserName("liwei");
		user.setAge(25);
		user.setWhere("anhui");
		String s = JSON.toJSONString(user);
		request.source(s,XContentType.JSON);

		// 执行操作
		IndexResponse index = client.index(request, LwElasticSearchConfig.COMMON_OPTIONS);

		// 操作有用得响应
		System.out.println(index);
	}
	@Test
	void contextLoads() {
		System.out.println(client);
	}

}

package com.lw.project.lwproduct;

import com.lw.common.utils.PageUtils;
import com.lw.project.lwproduct.entity.CategoryEntity;
import com.lw.project.lwproduct.service.BrandService;
import com.lw.project.lwproduct.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;


@Slf4j
@SpringBootTest
class LwProductApplicationTests {

	@Autowired
	BrandService brandService;
	@Autowired
	CategoryService categoryService;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Autowired
	RedissonClient redissonClient;

	@Test
	public void testRedisson() {
		System.out.println(redissonClient);
	}

	@Test
	public void testStringRedisTemplate(){
		ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
		operations.set("hello","world"+ UUID.randomUUID().toString());
		String hello = operations.get("hello");
		System.out.println("之前保存的数据："+hello);
	}
	@Test
	void test(){
		Map<String, Object> params = new HashMap<>();
		// page=1&limit=10&key=
		params.put("t","1670053083407");
		params.put("page","1");
		params.put("limit","10");
		params.put("key",null);
		PageUtils pageUtils = brandService.queryPage(params);
	}

	@Test
	void testCategoryPath(){
		Long[] paths = categoryService.findCategoryPath(225L);
		log.info("完整路径：{}",Arrays.toString(paths));
	}

}

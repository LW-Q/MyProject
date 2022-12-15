package com.lw.project.lwproduct;

import com.lw.common.utils.PageUtils;
import com.lw.project.lwproduct.entity.CategoryEntity;
import com.lw.project.lwproduct.service.BrandService;
import com.lw.project.lwproduct.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@SpringBootTest
class LwProductApplicationTests {

	@Autowired
	BrandService brandService;
	@Autowired
	CategoryService categoryService;

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

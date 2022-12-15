package com.lw.project.lwproduct.web;

import com.lw.project.lwproduct.entity.CategoryEntity;
import com.lw.project.lwproduct.service.CategoryService;
import com.lw.project.lwproduct.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;
    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        List<CategoryEntity> categoryEntities =  categoryService.getLevel1Categorys();
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/json/catalog.json")
    public Map<String, List<Catelog2Vo>>  getCatalogJson(){
        Map<String, List<Catelog2Vo>>  catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }
    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}

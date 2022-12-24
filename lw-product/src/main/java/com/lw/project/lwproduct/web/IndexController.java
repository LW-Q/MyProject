package com.lw.project.lwproduct.web;

import com.lw.project.lwproduct.entity.CategoryEntity;
import com.lw.project.lwproduct.service.CategoryService;
import com.lw.project.lwproduct.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
    private CategoryService categoryService;

    @Autowired
    private RedissonClient redisson;
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
        // 获取一把锁，只要锁名一样就是同一把
        RLock lock = redisson.getLock("my-lock");

        // 加锁,默认时间30s
        lock.lock();
        try {
            System.out.println("加锁成功，执行业务....."+Thread.currentThread().getId());
            Thread.sleep(20000);
        } catch (Exception e) {
        } finally{
            // 解锁
            System.out.println("释放锁....."+Thread.currentThread().getId());
            lock.unlock();
        }

        return "hello";
    }
}

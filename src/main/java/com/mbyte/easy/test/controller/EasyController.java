package com.mbyte.easy.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mbyte.easy.test.entity.Easy;
import com.mbyte.easy.test.service.IEasyService;
import com.mbyte.easy.common.controller.BaseController;
import com.mbyte.easy.common.web.AjaxResult;
import com.mbyte.easy.util.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* <p>
* 前端控制器
* </p>
* @author 会写代码的怪叔叔
* @since 2019-03-11
*/
@Controller
@RequestMapping("/test/easy")
public class EasyController extends BaseController  {

    private String prefix = "test/easy/";

    @Autowired
    private IEasyService easyService;

    /**
    * 查询列表
    *
    * @param model
    * @param pageNo
    * @param pageSize
    * @param easy
    * @return
    */
    @RequestMapping
    public String index(Model model,@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize, Easy easy) {
        Page<Easy> page = new Page<Easy>(pageNo, pageSize);
        QueryWrapper<Easy> queryWrapper = new QueryWrapper<Easy>();
        if(!ObjectUtils.isEmpty(easy.getName())) {
            queryWrapper = queryWrapper.like("name",easy.getName());
         }
        if(!ObjectUtils.isEmpty(easy.getPwd())) {
            queryWrapper = queryWrapper.like("pwd",easy.getPwd());
         }
        IPage<Easy> pageInfo = easyService.page(page, queryWrapper);
        model.addAttribute("searchInfo", easy);
        model.addAttribute("pageInfo", new PageInfo(pageInfo));
        return prefix+"list";
    }

    /**
    * 添加跳转页面
    * @return
    */
    @GetMapping("addBefore")
    public String addBefore(){
        return prefix+"add";
    }
    /**
    * 添加
    * @param easy
    * @return
    */
    @PostMapping("add")
    @ResponseBody
    public AjaxResult add(Easy easy){
        return toAjax(easyService.save(easy));
    }
    /**
    * 添加跳转页面
    * @return
    */
    @GetMapping("editBefore/{id}")
    public String editBefore(Model model,@PathVariable("id")Long id){
        model.addAttribute("easy",easyService.getById(id));
        return prefix+"edit";
    }
    /**
    * 添加
    * @param easy
    * @return
    */
    @PostMapping("edit")
    @ResponseBody
    public AjaxResult edit(Easy easy){
        return toAjax(easyService.updateById(easy));
    }
    /**
    * 删除
    * @param id
    * @return
    */
    @GetMapping("delete/{id}")
    @ResponseBody
    public AjaxResult delete(@PathVariable("id") Long id){
        return toAjax(easyService.removeById(id));
    }
    /**
    * 批量删除
    * @param ids
    * @return
    */
    @PostMapping("deleteAll")
    @ResponseBody
    public AjaxResult deleteAll(@RequestBody List<Long> ids){
        return toAjax(easyService.removeByIds(ids));
    }

}


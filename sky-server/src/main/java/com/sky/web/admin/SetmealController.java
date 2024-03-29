package com.sky.web.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    // 新增菜品套餐
    @CacheEvict(value = "setmealCache",key="#setmealDTO.categoryId")
    @PostMapping("/setmeal")
    public Result saveSetmealWithDish(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveSetmealWithDish(setmealDTO);
        return Result.success();
    }

    // 分页
    @GetMapping("/page")
    public Result getPage(SetmealPageDTO setmealPageDTO) {
        PageResult pageResult = setmealService.getPage(setmealPageDTO);
        return Result.success(pageResult);
    }

    // 回显套餐
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Long id) {
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    //
    //修改套餐
    @CacheEvict(value = "setmealCache",allEntries = true)
    @PutMapping
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
        setmealService.updateBySetmealId(setmealDTO);

        return Result.success();
    }


    // 修改套餐状态
    @CacheEvict(value = "setmealCache",allEntries = true)
    @PostMapping("status/{status}")
    public Result updateSetmealStatus(@PathVariable("status") Integer status,
                                      @RequestParam Long id) {
        setmealService.updateSetmealStatus(status,id);
        return Result.success();
    }

    //批量删除套餐
    @CacheEvict(value = "setmealCache",allEntries = true)
    @DeleteMapping
    public Result deleteSetmeal(@RequestParam ArrayList<Long> ids) {
        setmealService.deleteSetmeal(ids);
        return Result.success();
    }

}

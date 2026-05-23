package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = "dishCache", allEntries = true)
    public void save(DishDTO dto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);
        dish.setStatus(StatusConstant.ENABLE);
        dishMapper.insert(dish);

        Long dishId = dish.getId();
        if (dto.getFlavors() != null && !dto.getFlavors().isEmpty()) {
            for (DishFlavor flavor : dto.getFlavors()) {
                flavor.setDishId(dishId);
            }
            dishFlavorMapper.insertBatch(dto.getFlavors());
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<Dish> page = (Page<Dish>) dishMapper.pageQuery(
                dto.getName(), dto.getCategoryId(), dto.getStatus());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "dishCache", allEntries = true)
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (StatusConstant.ENABLE.equals(dish.getStatus())) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
            dishFlavorMapper.deleteByDishId(id);
            dishMapper.deleteById(id);
        }
    }

    @Override
    public DishVO getById(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        Category category = categoryMapper.getById(dish.getCategoryId());

        DishVO vo = new DishVO();
        BeanUtils.copyProperties(dish, vo);
        vo.setFlavors(flavors);
        if (category != null) {
            vo.setCategoryName(category.getName());
        }
        return vo;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "dishCache", allEntries = true)
    public void update(DishDTO dto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);
        dishMapper.update(dish);

        dishFlavorMapper.deleteByDishId(dto.getId());
        if (dto.getFlavors() != null && !dto.getFlavors().isEmpty()) {
            for (DishFlavor flavor : dto.getFlavors()) {
                flavor.setDishId(dto.getId());
            }
            dishFlavorMapper.insertBatch(dto.getFlavors());
        }
    }

    @Override
    @CacheEvict(cacheNames = "dishCache", allEntries = true)
    public void startOrStop(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.update(dish);
    }

    @Override
    @Cacheable(cacheNames = "dishCache", key = "#categoryId")
    public List<DishVO> listWithFlavor(Long categoryId) {
        List<Dish> dishes = dishMapper.listByCategoryId(categoryId);
        List<DishVO> voList = new ArrayList<>();
        for (Dish dish : dishes) {
            DishVO vo = new DishVO();
            BeanUtils.copyProperties(dish, vo);
            vo.setFlavors(dishFlavorMapper.getByDishId(dish.getId()));
            voList.add(vo);
        }
        return voList;
    }
}

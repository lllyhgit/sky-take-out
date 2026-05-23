package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public void save(SetmealDTO dto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto, setmeal);
        setmeal.setStatus(StatusConstant.ENABLE);
        setmealMapper.insert(setmeal);

        Long setmealId = setmeal.getId();
        if (dto.getSetmealDishes() != null && !dto.getSetmealDishes().isEmpty()) {
            for (SetmealDish sd : dto.getSetmealDishes()) {
                sd.setSetmealId(setmealId);
            }
            setmealDishMapper.insertBatch(dto.getSetmealDishes());
        }
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<Setmeal> page = (Page<Setmeal>) setmealMapper.pageQuery(
                dto.getName(), dto.getCategoryId(), dto.getStatus());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (StatusConstant.ENABLE.equals(setmeal.getStatus())) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
            setmealDishMapper.deleteBySetmealId(id);
            setmealMapper.deleteById(id);
        }
    }

    @Override
    public SetmealVO getById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        Category category = categoryMapper.getById(setmeal.getCategoryId());

        SetmealVO vo = new SetmealVO();
        BeanUtils.copyProperties(setmeal, vo);
        vo.setSetmealDishes(setmealDishes);
        if (category != null) {
            vo.setCategoryName(category.getName());
        }
        return vo;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public void update(SetmealDTO dto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto, setmeal);
        setmealMapper.update(setmeal);

        setmealDishMapper.deleteBySetmealId(dto.getId());
        if (dto.getSetmealDishes() != null && !dto.getSetmealDishes().isEmpty()) {
            for (SetmealDish sd : dto.getSetmealDishes()) {
                sd.setSetmealId(dto.getId());
            }
            setmealDishMapper.insertBatch(dto.getSetmealDishes());
        }
    }

    @Override
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        setmealMapper.update(setmeal);
    }

    @Override
    @Cacheable(cacheNames = "setmealCache", key = "#categoryId")
    public List<SetmealVO> list(Long categoryId) {
        List<Setmeal> setmeals = setmealMapper.listByCategoryId(categoryId);
        List<SetmealVO> voList = new ArrayList<>();
        for (Setmeal setmeal : setmeals) {
            SetmealVO vo = new SetmealVO();
            BeanUtils.copyProperties(setmeal, vo);
            List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(setmeal.getId());
            vo.setSetmealDishes(setmealDishes);
            voList.add(vo);
        }
        return voList;
    }
}

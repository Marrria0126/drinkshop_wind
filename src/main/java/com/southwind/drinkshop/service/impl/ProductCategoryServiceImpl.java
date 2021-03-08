package com.southwind.drinkshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.drinkshop.entity.ProductCategory;
import com.southwind.drinkshop.mapper.ProductCategoryMapper;
import com.southwind.drinkshop.service.ProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.southwind.drinkshop.vo.ProductCategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yihong
 * @since 2021-03-06
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public List<ProductCategoryVO> getAllProductCategoryVO(){



        //一级分类
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type",1);
        List<ProductCategory> levelOne = productCategoryMapper.selectList(wrapper);

        List<ProductCategoryVO> levelOneVO = levelOne.stream().map(e -> new ProductCategoryVO(e.getId(),e.getName())).collect(Collectors.toList());
        for (ProductCategoryVO levelOneProductCategoryVO : levelOneVO) {
            wrapper = new QueryWrapper();
            wrapper.eq("type",2);
            wrapper.eq("parent_id",levelOneProductCategoryVO.getId());
            List<ProductCategory> levelTwo = productCategoryMapper.selectList(wrapper);
            List<ProductCategoryVO> levelTwoVO = levelTwo.stream().map(e -> new ProductCategoryVO(e.getId(),e.getName())).collect(Collectors.toList());
            levelOneProductCategoryVO.setChildren(levelTwoVO);
            for (ProductCategoryVO levelTwoProductCategoryVO : levelTwoVO) {
                wrapper = new QueryWrapper();
                wrapper.eq("type",3);
                wrapper.eq("parent_id",levelTwoProductCategoryVO.getId());
                List<ProductCategory> levelThree = productCategoryMapper.selectList(wrapper);
                List<ProductCategoryVO> levelThreeVO = levelThree.stream().map(e -> new ProductCategoryVO(e.getId(),e.getName())).collect(Collectors.toList());
                levelTwoProductCategoryVO.setChildren(levelThreeVO);
            }
        }

        return levelOneVO;
    }
}

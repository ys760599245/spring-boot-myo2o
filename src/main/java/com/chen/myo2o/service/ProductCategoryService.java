package com.chen.myo2o.service;


import com.chen.myo2o.dto.ProductCategoryExecution;
import com.chen.myo2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    /**
     * 查询指定某个店铺下的所有商品类别信息
     *
     * @param shopId
     * @return List<ProductCategory>
     */
    List<ProductCategory> getByShopId(long shopId);

    /**
     * @param
     * @return
     * @throws RuntimeException
     */
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws RuntimeException;

    /**
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws RuntimeException
     */
    ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws RuntimeException;
}

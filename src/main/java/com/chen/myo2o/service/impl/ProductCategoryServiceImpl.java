package com.chen.myo2o.service.impl;

import com.chen.myo2o.dao.ProductCategoryDao;
import com.chen.myo2o.dao.ProductDao;
import com.chen.myo2o.dto.ProductCategoryExecution;
import com.chen.myo2o.entity.ProductCategory;
import com.chen.myo2o.enums.ProductCategoryStateEnum;
import com.chen.myo2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired(required = false)
    private ProductCategoryDao productCategoryDao;
    @Autowired(required = false)
    private ProductDao productDao;

    @Override
    public List<ProductCategory> getByShopId(long shopId) {

        List<ProductCategory> productCategories = productCategoryDao.queryByShopId(shopId);

        return productCategories;
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(
            List<ProductCategory> productCategoryList) throws RuntimeException {
        if (productCategoryList != null && productCategoryList.size() > 0) {
            try {
                int effectedNum = productCategoryDao
                        .batchInsertProductCategory(productCategoryList);
                if (effectedNum <= 0) {
                    throw new RuntimeException("店铺类别失败");
                } else {

                    return new ProductCategoryExecution(
                            ProductCategoryStateEnum.SUCCESS);
                }

            } catch (Exception e) {
                throw new RuntimeException("batchAddProductCategory error: "
                        + e.getMessage());
            }
        } else {
            return new ProductCategoryExecution(
                    ProductCategoryStateEnum.INNER_ERROR);
        }

    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(
            //将此商品类别下的商品的类别Id置空
            long productCategoryId, long shopId) throws RuntimeException {
        try {
            //解除tb_product里的商品与该Producategoryid的关联
            int effectedNum = productDao
                    .updateProductCategoryToNull(productCategoryId);
            if (effectedNum < 0) {
                throw new RuntimeException("商品类别更新失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("deleteProductCategory error: "
                    + e.getMessage());
        }
        try {
            //删除ProductCategory
            int effectedNum = productCategoryDao.deleteProductCategory(
                    productCategoryId, shopId);
            if (effectedNum <= 0) {
                throw new RuntimeException("店铺类别删除失败");
            } else {
                return new ProductCategoryExecution(
                        ProductCategoryStateEnum.SUCCESS);
            }

        } catch (Exception e) {
            throw new RuntimeException("deleteProductCategory error: "
                    + e.getMessage());
        }
    }

}

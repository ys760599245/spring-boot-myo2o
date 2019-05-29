package com.chen.myo2o.dao;

import com.chen.myo2o.entity.ProductSellDaily;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author yss
 * @date 2019/5/24 19:31
 * <p>
 * 包描述   com.chen.myo2o.dao
 * 类名称   spring-boot-myo2o
 * 类描述
 */
@Mapper
public interface ProductSellDailyDao {
    /**
     * 根据查询条件返回商品日销售的统计列表
     *
     * @param productSellDailyContion
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductSellDaily> queryProductSellDailyList(@Param("ProductSellDailyCondition") ProductSellDaily ProductSellDailyCondition, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 统计平台所有尚明的日销售量
     *
     * @return
     */
    int insertProductSellDaily();

    /**
     * * @return统计当天没有销售的商品 不销售量补全
     */
    int insertDefaultProductSellDaily();


}

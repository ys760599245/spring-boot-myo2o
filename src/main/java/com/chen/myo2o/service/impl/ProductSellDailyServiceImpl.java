package com.chen.myo2o.service.impl;

import com.chen.myo2o.dao.ProductSellDailyDao;
import com.chen.myo2o.entity.ProductSellDaily;
import com.chen.myo2o.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author yss
 * @date 2019/5/25 16:07
 * <p>
 * 包描述   com.chen.myo2o.service.impl
 * 类名称   spring-boot-myo2o
 * 类描述
 */
@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {
    private static Logger log = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);
    @Autowired(required = false)
    private ProductSellDailyDao productSellDailyDao;

    /**
     * 每日定时对所有店铺的商品销量进行统计
     */
    @Override
    public void dailyCalculate() {
        log.info("quartz跑起来啦");
        //统计在tb_user_product_map里面产生销量的每个店铺的各件商品的日销量
        productSellDailyDao.insertProductSellDaily();
        //统计余下商品的日销量 全部置为0 echart的数据请求
        productSellDailyDao.insertDefaultProductSellDaily();

    }

    /**
     * 根据查询条件返回商品日销售的统计列表
     *
     * @param ProductSellDailyCondition
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public List<ProductSellDaily> queryProductSellDailyList(ProductSellDaily ProductSellDailyCondition, Date beginTime, Date endTime) {
        return productSellDailyDao.queryProductSellDailyList(ProductSellDailyCondition, beginTime, endTime);
    }

    /**
     * 统计平台所有尚明的日销售量
     * .
     *
     * @return
     */
    @Override
    public int insertProductSellDaily() {
        return productSellDailyDao.insertProductSellDaily();
    }
}

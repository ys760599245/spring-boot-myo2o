package com.chen.myo2o.service.impl;

import com.chen.myo2o.dao.ProductDao;
import com.chen.myo2o.dao.ProductImgDao;
import com.chen.myo2o.dto.ProductExecution;
import com.chen.myo2o.entity.Product;
import com.chen.myo2o.entity.ProductImg;
import com.chen.myo2o.enums.ProductStateEnum;
import com.chen.myo2o.service.ProductService;
import com.chen.myo2o.util.FileUtil;
import com.chen.myo2o.util.ImageUtil;
import com.chen.myo2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired(required = false)
    private ProductDao productDao;
    @Autowired(required = false)
    private ProductImgDao productImgDao;

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        System.out.println("content===============:" + productCondition.getEnableStatus());
        System.out.println("productCondition.toString()==" + productCondition.toString() + "===pageIndex:" + pageIndex + "==pageSize:" + pageSize);
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
        int count = productDao.queryProductCount(productCondition);
        ProductExecution pe = new ProductExecution();
        pe.setProductList(productList);
        pe.setCount(count);
        return pe;
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductByProductId(productId);
    }

    @Override
    @Transactional
    // 1.处理缩略图，获取缩略图相对路径并且复制给Product
    // 2.往tb_product 写入商品信息，获取productid
    // 3.结合productid批量处理商品详情图
    // 4.将商品详情图列表插入tb_product_img中
    public ProductExecution addProduct(Product product, CommonsMultipartFile thumbnail,
                                       List<CommonsMultipartFile> productImgs) throws RuntimeException {
        // 空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            // 给商品设置默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            // 默认为上架状态
            product.setEnableStatus(1);
            // 若商品缩略图不为空添加
            if (thumbnail != null) {
                addThumbnail(product, thumbnail);
            }
            try {
                // 创建商品信息
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum <= 0) {
                    throw new RuntimeException("创建商品失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("创建商品失败:" + e.toString());
            }
            // 若商品详情图不为空则添加
            if (productImgs != null && productImgs.size() > 0) {
                addProductImgs(product, productImgs);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            // 传参为空则返回空值错误信息
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    //1.若缩略图参数有值 则处理缩略图
    //2.若原来存在缩略图则先删除在添加新图 之后获取缩略图的相对路径并且赋值给Product
    //3.若商品详情图列表参数有值 对商品详情图列表进行同样的操作
    //4. 将tb_product_img下面的该商品原先的商品详情图片记录全部清除
    //5. 更新tb_product以及tb_product_img的信息
    public ProductExecution modifyProduct(Product product, CommonsMultipartFile thumbnail,
                                          List<CommonsMultipartFile> productImgs) throws RuntimeException {
        //空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            //给商品设置默认的属性
            product.setLastEditTime(new Date());
            //若商品缩略图不为空而且原有的缩略图不为空 则删除原有的缩略图并且添加
            if (thumbnail != null) {
                //先获取一边原有的信息 因为原来的游戏里面有图片的地址
                Product tempProduct = productDao.queryProductByProductId(product.getProductId());
                if (tempProduct.getImgAddr() != null) {
                    FileUtil.deleteFile(tempProduct.getImgAddr());
                }
                addThumbnail(product, thumbnail);
            }
            //如果有新存入的的商品详情图 则将原来的删除，并且添加新的图片
            if (productImgs != null && productImgs.size() > 0) {
                deleteProductImgs(product.getProductId());
                addProductImgs(product, productImgs);
            }
            try {
                //更新商品信息
                int effectedNum = productDao.updateProduct(product);
                if (effectedNum <= 0) {
                    throw new RuntimeException("更新商品信息失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } catch (Exception e) {
                throw new RuntimeException("更新商品信息失败:" + e.toString());
            }
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 批量添加图片
     *
     * @param product
     * @param productImgs
     */
    private void addProductImgs(Product product, List<CommonsMultipartFile> productImgs) {
        // 获取图片的存储的路径这里直接放在相应店铺的文件夹下
        String dest = FileUtil.getShopImagePath(product.getShop().getShopId());
        List<String> imgAddrList = ImageUtil.generateNormalImgs(productImgs, dest);
        //如果确实有图片需要添加的 就执行批量添加操作
        if (imgAddrList != null && imgAddrList.size() > 0) {
            List<ProductImg> productImgList = new ArrayList<ProductImg>();
            // 遍历图片一次去处理 并且添加进去ProductImg实体类里面
            for (String imgAddr : imgAddrList) {
                ProductImg productImg = new ProductImg();
                productImg.setImgAddr(imgAddr);
                productImg.setProductId(product.getProductId());
                productImg.setCreateTime(new Date());
                productImgList.add(productImg);
            }
            try {
                int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if (effectedNum <= 0) {
                    throw new RuntimeException("创建商品详情图片失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("创建商品详情图片失败:" + e.toString());
            }
        }
    }

    /**
     * 删除某个下面的所有详情图
     *
     * @param productId
     */
    private void deleteProductImgs(long productId) {
        //根据productId 获取原来的图片
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        //干掉原来的图片
        for (ProductImg productImg : productImgList) {
            FileUtil.deleteFile(productImg.getImgAddr());
        }
        //删除数据库中原有图片的信息
        productImgDao.deleteProductImgByProductId(productId);
    }

    private void addThumbnail(Product product, CommonsMultipartFile thumbnail) {
        String dest = FileUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        product.setImgAddr(thumbnailAddr);
    }
}

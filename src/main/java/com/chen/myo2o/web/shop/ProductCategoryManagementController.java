package com.chen.myo2o.web.shop;

import com.chen.myo2o.dao.AreaDao;
import com.chen.myo2o.dao.PersonInfoDao;
import com.chen.myo2o.dao.ProductDao;
import com.chen.myo2o.dao.ProductSellDailyDao;
import com.chen.myo2o.dto.ProductCategoryExecution;
import com.chen.myo2o.dto.ProductExecution;
import com.chen.myo2o.dto.Result;
import com.chen.myo2o.entity.*;
import com.chen.myo2o.enums.ProductCategoryStateEnum;
import com.chen.myo2o.service.ProductCategoryService;
import com.chen.myo2o.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private ProductSellDailyDao productSellDailyDao;
    @Autowired(required = false)
    private ProductDao productDao;
    @Autowired(required = false)
    private ProductService productService;
    @Autowired(required = false)
    private PersonInfoDao personInfoDao;

    @RequestMapping(value = "/listproductcategorys", method = RequestMethod.GET)
    @ResponseBody
    private Result<List<ProductCategory>> listProductCategorys(HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> list = null;
        if (currentShop != null && currentShop.getShopId() > 0) {
            list = productCategoryService.getByShopId(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true, list);// WEB-INF/html/"shoplist".html
        } else {
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
        }
    }

    @RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
                                                    HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        for (ProductCategory pc : productCategoryList) {
            pc.setShopId(currentShop.getShopId());
        }
        if (productCategoryList != null && productCategoryList.size() > 0) {
            try {
                ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
                if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少输入一个商品类别");
        }
        return modelMap;
    }

    @RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (productCategoryId != null && productCategoryId > 0) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId,
                        currentShop.getShopId());
                if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少选择一个商品类别");
        }
        return modelMap;
    }

    @RequestMapping(value = "/listArea", method = RequestMethod.GET)
    @ResponseBody
    public Object listArea() {
        List<Area> areaList = areaDao.queryArea();
        return areaList;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    public Object add() {
        int i = productSellDailyDao.insertDefaultProductSellDaily();
        return i;
    }

    @RequestMapping(value = "/listProduct", method = RequestMethod.GET)
    @ResponseBody
    public Object listProduct() {
        Product productCondition = new Product();
        productCondition.setEnableStatus(1);
        ProductExecution productList = productService.getProductList(productCondition, 0, 3);
        return productList;
    }

    @RequestMapping(value = "/listPersoninfo", method = RequestMethod.GET)
    @ResponseBody
    public Object listPersoninfo() {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setEnableStatus(1);
        List<PersonInfo> personInfoList = personInfoDao.queryPersonInfoList(personInfo, 0, 3);
        return personInfoList;
    }

    @RequestMapping(value = "/lists", method = RequestMethod.GET)
    @ResponseBody
    public Object lists() {
        throw new RuntimeException();
    }
}

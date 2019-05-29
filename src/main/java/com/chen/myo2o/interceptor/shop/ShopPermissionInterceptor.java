package com.chen.myo2o.interceptor.shop;

import com.chen.myo2o.entity.Shop;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 760599245
 */
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {
    /**
     * 主要做事前拦截即用户操作 发生前 改写preHandle里面的逻辑 进行用户操作权限拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //从Session 中获取当前选择的店铺
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        @SuppressWarnings("unchecked")
        //从Session中获取当前用户可以操作的店铺列表
                List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
        //非空判断
        if (currentShop != null && shopList != null) {
            //遍历可以操作的店铺列表
            for (Shop shop : shopList) {
                //如果当前店铺在可以操作的列表里面返回为true 进行接下来的用户操作
                if (shop.getShopId().equals(currentShop.getShopId())) {
                    return true;
                }
            }
        }
        //如不满足拦截器的验证则返回为false 终止用户操作的执行
        return false;
    }
}

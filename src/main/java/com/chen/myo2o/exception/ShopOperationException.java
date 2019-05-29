package com.chen.myo2o.exception;

//店铺操作异常
public class ShopOperationException extends RuntimeException {

    private static final long serialVersionUID = -1148942449941113774L;

    public ShopOperationException(String msg) {
        super(msg);
    }

}

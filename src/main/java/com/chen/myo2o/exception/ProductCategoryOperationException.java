package com.chen.myo2o.exception;

//商品操作异常
public class ProductCategoryOperationException extends RuntimeException {

    private static final long serialVersionUID = 1414718467688187795L;

    public ProductCategoryOperationException(String msg) {
        super(msg);
    }

}

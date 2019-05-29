package com.chen.myo2o.service.impl;

import com.chen.myo2o.cache.JedisUtil;
import com.chen.myo2o.dao.AreaDao;
import com.chen.myo2o.dto.AreaExecution;
import com.chen.myo2o.entity.Area;
import com.chen.myo2o.enums.AreaStateEnum;
import com.chen.myo2o.service.AreaService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    private static String AREALISTKEY = "arealist";
    @Autowired
    private JedisUtil.Strings jedisStrings;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired(required = false)
    private AreaDao areaDao;

    @Override
    public List<Area> getAreaList() throws JsonParseException,
            JsonMappingException, IOException {
        //定义Redis的key
        String key = AREALISTKEY;
        //定义接受对象
        List<Area> areaList = null;
        //定义jackson数据转换操作类
        ObjectMapper mapper = new ObjectMapper();
        //判断key是否存在
        if (!jedisKeys.exists(key)) {
            //若不存在则从数据库中获取相应的数据
            areaList = areaDao.queryArea();
            //将相关的实体类集合转化成String 存到Redis对应的key中
            String jsonString = mapper.writeValueAsString(areaList);
            jedisStrings.set(key, jsonString);
        } else {
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory()
                    .constructParametricType(ArrayList.class, Area.class);
            areaList = mapper.readValue(jsonString, javaType);
        }
        return areaList;
    }

    @Override
    @Transactional
    public AreaExecution addArea(Area area) {
        if (area.getAreaName() != null && !"".equals(area.getAreaName())) {
            area.setCreateTime(new Date());
            area.setLastEditTime(new Date());
            try {
                int effectedNum = areaDao.insertArea(area);
                if (effectedNum > 0) {
                    String key = AREALISTKEY;
                    if (jedisKeys.exists(key)) {
                        jedisKeys.del(key);
                    }
                    return new AreaExecution(AreaStateEnum.SUCCESS, area);
                } else {
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("添加区域信息失败:" + e.toString());
            }
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public AreaExecution modifyArea(Area area) {
        if (area.getAreaId() != null && area.getAreaId() > 0) {
            area.setLastEditTime(new Date());
            try {
                int effectedNum = areaDao.updateArea(area);
                if (effectedNum > 0) {
                    String key = AREALISTKEY;
                    if (jedisKeys.exists(key)) {
                        jedisKeys.del(key);
                    }
                    return new AreaExecution(AreaStateEnum.SUCCESS, area);
                } else {
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("更新区域信息失败:" + e.toString());
            }
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public AreaExecution removeArea(long areaId) {
        if (areaId > 0) {
            try {
                int effectedNum = areaDao.deleteArea(areaId);
                if (effectedNum > 0) {
                    String key = AREALISTKEY;
                    if (jedisKeys.exists(key)) {
                        jedisKeys.del(key);
                    }
                    return new AreaExecution(AreaStateEnum.SUCCESS);
                } else {
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("删除区域信息失败:" + e.toString());
            }
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public AreaExecution removeAreaList(List<Long> areaIdList) {
        if (areaIdList != null && areaIdList.size() > 0) {
            try {
                int effectedNum = areaDao.batchDeleteArea(areaIdList);
                if (effectedNum > 0) {
                    String key = AREALISTKEY;
                    if (jedisKeys.exists(key)) {
                        jedisKeys.del(key);
                    }
                    return new AreaExecution(AreaStateEnum.SUCCESS);
                } else {
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("删除区域信息失败:" + e.toString());
            }
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }
}

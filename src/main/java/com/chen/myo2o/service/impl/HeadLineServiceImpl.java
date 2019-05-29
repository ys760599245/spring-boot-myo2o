package com.chen.myo2o.service.impl;

import com.chen.myo2o.cache.JedisUtil;
import com.chen.myo2o.dao.HeadLineDao;
import com.chen.myo2o.dto.HeadLineExecution;
import com.chen.myo2o.entity.HeadLine;
import com.chen.myo2o.enums.HeadLineStateEnum;
import com.chen.myo2o.service.HeadLineService;
import com.chen.myo2o.util.FileUtil;
import com.chen.myo2o.util.ImageUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class HeadLineServiceImpl implements HeadLineService {
    private static String HLLISTKEY = "headlinelist";
    @Autowired
    private JedisUtil.Strings jedisStrings;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired(required = false)
    private HeadLineDao headLineDao;

    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition)
            throws IOException {
        //定义接受对象
        List<HeadLine> headLineList = null;
        //定义jackson数据转换操作类
        ObjectMapper mapper = new ObjectMapper();
        //定义redis的key的前缀
        String key = HLLISTKEY;
        if (headLineCondition.getEnableStatus() != null) {
            //拼接出来Redis的key
            key = key + "_" + headLineCondition.getEnableStatus();
        }
        //判断key是否存在
        if (!jedisKeys.exists(key)) {
            //若不存在 则从数据库中直接取出
            headLineList = headLineDao.queryHeadLine(headLineCondition);
            //将相关实体类集合转换成String  存在Redis里面对应的key
            String jsonString = mapper.writeValueAsString(headLineList);
            jedisStrings.set(key, jsonString);
        } else {
            //若存在 则直接从Redis中直接取出 相关数据
            String jsonString = jedisStrings.get(key);
            //指定要将string类型转换成集合类型
            JavaType javaType = mapper.getTypeFactory()
                    .constructParametricType(ArrayList.class, HeadLine.class);
            //将先关的key对应的value里面String 转换成对象的实体类集合
            headLineList = mapper.readValue(jsonString, javaType);
        }
        return headLineList;
    }

    @Override
    @Transactional
    public HeadLineExecution addHeadLine(HeadLine headLine,
                                         CommonsMultipartFile thumbnail) {
        if (headLine != null) {
            headLine.setCreateTime(new Date());
            headLine.setLastEditTime(new Date());
            if (thumbnail != null) {
                addThumbnail(headLine, thumbnail);
            }
            try {
                int effectedNum = headLineDao.insertHeadLine(headLine);
                if (effectedNum > 0) {
                    String prefix = HLLISTKEY;
                    Set<String> keySet = jedisKeys.keys(prefix + "*");
                    for (String key : keySet) {
                        jedisKeys.del(key);
                    }
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS,
                            headLine);
                } else {
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("添加区域信息失败:" + e.toString());
            }
        } else {
            return new HeadLineExecution(HeadLineStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public HeadLineExecution modifyHeadLine(HeadLine headLine,
                                            CommonsMultipartFile thumbnail) {
        if (headLine.getLineId() != null && headLine.getLineId() > 0) {
            headLine.setLastEditTime(new Date());
            if (thumbnail != null) {
                HeadLine tempHeadLine = headLineDao.queryHeadLineById(headLine
                        .getLineId());
                if (tempHeadLine.getLineImg() != null) {
                    FileUtil.deleteFile(tempHeadLine.getLineImg());
                }
                addThumbnail(headLine, thumbnail);
            }
            try {
                int effectedNum = headLineDao.updateHeadLine(headLine);
                if (effectedNum > 0) {
                    String prefix = HLLISTKEY;
                    Set<String> keySet = jedisKeys.keys(prefix + "*");
                    for (String key : keySet) {
                        jedisKeys.del(key);
                    }
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS,
                            headLine);
                } else {
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("更新头条信息失败:" + e.toString());
            }
        } else {
            return new HeadLineExecution(HeadLineStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public HeadLineExecution removeHeadLine(long headLineId) {
        if (headLineId > 0) {
            try {
                HeadLine tempHeadLine = headLineDao
                        .queryHeadLineById(headLineId);
                if (tempHeadLine.getLineImg() != null) {
                    FileUtil.deleteFile(tempHeadLine.getLineImg());
                }
                int effectedNum = headLineDao.deleteHeadLine(headLineId);
                if (effectedNum > 0) {
                    String prefix = HLLISTKEY;
                    Set<String> keySet = jedisKeys.keys(prefix + "*");
                    for (String key : keySet) {
                        jedisKeys.del(key);
                    }
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS);
                } else {
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("删除头条信息失败:" + e.toString());
            }
        } else {
            return new HeadLineExecution(HeadLineStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public HeadLineExecution removeHeadLineList(List<Long> headLineIdList) {
        if (headLineIdList != null && headLineIdList.size() > 0) {
            try {
                List<HeadLine> headLineList = headLineDao
                        .queryHeadLineByIds(headLineIdList);
                for (HeadLine headLine : headLineList) {
                    if (headLine.getLineImg() != null) {
                        FileUtil.deleteFile(headLine.getLineImg());
                    }
                }
                int effectedNum = headLineDao
                        .batchDeleteHeadLine(headLineIdList);
                if (effectedNum > 0) {
                    String prefix = HLLISTKEY;
                    Set<String> keySet = jedisKeys.keys(prefix + "*");
                    for (String key : keySet) {
                        jedisKeys.del(key);
                    }
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS);
                } else {
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("删除头条信息失败:" + e.toString());
            }
        } else {
            return new HeadLineExecution(HeadLineStateEnum.EMPTY);
        }
    }

    private void addThumbnail(HeadLine headLine, CommonsMultipartFile thumbnail) {
        String dest = FileUtil.getHeadLineImagePath();
        String thumbnailAddr = ImageUtil.generateNormalImg(thumbnail, dest);
        headLine.setLineImg(thumbnailAddr);
    }

}

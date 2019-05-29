package com.chen.myo2o.service.impl;

import com.chen.myo2o.dao.AwardDao;
import com.chen.myo2o.dto.AwardExecution;
import com.chen.myo2o.entity.Award;
import com.chen.myo2o.enums.AwardStateEnum;
import com.chen.myo2o.service.AwardService;
import com.chen.myo2o.util.FileUtil;
import com.chen.myo2o.util.ImageUtil;
import com.chen.myo2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class AwardServiceImpl implements AwardService {

    @Autowired(required = false)
    private AwardDao awardDao;

    @Override
    public AwardExecution getAwardList(Award awardCondition, int pageIndex,
                                       int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        //根据查询条件分页获取出奖品列表信息
        List<Award> awardList = awardDao.queryAwardList(awardCondition,
                rowIndex, pageSize);
        //根据相同的查询条件返回查询条件的记录数
        int count = awardDao.queryAwardCount(awardCondition);
        AwardExecution ae = new AwardExecution();
        ae.setAwardList(awardList);
        ae.setCount(count);
        return ae;
    }

    @Override
    public Award getAwardById(long awardId) {
        return awardDao.queryAwardByAwardId(awardId);
    }

    @Override
    @Transactional
    //处理缩略图 获取缩略图相对路径并且赋值给award
    //向Tb_award写入奖品信息
    public AwardExecution addAward(Award award, CommonsMultipartFile thumbnail) {
        if (award != null && award.getShopId() != null) {
            //给award赋值 初始值
            award.setCreateTime(new Date());
            award.setLastEditTime(new Date());
            //award 默认可用 即 出现在前端展示系统
            award.setEnableStatus(1);
            if (thumbnail != null) {
                //若传入的图片的信息 不为空则更新图片
                addThumbnail(award, thumbnail);
            }
            try {
                //添加奖品信息
                int effectedNum = awardDao.insertAward(award);
                if (effectedNum <= 0) {
                    throw new RuntimeException("创建商品失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("创建商品失败:" + e.toString());
            }
            return new AwardExecution(AwardStateEnum.SUCCESS, award);
        } else {
            return new AwardExecution(AwardStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    //如素略图参数有值 则处理缩略图
    //若原先存在缩略图则删除在添加 之后获取缩略图相对路径并且赋值给Award
    //更新tb_award的信息
    public AwardExecution modifyAward(Award award,
                                      CommonsMultipartFile thumbnail) {
        if (award != null && award.getShopId() != null) {
            award.setLastEditTime(new Date());
            if (thumbnail != null) {
                //通过AwardID取出对应的实体类信息
                Award tempAward = awardDao.queryAwardByAwardId(award
                        .getAwardId());
                //如果传输过程中存在图片流 则删除图片
                if (tempAward.getAwardImg() != null) {
                    FileUtil.deleteFile(tempAward.getAwardImg());
                }
                //存在图片流获取相对路径
                addThumbnail(award, thumbnail);
            }
            try {
                //根据传入的实体类修改对应的信息
                int effectedNum = awardDao.updateAward(award);
                if (effectedNum <= 0) {
                    throw new RuntimeException("更新商品信息失败");
                }
                return new AwardExecution(AwardStateEnum.SUCCESS, award);
            } catch (Exception e) {
                throw new RuntimeException("更新商品信息失败:" + e.toString());
            }
        } else {
            return new AwardExecution(AwardStateEnum.EMPTY);
        }
    }

    private void addThumbnail(Award award, CommonsMultipartFile thumbnail) {
        String dest = FileUtil.getShopImagePath(award.getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        award.setAwardImg(thumbnailAddr);
    }

}

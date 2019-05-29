package com.chen.myo2o.dao;

import com.chen.myo2o.entity.Award;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author yss
 * @date 2019/5/24 17:14
 * <p>
 * 包描述   com.chen.myo2o.dao
 * 类名称   spring-boot-myo2o
 * 类描述
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardDaoTest {
    @Autowired
    private AwardDao awardDao;

    @Test
    public void testEDeleteAward() {
        Award awardCondition = new Award();
        awardCondition.setAwardName("测试");
        //查询出所有测试奖品并且删除
        List<Award> awardList = awardDao.queryAwardList(awardCondition, 0, 2);
        System.out.println(awardList.size() == 0);
        for (Award award : awardList) {
            int i = awardDao.deleteAward(award.getAwardId());
            System.out.println(i == 1);

        }

    }

    @Test
    public void testDUpdateAward() {
        Award awardCondition = new Award();
        awardCondition.setAwardName("测试一");
        //按照特定名字查询返回特定的奖品

        List<Award> awardList = awardDao.queryAwardList(awardCondition, 0, 1);
        System.out.println(awardList.size() == 0);
        //修改该商品的名称
        awardList.get(0).setAwardName("第一测试的奖品");

        int updateAward = awardDao.updateAward(awardList.get(0));
        System.out.println(updateAward == 1);
        //将修改名称后的奖品找出来并且验证
        Award award = awardDao.queryAwardByAwardId(awardList.get(0).getAwardId());
        System.out.println("第一测试的奖品" == award.getAwardName());
    }

}

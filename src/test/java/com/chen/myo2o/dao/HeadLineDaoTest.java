package com.chen.myo2o.dao;

import com.chen.myo2o.entity.HeadLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HeadLineDaoTest {
    @Autowired(required = false)
    private HeadLineDao headLineDao;

    @Test
    public void testAInsertHeadLine() throws Exception {
        HeadLine headLine = new HeadLine();
        headLine.setLineName("头条1");
        headLine.setLineLink("头条1");
        headLine.setLineImg("test1");
        headLine.setPriority(1);
        headLine.setCreateTime(new Date());
        headLine.setLastEditTime(new Date());
        headLine.setEnableStatus(1);
        int effectedNum = headLineDao.insertHeadLine(headLine);
        System.out.println(effectedNum);
        //assertEquals(1, effectedNum);
    }

    @Test
    public void testBQueryHeadLine() throws Exception {
        List<HeadLine> headLineList = headLineDao.queryHeadLine(new HeadLine());
        //assertEquals(2, headLineList.size());
        System.out.println(headLineList);
    }

    @Test
    public void testCUpdateHeadLine() throws Exception {
        HeadLine headLine = new HeadLine();
        headLine.setLineId(1L);
        headLine.setLineName("未来头条");
        headLine.setLastEditTime(new Date());
        int effectedNum = headLineDao.updateHeadLine(headLine);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testDDeleteHeadLine() throws Exception {
        long lineId = -1;
        List<HeadLine> headLineList = headLineDao.queryHeadLine(new HeadLine());
        for (HeadLine headLine : headLineList) {
            if ("头条1".equals(headLine.getLineName())) {
                lineId = headLine.getLineId();
            }
        }
        int effectedNum = headLineDao.deleteHeadLine(lineId);
        assertEquals(1, effectedNum);
    }
}

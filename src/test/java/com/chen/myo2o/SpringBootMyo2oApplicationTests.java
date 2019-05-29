package com.chen.myo2o;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootMyo2oApplicationTests {
    @Autowired
    private DataSource dataSource;

    @Test
    public void contextLoads() throws SQLException {

        Connection connection = dataSource.getConnection();
        System.out.println(connection.toString());
        System.out.println(connection.getClientInfo());
        System.out.println(connection.getClass());

        connection.close();
    }

}

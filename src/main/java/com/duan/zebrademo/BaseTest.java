package com.duan.zebrademo;

import com.duan.zebrademo.dao.SignInDao;
import com.duan.zebrademo.entity.SignInEntity;
import com.duan.zebrademo.util.P;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created on 2017/11/14.
 *
 * @author DuanJiaNing
 */
public class BaseTest {

    private static SignInDao signInDao;

    public static void main(String[] args) {
        new BaseTest().test();
    }

    @Transactional
    public void test() {


        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
        signInDao = context.getBean(SignInDao.class);

//        testInsert();
//        testBatchInsert();
//        testQueryById();
//        testDeleteById();
//        testUpdate();

//        long startTime = System.currentTimeMillis();
        final List<SignInEntity> list = signInDao.testSelect();
        list.forEach(System.out::println);
        System.out.println("----size "+list.size());
//        long endTime = System.currentTimeMillis();
//        long sqlCost = endTime - startTime;
//        System.out.println("--------------- cost time [" + sqlCost + "ms]");


    }


    private void testUpdate() {

        // 在此之前，确保执行过 testInsert
        for (Integer id : Arrays.asList(2, 122, 274)) {
            signInDao.updateSigninTime(id, new Timestamp(System.currentTimeMillis()));
            System.out.println(signInDao.get(id));
        }

    }

    private void testDeleteById() {

        signInDao.delete(133);
        System.out.println(signInDao.get(133)); // 正确删除，输出 null

    }

    private void testQueryById() {

        // 在此之前，确保执行过 testInsert
        // 5031 表3
        // 2981 表2

        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            // 三个数据将分表从表 signin0, signin1, signin3 中查询
            int id = r.nextInt(9999);
            List<SignInEntity> signs = signInDao.listByCustomerId(id);
//            for (SignInEntity sign : signs) {
//                P.ap.accept(sign);
//            }
        }
    }

    private void testInsert() {

        SignInEntity entity = new SignInEntity();
        Random r = new Random();
        // 路由规则将在三张表中插入数据，数据将平均分布
        for (int i = 1; i <= 50; i++) {
            entity.setId(i);
            entity.setType(r.nextInt(3) + 1);
            entity.setCustomerId(r.nextInt(999) + 1);
            entity.setCurrentSignInStoreId(r.nextInt(999) + 1);
            entity.setCreateEid(r.nextInt(999) + 1);
            entity.setDate(new Timestamp(System.currentTimeMillis()));
            signInDao.insert(entity);
        }

    }


    private void testBatchInsert() {
        SignInEntity entity = new SignInEntity();
        Random r = new Random();
        // 路由规则将在三张表中插入数据，数据将平均分布

        int c = 10;
        for (int j = 0; j < 10; j++) {
            List<SignInEntity> list = new ArrayList<>(c);
            for (int i = 1; i <= c; i++) {
                entity.setId(i);
                entity.setType(r.nextInt(3) + 1);
                entity.setCustomerId(r.nextInt(999) + 1);
                entity.setCurrentSignInStoreId(r.nextInt(999) + 1);
                entity.setCreateEid(r.nextInt(999) + 1);
                entity.setDate(new Timestamp(System.currentTimeMillis()));
                list.add(entity);
            }
            signInDao.batchInsert(list);
        }

    }
}

package com.wr.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            context = applicationContext;
            // ===== 在项目初始化bean后检验数据库连接是否
            DataSource dataSource = (DataSource) context.getBean("dataSource");
            dataSource.getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
            // ===== 当检测数据库连接失败时, 停止项目启动
            System.exit(-1);
        }
    }

    public ApplicationContext getApplicationContext() {
        return context;
    }

}

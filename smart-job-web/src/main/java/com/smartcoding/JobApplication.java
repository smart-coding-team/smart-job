package com.smartcoding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author wuque
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

public class JobApplication {

    private static Logger LOG = LoggerFactory.getLogger(JobApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
        LOG.info("=============================spring boot start successful !=============================");
    }
}

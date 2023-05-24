package cn.tedu.codeadmin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.tedu.codeadmin.mapper")
public class MybatisConfiguration {

    public MybatisConfiguration() {
        System.out.println("創建配置類:MybatisConfiguration");
    }
}

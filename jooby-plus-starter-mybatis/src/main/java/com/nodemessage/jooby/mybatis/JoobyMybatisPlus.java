package com.nodemessage.jooby.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author wjsmc
 * @date 2022/8/10 17:22
 * @description Mybatis Plus工具
 **/
public class JoobyMybatisPlus {

    public static MybatisConfiguration configuration;
    public static SqlSessionFactory sqlSessionFactory;
    public static byte init = 0;

    static {
        JoobyMybatisPlus joobyMybatisPlus = new JoobyMybatisPlus();

        configuration = new MybatisConfiguration();

        joobyMybatisPlus.initConfiguration(configuration);
        configuration.addInterceptor(joobyMybatisPlus.initInterceptor());
        configuration.setLogImpl(Slf4jImpl.class);
        configuration.addMappers(MybatisConfig.configEntity.getPackageName());

        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setSqlInjector(new DefaultSqlInjector());
        globalConfig.setIdentifierGenerator(new DefaultIdentifierGenerator());
        globalConfig.setSuperMapperClass(BaseMapper.class);
        GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);

        Environment environment = new Environment("1", new JdbcTransactionFactory(), joobyMybatisPlus.initDataSource());
        configuration.setEnvironment(environment);

        try {
            joobyMybatisPlus.registryMapperXml(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        sqlSessionFactory = sqlSessionFactoryBuilder.build(configuration);
    }

    private void initConfiguration(MybatisConfiguration configuration) {
        //开启驼峰大小写转换
        configuration.setMapUnderscoreToCamelCase(true);
        //配置添加数据自动返回数据主键
        configuration.setUseGeneratedKeys(true);
    }

    private DataSource initDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(MybatisConfig.configEntity.getUrl());
        dataSource.setDriverClassName(MybatisConfig.configEntity.getDriver());
        dataSource.setUsername(MybatisConfig.configEntity.getUsername());
        dataSource.setPassword(MybatisConfig.configEntity.getPassword());
        dataSource.setConnectionTestQuery("SELECT 1");
        return dataSource;
    }


    private Interceptor initInterceptor() {
        //创建mybatis-plus插件对象
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //构建分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    private void registryMapperXml(MybatisConfiguration configuration) throws IOException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> mapper = contextClassLoader.getResources(MybatisConfig.configEntity.getMapperPath().replace("classpath:", ""));
        while (mapper.hasMoreElements()) {
            URL url = mapper.nextElement();
            if ("file".equals(url.getProtocol())) {
                String path = url.getPath();
                File file = new File(path);
                File[] files = file.listFiles();
                for (File f : files) {
                    FileInputStream in = new FileInputStream(f);
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, f.getPath(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                    in.close();
                }
            } else {
                JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = urlConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    if (jarEntry.getName().endsWith(".xml") && jarEntry.getName().startsWith(MybatisConfig.configEntity.getMapperPath().replace("classpath:", ""))) {
                        InputStream in = jarFile.getInputStream(jarEntry);
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, jarEntry.getName(), configuration.getSqlFragments());
                        xmlMapperBuilder.parse();
                        in.close();
                    }
                }
            }
        }
    }

    public static <T> T getMapper(Class<T> tClass) {
        return configuration.getMapper(tClass, sqlSessionFactory.openSession());
    }

}

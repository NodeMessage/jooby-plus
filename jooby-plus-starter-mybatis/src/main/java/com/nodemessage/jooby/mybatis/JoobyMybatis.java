package com.nodemessage.jooby.mybatis;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wjsmc
 * @date 2022/8/9 11:32
 * @description
 **/
public class JoobyMybatis {

    public static SqlSessionFactory sqlSessionFactory = null;

    private static final ConcurrentHashMap<Class<?>, Object> mapperFactory = new ConcurrentHashMap<>();

    static {
        String resource = "mybatis-config.xml";
        try {
            addMappers(resource, Resources.getResourceAsStream(resource));
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private static void addMappers(String xmlPath, InputStream inputStream) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();
        Document xml = saxReader.read(inputStream);
        Element rootElement = xml.getRootElement();

        //设置连接属性
        Config application = ConfigFactory.load("application");
        String url = application.getString("mybatis.url");
        String driver = application.getString("mybatis.driver");
        String username = application.getString("mybatis.username");
        String password = application.getString("mybatis.password");
        Element environment = rootElement.element("environments").element("environment");
        environment.remove(environment.element("dataSource"));
        Element dataSource = DocumentHelper.createElement("dataSource");
        dataSource.addAttribute("type", "POOLED");
        environment.add(dataSource);
        for (int i = 0; i < 4; i++) {
            Element property = DocumentHelper.createElement("property");
            switch (i) {
                case 0:
                    property.addAttribute("name", "url");
                    property.addAttribute("value", url);
                    break;
                case 1:
                    property.addAttribute("name", "driver");
                    property.addAttribute("value", driver);
                    break;
                case 2:
                    property.addAttribute("name", "username");
                    property.addAttribute("value", username);
                    break;
                case 3:
                    property.addAttribute("name", "password");
                    property.addAttribute("value", password);
                    break;
                default:
                    throw new Error("请指定mybatis dataSource连接属性");
            }
            dataSource.add(property);
        }

        // 扫描mapper.xml
        rootElement.remove(rootElement.element("mappers"));
        Element mappers = DocumentHelper.createElement("mappers");
        rootElement.add(mappers);
        String mapperPath = application.getString("mybatis.mapper-path");
        if (mapperPath == null || "".equals(mapperPath)) {
            throw new Error("请指定mybatis mapper目录");
        } else if (mapperPath.startsWith("classpath:")) {
            mapperPath = mapperPath.replace("classpath:", "").trim();
            mapperPath = JoobyMybatis.class.getClassLoader().getResource(mapperPath).getPath().substring(1);
        } else {
            throw new Error("mybatis mapper目录" + mapperPath + "解析失败");
        }

        String finalMapperPath = mapperPath;
        Files.walkFileTree(Paths.get(mapperPath), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".xml")) {
                    Element element = DocumentHelper.createElement("mapper");
                    element.addAttribute("url", "file:///" + finalMapperPath + file.getFileName());
                    mappers.add(element);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(JoobyMybatis.class.getClassLoader().getResource(xmlPath).getPath().substring(1)), format);
        xmlWriter.write(xml);
        xmlWriter.close();
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(new FileInputStream(JoobyMybatis.class.getClassLoader().getResource(xmlPath).getPath().substring(1)));
    }

    public static <T> T getMapper(Class<T> mapper) {
        if (mapperFactory.containsKey(mapper)) {
            return (T) mapperFactory.get(mapper);
        } else {
            T result = sqlSessionFactory.openSession().getMapper(mapper);
            mapperFactory.put(mapper, result);
            return result;
        }
    }
}

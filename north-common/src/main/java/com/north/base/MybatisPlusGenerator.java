package com.north.base;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mybatisPlus生成器
 *
 * @Author zxn
 * @Date 2018-9-18 15:07
 */
public class MybatisPlusGenerator {

    public static void main(String[] args) {
        String TEMPLATE_PATH = "src\\main\\resources\\ftl\\";
        String CLASS_PATH = "D:\\mybatis";
        String packageName = "com.north.sys";
        String[] tableName = new String[]{"sys_log"};
//
//
//
//        createMybatisPlus(CLASS_PATH, packageName, tableName);
        createMybatisPlus(CLASS_PATH, packageName, tableName);
//        generator(TEMPLATE_PATH, CLASS_PATH, packageName, tableName);

    }

    private static void generator(String TEMPLATE_PATH, String CLASS_PATH, String packageName, String tableName) {
        List<TableInfo> tableInfoList = createMybatisPlus(CLASS_PATH, packageName, tableName);

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("title", tableInfoList.get(0).getComment());
        dataMap.put("entityName", tableInfoList.get(0).getEntityName());
        dataMap.put("serviceName", tableInfoList.get(0).getServiceName());
        dataMap.put("requestMapping", "/sys/dict");
        dataMap.put("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dataMap.put("tableInfoList", tableInfoList.get(0).getFields());
        createVue(TEMPLATE_PATH, CLASS_PATH,tableInfoList.get(0).getControllerName()+".java","controller.ftl", dataMap);
        createVue(TEMPLATE_PATH, CLASS_PATH,tableInfoList.get(0).getEntityName()+"ExcelService.java","excelService.ftl", dataMap);

        createVue(TEMPLATE_PATH, CLASS_PATH,"sysdict.vue","page.ftl", dataMap);

        List<TableField> fieldList= tableInfoList.get(0).getFields();
        fieldList = fieldList.subList(1,fieldList.size());
        dataMap.put("tableInfoList", fieldList);

        createVue(TEMPLATE_PATH, CLASS_PATH,"formModal.vue","formModal.ftl", dataMap);
    }

    private static List<TableInfo> createMybatisPlus(String CLASS_PATH, String packageName, String tableName) {
        AutoGenerator mpg = new AutoGenerator();
        // 选择 freemarker 引擎，默认 Veloctiy
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setAuthor("NorthZX");
        gc.setOutputDir(CLASS_PATH);
        gc.setFileOverride(false);// 是否覆盖同名文件，默认是false
        gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        /* 自定义文件命名，注意 %s 会自动填充表实体属性！ */
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.POSTGRE_SQL);
        dsc.setDriverName("org.postgresql.Driver");
        dsc.setUsername("postgres");
        dsc.setPassword("postgres");
        dsc.setUrl("jdbc:postgresql://47.96.23.168:5432/north");
        dsc.setTypeConvert(new PostgreSqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
//            @Override
//            public DbColumnType processTypeConvert(String fieldType) {
//                System.out.println("转换类型：" + fieldType);
//                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
//                return super.processTypeConvert(fieldType);
//            }
        });
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setTablePrefix(new String[] { "" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(new String[] { tableName }); // 需要生成的表
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
//        //自定义实体父类
//         strategy.setSuperEntityClass("com.north.base.BaseModel");
//        // 自定义实体，公共字段
//         strategy.setSuperEntityColumns(new String[] { "is_deleted", "revision", "created_by", "created_time", "updated_by", "update_time" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
        strategy.setSuperControllerClass("com.north.base.BaseController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(packageName);
//        pc.setModuleName("test");
        mpg.setPackageInfo(pc);

//        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("title", "==我的字典==");
//                this.setMap(map);
//            }
//        };
//
//        // 自定义 xxList.jsp 生成
//        List<FileOutConfig> focList = new ArrayList<>();
//        focList.add(new FileOutConfig("/template/list.jsp.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输入文件名称
//                return "D://my_" + tableInfo.getEntityName() + ".jsp";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);
//
//        // 调整 xml 生成目录演示
//        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return "/develop/code/xml/" + tableInfo.getEntityName() + ".xml";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);
//
//        // 关闭默认 xml 生成，调整生成 至 根目录
//        TemplateConfig tc = new TemplateConfig();
//        tc.setXml(null);
//        mpg.setTemplate(tc);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        // TemplateConfig tc = new TemplateConfig();
        // tc.setController("...");
        // tc.setEntity("...");
        // tc.setMapper("...");
        // tc.setXml("...");
        // tc.setService("...");
        // tc.setServiceImpl("...");
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        // mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();
        ConfigBuilder config = mpg.getConfig();
        return config.getTableInfoList();
    }

    private static void createVue(String TEMPLATE_PATH, String CLASS_PATH, String tableName, String ftlName, Map<String, Object> dataMap) {
        // step1 创建freeMarker配置实例
        Configuration configuration = new Configuration(Configuration. getVersion());
        Writer out = null;
        try {
            // step2 获取模版路径
            configuration.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));
            // step3 创建数据模型

            // step4 加载模版文件
            Template template = configuration.getTemplate(ftlName);
            // step5 生成数据

            File docFile = new File(CLASS_PATH + "\\vue\\" + tableName);
            Files.createDirectories(Paths.get(CLASS_PATH + "\\vue\\"));
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // step6 输出文件
            template.process(dataMap, out);
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^文件创建成功 !");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private static List<TableInfo> createMybatisPlus(String CLASS_PATH, String packageName,String[] tableName) {
        AutoGenerator mpg = new AutoGenerator();
        // 选择 freemarker 引擎，默认 Veloctiy
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setAuthor("NorthZX");
        gc.setOutputDir(CLASS_PATH);
        gc.setFileOverride(true);// 是否覆盖同名文件，默认是false
        gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(false);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        /* 自定义文件命名，注意 %s 会自动填充表实体属性！ */
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.POSTGRE_SQL);
        dsc.setDriverName("org.postgresql.Driver");
        dsc.setUsername("postgres");
        dsc.setPassword("postgres");
        dsc.setUrl("jdbc:postgresql://127.0.0.1:5432/north");
        dsc.setTypeConvert(new PostgreSqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
//            @Override
//            public DbColumnType processTypeConvert(String fieldType) {
//                System.out.println("转换类型：" + fieldType);
//                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
//                return super.processTypeConvert(fieldType);
//            }
        });
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setTablePrefix("");// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(tableName); // 需要生成的表

        strategy.setRestControllerStyle(true);
        // 自定义 controller 父类
        strategy.setSuperControllerClass("com.north.base.BaseController");
        //自定义实体父类
        strategy.setSuperEntityClass("com.north.base.BaseModel");
        // 自定义实体，公共字段
        strategy.setSuperEntityColumns(new String[] { "is_deleted", "revision", "created_by", "created_time", "updated_by", "update_time" });

        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(packageName);
//        pc.setModuleName("test");
        mpg.setPackageInfo(pc);

        // 执行生成
        mpg.execute();
        ConfigBuilder config = mpg.getConfig();
        return config.getTableInfoList();
    }
}



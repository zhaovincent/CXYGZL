package com.cxygzl.biz;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.anyline.data.jdbc.adapter.JDBCAdapter;
import org.anyline.metadata.Column;
import org.anyline.metadata.Index;
import org.anyline.metadata.Table;
import org.anyline.service.AnylineService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@MapperScan(basePackages = "com.cxygzl.biz.mapper")
@SpringBootApplication(scanBasePackages = {"com.cxygzl.biz","org.anyline"})
public class BizApp {
    public static void main(String[] args) {
        SpringApplication.run(BizApp.class, args);
        log.info("=====================Biz App  Start========================");
    }

    @Resource
    public AnylineService service;

    @PostConstruct
    @SneakyThrows
    public void init(){

        ThreadUtil.execute(()->{

            try {
                TimeUnit.SECONDS.sleep(20);
//
//        DataSourceHolder.reg("tes11t", SpringUtil.getBean(DataSource.class));
//        service.setDataSource("tes11t");

//        String[] beanNamesForType = SpringUtil.getBeanNamesForType(DataSource.class);
//
//        ClientHolder.reg(beanNamesForType[0]);
//        RuntimeHolder.reg(beanNamesForType[0], DataRunt);
//        service.setDataSource(beanNamesForType[0]);


                String name="ddddddd";

                //查询表结构
                Table table = service.metadata().table(name, false); //false表示不加载表结构，只简单查询表名
                //如果已存在 删除重键
                if(null != table){
                    service.ddl().drop(table);
                }
                table = new Table(name);
                //根据不同数据库长度精度有可能忽略
                Column column=new Column(StrUtil.format("{}_name","aa"),"decimal",30);
                column.setNullable(true);
                column.setScale(10);
                column.setComment("哈哈哈");
                table.addColumn(column);
//        table.addColumn("ID", "bigint",12,11).setPrimaryKey(true).setAutoIncrement(true).setComment("主键");
                table.addColumn("CODE", "varchar(20)").setComment("编号");
                table.addColumn("a", "longtext").setNullable(true).setComment("编号");
                table.addColumn("id", "bigint").setPrimaryKey(true).setComment("id主键");
                table.addColumn("create_time", "datetime").setComment("创建时间").setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
//        table.addColumn("NAME", "varchar(50)").setComment("名称");
//        table.addColumn("O_NAME", "varchar(50)").setComment("原列表");
//        table.addColumn("SALARY", "decimal(10,2)").setComment("精度").setNullable(false);
//        table.addColumn("DEL_COL", "varchar(50)").setComment("删除");
//        table.addColumn("CREATE_TIME", "datetime").setComment("创建时间").setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
                service.ddl().create(table);
                //获取执行的SQL
                List<String> ddls = table.ddls();
                for(String ddl:ddls){
                    System.out.println(ddl);
                }
                table = service.metadata().table(name);
        Index index = new Index();
        index.addColumn("id");

        index.setName("id_idx");
        index.setUnique(true);
        index.setTable(table);
                service.ddl().add(index);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }
}

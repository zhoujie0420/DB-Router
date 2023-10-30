package org.jiezhou.dynamic;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.jiezhou.DBContextHolder;
import org.jiezhou.annotation.DBRouterStrategy;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mybatis 拦截器  对sql进行拦截处理，修改分表信息
 */
//拦截MyBatis框架中StatementHandler接口的prepare方法。
// 具体来说，它会在方法执行前进行拦截，并执行自定义的逻辑。
// 其中，@Signature注解指定了要拦截的接口类型、方法名称和参数类型，
// 而@Intercepts注解则表示这是一个拦截器。通过使用这些注解，我们可以在MyBatis框架中添加自定义的拦截逻辑，以满足特定的需求。
@Intercepts({@Signature(type = StatementHandler.class,method = "prepare",args = {Connection.class,Integer.class})})
public class DynamicMybatisPlugin implements Interceptor {

    //提取表名
    private Pattern pattern = Pattern.compile("(from|into|update)[\\s]{1,}(\\w{1,})", Pattern.CASE_INSENSITIVE);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        //获取自定义注解，判断是否分库分表
        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        Class<?> clazz = Class.forName(className);
        DBRouterStrategy annotation = clazz.getAnnotation(DBRouterStrategy.class);
        if(annotation == null || !annotation.splitTable()) {
            return invocation.proceed();
        }

        //获取sql
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        // 替换sql表名
        Matcher matcher = pattern.matcher(sql);
        String tableName = null;
        if(matcher.find()){
            tableName = matcher.group().trim();
        }
        assert null != tableName;
        String replaceSql = matcher.replaceAll("tableName" + "_" + DBContextHolder.getTBKey());

        //通过反射修改sql
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, replaceSql);
        field.setAccessible(false);

        return invocation.getArgs();

    }
}

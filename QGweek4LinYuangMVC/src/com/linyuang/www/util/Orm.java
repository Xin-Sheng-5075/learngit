package com.linyuang.www.util;
import java.util.Arrays;

import com.linyuang.www.annotation.Column;
import com.linyuang.www.annotation.Id;
import com.linyuang.www.annotation.Table;

import java.lang.reflect.Field;

/**
 * @author Orm
 */
public class Orm<E> {
    /*---------------------------------添加对象到数据库---------------------------------*/
    /**
     * 添加一个对象
     * @param element 要添加的对象
     * @return 添加成功返回 1，否则返回 0
     */
    public int insert(E element) {
        isNull(element);
        Class clas = element.getClass();
        String tableName = getTableName(clas);
        Field[] fields = clas.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            throw new RuntimeException(element + "没有属性.");
        }

        String sql = getInsertSql(tableName, fields.length);
        Object[] params = getSqlParams(element, fields);
        System.out.println("insertSql = " + sql);
        System.out.println(Arrays.toString(params));
        return  DbUtils.excuteUpdate(sql, params);
    }

    /**
     * 根据对象获取sql语句的参数
     * @param element 值对象
     * @param fields 值对象包含的Field
     * @return sql 的参数
     */
    private Object[] getSqlParams(E element, Field[] fields) {
        Object[] params = new Object[fields.length];
        for (int i = 0; i < fields.length; i ++){
            fields[i].setAccessible(true);
            try {
                params[i] = fields[i].get(element);
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
                System.out.println("获取" + element + "的属性值失败！");
                // e.printStackTrace();
            }
        }
        return params;
    }

    /**
     * 插入对象的sql语句
     * @param tableName 表名称
     * @param length 字段长度
     * @return 插入记录的sql语句
     */
    private String getInsertSql(String tableName, int length) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tableName).append(" values(");
        // 添加参数占位符?
        for (int i = 0; i < length; i ++)
        {
            sql.append("?,");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(")");
        return sql.toString();
    }
    /**
     *@Auther Yuang
     *@Description //TODO 删除表中数据
     *@Date
     *@Param [element]
     *@return int
     **/

    public int delete(E element){
        isNull(element);
        Class clas = element.getClass();
        Field[] fields = clas.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            throw new RuntimeException(element + "没有属性.");
        }
        Object[] params = new Object[1];
        String sql = getDeleteSql(element,params);
        return DbUtils.excuteUpdate(sql,params);
    }

    private String getDeleteSql(E element,Object[] param){
        Class clazz = element.getClass();
        String tableName = getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        String idName = new String();
        for (int i = 0; i < fields.length; i ++) {
            fields[i].setAccessible(true);
            // 找到id对应的列名和值
            if (fields[i].isAnnotationPresent(Id.class)){
                idName = fields[0].getAnnotation(Id.class).name();
                try {
                    param[0] = fields[i].get(element);  // id作为update sql 的最后一个参数
                    if (param[0] == null) {
                        throw new RuntimeException(element + "没有Id属性!");
                    }
                } catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                    System.out.println("获取" + element + "的属性值失败！");
                }
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(tableName).append(" ");
        sql.append("where ").append(idName).append(" = ?");
        return sql.toString();
    }

    /*---------------------------------更新对象到数据库---------------------------------*/
    /**
     * 更新一个对象
     * @param element 待更新的对象
     * @return 若成功更新则返回1，否则返回0
     */
    public int update(E element) {
        isNull(element);
        Class clazz = element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            throw new RuntimeException(element + "没有属性.");
        }
        Object[] params = new Object[fields.length];
        String sql = getUpdateSqlAndParams(element, params);
        System.out.println("update sql = " + sql);
        System.out.println("params = " + Arrays.toString(params));
        return DbUtils.excuteUpdate(sql, params);
    }

    /**
     * 获取更新记录的sql语句和参数
     * @param element 对象
     * @param params 新的参数数组
     * @return update sql 和 sql语句的参数
     */
    private String getUpdateSqlAndParams(E element, Object[] params) {
        Class clazz = element.getClass();
        String tableName = getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();

        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update ").append(tableName).append(" set ");
        String idName = "";
        // 记录参数的位置
        int index = 0;

        for (int i = 0; i < fields.length; i ++){
            fields[i].setAccessible(true);
            // 找到id对应的列名和值
            if (fields[i].isAnnotationPresent(Id.class)){
                idName = fields[0].getAnnotation(Id.class).name();
                try {
                    params[params.length-1] = fields[i].get(element);  // id作为update sql 的最后一个参数
                    if (params[params.length-1] == null) {
                        throw new RuntimeException(element + "没有Id属性!");
                    }
                } catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                    System.out.println("获取" + element + "的属性值失败！");
                }
            }
            boolean isPresent = fields[i].isAnnotationPresent(Column.class);
            if (isPresent) {
                Column column = fields[i].getAnnotation(Column.class);
                String columnName = column.name();
                updateSql.append(" ").append(columnName).append( " = ? ,");
                // update sql 的参数
                try {
                    params[index++] = fields[i].get(element);  // 添加参数到数组，并更新下标
                } catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                    System.out.println("获取" + element + "的属性值失败！");
                }
            }
        }
        updateSql.deleteCharAt(updateSql.length()-1);
        updateSql.append("where ").append(idName).append(" = ?");
        return updateSql.toString();
    }

    /**
     * 根据值对象的注解获取其对应的表名称
     * @param clazz 值对象的字节码
     * @return 表名称
     */
    private String getTableName(Class<E> clazz) {
        boolean existTableAnno = clazz.isAnnotationPresent(Table.class);
        if (!existTableAnno) {
            throw new RuntimeException(clazz + " 没有Table注解.");
        }
        Table tableAnno = (Table)clazz.getAnnotation(Table.class);
        return tableAnno.name();
    }

    private boolean isNull(E ele){
        if (ele == null) {
            throw new IllegalArgumentException("插入的元素为空.");
        }
        return true;
    }
}
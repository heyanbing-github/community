package com.jpx.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装jdbc的常用操作
 * 主要包含增删改查的几个操作
 * @author HASEE
 * 1.加载驱动  在同一个项目其实只需要加载一次驱动即可
 * 2.获取连接
 * 3.回去执行器（预处理器）
 * 4.执行操作
 * 5.处理结果
 * 6.释放资源
 *	
 */
public class SqlUtils {
	

	private static DataSource dataSource;

	static {


		try {
			dataSource = DruidDataSourceFactory.createDataSource(PropertiesUtils.getProperties());
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	/**
	 * 获取连接
	 * @return
	 */
	public static Connection getConnection() {
		try {
			// jdbc:mysql://127.0.0.1:3306/test3  如果连接的是本机的mysql并且端口号为3306 那么可以简写jdbc:mysql:///test3
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 释放资源
	 * @param rs
	 * @param pst
	 */
	public static void close(ResultSet rs,PreparedStatement pst,Connection conn) {
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(pst != null){
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 统一执行增删改的操作
	 * @param sql  执行增删改的sql语句   类似 delete from user where uid=?
	 * @param params  sql 执行所需的参数
	 * @return
	 */
	public static int update(String sql,Object... params) {
		//获取连接
		Connection conn = getConnection();

		if(conn != null) {
			PreparedStatement pst = null;

			int result = 0;
			try {
				pst = conn.prepareStatement(sql);
				for (int i = 1; i <= params.length; i++) {
					pst.setObject(i, params[i-1]);
				}
				result = pst.executeUpdate();//执行增删改的操作
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				//释放资源
				close(null, pst, conn);
			}
			return result;
		}
		return 0;
	}
	
	/**
	 * 执行查询操作
	 * select * from user
	 * 
	 * 
	 * @param <T>  方法泛型
	 * @param clazz 查询的对象
	 * @param sql 查询的sql语句
	 * @param params  sql语句所需的参数
	 * @return
	 */
	public static <T> List<T> select(Class<T> clazz,String sql,Object... params){
		Connection conn = getConnection();
		if(conn != null) {
			PreparedStatement pst = null;
			ResultSet rs = null;
			List<T> list = new ArrayList<T>();
			try {
				pst = conn.prepareStatement(sql);
				//注入参数
				for (int i = 1; i <= params.length; i++) {
					pst.setObject(i, params[i-1]);
				}
				rs = pst.executeQuery();//执行查询操作
				//处理查询的结果集
				while(rs.next()) {//如果结果集中有数据就进入循环
					//每一条数据就代表了一个对象
					T obj = clazz.newInstance();//通过无参构造方法创建对象
					list.add(obj);
					//需要知道这一行数据有多少个字段
					//获取结果集中的元数据  包含了 结果集中有多少个字段 每个字段叫什么名字等信息
					ResultSetMetaData metaData = rs.getMetaData();
					//循环每个字段
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						//获取每个字段名
						String columnName = metaData.getColumnName(i);
						
						//需要找到属性的类型是什么  
						Field field = clazz.getDeclaredField(columnName);//
						String first = columnName.charAt(0)+"";
						//找到对应的set方法
						String methodName = "set" + first.toUpperCase() + columnName.substring(1);
						//通过反射获取到set方法
						Method method = clazz.getDeclaredMethod(methodName, field.getType());
						//根据类型执行不同地方set方法
						if(field.getType() == int.class || field.getType() == Integer.class) {
							method.invoke(obj, rs.getInt(columnName));
						} else if(field.getType() == double.class || field.getType() == Double.class) {
							method.invoke(obj, rs.getDouble(columnName));
						} else if(field.getType() == String.class) {
							method.invoke(obj, rs.getString(columnName));
						} else {//每个字段的值
							Object value = rs.getObject(columnName);
							method.invoke(obj, value);
						}
					}
					
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			} catch (NumberFormatException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			} finally {
				close(rs, pst, conn);
			}
			return list;
			
		}
		return null;
	}
	/**
	 * 将对象插入数据库
	 * insert into 表名 (字段1，字段2...) values (?,?,?....)
	 * @param obj
	 * @return
	 */
	public static int insert (Object obj) {
		//需要将对象变成sql和参数
		//sql创建
		StringBuilder sqlBuilder = new StringBuilder();
		//values创建
		StringBuilder valueBuilder = new StringBuilder();
		String sql = "";
		//参数集合
		List<Object> params = new ArrayList<Object>();

		sqlBuilder.append("insert into ");
		//添加表名  表名和类名相同  字段名和属性名相同
		Class clazz = obj.getClass();
		sqlBuilder.append(clazz.getSimpleName());//只添加类型
		sqlBuilder.append(" (");
		//通过反射获取对象里面的字段
		Field [] fields = clazz.getDeclaredFields();
		valueBuilder.append(") values (");
		try {
			//循环属性 拼接sql
			for (Field field : fields) {
				field.setAccessible(true);
				Object o = field.get(obj);
				if(o != null) {
					sqlBuilder.append(field.getName());//字段名
					sqlBuilder.append(",");
					valueBuilder.append("?,");
					params.add(o);
				}
			}
			// sql = "insert into 表名  (字段1，字段2,"
			// values = ") values (?,?,";
			//需要将sql和values最后的，去掉
			sql = sqlBuilder.substring(0, sqlBuilder.length()-1) + valueBuilder.substring(0, valueBuilder.length()-1) + ")";
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		//调用执行增删改的方法
		return update(sql, params.toArray());
	}
	
	/**
	 * 根据对象修改数据库的表
	 * 通过主键修改其他的属性值
	 * 要求表都应该有主键
	 * 约定属性里面以id结尾的是主键
	 * update 表名 set 字段1=?,字段2=? ... where 主键=?
	 * @param obj
	 * @return
	 */
	public static int update(Object obj) {
		//sql构建
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update ");
		//参数集合
		List<Object> params = new ArrayList<Object>();
		Class clazz = obj.getClass();
		//拼接表名
		sqlBuilder.append(clazz.getSimpleName());
		sqlBuilder.append(" set ");
		//循环字段
		Field [] fields = clazz.getDeclaredFields();
		String primaryName = null;
		Object primaryValue = null;
		for (Field field : fields) {
			field.setAccessible(true);
			Object o = null;
			try {
				o = field.get(obj);//获取属性值
				String fieldName = field.getName();//获取属性名
				if(o != null && !fieldName.endsWith("id")) {
					sqlBuilder.append(fieldName);//字段名
					sqlBuilder.append("=?,");
					params.add(o);
				} else if(fieldName.endsWith("id")) {//如果是主键
					primaryName = fieldName;
					primaryValue = o;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		if(params.isEmpty()) {//如果没有要修改的值 就直接返回
			return 0;
		}
		//去掉最后的逗号
		String sql = sqlBuilder.substring(0, sqlBuilder.length()-1);
		sql += " where " + primaryName + "=?";
		//将主键的值添加到集合中去
		params.add(primaryValue);
		return update(sql, params.toArray());
	}

	/**
	 * 执行一个insert语句 返回insert后自动生成的主键
	 * @param
	 * @return
	 */
	public static int insert (String sql,Object...params) {
		//需要将对象变成sql和参数
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int id = 0;
		try {
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0 ;i < params.length;i++){
				pst.setObject(i+1,params[i]);
			}
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs,pst,conn);
		}

		return id;

	}



	/**
	 * 查询单个对象的
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param params
	 * @return
	 */
	public static <T> T selectOne(Class<T> clazz,String sql,Object...params) {
		List<T> list = select(clazz, sql, params);
		return list.isEmpty() ? null : list.get(0);
	}
	
	
	
	
	/**
	 * 多表查询  ，也可以用连接查询
	 * select * from book left join bookType on book.btid=booktype.btid 
	 * class [] = {Book.class,BookType.class}
	 * @param classes  最后要封装的对象的类型
	 * @param sql 
	 * @param params
	 * @return  List<List>  外面的List是行  里面的list是每一行可以封装多个对象
	 */
	public static List<List> select (Class [] classes,String sql,Object... params){
		Connection conn = getConnection();
		if(conn != null) {
			List<List> list = new ArrayList<List>();
			PreparedStatement pst = null;
			ResultSet rs = null;
			try {
				pst = conn.prepareStatement(sql);
				for (int i = 0; i < params.length; i++) {
					pst.setObject(i+1, params[i]);
				}
				rs = pst.executeQuery();
				while(rs.next()) {//有数据
					//存放对象的集合
					List datas = new ArrayList();
					for (Class clazz : classes) {
						//反射生成对象，放入集合中
						datas.add(clazz.newInstance());
					}
					list.add(datas);//将对象集合放入外层集合
					//将查询出来的数据放入对象中
					ResultSetMetaData metaData = rs.getMetaData();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						//每个字段的名字
						String columnName = metaData.getColumnName(i);
						//判断一下这个字段是属于哪一个class
						for (int j = 0; j < classes.length; j++) {
							//从类中去获取
							Field field = ClassUtils.getField(classes[j], columnName);
							if(field != null) {//如果这个类中存在了
								Object o = datas.get(j);//找个该类对应的对象
								//执行set方法
								String first = columnName.charAt(0)+"";
								//找到对应的set方法
								String methodName = "set" + first.toUpperCase() + columnName.substring(1);
								//通过反射获取到set方法
								Method method = classes[j].getDeclaredMethod(methodName, field.getType());
								//根据类型执行不同地方set方法
								if(field.getType() == int.class || field.getType() == Integer.class) {
									method.invoke(o, rs.getInt(columnName));
								} else if(field.getType() == double.class || field.getType() == Double.class) {
									method.invoke(o, rs.getDouble(columnName));
								} else if(field.getType() == String.class) {
									method.invoke(o, rs.getString(columnName));
								} else {//每个字段的值
									Object value = rs.getObject(columnName);
									method.invoke(o, value);
								}
							}
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} finally{
				close(rs,pst,conn);
			}
			
			return list;
		}
		return null;
	}
	
	/**
	 * 多表查询  ，也可以用连接查询
	 * select * from book left join bookType on book.btid=booktype.btid 
	 * class [] = {Book.class,BookType.class}
	 *   最后要封装的对象的类型
	 * @param sql 
	 * @param params
	 * @return  List<List>  外面的List是行  里面的list是每一行可以封装多个对象
	 */
	public static List<Map<String, Object>> select (String sql,Object... params){
		Connection conn = getConnection();
		if(conn != null) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			PreparedStatement pst = null;
			ResultSet rs = null;
			try {
				pst = conn.prepareStatement(sql);
				for (int i = 0; i < params.length; i++) {
					pst.setObject(i+1, params[i]);
				}
				rs = pst.executeQuery();
				while(rs.next()) {//有数据
					//存放对象的集合
					Map<String, Object> datas = new HashMap<String, Object>();
					list.add(datas);//将对象集合放入外层集合
					//将查询出来的数据放入对象中
					ResultSetMetaData metaData = rs.getMetaData();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						//每个字段的名字
						String columnName = metaData.getColumnName(i);
						//字段的值
						Object value = rs.getObject(columnName);
						datas.put(columnName, value);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally{
				close(rs,pst,conn);
			}
			
			return list;
		}
		return null;
	}
	

	
}

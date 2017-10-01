package com.jsan.convert;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jsan.convert.annotation.ConvertServiceRegister;
import com.jsan.convert.annotation.DateTimePattern;
import com.jsan.convert.annotation.NumberPattern;

public class BeanConvertUtilsTest {

	@Test
	public void testFoo() {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("id", "1265.0132");
		map.put("name", "张三");
		// map.put("sex", "On");
		// map.put("salary", "12,123.15");
		// map.put("money", "12,123.15");
		// map.put("selected", "1, a, b , c , d, e ,f, 9 ");
		// map.put("map", "{\"a\":\"123.11\",b:444.566,c:656}");

		map.put("date", "2008年05月18-日"); // 日期的 parse 和 format 比较耗时，一个操作约 100 微妙左右
		// map.put("list", "[2010年05月10日,2011年12月18日,2012年08月20日,2011年12月18日,2012年08月20日]");

		ConvertService convertService = new SplitTrimConvertService();

		FooBean testBean = BeanConvertUtils.getObject(FooBean.class, map, convertService);
		testBean = BeanConvertUtils.getObject(FooBean.class, map, convertService);
		testBean = BeanConvertUtils.getObject(FooBean.class, map, convertService);

		long start = System.nanoTime();

		testBean = BeanConvertUtils.getObject(FooBean.class, map, convertService);

		long end = System.nanoTime() - start;

		System.out.println("time1：" + end);

		testBean = BeanConvertUtils.getObject(FooBean.class, map, new SplitTrimConvertService());

		start = System.nanoTime();

		testBean = BeanConvertUtils.getObject(FooBean.class, map, new SplitTrimConvertService());
		end = System.nanoTime() - start;

		System.out.println("time2：" + end);

		System.out.println(testBean);

	}
	
	/**
	 * 转换成枚举类型的时候如果第一次转换不成功，第二次将转换成大写再试一次。
	 * 
	 */
	@Test
	public void testBar() {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 123);
		map.put("name", "Jack");
		map.put("color", "red"); // 可以转换

		BarBean barBean = BeanConvertUtils.getObject(BarBean.class, map);
		System.out.println(barBean);

		barBean.setName("Tom");

		System.out.println(BeanConvertUtils.getMap(barBean));
		System.out.println(BeanConvertUtils.getMapBaseOnReadMethod(barBean));

		long s = System.nanoTime();

		for (int i = 0; i < 10000; i++) {
			BeanConvertUtils.getMap(barBean);
		}

		long e = System.nanoTime() - s;

		System.out.println(e);
	}

}

enum Color {
	GREEN, BLUE, RED
}

class BarBean {

	int id;
	String name;
	Color color;

	public boolean getSex() {

		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "BarBean [id=" + id + ", name=" + name + ", color=" + color + "]";
	}

}

@ConvertServiceRegister(JsonConvertService.class)
@DateTimePattern("yyyy-MM-dd")
@NumberPattern("#,###.00")
class FooBean {

	int id;
	String name;
	Boolean sex;
	double salary;
	Float money;
	String[] selected;
	Date date;
	Map<String, Double> map;
	List<Date> list;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@ConvertServiceRegister(GeneralConvertService.class)
	public void setName(String name) {
		this.name = name;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public String[] getSelected() {
		return selected;
	}

	@ConvertServiceRegister(SplitConvertService.class)
	public void setSelected(String[] selected) {
		this.selected = selected;
	}

	public Date getDate() {
		return date;
	}

	// @DateTimePattern("yyyy年MM月dd-日")
	public void setDate(@DateTimePattern("yyyy年MM月dd-日") Date date) {
		this.date = date;
	}

	public Map<String, Double> getMap() {
		return map;
	}

	public void setMap(Map<String, Double> map) {
		this.map = map;
	}

	public List<Date> getList() {
		return list;
	}

	public void setList(@DateTimePattern("yyyy年MM月dd日") List<Date> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "FooBean [id=" + id + ", name=" + name + ", sex=" + sex + ", salary=" + salary + ", money=" + money
				+ ", selected=" + Arrays.toString(selected) + ", date=" + date + ", map=" + map + ", list=" + list
				+ "]";
	}

}

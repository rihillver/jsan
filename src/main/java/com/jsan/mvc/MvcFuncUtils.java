package com.jsan.mvc;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * MVC 功能性的实用方法。
 *
 */

public class MvcFuncUtils {

	private static final String CHARACTER_ENCODING = "utf-8";

	public static Cookie getCookie(HttpServletRequest request, String name) {

		Cookie[] cookies = request.getCookies();

		if (cookies != null && name != null) {
			try {
				name = URLEncoder.encode(name, CHARACTER_ENCODING);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			for (Cookie itemCookie : cookies) {
				if (name.equals(itemCookie.getName())) {
					return itemCookie;
				}
			}
		}

		return null;
	}

	public static String getCookieValue(HttpServletRequest request, String name) {

		Cookie cookie = getCookie(request, name);

		if (cookie != null) {
			try {
				return URLDecoder.decode(cookie.getValue(), CHARACTER_ENCODING);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}

		return null;
	}

	/**
	 * 返回指定包下的所有类（含子包）。
	 * 
	 * @param packageName
	 * @return
	 */
	public static Set<Class<?>> getClasses(String packageName) {

		return getClasses(packageName, true);
	}

	/**
	 * 返回指定包下的所有类（指定是否查找子包）。
	 * 
	 * @param packageName
	 * @param recursive
	 * @return
	 */
	public static Set<Class<?>> getClasses(String packageName, boolean recursive) {

		Set<Class<?>> classSet = new LinkedHashSet<>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		String packageDir = packageName.replace('.', '/');

		try {

			Enumeration<URL> resources = classLoader.getResources(packageDir);
			while (resources.hasMoreElements()) {

				URL url = resources.nextElement();
				String protocol = url.getProtocol();

				if ("file".equals(protocol)) {

					String path = URLDecoder.decode(url.getFile(), "UTF-8");
					File dirFile = new File(path);

					fillClasses(classLoader, packageName, dirFile, recursive, classSet);

				} else if ("jar".equals(protocol)) {

					JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
					Enumeration<JarEntry> entries = jarFile.entries();
					while (entries.hasMoreElements()) {

						JarEntry entry = entries.nextElement();
						String name = entry.getName();

						if (name.startsWith(packageDir)) {
							if (!recursive && name.lastIndexOf('/') > packageDir.length()) {
								continue;
							}

							if (name.endsWith(".class") && !entry.isDirectory()) {
								String className = name.substring(0, name.length() - 6).replace('/', '.');
								try {
									classSet.add(classLoader.loadClass(className));
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
							}
						}

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return classSet;
	}

	private static void fillClasses(ClassLoader classLoader, String packageName, File dirFile, boolean recursive, Set<Class<?>> classSet) throws IOException {

		if (dirFile.exists() && dirFile.isDirectory()) {

			File[] files = dirFile.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					String fileName = file.getName();
					if (fileName.endsWith(".class")) {
						String className = fileName.substring(0, fileName.length() - 6);
						try {
							classSet.add(classLoader.loadClass(packageName + "." + className));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				} else {
					if (recursive) {
						String dirPath = file.getCanonicalPath();
						String dirItem = dirPath.substring(dirPath.lastIndexOf(File.separatorChar) + 1);
						String packageStr = packageName + "." + dirItem;
						fillClasses(classLoader, packageStr, file, recursive, classSet);
					}
				}
			}
		}

	}

	// ==================================================

	/**
	 * 通过读取 Class 文件，获得方法形参名称，以下代码来复制于互联网。
	 * 
	 * <ul>
	 * <li>更专业的实现可参见 Parameter ， https://github.com/paul-hammant/paranamer</li>
	 * <li>Java8 已经内置实现获得方法形参名称的方法了</li>
	 * </ul>
	 * 
	 */

	// ==================================================

	/**
	 * 获取一个类的所有方法/构造方法的形参名称 Map 。
	 * 
	 * @param clazz
	 *            需要解析的类
	 * @return 所有方法/构造方法的形参名称 Map
	 * @throws IOException
	 *             如果有任何 IO 异常，不应该有，如果是本地文件，那 100% 遇到 BUG 了
	 */
	public static Map<String, List<String>> getParameterNamesMap(Class<?> clazz) throws IOException {

		InputStream is = clazz.getResourceAsStream("/" + clazz.getName().replace('.', '/') + ".class");
		return getParameterNamesMap(is);
	}

	/**
	 * 传入 Method 或 Constructor ，获取 getParamNames 方法返回的 Map 所对应的 key 。
	 * 
	 * @param object
	 * @return
	 */
	public static String getParameterNamesKey(Object object) {

		StringBuilder stringBuilder = new StringBuilder();

		if (object instanceof Method) {

			stringBuilder.append(((Method) object).getName()).append(',');
			getParameterNamesDescriptor(stringBuilder, (Method) object);

		} else if (object instanceof Constructor) {

			stringBuilder.append("<init>,"); // 只有非静态构造方法才能用有方法参数的,而且通过反射API拿不到静态构造方法
			getParameterNamesDescriptor(stringBuilder, (Constructor<?>) object);

		} else {

			throw new RuntimeException("Not Method or Constructor!");
		}
		return stringBuilder.toString();
	}

	private static Map<String, List<String>> getParameterNamesMap(InputStream in) throws IOException {

		DataInputStream dis = new DataInputStream(new BufferedInputStream(in));

		Map<String, List<String>> names = new HashMap<String, List<String>>();
		Map<Integer, String> strs = new HashMap<Integer, String>();

		dis.skipBytes(4); // Magic
		dis.skipBytes(2); // 副版本号
		dis.skipBytes(2); // 主版本号

		int constantPoolCount = dis.readUnsignedShort(); // 读取常量池

		for (int i = 0; i < (constantPoolCount - 1); i++) {

			byte flag = dis.readByte();

			switch (flag) {
			case 7: // CONSTANT_Class:
				dis.skipBytes(2);
				break;
			case 9: // CONSTANT_Fieldref:
			case 10: // CONSTANT_Methodref:
			case 11: // CONSTANT_InterfaceMethodref:
				dis.skipBytes(2);
				dis.skipBytes(2);
				break;
			case 8: // CONSTANT_String:
				dis.skipBytes(2);
				break;
			case 3: // CONSTANT_Integer:
			case 4: // CONSTANT_Float:
				dis.skipBytes(4);
				break;
			case 5: // CONSTANT_Long:
			case 6: // CONSTANT_Double:
				dis.skipBytes(8);
				i++; // 必须跳过一个，这是 class 文件设计的一个缺陷,历史遗留问题
				break;
			case 12: // CONSTANT_NameAndType:
				dis.skipBytes(2);
				dis.skipBytes(2);
				break;
			case 1: // CONSTANT_Utf8:
				int length = dis.readUnsignedShort();
				byte[] data = new byte[length];
				dis.read(data);
				strs.put(i + 1, new String(data, "UTF-8")); // 必然是 UTF-8 的
				break;
			case 15: // CONSTANT_MethodHandle:
				dis.skipBytes(1);
				dis.skipBytes(2);
				break;
			case 16: // CONSTANT_MethodType:
				dis.skipBytes(2);
				break;
			case 18: // CONSTANT_InvokeDynamic:
				dis.skipBytes(2);
				dis.skipBytes(2);
				break;
			default:
				throw new RuntimeException("Impossible!! flag= " + flag);
			}
		}

		dis.skipBytes(2); // 版本控制符
		dis.skipBytes(2); // 类名
		dis.skipBytes(2); // 超类

		int interfacesCount = dis.readUnsignedShort(); // 跳过接口定义

		dis.skipBytes(2 * interfacesCount); // 每个接口数据,是2个字节

		int fieldsCount = dis.readUnsignedShort(); // 跳过字段定义

		for (int i = 0; i < fieldsCount; i++) {

			dis.skipBytes(2);
			dis.skipBytes(2);
			dis.skipBytes(2);

			int attributesCount = dis.readUnsignedShort();

			for (int j = 0; j < attributesCount; j++) {
				dis.skipBytes(2); // 跳过访问控制符
				int attributeLength = dis.readInt();
				dis.skipBytes(attributeLength);
			}
		}

		int methodsCount = dis.readUnsignedShort(); // 开始读取方法

		for (int i = 0; i < methodsCount; i++) {

			dis.skipBytes(2); // 跳过访问控制符

			String methodName = strs.get(dis.readUnsignedShort());
			String descriptor = strs.get(dis.readUnsignedShort());

			short attributesCount = dis.readShort();

			for (int j = 0; j < attributesCount; j++) {

				String attributeName = strs.get(dis.readUnsignedShort());
				int attributeLength = dis.readInt();

				if ("Code".equals(attributeName)) { // 形参只在Code属性中

					dis.skipBytes(2);
					dis.skipBytes(2);
					int codeLength = dis.readInt();
					dis.skipBytes(codeLength); // 跳过具体代码
					int exceptionTableLength = dis.readUnsignedShort();
					dis.skipBytes(8 * exceptionTableLength); // 跳过异常表

					int codeAttributesCount = dis.readUnsignedShort();

					for (int k = 0; k < codeAttributesCount; k++) {

						int strIndex = dis.readUnsignedShort();
						String codeAttributeName = strs.get(strIndex);
						int codeAttributeLength = dis.readInt();

						if ("LocalVariableTable".equals(codeAttributeName)) { // 形参在LocalVariableTable属性中

							int localVariableTableLength = dis.readUnsignedShort();
							List<String> varNames = new ArrayList<String>(localVariableTableLength);

							for (int l = 0; l < localVariableTableLength; l++) {

								dis.skipBytes(2);
								dis.skipBytes(2);
								String varName = strs.get(dis.readUnsignedShort());
								dis.skipBytes(2);
								dis.skipBytes(2);
								if (!"this".equals(varName)) { // 非静态方法,第一个参数是this
									varNames.add(varName);
								}
							}
							names.put(methodName + "," + descriptor, varNames);
						} else {
							dis.skipBytes(codeAttributeLength);
						}
					}
				} else {
					dis.skipBytes(attributeLength);
				}
			}
		}
		dis.close();
		return names;
	}

	private static void getParameterNamesDescriptor(StringBuilder stringBuilder, Method method) {

		stringBuilder.append('(');

		for (Class<?> clazz : method.getParameterTypes()) {
			getParameterNamesDescriptor(stringBuilder, clazz);
		}

		stringBuilder.append(')');

		getParameterNamesDescriptor(stringBuilder, method.getReturnType());
	}

	private static void getParameterNamesDescriptor(StringBuilder stringBuilder, Constructor<?> constructor) {

		stringBuilder.append('(');

		for (Class<?> clazz : constructor.getParameterTypes()) {
			getParameterNamesDescriptor(stringBuilder, clazz);
		}

		stringBuilder.append(')');
		stringBuilder.append('V');
	}

	private static void getParameterNamesDescriptor(final StringBuilder stringBuilder, final Class<?> clazz) {

		Class<?> clss = clazz;

		while (true) {
			if (clss.isPrimitive()) {
				char c;
				if (clss == Integer.TYPE) {
					c = 'I';
				} else if (clss == Void.TYPE) {
					c = 'V';
				} else if (clss == Boolean.TYPE) {
					c = 'Z';
				} else if (clss == Byte.TYPE) {
					c = 'B';
				} else if (clss == Character.TYPE) {
					c = 'C';
				} else if (clss == Short.TYPE) {
					c = 'S';
				} else if (clss == Double.TYPE) {
					c = 'D';
				} else if (clss == Float.TYPE) {
					c = 'F';
				} else /* if (clss == Long.TYPE) */ {
					c = 'J';
				}
				stringBuilder.append(c);
				return;
			} else if (clss.isArray()) {
				stringBuilder.append('[');
				clss = clss.getComponentType();
			} else {
				stringBuilder.append('L');
				String name = clss.getName();
				int length = name.length();
				for (int i = 0; i < length; ++i) {
					char c = name.charAt(i);
					stringBuilder.append(c == '.' ? '/' : c);
				}
				stringBuilder.append(';');
				return;
			}
		}
	}

}

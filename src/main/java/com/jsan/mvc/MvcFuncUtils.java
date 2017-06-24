package com.jsan.mvc;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * MVC 功能性的实用方法。
 *
 */

public class MvcFuncUtils {

	public static Class<?> getClassByProperties(Properties properties, String key) {

		Class<?> clazz = null;
		String className = null;

		if (properties != null) {
			className = properties.getProperty(key);
		}

		if (className != null && !className.isEmpty()) {
			try {
				clazz = Class.forName(className);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return clazz;
	}

	public static Object getObjectByProperties(Properties properties, String key) {

		Object object = null;

		Class<?> clazz = getClassByProperties(properties, key);
		if (clazz != null) {
			try {
				object = clazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return object;
	}

	public static String[] getStringArrayByProperties(Properties properties, String key) {

		String[] strs = null;
		String str = null;

		if (properties != null) {
			str = properties.getProperty(key);
		}

		if (str != null && !str.isEmpty()) {
			strs = str.split(",");
			for (int i = 0; i < strs.length; i++) {
				strs[i] = strs[i].trim(); // 去除首尾空白
			}
		}

		return strs;
	}

	public static Properties getProperties(String path) throws IOException {

		InputStream inputStream = Object.class.getResourceAsStream(path);

		if (inputStream == null) {
			throw new IOException("failed to open the file: " + path);
		}

		Properties properties = new Properties();

		try {
			properties.load(inputStream);
		} catch (Exception e) {
			throw new IOException("failed to read the file: " + path);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return properties;
	}

	// ==================================================
	// 扫描包下的 class 文件，以下代码来复制于互联网。
	// ==================================================

	public static void addPackageClassToMap(String packageName, Map<String, Class<?>> classMap) {

		addPackageClassToMap(packageName, classMap, false);
	}

	public static void addPackageClassToMap(String packageName, Map<String, Class<?>> classMap,
			boolean qualifiedClassNameToLowerCase) {

		boolean recursive = true; // 是否循环迭代

		String packageDirName = packageName.replace('.', '/');

		Enumeration<URL> dirs;

		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {

				URL url = dirs.nextElement();
				String protocol = url.getProtocol(); // 得到协议的名称

				if ("file".equals(protocol)) { // file 类型的扫描

					String packagePath = URLDecoder.decode(url.getFile(), "utf-8"); // 获取包的物理路径
					addPackageClassToMapByFile(packageName, packagePath, recursive, classMap,
							qualifiedClassNameToLowerCase);

				} else if ("jar".equals(protocol)) { // jar 类型的扫描

					JarFile jar;
					try {
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						Enumeration<JarEntry> entries = jar.entries(); // 从此jar包得到一个枚举类

						while (entries.hasMoreElements()) {

							JarEntry entry = entries.nextElement(); // 获取jar包里的一个实体，可以是目录和一些jar包里的其他文件如META-INF等文件
							String name = entry.getName();
							if (name.charAt(0) == '/') { // 如果是以 / 开头的则获取后面的字符串
								name = name.substring(1);
							}

							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								if (idx != -1) { // 如果以 / 结尾则是一个包，获取包名 把 / 替换成 .
									packageName = name.substring(0, idx).replace('/', '.');
								}

								if ((idx != -1) || recursive) {
									if (name.endsWith(".class") && !entry.isDirectory()) {
										// 去掉后面的 .class 获取真正的类名
										String className = name.substring(packageName.length() + 1, name.length() - 6);
										try {
											String qualifiedClassName = packageName + '.' + className;
											classMap.put(qualifiedClassNameToLowerCase
													? qualifiedClassName.toLowerCase() : qualifiedClassName,
													Class.forName(qualifiedClassName)); // 此处判断类名是否转换为小写作为key
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void addPackageClassToMapByFile(String packageName, String packagePath, final boolean recursive,
			Map<String, Class<?>> classMap, boolean qualifiedClassNameToLowerCase) {

		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}

		File[] dirfiles = dir.listFiles(new FileFilter() {
			public boolean accept(File file) { // 自定义过滤规则，如果可以循环（包含子目录）或者是以.class结尾的文件
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});

		for (File file : dirfiles) {

			if (file.isDirectory()) {
				addPackageClassToMapByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
						classMap, qualifiedClassNameToLowerCase);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6); // 如果是java类文件去掉后面的.class只留下类名
				try {
					String qualifiedClassName = packageName + '.' + className;
					classMap.put(qualifiedClassNameToLowerCase ? qualifiedClassName.toLowerCase() : qualifiedClassName,
							Thread.currentThread().getContextClassLoader().loadClass(qualifiedClassName)); // 此处将类名转换为小写并作为key
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
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

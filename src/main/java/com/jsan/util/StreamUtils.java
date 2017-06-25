package com.jsan.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * 数据流转换工具类。
 * <p>
 * 更专业的可参 Apache Commons IO （IOUtils.class）。
 *
 */

public class StreamUtils {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 8; // 默认缓冲区大小

	/**
	 * 输入流转为输出流。
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void convertStream(InputStream in, OutputStream out) throws IOException {

		convertStream(in, out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 输入流转为输出流（指定缓冲区大小）。
	 * 
	 * @param in
	 * @param out
	 * @param bufferSize
	 * @throws IOException
	 */
	public static void convertStream(InputStream in, OutputStream out, int bufferSize) throws IOException {

		if (in != null && out != null) {
			byte[] buffer = new byte[bufferSize];
			int len;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
		}
	}

	/**
	 * 从输入流中获取数据。
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] readStreamToByte(InputStream in) throws IOException {

		if (in == null) {
			return null;
		}

		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			convertStream(in, out);
			return out.toByteArray();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 从输入流中获取数据。
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String readStreamToString(InputStream in) throws IOException {

		return readStreamToString(in, null);
	}

	/**
	 * 从输入流中获取数据（指定字符编码）。
	 * 
	 * @param in
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String readStreamToString(InputStream in, String charset) throws IOException {

		if (in == null) {
			return null;
		}

		byte[] result = readStreamToByte(in);

		if (charset == null) {
			return new String(result);
		} else {
			return new String(result, charset);
		}
	}

	/**
	 * 从文件获取数据。
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileToByte(File file) throws IOException {

		InputStream in = null;
		try {
			in = new FileInputStream(file);
			return readStreamToByte(in);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 从文件获取数据。
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(File file) throws IOException {

		return readFileToString(file, null);
	}

	/**
	 * 从文件获取数据（指定字符编码）。
	 * 
	 * @param file
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(File file, String charset) throws IOException {

		InputStream in = null;
		try {
			in = new FileInputStream(file);
			return readStreamToString(in, charset);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 从文件路径获取数据。
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileToByte(String filePath) throws IOException {

		File file = new File(filePath);
		return readFileToByte(file);
	}

	/**
	 * 从文件路径获取数据。
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(String filePath) throws IOException {

		File file = new File(filePath);
		return readFileToString(file);
	}

	/**
	 * 从文件路径获取数据（指定字符编码）。
	 * 
	 * @param filePath
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(String filePath, String charset) throws IOException {

		File file = new File(filePath);
		return readFileToString(file, charset);
	}

	/**
	 * 将数据写入输出流。
	 * 
	 * @param out
	 * @param bytes
	 * @throws IOException
	 */
	public static void writeStreamFromByte(OutputStream out, byte[] bytes) throws IOException {

		out.write(bytes);
	}

	/**
	 * 将数据写入输出流。
	 * 
	 * @param out
	 * @param str
	 * @throws IOException
	 */
	public static void writeStreamFromString(OutputStream out, String str) throws IOException {

		writeStreamFromString(out, str, null);
	}

	/**
	 * 将数据写入输出流（指定字符编码）。
	 * 
	 * @param out
	 * @param str
	 * @param charset
	 * @throws IOException
	 */
	public static void writeStreamFromString(OutputStream out, String str, String charset) throws IOException {

		if (str != null) {
			byte[] bytes;
			if (charset == null) {
				bytes = str.getBytes();
			} else {
				bytes = str.getBytes(charset);
			}
			writeStreamFromByte(out, bytes);
		}

	}

	/**
	 * 将数据写入文件。
	 * 
	 * @param file
	 * @param bytes
	 * @throws IOException
	 */
	public static void writeFileFromByte(File file, byte[] bytes) throws IOException {

		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			writeStreamFromByte(out, bytes);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将数据写入文件。
	 * 
	 * @param file
	 * @param str
	 * @throws IOException
	 */
	public static void writeFileFromString(File file, String str) throws IOException {

		writeFileFromString(file, str, null);
	}

	/**
	 * 将数据写入文件（指定字符编码）。
	 * 
	 * @param file
	 * @param str
	 * @param charset
	 * @throws IOException
	 */
	public static void writeFileFromString(File file, String str, String charset) throws IOException {

		if (str != null) {
			byte[] bytes = null;
			if (charset == null) {
				bytes = str.getBytes();
			} else {
				bytes = str.getBytes(charset);
			}
			writeFileFromByte(file, bytes);
		}
	}

	/**
	 * 将数据写入文件路径。
	 * 
	 * @param filePath
	 * @param bytes
	 * @throws IOException
	 */
	public static void writeFileFromByte(String filePath, byte[] bytes) throws IOException {

		File file = new File(filePath);
		writeFileFromByte(file, bytes);
	}

	/**
	 * 将数据写入文件路径。
	 * 
	 * @param filePath
	 * @param str
	 * @throws IOException
	 */
	public static void writeFileFromString(String filePath, String str) throws IOException {

		File file = new File(filePath);
		writeFileFromString(file, str);
	}

	/**
	 * 将数据写入文件路径（指定字符编码）。
	 * 
	 * @param filePath
	 * @param str
	 * @param charset
	 * @throws IOException
	 */
	public static void writeFileFromString(String filePath, String str, String charset) throws IOException {

		File file = new File(filePath);
		writeFileFromString(file, str, charset);
	}

	/**
	 * 将数据追加写入文件。
	 * 
	 * @param file
	 * @param str
	 * @throws IOException
	 */
	public static void appendFileFromString(File file, String str) throws IOException {

		appendFileFromString(file, str, null);
	}

	/**
	 * 将数据追加写入文件（指定字符编码）。
	 * 
	 * @param file
	 * @param str
	 * @param charset
	 * @throws IOException
	 */
	public static void appendFileFromString(File file, String str, String charset) throws IOException {

		OutputStream out = null;
		try {
			out = new FileOutputStream(file, true); // 写入文件末尾处
			writeStreamFromString(out, str, charset);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将数据追加写入文件路径。
	 * 
	 * @param filePath
	 * @param str
	 * @throws IOException
	 */
	public static void appendFileFromString(String filePath, String str) throws IOException {

		appendFileFromString(filePath, str, null);
	}

	/**
	 * 将数据追加写入文件路径（指定字符编码）。
	 * 
	 * @param filePath
	 * @param str
	 * @param charset
	 * @throws IOException
	 */
	public static void appendFileFromString(String filePath, String str, String charset) throws IOException {

		File file = new File(filePath);
		appendFileFromString(file, str, charset);
	}

	/**
	 * 将对象存储到文件（进行序列化与反序列化时，对象必须实现 Serializable 接口）。
	 * 
	 * @param file
	 * @param obj
	 * @throws IOException
	 */
	public static void writeObject(File file, Object obj) throws IOException {

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.reset();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将对象存储到文件（进行序列化与反序列化时，对象必须实现 Serializable 接口）。
	 * 
	 * @param filePath
	 * @param obj
	 * @throws IOException
	 */
	public static void writeObject(String filePath, Object obj) throws IOException {

		File file = new File(filePath);
		writeObject(file, obj);
	}

	/**
	 * 读取文件并转化到对象（进行序列化与反序列化时，对象必须实现 Serializable 接口）。
	 * 
	 * @param file
	 * @return
	 */
	public static Object readObject(File file) {

		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			return ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 读取文件并转化到对象（进行序列化与反序列化时，对象必须实现 Serializable 接口）。
	 * 
	 * @param filePath
	 * @return
	 */
	public static Object readObject(String filePath) {

		File file = new File(filePath);
		return readObject(file);
	}

	/**
	 * 简易判断文件编码，默认为 GBK，仅判断 UTF-8，虽然不准确，但一般中文系统除了 UTF-8 之外多数就是 GBK
	 * 了，所以在此仅作最简单初级的判断。
	 * <p>
	 * 若要实现更专业的文件编码检测，可使用 cpdetector 进行探测。
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileCharset(File file) {

		String charset = "GBK";
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] b = new byte[3];
			in.read(b);
			if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
				charset = "UTF-8";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return charset;
	}

	/**
	 * 简易判断文件编码，默认为 GBK，仅判断 UTF-8，虽然不准确，但一般中文系统除了 UTF-8 之外多数就是 GBK
	 * 了，所以在此仅作最简单初级的判断。
	 * <p>
	 * 若要实现更专业的文件编码检测，可使用 cpdetector 进行探测。
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileCharset(String filePath) {

		File file = new File(filePath);
		return getFileCharset(file);
	}

}
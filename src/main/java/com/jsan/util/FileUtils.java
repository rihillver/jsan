package com.jsan.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 简易的文件处理工具类。
 * <p>
 * 更专业的可参 Apache Commons IO （FileUtils.class）。
 *
 */

public class FileUtils {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 8; // 默认缓冲区大小

	/**
	 * 创建文件。
	 * 
	 * @param file
	 * @return
	 */
	public static boolean createFile(File file) {

		File parentFile = file.getParentFile();

		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}

		if (!file.exists()) {
			try {
				return file.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return false;
	}

	/**
	 * 创建文件。
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean createFile(String filePath) {

		File file = new File(filePath);
		return createFile(file);
	}

	/**
	 * 删除单个文件。
	 * 
	 * @param file
	 * @return
	 */
	public static boolean deleteFile(File file) {

		if (file.exists() && file.isFile()) {
			return file.delete();
		}

		return false;
	}

	/**
	 * 删除单个文件。
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(String filePath) {

		File file = new File(filePath);
		return deleteFile(file);
	}

	/**
	 * 删除指定路径下的所有文件（仅当前目录下的文件，不包括子文件夹内的文件）。
	 * 
	 * @param dirPath
	 * @return
	 */
	public static int deleteFiles(String dirPath) {

		int count = 0;
		File dir = new File(dirPath);

		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isFile() && file.delete()) {
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * 重命名文件（相对于原文件目录下）。
	 * 
	 * @param sourceFile
	 * @param newName
	 * @param overlay
	 * @return
	 */
	public static boolean renameFile(File sourceFile, String newName, boolean overlay) {

		if (sourceFile.exists() && sourceFile.isFile()) {
			String path = sourceFile.getPath();
			path = path.substring(0, path.lastIndexOf(File.separatorChar) + 1);
			path += newName;
			File destFile = new File(path);
			return rename(sourceFile, destFile, overlay);
		} else {
			return false;
		}
	}

	/**
	 * 重命名文件（相对于原文件目录下）。
	 * 
	 * @param sourceFilePath
	 * @param newName
	 *            文件名或相对于原文件目录下的相对路径名
	 * @param overlay
	 * @return
	 */
	public static boolean renameFile(String sourceFilePath, String newName, boolean overlay) {

		File sourceFile = new File(sourceFilePath);
		return renameFile(sourceFile, newName, overlay);
	}

	/**
	 * 复制单个文件。
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @param overlay
	 * @return
	 */
	public static boolean copyFile(File sourceFile, File destFile, boolean overlay) {

		if (sourceFile.exists() && sourceFile.isFile()) {
			if (destFile.exists() && !overlay) {
				return true;
			} else {
				File parentFile = destFile.getParentFile();

				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}

				FileInputStream in = null;
				FileOutputStream out = null;
				try {
					in = new FileInputStream(sourceFile);
					out = new FileOutputStream(destFile);
					byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
					int len;
					while ((len = in.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * 复制单个文件。
	 * 
	 * @param sourceFilePath
	 * @param destFilePath
	 * @param overlay
	 * @return
	 */
	public static boolean copyFile(String sourceFilePath, String destFilePath, boolean overlay) {

		File sourceFile = new File(sourceFilePath);
		File destFile = new File(destFilePath);
		return copyFile(sourceFile, destFile, overlay);
	}

	/**
	 * 复制源路径下的所有文件到目标路径下。
	 * 
	 * @param sourceDirPath
	 * @param destDirPath
	 * @param overlay
	 * @return
	 */
	public static int copyFiles(String sourceDirPath, String destDirPath, boolean overlay) {

		int count = 0;
		File sourceDir = new File(sourceDirPath);
		File destDir = new File(destDirPath);

		if (sourceDir.exists() && sourceDir.isDirectory()) {
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			File[] files = sourceDir.listFiles();
			for (File sourceFile : files) {
				File destFile = new File(destDir.getPath() + File.separator + sourceFile.getName());
				if (sourceFile.isFile() && copyFile(sourceFile, destFile, overlay)) {
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * 移动单个文件。
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @param overlay
	 * @return
	 */
	public static boolean moveFile(File sourceFile, File destFile, boolean overlay) {

		if (copyFile(sourceFile, destFile, overlay)) {
			return deleteFile(sourceFile);
		} else {
			return false;
		}
	}

	/**
	 * 移动单个文件。
	 * 
	 * @param sourceFilePath
	 * @param destFilePath
	 * @param overlay
	 * @return
	 */
	public static boolean moveFile(String sourceFilePath, String destFilePath, boolean overlay) {

		File sourceFile = new File(sourceFilePath);
		File destFile = new File(destFilePath);
		return moveFile(sourceFile, destFile, overlay);
	}

	/**
	 * 移动源路径下的所有文件到目标路径下。
	 * 
	 * @param sourceDirPath
	 * @param destDirPath
	 * @param overlay
	 * @return
	 */
	public static int moveFiles(String sourceDirPath, String destDirPath, boolean overlay) {

		int count = 0;

		if ((count = copyFiles(sourceDirPath, destDirPath, overlay)) > 0) {
			deleteFiles(sourceDirPath);
		}

		return count;
	}

	/**
	 * 列出指定路径下的文件。
	 * 
	 * @param dirPath
	 * @return
	 */
	public static File[] listFiles(String dirPath) {

		File dir = new File(dirPath);

		if (dir.exists()) {
			return dir.listFiles(new FileFilter() {
				public boolean accept(File file) {
					return file.isFile();
				}
			});
		} else {
			return null;
		}
	}

	/**
	 * 列出指定路径下的所有文件。
	 * 
	 * @param dirPath
	 * @return
	 */
	public static List<File> listAllFiles(String dirPath) {

		return listAllFiles(dirPath, null);
	}

	/**
	 * 列出指定路径下的所有文件（匹配 regex）。
	 * 
	 * @param dirPath
	 * @param regex
	 * @return
	 */
	public static List<File> listAllFiles(String dirPath, String regex) {

		return listAll(dirPath, regex, false);
	}

	// ==================================================

	/**
	 * 创建文件夹。
	 * 
	 * @param folder
	 * @return
	 */
	public static boolean createFolder(File folder) {

		if (!folder.exists()) {
			return folder.mkdirs(); // 包括创建必需但不存在的父目录
		} else {
			return true; // 若目录已存在则返回true
		}
	}

	/**
	 * 创建文件夹。
	 * 
	 * @param folderPath
	 * @return
	 */
	public static boolean createFolder(String folderPath) {

		File folder = new File(folderPath);
		return createFolder(folder);
	}

	/**
	 * 删除单个文件夹（包括非空文件夹）。
	 * 
	 * @param folder
	 * @return
	 */
	public static boolean deleteFolder(File folder) {

		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files.length > 0) {
				for (File file : files) {
					if (file.isFile()) {
						if (!file.delete()) {
							return false;
						}
					} else {
						if (!deleteFolder(file)) {
							return false;
						}
					}
				}
			}
			return folder.delete();
		} else {
			return false;
		}
	}

	/**
	 * 删除单个文件夹（包括非空文件夹）。
	 * 
	 * @param folderPath
	 * @return
	 */
	public static boolean deleteFolder(String folderPath) {

		File folder = new File(folderPath);
		return deleteFolder(folder);
	}

	/**
	 * 删除指定路径下的所有文件夹（包括非空文件夹）。
	 * 
	 * @param dirPath
	 * @return
	 */
	public static int deleteFolders(String dirPath) {

		int count = 0;
		File dir = new File(dirPath);

		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files.length > 0) {
				for (File file : files) {
					if (file.isDirectory()) {
						if (deleteFolder(file)) {
							count++;
						}
					}
				}
			}
		}

		return count;
	}

	/**
	 * 重命名文件夹（相对于原文件夹目录下）。
	 * 
	 * @param sourceFolder
	 * @param newName
	 * @param overlay
	 * @return
	 */
	public static boolean renameFolder(File sourceFolder, String newName, boolean overlay) {

		if (sourceFolder.exists() && sourceFolder.isDirectory()) {
			String path = sourceFolder.getPath();
			path = path.substring(0, path.lastIndexOf(File.separatorChar) + 1);
			path += newName;
			File destFolder = new File(path);
			return rename(sourceFolder, destFolder, overlay);
		} else {
			return false;
		}
	}

	/**
	 * 重命名文件夹（相对于原文件夹目录下）。
	 * 
	 * @param sourceFolderPath
	 * @param newName
	 *            文件夹名或相对于原文件夹目录下的相对路径名
	 * @param overlay
	 * @return
	 */
	public static boolean renameFolder(String sourceFolderPath, String newName, boolean overlay) {

		File sourceFolder = new File(sourceFolderPath);
		return renameFolder(sourceFolder, newName, overlay);
	}

	/**
	 * 复制单个文件夹。
	 * 
	 * @param sourceFolder
	 * @param destFolder
	 * @param overlay
	 * @return
	 */
	public static boolean copyFolder(File sourceFolder, File destFolder, boolean overlay) {

		if (sourceFolder.exists() && sourceFolder.isDirectory()) {
			File[] files = sourceFolder.listFiles();
			for (File sourceFile : files) {
				File destFile = new File(destFolder.getPath() + File.separator + sourceFile.getName());
				if (sourceFile.isFile()) {
					if (!copyFile(sourceFile, destFile, overlay)) {
						return false;
					}
				} else {
					if (!copyFolder(sourceFile, destFile, overlay)) {
						return false;
					}
				}
			}
			if (!destFolder.exists()) {
				destFolder.mkdirs();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 复制单个文件夹。
	 * 
	 * @param sourceFolderPath
	 * @param destFolderPath
	 * @param overlay
	 * @return
	 */
	public static boolean copyFolder(String sourceFolderPath, String destFolderPath, boolean overlay) {

		File sourceFolder = new File(sourceFolderPath);
		File destFolder = new File(destFolderPath);
		return copyFolder(sourceFolder, destFolder, overlay);
	}

	/**
	 * 复制源路径下的所有文件夹到目标路径下。
	 * 
	 * @param sourceDirPath
	 * @param destDirPath
	 * @param overlay
	 * @return
	 */
	public static int copyFolders(String sourceDirPath, String destDirPath, boolean overlay) {

		int count = 0;
		File sourceDir = new File(sourceDirPath);
		File destDir = new File(destDirPath);

		if (sourceDir.exists() && sourceDir.isDirectory()) {
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			File[] files = sourceDir.listFiles();
			for (File sourceFile : files) {
				File destFile = new File(destDir.getPath() + File.separator + sourceFile.getName());
				if (sourceFile.isDirectory() && copyFolder(sourceFile, destFile, overlay)) {
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * 移动单个文件夹。
	 * 
	 * @param sourceFolder
	 * @param destFolder
	 * @param overlay
	 * @return
	 */
	public static boolean moveFolder(File sourceFolder, File destFolder, boolean overlay) {

		if (copyFolder(sourceFolder, destFolder, overlay)) {
			return deleteFolder(sourceFolder);
		} else {
			return false;
		}
	}

	/**
	 * 移动单个文件夹。
	 * 
	 * @param sourceFolderPath
	 * @param destFolderPath
	 * @param overlay
	 * @return
	 */
	public static boolean moveFolder(String sourceFolderPath, String destFolderPath, boolean overlay) {

		File sourceFolder = new File(sourceFolderPath);
		File destFolder = new File(destFolderPath);
		return moveFolder(sourceFolder, destFolder, overlay);
	}

	/**
	 * 移动源路径下的所有文件夹到目标路径下。
	 * 
	 * @param sourceDirPath
	 * @param destDirPath
	 * @param overlay
	 * @return
	 */
	public static int moveFolders(String sourceDirPath, String destDirPath, boolean overlay) {

		int count = 0;

		if ((count = copyFolders(sourceDirPath, destDirPath, overlay)) > 0) {
			deleteFolders(sourceDirPath);
		}

		return count;
	}

	/**
	 * 列出指定路径下的文件夹。
	 * 
	 * @param dirPath
	 * @return
	 */
	public static File[] listFolders(String dirPath) {

		File dir = new File(dirPath);

		if (dir.exists()) {
			return dir.listFiles(new FileFilter() {
				public boolean accept(File file) {
					return file.isDirectory();
				}
			});
		} else {
			return null;
		}
	}

	/**
	 * 列出指定路径下的所有文件夹。
	 * 
	 * @param dirPath
	 * @return
	 */
	public static List<File> listAllFolders(String dirPath) {

		return listAllFolders(dirPath, null);
	}

	/**
	 * 列出指定路径下的所有文件夹（匹配 regex）。
	 * 
	 * @param dirPath
	 * @param regex
	 * @return
	 */
	public static List<File> listAllFolders(String dirPath, String regex) {

		return listAll(dirPath, regex, true);
	}

	// ============================================================

	/**
	 * （重命名/移动）文件或文件夹，仅在同一个盘符上操作有效，不同盘符或驱动器上移动文件或文件夹请使用 move 方法。
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @param overlay
	 * @return
	 */
	public static boolean rename(File sourceFile, File destFile, boolean overlay) {

		if (sourceFile.exists()) {
			if (sourceFile.isFile()) {
				if (destFile.exists()) {
					if (overlay) {
						destFile.delete();
					}
				} else {
					File parentFile = destFile.getParentFile();
					if (!parentFile.exists()) {
						parentFile.mkdirs();
					}
				}
				return sourceFile.renameTo(destFile);
			} else {
				if (destFile.exists()) {
					File[] files = sourceFile.listFiles();
					for (File itemSourceFile : files) {
						File itemDestinationFile = new File(
								destFile.getPath() + File.separator + itemSourceFile.getName());
						if (!rename(itemSourceFile, itemDestinationFile, overlay) && overlay) {
							return false;
						}
					}
					return deleteFolder(sourceFile);
				} else {
					File parentFile = destFile.getParentFile();
					if (!parentFile.exists()) {
						parentFile.mkdirs();
					}
					return sourceFile.renameTo(destFile);
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * （重命名/移动）文件或文件夹，仅在同一个盘符上操作有效，不同盘符或驱动器上移动文件或文件夹请使用 move 方法。
	 * 
	 * @param sourceFilePath
	 * @param destFilePath
	 * @param overlay
	 * @return
	 */
	public static boolean rename(String sourceFilePath, String destFilePath, boolean overlay) {

		File sourceFile = new File(sourceFilePath);
		File destFile = new File(destFilePath);
		return rename(sourceFile, destFile, overlay);
	}

	/**
	 * 删除指定路径下的所有文件夹和文件。
	 * 
	 * @param dirPath
	 * @return
	 */
	public static boolean delete(String dirPath) {

		File dir = new File(dirPath);

		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					if (!file.delete()) {
						return false;
					}
				} else {
					if (!deleteFolder(file)) {
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 复制源路径下的所有文件夹和文件到目标路径下。
	 * 
	 * @param sourceDirPath
	 * @param destDirPath
	 * @param overlay
	 * @return
	 */
	public static boolean copy(String sourceDirPath, String destDirPath, boolean overlay) {

		return copyFolder(sourceDirPath, destDirPath, overlay);
	}

	/**
	 * 移动源路径下的所有文件夹和文件到目标路径下。
	 * 
	 * @param sourceDirPath
	 * @param destDirPath
	 * @param overlay
	 * @return
	 */
	public static boolean move(String sourceDirPath, String destDirPath, boolean overlay) {

		File sourceDir = new File(sourceDirPath);
		File destDir = new File(destDirPath);

		if (sourceDir.exists() && sourceDir.isDirectory()) {
			File[] files = sourceDir.listFiles();
			for (File sourceFile : files) {
				String path = destDir.getPath() + File.separator + sourceFile.getName();
				File destFile = new File(path);
				if (sourceFile.isFile()) {
					if (!moveFile(sourceFile, destFile, overlay)) {
						return false;
					}
				} else {
					if (!moveFolder(sourceFile, destFile, overlay)) {
						return false;
					}
				}

			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 列出指定路径下的文件夹和文件。
	 * 
	 * @param dirPath
	 * @return
	 */
	public static File[] list(String dirPath) {

		return list(dirPath, null);
	}

	/**
	 * 列出指定路径下的文件夹和文件（匹配 FileFilter）。
	 * 
	 * @param dirPath
	 * @param fileFilter
	 * @return
	 */
	public static File[] list(String dirPath, FileFilter fileFilter) {

		File dir = new File(dirPath);
		if (dir.exists()) {
			return dir.listFiles(fileFilter);
		} else {
			return null;
		}
	}

	/**
	 * 列出指定路径下的所有文件夹和文件。
	 * 
	 * @param dirPath
	 * @return
	 */
	public static List<File> listAll(String dirPath) {

		return listAll(dirPath, null);
	}

	/**
	 * 列出指定路径下的所有文件夹和文件（匹配 regex）。
	 * 
	 * @param dirPath
	 * @param regex
	 * @return
	 */
	public static List<File> listAll(String dirPath, String regex) {

		return listAll(dirPath, regex, null);
	}

	/**
	 * 列出指定路径下的所有文件夹和文件、或文件夹、或文件（匹配 regex）。
	 * 
	 * @param dirPath
	 * @param regex
	 * @param type
	 *            null：所有文件夹和文件、true：文件夹、false：文件
	 * @return
	 */
	public static List<File> listAll(String dirPath, final String regex, final Boolean type) {

		final List<File> list = new ArrayList<File>();
		final File dir = new File(dirPath);

		if (dir.exists() && dir.isDirectory()) {

			class Inner {
				void addList(File dirFile) {
					File[] files = dirFile.listFiles();
					for (File file : files) {
						if (file.isFile()) {
							if (type == null || type == false) {
								if (regex == null) {
									list.add(file);
								} else {
									if (Pattern.matches(regex, file.getName())) {
										list.add(file);
									}
								}
							}
						} else {
							if (type == null || type == true) {
								if (regex == null) {
									list.add(file);
								} else {
									if (Pattern.matches(regex, file.getName())) {
										list.add(file);
									}
								}
							}
							addList(file);
						}
					}
				}
			}

			new Inner().addList(dir);
		}

		return list;
	}

	/**
	 * 获取文件夹或文件的大小（单位：Byte）
	 * 
	 * @param file
	 * @return
	 */
	public static long getSize(final File file) {

		if (file.exists()) {
			if (file.isFile()) {
				return file.length();
			} else {

				class Inner {
					long size = 0;

					long getSize() {
						addSize(file);
						return size;
					}

					void addSize(File dirFile) {
						File[] files = dirFile.listFiles();
						for (File file : files) {
							if (file.isFile()) {
								size += file.length();
							} else {
								addSize(file);
							}
						}
					}
				}

				return new Inner().getSize();
			}
		} else {
			return 0;
		}
	}

	/**
	 * 获取文件夹或文件的大小（单位：Byte）
	 * 
	 * @param path
	 * @return
	 */
	public static long getSize(String path) {

		File file = new File(path);
		return getSize(file);
	}

	/**
	 * 高效率复制（例如用于大文件复制）。
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @param overlay
	 * @return
	 */
	public static boolean fileChannelCopy(File sourceFile, File destFile, boolean overlay) {

		if (sourceFile.exists() && sourceFile.isFile()) {

			if (destFile.exists() && !overlay) {
				return true;
			}

			File parentFile = destFile.getParentFile();

			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}

			FileChannel in = null;
			FileChannel out = null;
			FileInputStream fis = null;
			FileOutputStream fos = null;
			try {
				fis = new FileInputStream(sourceFile);
				fos = new FileOutputStream(destFile);
				in = fis.getChannel();
				out = fos.getChannel();
				in.transferTo(0, in.size(), out);
				out.transferFrom(in, 0, in.size());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				if (fos != null) {
					try {
						fos.close();
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
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * 高效率复制（例如用于大文件复制）。
	 * 
	 * @param sourceFilePath
	 * @param destFilePath
	 * @param overlay
	 * @return
	 */
	public static boolean fileChannelCopy(String sourceFilePath, String destFilePath, boolean overlay) {

		File sourceFile = new File(sourceFilePath);
		File destFile = new File(destFilePath);
		return fileChannelCopy(sourceFile, destFile, overlay);
	}

}

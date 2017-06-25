package com.jsan.util;

import org.apache.commons.fileupload.FileUpload;

import junit.framework.TestCase;

public class PathUtilsTest extends TestCase {

	public void testFoo() {

		System.out.println(PathUtils.getWebRootPath());
		System.out.println(PathUtils.getWebInfPath());
		System.out.println(PathUtils.getClassesRootFile());
		System.out.println(PathUtils.getClassDirFile(PathUtilsTest.class));
		System.out.println(PathUtils.getClassDirFile(FileUpload.class));
	}
}

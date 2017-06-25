package com.jsan.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取 IP 的工具类。
 *
 */

public class IpUtils {

	/**
	 * 返回远程 IP 。
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteIp(HttpServletRequest request) {

		String ip = getRemoteIps(request);

		if (ip != null) {
			int i = ip.indexOf(',');
			if (i != -1) {
				ip = ip.substring(0, i);
			}
		}

		return ip;
	}

	/**
	 * 返回远程 IP（可能是一系列通过代理的 IP 串，用逗号分隔，其中第一个理论上是真实 IP）。
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteIps(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

	/**
	 * 返回本机 IP ，外网 IP 优先。
	 * 
	 * @return
	 */
	public static String getLocalIp() {

		String localIp = null; // 本地IP，如果没有配置外网IP则返回它
		String netIp = null; // 外网IP

		Enumeration<NetworkInterface> netInterfaces = null;

		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		boolean finded = false; // 是否找到外网IP
		InetAddress ip = null;

		while (netInterfaces.hasMoreElements() && !finded) {

			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();

			while (address.hasMoreElements()) {
				ip = address.nextElement();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(':') == -1) { // 外网IP
					netIp = ip.getHostAddress();
					finded = true;
					break;
				} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(':') == -1) { // 内网IP
					localIp = ip.getHostAddress();
				}
			}
		}

		if (netIp == null || netIp.length() == 0) {
			return localIp;
		} else {
			return netIp;
		}
	}

}

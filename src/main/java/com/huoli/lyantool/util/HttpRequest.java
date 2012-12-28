package com.huoli.lyantool.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP请求
 * 
 * @author liaorui
 * 
 */
public class HttpRequest {

	public static boolean testConnect(String urlStr) {
		try {
			URL url = new URL(urlStr);
			String userInfo = url.getUserInfo();
			if (userInfo != null && userInfo.length() > 0) {
				String string = urlStr.replace(userInfo, "");
				url = new URL(string);
			}

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			if (userInfo != null && userInfo.length() > 0)
				connection.setRequestProperty("Authorization", userInfo);
			connection.connect();

			int resultCode = connection.getResponseCode();
			System.out.println(resultCode);

			if (resultCode == 200)
				return true;
			else
				return false;
		} catch (IOException ie) {
			return false;
		}
	}

	public static String getContent(HttpServletRequest request)
			throws Exception {
		BufferedInputStream bis = null;
		bis = new BufferedInputStream(request.getInputStream());

		byte[] buffer = new byte[1024]; // 固定大小缓冲区
		byte[] tmp = new byte[1024]; // 作为中间缓冲区，在buffer和all中复制数据
		byte[] data = null; // 作为最终数据区

		int len = 0; // 每次读取的数据长度
		int dstStart = 0;
		int length = 0; // 数据累计总长度

		while ((len = bis.read(buffer, 0, 1024)) != -1) {
			System.arraycopy(buffer, 0, tmp, dstStart, len);
			length = dstStart + len; // 很关键
			data = new byte[length];
			System.arraycopy(tmp, 0, data, 0, length);
			tmp = new byte[length + 1024]; // 继续扩充容量
			System.arraycopy(data, 0, tmp, 0, length);
			dstStart = length;
		}
		tmp = null;

		return new String(data, "UTF-8");
	}

	public static String post(String url, Map<String, String> params,
			boolean gzip) throws IOException {

		if (params != null && params.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (String key : params.keySet()) {
				if (params.get(key) == null)
					continue;
				sb.append(key);
				sb.append("=");
				sb.append(params.get(key));
				sb.append("&");
			}

			return post(url, sb.toString(), gzip);
		} else {
			return post(url, "", gzip);
		}

	}

	public static String post(String url, Map<String, String> params,
			boolean gzip, boolean test) throws IOException {

		if (params != null && params.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (String key : params.keySet()) {
				if (params.get(key) == null)
					continue;
				sb.append(key);
				sb.append("=");
				sb.append(params.get(key));
				sb.append("&");
			}

			return post(url, sb.toString(), gzip, true);
		} else {
			return post(url, "", gzip, true);
		}

	}

	public static String post(String url, Map<String, String> params)
			throws IOException {

		return post(url, params, false);

	}

	public static String post(String url, String post, boolean gzip,
			boolean test) throws IOException {

		String line = null;
		URL _url = new URL(url);
		URLConnection urlc = _url.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection) urlc;
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		httpConnection.setRequestProperty("token", "lyan");
		httpConnection.setRequestProperty("auth", "addb");
		httpConnection
				.setRequestProperty(
						"extra",
						"cf9a998e-fc2f-4795-b28b-7257a74b3bb9:Android:HUAWEI C8812_480x800_15_4.0.3:91061f6d2a299030:YTAhMDAkMdNlZdYfNGU=:ODy4NjAnMTEwMDqeNDA5MDy4NDc=:2.2.0:gfan");
		httpConnection.setRequestProperty("reqtime", System.currentTimeMillis()
				+ "");
		httpConnection.setConnectTimeout(60000);
		httpConnection.setReadTimeout(60000);

		httpConnection.setDoInput(true);
		httpConnection.setDoOutput(true);

		httpConnection.connect();

		OutputStream osw = httpConnection.getOutputStream();

		osw.write(post.getBytes("UTF-8"));
		osw.flush();
		osw.close();

		InputStream is = null;
		if (gzip) {
			is = new GZIPInputStream(httpConnection.getInputStream());
		} else {
			is = httpConnection.getInputStream();
		}

		byte[] buffer = new byte[1024]; // 固定大小缓冲区
		byte[] tmp = new byte[1024]; // 作为中间缓冲区，在buffer和all中复制数据
		byte[] data = null; // 作为最终数据区

		int len = 0; // 每次读取的数据长度
		int dstStart = 0;
		int length = 0; // 数据累计总长度

		while ((len = is.read(buffer, 0, 1024)) != -1) {
			System.arraycopy(buffer, 0, tmp, dstStart, len);
			length = dstStart + len; // 很关键
			data = new byte[length];
			System.arraycopy(tmp, 0, data, 0, length);
			tmp = new byte[length + 1024]; // 继续扩充容量
			System.arraycopy(data, 0, tmp, 0, length);
			dstStart = length;
		}
		tmp = null;

		if (data != null) {
			line = new String(data, "utf-8");
		}

		is.close();
		httpConnection.disconnect();

		return line;
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param post
	 * @param timeOut
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, String post, boolean gzip)
			throws IOException {

		String line = null;
		URL _url = new URL(url);
		URLConnection urlc = _url.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection) urlc;
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		httpConnection.setConnectTimeout(60000);
		httpConnection.setReadTimeout(60000);

		httpConnection.setDoInput(true);
		httpConnection.setDoOutput(true);

		httpConnection.connect();

		OutputStream osw = httpConnection.getOutputStream();

		osw.write(post.getBytes("UTF-8"));
		osw.flush();
		osw.close();

		InputStream is = null;
		if (gzip) {
			is = new GZIPInputStream(httpConnection.getInputStream());
		} else {
			is = httpConnection.getInputStream();
		}

		byte[] buffer = new byte[1024]; // 固定大小缓冲区
		byte[] tmp = new byte[1024]; // 作为中间缓冲区，在buffer和all中复制数据
		byte[] data = null; // 作为最终数据区

		int len = 0; // 每次读取的数据长度
		int dstStart = 0;
		int length = 0; // 数据累计总长度

		while ((len = is.read(buffer, 0, 1024)) != -1) {
			System.arraycopy(buffer, 0, tmp, dstStart, len);
			length = dstStart + len; // 很关键
			data = new byte[length];
			System.arraycopy(tmp, 0, data, 0, length);
			tmp = new byte[length + 1024]; // 继续扩充容量
			System.arraycopy(data, 0, tmp, 0, length);
			dstStart = length;
		}
		tmp = null;

		if (data != null) {
			line = new String(data, "utf-8");
		}

		is.close();
		httpConnection.disconnect();

		return line;
	}

	public static String get(String url) throws IOException {
		return get(url, 30000, false);
	}

	public static String get(String url, int timeOut) throws IOException {
		return get(url, timeOut > 0 ? timeOut : 3000, false);
	}

	/**
	 * GET请求
	 * 
	 * @param address
	 * @param gzip
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, int timeOut, boolean gzip)
			throws IOException {

		String line = null;
		URL _url = new URL(url);
		URLConnection urlc = _url.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection) urlc;
		httpConnection.setRequestMethod("GET");
		httpConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		if (timeOut > 0) {
			httpConnection.setConnectTimeout(timeOut);
			httpConnection.setReadTimeout(timeOut);
		} else {
			httpConnection.setConnectTimeout(30000);
			httpConnection.setReadTimeout(30000);
		}

		httpConnection.setDoInput(true);
		httpConnection.setDoOutput(true);
		httpConnection.connect();

		InputStream is = null;
		if (gzip) {
			is = new GZIPInputStream(httpConnection.getInputStream());
		} else {
			is = httpConnection.getInputStream();
		}

		byte[] buffer = new byte[1024]; // 固定大小缓冲区
		byte[] tmp = new byte[1024]; // 作为中间缓冲区，在buffer和all中复制数据
		byte[] data = null; // 作为最终数据区

		int len = 0; // 每次读取的数据长度
		int dstStart = 0;
		int length = 0; // 数据累计总长度

		while ((len = is.read(buffer, 0, 1024)) != -1) {
			System.arraycopy(buffer, 0, tmp, dstStart, len);
			length = dstStart + len; // 很关键
			data = new byte[length];
			System.arraycopy(tmp, 0, data, 0, length);
			tmp = new byte[length + 1024]; // 继续扩充容量
			System.arraycopy(data, 0, tmp, 0, length);
			dstStart = length;
		}
		tmp = null;

		if (data != null) {
			line = new String(data, "utf-8");
		}

		is.close();
		httpConnection.disconnect();

		return line;
	}
}

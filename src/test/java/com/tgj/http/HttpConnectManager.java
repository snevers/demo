package com.tgj.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * 
 * <p> Http连接池. </p>
 * <p>
 * 单线程及多线程的方式
 * </p>
 * 
 * @className HttpConnectManager
 * @author Server
 * @date 2018年5月21日 上午9:49:44
 * 
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
public class HttpConnectManager {
	
	private PoolingHttpClientConnectionManager connectionManager;
	
	private IdleConnectionEvictor idleConnectionEvictor;
	
	private CloseableHttpClient httpClient;
	
	public HttpConnectManager() {
		init();
		this.connectionManager = createConnectionManager();
		createHttpClient();
	}
	
	public void init() {
		cookieStore = new BasicCookieStore();
		clientContext = HttpClientContext.create();
		clientContext.setCookieStore(cookieStore);
	}
	
	public PoolingHttpClientConnectionManager createConnectionManager() {
		// 创建HTTP的连接池管理对象
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		// 设置最大连接数
		connectionManager.setMaxTotal(1000);
		// 每个路由最大连接数
		connectionManager.setDefaultMaxPerRoute(500);
		// 设置单个路由连接数
//		HttpHost httpHost = new HttpHost("http://www.baidu.com", 80);
//		connectionManager.setMaxPerRoute(new HttpRoute(httpHost), 50);

		connectionManager.setValidateAfterInactivity(5);
		idleConnectionEvictor = new IdleConnectionEvictor(connectionManager);
		idleConnectionEvictor.start();
		return connectionManager;
	}

	/**
	 * 
	 * <p> 请求重试处理. </p>
	 * 
	 * @title retryHandler
	 * @param tryTimes 重试次数
	 * @return HttpRequestRetryHandler
	 */
	public HttpRequestRetryHandler createRetryHandler(final int tryTimes) {
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {

			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				/**
				 * 重试次数超过指定次数(tryTimes)
				 */
				if (executionCount > tryTimes)
					return false;

				/**
				 * 没有回复（服务器丢掉了连接）
				 */
				if (exception instanceof NoHttpResponseException)
					return true;

				/**
				 * SSL握手异常不重试
				 */
				if (exception instanceof SSLHandshakeException)
					return false;

				/**
				 * 超时不重试
				 */
				if (exception instanceof InterruptedIOException)
					return false;

				/**
				 * 服务不可达，重试
				 */
				if (exception instanceof UnknownHostException)
					return true;

				/**
				 * 连接超时不重试
				 */
				if (exception instanceof ConnectTimeoutException)
					return false;

				/**
				 * SSL其它异常，一律不重试
				 */
				if (exception instanceof SSLException)
					return false;

				/**
				 * 如果幂等，也重试
				 */
				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				if (!(request instanceof HttpEntityEnclosingRequest))
					return false;

				return false;
			}
		};
		return httpRequestRetryHandler;
	}
	
	public CloseableHttpClient createHttpClient() {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager)
				.setRetryHandler(createRetryHandler(3))
				.setDefaultCookieStore(cookieStore).build();
		closeHttpClient();
		this.httpClient = httpClient;
		return httpClient;
	}
	
	public CloseableHttpClient createHttpClient(String host, String username, String password) {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager)
				.setRetryHandler(createRetryHandler(3))
				.setDefaultCookieStore(StringUtils.isBlank(host) ? cookieStore : createCookieStoreByBasic(host, username, password)).build();
		closeHttpClient();
		this.httpClient = httpClient;
		return httpClient;
	}
	
	public void closeHttpClient() {
		try {
			if (Objects.nonNull(httpClient)) {
				this.httpClient.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String doGet(String url) throws ClientProtocolException, IOException {
		return doGet(url, null, null, null, null);
	}
	
	public String doGet(String url, String paras) throws ClientProtocolException, IOException {
		return doGet(url, null, paras);
	}
	
	public String doGet(String url, Map<String, String> mapParas) throws ClientProtocolException, IOException {
		return doGet(url, mapParas, null, null, null);
	}
	
	public String doGet(String url, Map<String, String> mapParas, String paras) throws ClientProtocolException, IOException {
		return doGet(url, mapParas, paras, null, null);
	}

	public String doGet(String url, Map<String, String> mapParas, Map<String, String> mapHeaders) throws ClientProtocolException, IOException {
		return doGet(url, mapParas, null, mapHeaders, null);
	}

	public String doGet(String url, Map<String, String> mapParas, String paras, Map<String, String> mapHeaders, List<Header> headers) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(url);
		StringBuffer sb = new StringBuffer();
		if (null != paras) {
			sb.append(paras);
		}
		
		if (null != mapParas) {
			for (Entry<String, String> en : mapParas.entrySet()) {
				sb.append("&" + en.getKey() + "=" + en.getValue());
			}
		}
		if (sb.length() > 0) {
			url += "?" + sb.toString();
			httpGet.setURI(URI.create(url));
		}
		if (null != mapHeaders) {
			for (Entry<String, String> en : mapHeaders.entrySet()) {
				httpGet.addHeader(en.getKey(), en.getValue());
			}
		}
		if (null != headers) {
			for (Header header : headers) {
				httpGet.addHeader(header);
			}
		}
		CloseableHttpResponse chr = httpClient.execute(httpGet, clientContext);
		String result = EntityUtils.toString(chr.getEntity());
		chr.close();
		return result;
	}

	public String doPost(String url) throws ClientProtocolException, IOException {
		return doPost(url, null, null, null, null);
	}
	
	public String doPost(String url, Map<String, String> mapParas) throws ClientProtocolException, IOException {
		return doPost(url, mapParas, null, null, null);
	}

	public String doPost(String url, List<NameValuePair> mapHeaders) throws ClientProtocolException, IOException {
		return doPost(url, null, mapHeaders);
	}

	public String doPost(String url, Map<String, String> mapParas, Map<String, String> mapHeaders) throws ClientProtocolException, IOException {
		return doPost(url, mapParas, null, mapHeaders, null);
	}

	public String doPost(String url, Map<String, String> mapParas, List<NameValuePair> paras) throws ClientProtocolException, IOException {
		return doPost(url, mapParas, paras, null, null);
	}
	
	public String doPost(String url, Map<String, String> mapParas, List<NameValuePair> paras,
			Map<String, String> mapHeaders, List<Header> headers) throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (null != mapParas) {
			for (Entry<String, String> en : mapParas.entrySet()) {
				list.add(new BasicNameValuePair(en.getKey(), en.getValue()));
			}
		}
		if (null != paras) {
			list.addAll(paras);
		}
		if (!list.isEmpty()) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, StandardCharsets.UTF_8);
			httpPost.setEntity(entity);
		}
		if (null != mapHeaders) {
			for (Entry<String, String> en : mapHeaders.entrySet()) {
				httpPost.addHeader(en.getKey(), en.getValue());
			}
		}
		if (null != headers) {
			for (Header header : headers) {
				httpPost.addHeader(header);
			}
		}
		CloseableHttpResponse chr = null;
		try {
			chr = httpClient.execute(httpPost, clientContext);
			if (chr.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(chr.getEntity());
			}
		} finally {
			chr.close();
		}
		return null;
	}
	
	/**
	 * 
	 * <p>
	 *		定时关闭空闲连接.
	 * </p>
	 * @className IdleConnectionEvictor
	 * @author Server  
	 * @date 2018年5月21日 上午11:18:37 
	 *    
	 * @copyright 2018 www.rykj.com Inc. All rights reserved.
	 */
	public class IdleConnectionEvictor extends Thread {
		HttpClientConnectionManager connectionManager;
		volatile boolean shutdown;
		public IdleConnectionEvictor(HttpClientConnectionManager connectionManager) {
			this.connectionManager = connectionManager;
		}
		
		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(3000);
						connectionManager.closeExpiredConnections();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void shutdown() {
			shutdown = true;
			synchronized (this) {
				notifyAll();
			}
		}
	}

	public RequestConfig createRequestConfig() {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(1000). // 创建连接的最长时间
				setConnectionRequestTimeout(500). // 从连接池取得连接的最长时间
				setSocketTimeout(10 * 1000). // 数据传输的最长时间
				setCookieSpec(CookieSpecs.STANDARD).build();
		return config;
	}

	/**
	 * 
	 * <p>
	 *		baic auth.
	 * </p>
	 * @title createCookieStoreByBasic
	 * @param address 地址
	 * @param username 用户名
	 * @param password 密码
	 * @return CookieStore
	 */
	public CookieStore createCookieStoreByBasic(String address, String username, String password) {
		CookieStore cookieStore = new BasicCookieStore();

		HttpHost host = new HttpHost(address);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		Credentials defaultCreds = new UsernamePasswordCredentials(username, password);
		credsProvider.setCredentials(new AuthScope(host.getHostName(), host.getPort()), defaultCreds);

		// 创建认证缓存  
		AuthCache authCache = new BasicAuthCache();
		
		// 创建基础认证机制 添加到缓存  
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);

		// 将认证缓存添加到执行环境中  即预填充  
		clientContext.setCredentialsProvider(credsProvider);
		clientContext.setAuthCache(authCache);
		clientContext.setCookieStore(cookieStore);

		return cookieStore;
	}
	
	private CookieStore cookieStore;
	
	private HttpClientContext clientContext;
	
	
}

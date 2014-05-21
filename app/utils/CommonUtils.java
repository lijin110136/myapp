package utils;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;

public class CommonUtils {
	public static final String BUCKET = "js0801";
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);
	private static Mac mac;
	static{
		Config.ACCESS_KEY = "EcJodayfucdUyzGAXf7ilY7YnEFoedppFDXXYzoY";
        Config.SECRET_KEY = "ddUKm74i5h_W1UqJaplfWr08nIcnfc4EfUMdubV6";
        mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
	}
	
	public static Mac getMac(){
		if(mac == null){
			mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
		}
		return mac;
	}
	
	public static String getRequest(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		//设置HttpClient的连接超时和读取超时时间
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,60000);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,60000);
		String json = null;
		try {
			LOGGER.info("====================post request===============");
			LOGGER.info("Request URL: " + url);
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpclient.execute(httpGet);
			LOGGER.info("httpResponse.getStatusLine: " + httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				return json;
			} else {
				json = EntityUtils.toString(httpResponse.getEntity());
				LOGGER.info("Response data: " + json);
			}
			LOGGER.info("--------------------post ended-----------------");
		} catch (Exception e) {
			LOGGER.info("postRequest exception: ", e.toString());
		}
		return json;

	}
	
	public static String getRequest(String url, Map<String, String> params) {
		if(params == null || params.size() == 0){
			return getRequest(url);
		}
		if(url.indexOf('?') < 0) {
			url += '?';
		}
		if(!url.endsWith("?") && !url.endsWith("&")){
			url += "&";
		}
		Set<Map.Entry<String, String>> set = params.entrySet();
		for(Map.Entry<String, String> entry : set){
			String name = entry.getKey();
			String value = entry.getValue();
			if(StringUtils.isNotEmpty(name)){
				url += name + "=" + value + "&";
			}
		}
		return getRequest(url);

	}
}

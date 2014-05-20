package utils;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;

public class CommonUtils {
	public static final String BUCKET = "js0801";
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
}

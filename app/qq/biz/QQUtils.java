package qq.biz;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;

import javax.imageio.ImageIO;

import utils.CommonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class QQUtils {
	public static final String QQ_INFO_URL = "http://r.qzone.qq.com/cgi-bin/user/cgi_personal_card?uin=";
	public static QQInfo getQQInfo(String qq) {
		QQInfo qqInfo = new QQInfo();
		try {
			String str = CommonUtils.getRequest(QQ_INFO_URL + qq);
			str = str.substring("_Callback(".length(), str.length() - 3);
			JSONObject jsonObject = JSONObject.fromObject(str);
			String headUrl = jsonObject.getString("avatarUrl");
			String qqName = jsonObject.getString("nickname");
			qqInfo.setQq(qq);
			qqInfo.setQqName(qqName);
			qqInfo.setHeadUrl(headUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qqInfo;
	}
	
	public static BufferedImage imageFromURL(String url){
		BufferedImage image = null;
		try {
			URL urlObj = new URL(url);
			image = ImageIO.read(urlObj);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}
	
}	

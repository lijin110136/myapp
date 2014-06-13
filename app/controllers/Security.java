package controllers;

import java.util.HashMap;
import java.util.Map;

import models.User;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;

import utils.CommonUtils;
import utils.SqlSessionFactoryUtls;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.net.CallRet;
import com.qiniu.api.rs.RSClient;

public class Security extends Secure.Security{
	static boolean authenticate(String username, String password) {
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password))
			return false;
		SqlSession sqlSession = SqlSessionFactoryUtls.getSessionFactory().openSession();
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("username", username);
    		map.put("password", password);
    		User user = (User)sqlSession.selectOne("models.User.selectUser", map);
    		if(user != null){
    			return true;
    		}
    		return false;
		}finally{
			sqlSession.close();
		}
    }
}

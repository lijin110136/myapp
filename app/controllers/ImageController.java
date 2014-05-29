package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONException;

import models.Image;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.net.CallRet;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.RSClient;
import com.qiniu.api.rsf.ListItem;
import com.qiniu.api.rsf.ListPrefixRet;
import com.qiniu.api.rsf.RSFClient;

import play.mvc.Controller;
import utils.CommonUtils;
import utils.SqlSessionFactoryUtls;

public class ImageController extends Controller{
	/**
	 * 查看七牛云服务器上的图片列表
	 */
	public static void listImage(){
		List<Image> all = getImageList();
		render("image/listImage.html", all);
	}
	
	public static void deleteImage(Long id, String key){
		SqlSession sqlSession = SqlSessionFactoryUtls.getSessionFactory().openSession();
    	try {
    		sqlSession.delete("models.Image.deleteImage", id);
    		Mac mac = CommonUtils.getMac();
    		RSClient client = new RSClient(mac);
    		CallRet ret = client.delete(CommonUtils.BUCKET, key);
    		sqlSession.commit();
    		renderJSON("{\"message\":\"删除成功！\"}");
		}finally{
			sqlSession.close();
		}
		
		
	}
	
	protected static List<ListItem> getQiNiuImageList(){
		Mac mac = CommonUtils.getMac();
        RSFClient client = new RSFClient(mac);
        String marker = "";
        List<ListItem> all = new ArrayList<ListItem>();
        ListPrefixRet ret = null;
        while (true) {
            ret = client.listPrifix(CommonUtils.BUCKET, null, marker, 10);
            marker = ret.marker;
            all.addAll(ret.results);
            if (!ret.ok()) {
                break;
            }
        }
        return all;
	}
	
	protected static List<Image> getImageList(){
		List<Image> list = new ArrayList<Image>();
		SqlSession sqlSession = SqlSessionFactoryUtls.getSessionFactory().openSession();
    	try {
    		list = sqlSession.selectList("models.Image.selectImage");
		}finally{
			sqlSession.close();
		}
        return list;
	}
	
	public static void uploadImage(File file, String description, String title){
		//上传图片到七牛空间
		Mac mac = CommonUtils.getMac();
		PutPolicy putPolicy = new PutPolicy(CommonUtils.BUCKET);
		SqlSession sqlSession = SqlSessionFactoryUtls.getSessionFactory().openSession();
		try {
			String uptoken = putPolicy.token(mac);
			PutExtra extra = new PutExtra();
			String key = null;
	        PutRet ret = IoApi.putFile(uptoken, key, file.getAbsolutePath(), extra);
	        if(ret.getStatusCode() > 200){
	        	return;
	        }
	        Image image = new Image();
			image.setFname(file.getName());
			image.setUrl(ret.getKey());
			image.setDescription(description);
			image.setTitle(title);
			image.setCreatedTime(new Date());
			image.setModifiedTime(new Date());
			image.setFsize((double)(file.length()/1024));
			
	    	sqlSession.insert("models.Image.insertImage", image);
	    	sqlSession.commit();
			sqlSession.close();
			renderHtml("{\"message\":\"删除成功！\"}");
			
		} catch (AuthException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
			sqlSession.close();
		}
	}
}

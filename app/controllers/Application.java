package controllers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import models.Student;

import org.apache.ibatis.session.SqlSession;
import org.jboss.netty.logging.CommonsLoggerFactory;
import org.json.JSONException;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.net.CallRet;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.RSClient;
import com.qiniu.api.rsf.ListItem;
import com.qiniu.api.rsf.ListPrefixRet;
import com.qiniu.api.rsf.RSFClient;

import play.data.validation.Required;
import play.libs.Files;
import play.mvc.Controller;
import qq.biz.QQInfo;
import qq.biz.QQUtils;
import utils.CommonUtils;
import utils.ConnectionUtils;
import utils.SqlSessionFactoryUtls;

public class Application extends Controller {

    public static void index() {
    	List<ListItem> lists = getImageList();
    	
        render(lists, "");
    }
   
    public static void lijin(){
    	renderJSON("{name:'李金'}");
    }
    
    public static void sayHello(@Required String myName){
    	if(validation.hasErrors()){
    		flash.error("Please enter name");
    		index();
    	}
    	render(myName);
    }
    
    public static void proxy(String urlStr){
    	try {
			URL url  = new URL(urlStr);
			InputStream ins = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(ins));
			String strLine = "";
			StringBuffer sb = new StringBuffer();
			while((strLine = br.readLine()) != null){
				sb.append(strLine + "\r\n");
			}
			renderText(sb.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    /**将文件上传到七牛空间中
     * @param uploadFile
     */
    public static void upload(File uploadFile){
    	File f = new File("upload/" + uploadFile.getName());
    	Files.copy(uploadFile, f);
    	renderText(uploadFile.getName());
    }
    
    public static void list(){
    	render();
    }
    
    public static void listStudent(){
    	//Connection conn = ConnectionUtils.getConnection();
//    	ResultSet rs = ConnectionUtils.exeucteQuery("select * from student");
//    	StringBuffer sb = new StringBuffer();
//    	try {
//    		while(rs.next()){
//    			sb.append("name:" + rs.getString("sname") + ", age:" + rs.getInt(3) + "\r\n");
//        	}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	renderText(sb.toString());
    	
    	List<Student> studentList = new ArrayList<Student>();
		SqlSession sqlSession = SqlSessionFactoryUtls.getSessionFactory().openSession();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", null);
			studentList = sqlSession.selectList("models.Student.selectStudent", map);
		}finally{
			sqlSession.close();
		}
		render(studentList);
    }
    
    public static void selectStudent(Long id){
    	SqlSession sqlSession = SqlSessionFactoryUtls.getSessionFactory().openSession();
    	Student student = new Student();
		try {
			if(id != null){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				student = (Student)sqlSession.selectOne("models.Student.selectStudent", map);
			}
		}finally{
			sqlSession.close();
		}
    	renderJSON(student);
    }
    
    public static void doSave(Long id, String sname, Integer sage, String email, String phoneno){
    	Student student = new Student();
    	student.setId(id);
    	student.setSage(sage);
    	student.setSname(sname);
    	student.setEmail(email);
    	student.setPhoneno(phoneno);
    	student.setSno("");
    	SqlSession sqlSession = SqlSessionFactoryUtls.getSessionFactory().openSession();
    	try {
    		if(student.getId() == null){
    			sqlSession.insert("models.Student.insertStudent", student);
    		}else{
    			
    			sqlSession.update("models.Student.updateStudent", student);
    		}
    		sqlSession.commit();
		}finally{
			sqlSession.close();
		}
    	renderJSON("{\"message\":\"保存成功！\"}");
    }

	public static void baiduVerify() {
		redirect("/public/baidu_verify_Ihg2FfaG4m.html");
	}
	
	public static void listUploadFile(){
		File f = new File("public/mp3/");
		if(f.exists()){
			String[] list = f.list();
			render(list,"");
		}else{
			render();
		}
	}
	
	public static void del(Long id){
		SqlSession sqlSession = SqlSessionFactoryUtls.getSessionFactory().openSession();
    	try {
    		sqlSession.delete("models.Student.deleteStudent", id);
    		sqlSession.commit();
		}finally{
			sqlSession.close();
		}
    	renderJSON("{\"message\":\"删除成功！\"}");
	}
	
	private static List<ListItem> getImageList(){
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
	
	/**
	 * 查看七牛云服务器上的图片列表
	 */
	public static void listImage(){
		List<ListItem> all = getImageList();
		render(all, "");
	}
	
	public static void deleteImage(String key){
		Mac mac = CommonUtils.getMac();
		RSClient client = new RSClient(mac);
		CallRet ret = client.delete(CommonUtils.BUCKET, key);
		renderJSON("{\"message\":\"删除成功！\"}");
	}
	
	public static void showQQInfo(){
		File f = new File("public/images/" + "qqbackground.jpg");
		BufferedImage image = null;
		FileInputStream fis = null;
		try {
			image = ImageIO.read(f);
			fis = new FileInputStream(f);
			//取得访问者的QQ信息
			String qqZoneURL = request.headers.get("referer").value();
			//qqZoneURL  = "http://user.qzone.qq.com/838812705/infocenter";
			if((qqZoneURL != null) && (!"".equals(qqZoneURL))){
				String qqtmp = qqZoneURL.substring(qqZoneURL.indexOf("uin") + "uin=".length());
				String qq = qqtmp.substring(0, qqtmp.indexOf("&"));
				QQInfo qqInfo = QQUtils.getQQInfo(qq);
				String qqName = qqInfo.getQqName();
				String headUrl = qqInfo.getHeadUrl();
				Graphics graphics = image.getGraphics();
				Font font = new Font("楷体", Font.BOLD, 50);
				graphics.setFont(font);
				graphics.setColor(Color.RED);
				graphics.drawString(qqName, 0, 100);
				BufferedImage headImage = QQUtils.imageFromURL(headUrl);
				if(headImage != null){
					graphics.drawImage(headImage, 0, 150, null);
				}
			}
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", bs);
			ByteArrayInputStream is= new ByteArrayInputStream(bs.toByteArray());
			response.setContentTypeIfNotSet("image/jpg");
			renderBinary(is);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void videoShow() {
		List<ListItem> lists = getImageList();
		render(lists, "");
	}
}
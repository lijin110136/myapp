package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;

import play.data.validation.Required;
import play.libs.Files;
import play.mvc.Controller;
import utils.ConnectionUtils;

public class Application extends Controller {

    public static void index() {
        render(null,123);
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
    	ResultSet rs = ConnectionUtils.exeucteQuery("select * from student");
    	StringBuffer sb = new StringBuffer();
    	try {
    		while(rs.next()){
    			sb.append("name:" + rs.getString("sname") + ", age:" + rs.getInt(3) + "\r\n");
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	renderText(sb.toString());
    }
}
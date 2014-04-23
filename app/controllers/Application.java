package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import play.data.validation.Required;
import play.libs.Files;
import play.mvc.Controller;

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
}
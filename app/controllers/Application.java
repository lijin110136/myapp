package controllers;

import play.*;
import play.data.validation.Required;
import play.mvc.*;

import java.util.*;

import models.*;

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

}
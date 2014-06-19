package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Album;
import models.Image;
import models.Student;

import org.apache.ibatis.session.SqlSession;

import play.mvc.Controller;
import utils.SqlSessionFactoryUtls;

public class AlbumController extends Controller{
	public static void listAlbum(){
		List<Album> list = new ArrayList<Album>();
		SqlSession sqlSession = SqlSessionFactoryUtls.getSessionFactory().openSession();
    	try {
    		list = sqlSession.selectList("models.Album.selectAlbum");
		}finally{
			sqlSession.close();
		}
    	render("album/listAlbum.html", list);
	}
	
	public static void selectAlbum(Long id){
		SqlSession sqlSession = SqlSessionFactoryUtls.getSessionFactory().openSession();
		Album album = new Album();
		try {
			if(id != null){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				album = (Album)sqlSession.selectOne("models.Album.selectAlbum", map);
			}
		}finally{
			sqlSession.close();
		}
    	renderJSON(album);
	}
	
	
	public static void doSave(Album album){
		SqlSession sqlSession = SqlSessionFactoryUtls.getSessionFactory().openSession();
    	try {
    		if(album.getId() == null){
    			sqlSession.insert("models.Album.insertAlbum", album);
    		}else{
    			sqlSession.update("models.Album.updateAlbum", album);
    		}
    		sqlSession.commit();
    		play.Logger.info("保存相册,相册ID:"+album.getId()+";相册名称:" + album.getAlbumName());
		}finally{
			sqlSession.close();
		}
    	renderJSON("{\"message\":\"保存成功！\"}");
	}
	
	public static void doDelete(Long id){
		SqlSession sqlSession = SqlSessionFactoryUtls.getSessionFactory().openSession();
    	try {
    		sqlSession.delete("models.Album.deleteAlbum", id);
    		sqlSession.commit();
    		play.Logger.info("删除相册,相册ID:"+id);
		}finally{
			sqlSession.close();
		}
    	renderJSON("{\"message\":\"删除成功！\"}");
	}
}

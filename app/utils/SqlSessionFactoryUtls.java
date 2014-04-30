package utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionFactoryUtls {
	private static SqlSessionFactory sqlSessionFacory;
	static {
		URL url = ConfigurationUtils.locate("conf", "Configuration.xml");
		try {
			InputStreamReader reader = new InputStreamReader(url.openStream());
			sqlSessionFacory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static SqlSessionFactory getSessionFactory(){
		return sqlSessionFacory;
	}
}

package qiaohuang.tdt.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;


/**
 * @author xuexian.wu and qiaohuang  
 *
 */

public class DBConnection {
	  public static Connection getConnection() {
	    // TODO Auto-generated method stub
	    Connection con = null;
	    try {
	      Class.forName("com.mysql.jdbc.Driver");

	      con = DriverManager.getConnection(
	    		  "jdbc:mysql://172.16.3.96:3306/?useUnicode=true&characterEncoding=utf-8",
	          "root", "123456");

	    } catch (Exception e) {
	      System.out.println(" connect mysql error " + e.getMessage());
	      e.printStackTrace();
	    }
	    return con;
	  }

	


	public static ArrayList<DBFile> resultResultQuery(String sql) {

	    Connection conn = DBConnection.getConnection();
	    ArrayList<DBFile> list = new ArrayList<DBFile>();

	    Statement st=null;
		try {
			
	      st = (Statement) conn.createStatement();
	      ResultSet rs = st.executeQuery(sql);
	      while (rs.next()) {
	        DBFile temp =new DBFile();
	        temp.setTitle(rs.getString("title"));
	        temp.setUrl("null");
	        temp.setTimeDate(rs.getDate("create_time"));
	        temp.setContent(replaceAll(rs.getString("content")));
	        temp.setSource("null");
	        list.add(temp);
	      }
	      st.close();
	      conn.close();

	    } catch (SQLException e) {
	      System.out.println("lotquery error...");
	      e.printStackTrace();

	    } finally {
	      try {
	    	  
	        if (st != null)
	          st.close();
	        if (conn != null)
	          conn.close();
	      } catch (SQLException e) {
	        System.out.println("close error...");
	        e.printStackTrace();
	      }
	    }
	    return list;
	  }


	public static void getResult() {
	    
		//for debug
		
		String sql= "select title,content,create_time from`htnewsroom`.`article` where create_time between '2015-04-06' and '2015-04-12'";
		ArrayList<DBFile> test = resultResultQuery(sql);

	    int i =0;
	    for (DBFile te : test) {
	   
	      System.out.println((i++)+"\n"+te.getUrl() + "\n" + te.getSource()+"\n"+te.getTimeDate().toString()+"\n"+te.getTitle()+"\n");

	    }

	  }
	
	
	private static String replaceAll(String html) {
	    String htmlStr;
	    htmlStr = html.replaceAll("(?is)<!--.*?-->", "");
	    htmlStr = htmlStr.replaceAll("(?is)<script.*?>.*?</script>", "");
	    htmlStr = htmlStr.replaceAll("(?is)<style.*?>.*?</style>", "");
	    htmlStr = htmlStr.replaceAll("&.{2,5};|&#.{2,5};", " ");
	    htmlStr = htmlStr.replaceAll("(?is)<.*?>", "");
	    return htmlStr;
	  }
}


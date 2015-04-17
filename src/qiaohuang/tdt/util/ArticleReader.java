package qiaohuang.tdt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import qiaohuang.tdt.core.Article;
import qiaohuang.tdt.core.Stream;

/**
 * @author qiaohuang
 *
 */
public class ArticleReader {
	
	//"static" so that the stopword.dic and noiseword.dic only need to be loaded once
	public static WordFilter wordFilter;
	
	
	public ArticleReader(){
		wordFilter = new WordFilter();
	}
	
	public Stream readArticleFiles(String dirPath){
		
		/*
		 * Read all news articles in a single directory as a stream
		 * These articles have the same "Calendar" (same day) 
		 * The algorithm's default time slot setting is ONE DAY
		 */
		
		
		/*
		 * Sample news article:
		 * 
		 * title	吕秀莲结束“救扁绝食” 送医开始喝米汤
		 * url		http://news.ifeng.com/a/20150101/42845791_0.shtml
		 * calendar	2015-01-01 23:52:00
		 * content	据今日新闻网报道，台法务部矫正署因塞车延误公文送件，致使陈水扁...
		 * 
		 * WARNING: in my local dataset, the default encoding is GB2312
		 * 
		 */
		
		Stream stream = new Stream();
		
		
		for (File articleFile : new File(dirPath).listFiles()) {
			
			BufferedReader reader = null;

			try {

				reader = new BufferedReader(new FileReader(articleFile));

				Article article = new Article();

				article.setTitle(reader.readLine());
				article.setUrl(reader.readLine());
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {  
					calendar.setTime(formatter.parse(reader.readLine()));
				}catch (ParseException e) {  
		            e.printStackTrace(); 
		        }  
				article.setCalendar(calendar);
				article.setContent(reader.readLine());
				
				//empty content or empty title, no use
				if(article.getContent()==null||article.getTitle()==null)
					continue;
				
				article.segmentTerms();
				
				stream.getArticles().add(article);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			
		}
		
		stream.build();
		
		return stream;
		
	}

	
	public Stream readArticleFromDB(String sql){
		Stream stream = new Stream();
		
		ArrayList<DBFile> list= DBConnection.resultResultQuery(sql);
		for(DBFile file:list){
			Article article = new Article();
			
			if(file.getContent()==null||file.getTitle()==null)
				continue;
			
			article.setContent(file.getContent());
			
			article.setTitle(file.getTitle());
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(file.getTimeDate());
			article.setCalendar(calendar);
			
			
			
			article.segmentTerms();
			stream.getArticles().add(article);
		}
		
		
		stream.build();
		
		
		return stream;
	}
}

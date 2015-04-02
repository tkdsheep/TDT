package qiaohuang.tdt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import qiaohuang.tdt.core.Document;

public class DocFileReader {
	
	
	public void readDocFiles(String dirPath,List<Document> docs){
		
		/*
		 * Read all news documents in a single directory
		 * These documents have the same "Calendar" (same day) 
		 * The algorithm's default time slot setting is ONE DAY
		 */
		
		
		/*
		 * Sample news document:
		 * 
		 * title	吕秀莲结束“救扁绝食” 送医开始喝米汤
		 * url		http://news.ifeng.com/a/20150101/42845791_0.shtml
		 * calendar	2015-01-01 23:52:00
		 * content	据今日新闻网报道，台法务部矫正署因塞车延误公文送件，致使陈水扁...
		 * 
		 * WARNING: in my local dataset, the default encoding is GB2312
		 * 
		 */
		
		for (File docFile : new File(dirPath).listFiles()) {
			
			BufferedReader reader = null;

			try {

				reader = new BufferedReader(new FileReader(docFile));

				Document doc = new Document();

				doc.setTitle(reader.readLine());

				doc.setUrl(reader.readLine());

				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {  
					calendar.setTime(formatter.parse(reader.readLine()));
				}catch (ParseException e) {  
		            e.printStackTrace(); 
		        }  
				doc.setCalendar(calendar);

				doc.setContent(reader.readLine());

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
		
	}

}

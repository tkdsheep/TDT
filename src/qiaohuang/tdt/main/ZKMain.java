package qiaohuang.tdt.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import qiaohuang.tdt.core.History;
import qiaohuang.tdt.core.Stream;
import qiaohuang.tdt.core.TDTModel;
import qiaohuang.tdt.core.Topic;
import qiaohuang.tdt.util.ArticleReader;

public class ZKMain {
	
public static void main(String args[])throws Exception{
		
		/*
		 * Training for historical data (historical document frequency)
		 * Solve the "cold start" problem
		 */
		System.out.println("Start training");
		
		ArticleReader articleReader = new ArticleReader();
		History history = new History();
		
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");  
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(df.parse("2015-04-01"));
		for(int i=1;i<=10;i++){
			String sql = "select title,content,create_time from`htnewsroom`.`article` where create_time = '";
			sql+=df.format(calendar.getTime())+"'";
			System.out.println(sql);
			Stream stream = articleReader.readArticleFromDB(sql);
			System.out.println("stream size: "+stream.getArticles().size());
			System.out.println("word size: "+stream.getDf().size());
			history.updateHistory(stream);
			calendar.add(Calendar.DATE,1);
		}
		
		
		System.out.println("Training finished");
		
		
		
		
		//run TDT on new data
		LinkedList<Topic> topics = new LinkedList<Topic>();
		ArrayList<Topic> deletedTopics = new ArrayList<Topic>();
		TDTModel model = new TDTModel("ZK_Result.txt");
		
		
		
		
		calendar.setTime(df.parse("2015-04-11"));
		for(int i=1;i<=11;i++){
			String sql = "select title,content,create_time from`htnewsroom`.`article` where create_time = '";
			sql+=df.format(calendar.getTime())+"'";
			System.out.println(sql);
			Stream stream = articleReader.readArticleFromDB(sql);
			System.out.println("stream size: "+stream.getArticles().size());
			model.process(history, topics, stream,deletedTopics);
			calendar.add(Calendar.DATE,1);
		}
		
		for(Topic topic:deletedTopics){
			if(!topic.isNoiseTopic())
				topic.printHistory(TDTModel.writer);
		}
		
		for(Topic topic:topics){
			if(!topic.isNoiseTopic())
				topic.printHistory(TDTModel.writer);
		}
		
		//very important!!!
		TDTModel.writer.flush();
		
		
		
	}
	

}

package qiaohuang.tdt.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import qiaohuang.tdt.core.History;
import qiaohuang.tdt.core.Stream;
import qiaohuang.tdt.core.TDTModel;
import qiaohuang.tdt.core.Topic;
import qiaohuang.tdt.util.ArticleReader;

/**
 * @author qiaohuang
 *
 */
public class TDTMain {
	
	
	public static void main(String args[])throws IOException{
		
		/*
		 * Training for historical data (historical document frequency)
		 * Solve the "cold start" problem
		 */
		System.out.println("Start training");
		String fileRootPath = "F:/data/IFENG_Oct";
		ArticleReader articleReader = new ArticleReader();
		History history = new History();
		for (File docFile : new File(fileRootPath).listFiles()) {
			int day = Integer.parseInt(docFile.getName());
			if(day>=20131001&&day<=20131010){
				System.out.println("reading and processing "+docFile.getAbsolutePath());
				Stream stream = articleReader.readArticleFiles(docFile.getAbsolutePath());
				history.updateHistory(stream);
			}
		}
		System.out.println("Training finished");
		
		
		
		//run TDT on new data
		fileRootPath = "F:/data/IFENG_Dec";
		LinkedList<Topic> topics = new LinkedList<Topic>();
		ArrayList<Topic> deletedTopics = new ArrayList<Topic>();
		TDTModel model = new TDTModel();
		
		for (File docFile : new File(fileRootPath).listFiles()) {
			
			int day = Integer.parseInt(docFile.getName());
			
			if(day>=20131201&&day<=20131230){
				System.out.println("reading and processing "+docFile.getAbsolutePath());
				
				//read a new article stream
				Stream stream = articleReader.readArticleFiles(docFile.getAbsolutePath());
				
				//call TDT algorithm model, tracking and detecting topics
				model.process(history, topics, stream,deletedTopics);		
				
				
			}
		}
		
		for(Topic topic:deletedTopics){
			if(!topic.isNoiseTopic())
				topic.printHistory();
		}
		
		for(Topic topic:topics){
			if(!topic.isNoiseTopic())
				topic.printHistory();
		}
		
		//very important!!!
		TDTModel.writer.flush();
		
		
		
	}

}

package qiaohuang.tdt.main;

import java.io.File;
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
	
	
	public static void main(String args[]){
		
		
		String rootPath = "F:/data/IFENG_Dec";
		ArticleReader articleReader = new ArticleReader();
		History history = new History();
		LinkedList<Topic> topics = new LinkedList<Topic>();
		TDTModel model = new TDTModel();
		
		for (File docFile : new File(rootPath).listFiles()) {
			
			int day = Integer.parseInt(docFile.getName());
			
			if(day>=20131201&&day<=20131207){
				System.out.println(docFile.getAbsolutePath());
				
				//read a new article stream
				Stream stream = articleReader.readArticleFiles(docFile.getAbsolutePath());
				System.out.println("stream article size: "+stream.getArticles().size());
				System.out.println("stream word size: "+stream.getDf().size());
				
				
				//call TDT algorithm model, tracking and detecting topics
				model.process(history, topics, stream);		
				
				
			}
		}
		
		
	}

}

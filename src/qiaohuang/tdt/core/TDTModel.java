package qiaohuang.tdt.core;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;

import qiaohuang.tdt.conf.GlobalParam;
import qiaohuang.tdt.conf.PathConfig;
import qiaohuang.tdt.util.TopicComparator;

/**
 * @author qiaohuang
 *
 */
public class TDTModel {
	
	public static BufferedWriter writer;
	
	public TDTModel() throws IOException{
		writer = new BufferedWriter(new FileWriter(PathConfig.resultPath+"result.txt"));
	}
			
	
	public void process (History history,LinkedList<Topic> topics,Stream stream)throws IOException{
		
		Integer newTopicId = 1;
		
		for(Article article:stream.getArticles()){
			double maxSim = 0;
			Topic relatedTopic = null;
			for(Topic topic:topics){
				double sim = SimFunction.cosSim(article, topic);
				if(sim > maxSim){
					maxSim = sim;
					relatedTopic = topic;
				}
			}
			if(maxSim>=GlobalParam.simThreshold){//this article belongs to an existing topic
				relatedTopic.Update(article);
				article.setSim(maxSim);
			}
			else{//here we find a new topic!
				
				Integer tmp = article.getCalendar().get(Calendar.YEAR)*10000
						+article.getCalendar().get(Calendar.MONTH+1)*100
						+article.getCalendar().get(Calendar.DAY_OF_MONTH);
				System.out.println(tmp);
				String topicId = tmp.toString()+newTopicId.toString();
				newTopicId++;
				relatedTopic = new Topic(article,topicId);
				topics.add(relatedTopic);
				article.setSim(1);
			}
			article.setRelatedTopic(relatedTopic);
			
		}
		
		writer.write("\n---------------------------------\n\r\n");
		
		writer.write("stream article size: "+stream.getArticles().size()+"\r\n");
		writer.write("stream word size: "+stream.getDf().size()+"\r\n");
		
		writer.write("number of topics: "+topics.size()+"\r\n");
		
		printTopics(topics);	
		
		//process done
		
		//update history
		history.updateHistory(stream);
		
		//topic decay and clear topic's "newArticle" list
		ArrayList<Topic> deleteList = new ArrayList<Topic>();
		for(Topic topic:topics){
			topic.getNewArticles().clear();
			topic.setLife(EnergyFunction.lifeDecay(topic.getLife()));//life decay
			if(topic.getLife()<GlobalParam.lifeDecay)//topic "die"
				deleteList.add(topic);
		}
		for(Topic topic:deleteList){
			topics.remove(topic);
		}
		
		writer.write("after decay, number of topics: "+topics.size()+"\r\n");
		
	}
	
	private void printTopics (LinkedList<Topic> topics)throws IOException{
		
		Collections.sort(topics, new TopicComparator());
		for(Topic topic:topics){
			if(topic.getNewArticles().size()>=30)
			topic.printInfo();
		}
		
	}
	
}

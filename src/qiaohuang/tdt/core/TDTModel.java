package qiaohuang.tdt.core;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;

import qiaohuang.tdt.conf.GlobalParam;
import qiaohuang.tdt.util.TopicComparator;

/**
 * @author qiaohuang
 *
 */
public class TDTModel {
	
	
	
	
	
	public void process(History history,LinkedList<Topic> topics,Stream stream){
		
		
		
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
			}
			else{//here we find a new topic!
				Topic topic = new Topic(article);
				topics.add(topic);
			}
		}
		
		System.out.println("\n---------------------------------\n");
		System.out.println(stream.getArticles().get(0).getCalendar());
		
		printTopics(topics,stream.getArticles().get(0).getCalendar());
		
		System.out.println("number of topics: "+topics.size());
		
		//process done, update history
		history.updateHistory(stream);
		ArrayList<Topic> deleteList = new ArrayList<Topic>();
		for(Topic topic:topics){
			topic.setLife(EnergyFunction.lifeDecay(topic.getLife()));//life decay
			if(topic.getLife()<GlobalParam.lifeDecay)//topic "die"
				deleteList.add(topic);
		}
		for(Topic topic:deleteList){
			topics.remove(topic);
		}
		
		System.out.println("after decay, number of topics: "+topics.size());
		
	}
	
	private void printTopics(LinkedList<Topic> topics,Calendar calendar){
		
		Collections.sort(topics, new TopicComparator());
		for(Topic topic:topics){
			if(topic.getArticles().size()>=30)
			topic.printInfo(calendar);
		}
		
	}
	
}

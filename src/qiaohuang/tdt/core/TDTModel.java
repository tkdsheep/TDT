package qiaohuang.tdt.core;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
			
	
	public void process (History history,LinkedList<Topic> topics,Stream stream,ArrayList<Topic> deletedTopics)throws IOException{
		
		LdaModel lda = new LdaModel(stream.getArticles().size()/15,100);//TODO should be global param
		ArrayList<Topic> newTopics = lda.run(stream);
		
		
		for(Topic newTopic:newTopics){
			
			double maxSim = 0;
			Topic relatedTopic = null;
			for(Topic oldTopic:topics){
				double sim = SimFunction.cosSim(newTopic,oldTopic);
				if (sim >= GlobalParam.simThreshold && sim > maxSim) {
					maxSim = sim;
					relatedTopic = oldTopic;
				}
			}		
			
			//this new topic has similar old topic, need to merge them
			if(relatedTopic!=null){
				relatedTopic.merge(newTopic);
			}
			else{
				//this is a real new topic!
				topics.add(newTopic);	
			}	
		}
		
		writer.write("\n---------------------------------\n\r\n");
		
		writer.write("stream article size: "+stream.getArticles().size()+"\r\n");
		writer.write("stream word size: "+stream.getDf().size()+"\r\n");
		
		writer.write("number of topics: "+topics.size()+"\r\n");
		
		for(Topic topic:topics)
			topic.record(stream.getArticles().get(0).getCalendar());
		printTopics(topics);	
		
		//process done
		
		//update history
		history.updateHistory(stream);
		
		/*for(Topic topic:topics){
			topic.deleteArticles(stream.getArticles().get(0).getCalendar());
		}*/
		
		//topic decay and clear topic's "newArticle" list
		ArrayList<Topic> deleteList = new ArrayList<Topic>();
		for(Topic topic:topics){
			topic.getNewArticles().clear();
			topic.setLife(EnergyFunction.lifeDecay(topic.getLife()));//life decay
			if(topic.getLife()<0)//topic "die"
				deleteList.add(topic);
		}
		for(Topic topic:deleteList){
			deletedTopics.add(topic);
			topics.remove(topic);
		}
		
		writer.write("after decay, number of topics: "+topics.size()+"\r\n");
		
		//very important!!!
		writer.flush();
		
	}
	
	private void printTopics (LinkedList<Topic> topics)throws IOException{
		
		Collections.sort(topics, new TopicComparator());
		for(Topic topic:topics){
			if(topic.getNewArticles().size()>0)//TODO should be a global param
				if(topic.getLife()>0.6)
					topic.printCurrentInfo();
		}
		
	}
	
}

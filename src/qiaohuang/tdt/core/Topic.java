package qiaohuang.tdt.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import qiaohuang.tdt.conf.GlobalParam;
import qiaohuang.tdt.util.ArticleComparator;
import qiaohuang.tdt.util.ToString;
import qiaohuang.tdt.util.TraverseMap;



/**
 * @author qiaohuang
 *
 */
public class Topic {
	
	private String topicId;
	private LinkedList<Article> articles;
	private ArrayList<Article> newArticles;
	private boolean isNoiseTopic;
	
	private class State{
		double life;
		Calendar calendar;
		int size;
		
		public State(double life,Calendar calendar,int size){
			this.life = life;
			this.calendar = calendar;
			this.size = size;
		}
	}
	//Record the latest state into history (do it every day)	
	private ArrayList<State> historyState;
	
	/*
	 * wordVector is defined as the arithmetic average of term vectors of all articles in this topic
	 * String is term, Double is weight
	 */
	private HashMap<String,Double> wordVector;
	
	
	private double life;//topic life, can be treated as "hotness"
	
	public Topic(String id){
		articles = new LinkedList<Article>();
		newArticles = new ArrayList<Article>();
		wordVector = new HashMap<String,Double>();
		historyState = new ArrayList<State>();
		life = 0;
		topicId = id;
		isNoiseTopic = true;//default setting is noise topic
	}
	
	public void merge(Topic topic){
		/*
		 * merge two topics
		 * need to update wordVector and topic life
		 */
		
		double energy = EnergyFunction.lifeToEnergy(this.life);
		for(Article article:topic.getArticles())
			energy+=GlobalParam.tdtAlpha*article.getSim();
		
		//update topic life
		this.life = EnergyFunction.energyToLife(energy);
		
		for(Article article:topic.getArticles()){
			insertArticle(article);
			//article.setSim(SimFunction.cosSim(article,this));
			
		}
		
		
		
		//update wordVector
		//updateWordVector();

	}
	
	public void insertArticle(Article article){
		articles.add(article);
		newArticles.add(article);
		
		//update wordVector
		for(Entry<String,WordInfo> entry:TraverseMap.traverseWordInfo(article.getWords())){
			String word = entry.getKey();
			if(wordVector.containsKey(word))
				wordVector.put(word, wordVector.get(word)+entry.getValue().getWeight());
			else wordVector.put(word, entry.getValue().getWeight());
		}		
		
	}
	
	public void deleteArticles(Calendar calendar){
		
		int currentDay = calendar.get(Calendar.DAY_OF_YEAR);
		
		ArrayList<Article> deleteList = new ArrayList<Article>();
		for(Article article:newArticles){
			
			int day = article.getCalendar().get(Calendar.DAY_OF_YEAR);
			if(currentDay-day<7)//TODO: should be global param
				break;
			deleteList.add(article);
		}
		for(Article article:deleteList){
			articles.remove(article);
			
			//update wordVector
			for(Entry<String,WordInfo> entry:TraverseMap.traverseWordInfo(article.getWords())){
				String word = entry.getKey();
				wordVector.put(word, wordVector.get(word)-entry.getValue().getWeight());
			}	
		}
	}
	
	public void record(Calendar calendar){
		//Record current topic state
		State state = new State(this.life,calendar,this.articles.size());
		historyState.add(state);
		if(newArticles.size()>10)
			isNoiseTopic = false;
	}
	
	public void printHistory() throws IOException {
		TDTModel.writer.write("Notice: this topic is dead!!!!!!!");
		TDTModel.writer.newLine();
		TDTModel.writer.write("topic id: "+topicId);
		TDTModel.writer.newLine();
		for (State state : historyState) {
			TDTModel.writer.write(state.size+"\t");
			TDTModel.writer.write(ToString.toString(state.calendar)+"\t");
			int hotness = (int)(state.life*100);
			for(int i=0;i<hotness;i++)
				TDTModel.writer.write("|");
			TDTModel.writer.write("\t"+hotness);
			
			TDTModel.writer.newLine();
			
		}
		for(Article article:articles){
			TDTModel.writer.write(ToString.toString(article.getCalendar())+"\t");
			TDTModel.writer.write(article.getTitle());
			TDTModel.writer.newLine();
			
		}
		TDTModel.writer.newLine();
	}
	
	public void printCurrentInfo() throws IOException {

		TDTModel.writer.write("topic id: " + this.topicId);
		TDTModel.writer.newLine();
		TDTModel.writer.write("topic life: " + this.life);
		TDTModel.writer.newLine();
		TDTModel.writer.write("topic size: " + this.articles.size());
		TDTModel.writer.newLine();

		// print top 25 related articles
		Collections.sort(this.newArticles, new ArticleComparator());
		int i = 0;
		for (Article article : this.newArticles) {
			TDTModel.writer.write(ToString.toString(article.getCalendar())+" ");
			TDTModel.writer.write(article.getTitle());
			TDTModel.writer.newLine();
			if (++i > 25)
				break;
		}

	}



	public LinkedList<Article> getArticles() {
		return articles;
	}

	public ArrayList<Article> getNewArticles() {
		return newArticles;
	}

	public HashMap<String, Double> getWordVector() {
		return wordVector;
	}

	public double getLife() {
		return life;
	}

	public void setLife(double life) {
		this.life = life;
	}

	public String getTopicId() {
		return topicId;
	}

	public boolean isNoiseTopic() {
		return isNoiseTopic;
	}
	


	/*
	public void updateWordVector(){
		
		wordVector.clear();
		for(Article article:articles){
			for(Entry<String,WordInfo> entry:TraverseMap.traverseWordInfo(article.getWords())){
				String word = entry.getKey();
				if(wordVector.containsKey(word))
					wordVector.put(word, wordVector.get(word)+entry.getValue().getWeight());
				else wordVector.put(word, entry.getValue().getWeight());
			}
		}
		
		for(Entry<String,Double> entry:TraverseMap.traverseDouble(wordVector)){
			wordVector.put(entry.getKey(), wordVector.get(entry.getKey())/articles.size());
		}
		
	}*/

	

}

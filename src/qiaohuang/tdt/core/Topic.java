package qiaohuang.tdt.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import qiaohuang.tdt.util.ArticleComparator;



/**
 * @author qiaohuang
 *
 */
public class Topic {
	
	private String topicId;
	private ArrayList<Article> articles;
	private ArrayList<Article> newArticles;
	
	/*
	 * wordVector is defined as the arithmetic average of term vectors of all articles in this topic
	 * String is term, Double is weight
	 */
	private HashMap<String,Double> wordVector;
	
	
	private double life;//topic life, can be treated as "hotness"
	
	public Topic(Article article,String id){
		
		//the param "article" is the first article detected for this new topic
		
		articles = new ArrayList<Article>();
		newArticles = new ArrayList<Article>();
		wordVector = new HashMap<String,Double>();
		life=0.5;
		topicId = id;
		
		Iterator<Entry<String, WordInfo>> it=article.getWords().entrySet().iterator(); 
		while(it.hasNext()){
			Map.Entry<String,WordInfo> entry = (Map.Entry<String,WordInfo>)it.next();
			wordVector.put(entry.getKey(), entry.getValue().getWeight());
		}
		
	}
	
	public void Update(Article article){
		
		/*
		 * merge new article into this topic
		 * need to update wordVector and topic life
		 *  
		 */
		
		//update wordVector
		Iterator<Entry<String, WordInfo>> it=article.getWords().entrySet().iterator(); 
		while(it.hasNext()){
			Map.Entry<String,WordInfo> entry = (Map.Entry<String,WordInfo>)it.next();
			String word = entry.getKey();
			double weight = entry.getValue().getWeight();
			
			if(wordVector.containsKey(word))
				wordVector.put(word, (wordVector.get(word)*articles.size()+weight)/(articles.size()+1));
			else wordVector.put(word, weight/(articles.size()+1));
		}		
		articles.add(article);
		newArticles.add(article);
		
		//update life
		double energy = EnergyFunction.lifeToEnergy(life);
		energy += EnergyFunction.getEnergy(article, this);
		life = EnergyFunction.energyToLife(energy);
	}
	
	public void printInfo() throws IOException {

		TDTModel.writer.write("topic id: " + this.topicId+"\r\n");
		TDTModel.writer.write("topic life: " + this.life+"\r\n");
		TDTModel.writer.write("topic size: " + this.articles.size()+"\r\n");
		
		//print top 20 words
		int c=20;
		HashSet<String> set = new HashSet<String>();
		while(c-->0){
			double maxWeight = 0;
			String word = null;
			Iterator it = wordVector.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String,Double> entry = (Map.Entry<String,Double>)it.next();
				if(set.contains(entry.getKey()))
					continue;
				if(entry.getValue()>maxWeight){
					maxWeight = entry.getValue();
					word=entry.getKey();
				}
				
			}
			set.add(word);
			
		}
		for(String word:set)
			TDTModel.writer.write(word+" ");
		TDTModel.writer.write("\r\n");

		// print top 25 related articles
		Collections.sort(this.newArticles, new ArticleComparator());
		int i = 0;
		for (Article article : this.newArticles) {
			TDTModel.writer.write(article.getTitle()+"\r\n");
			if (++i > 25)
				break;
		}

	}



	public ArrayList<Article> getArticles() {
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
	



	

}

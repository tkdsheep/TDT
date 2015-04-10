package qiaohuang.tdt.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author qiaohuang
 *
 */
public class Topic {
	
	private ArrayList<Article> articles;
	
	
	/*
	 * wordVector is defined as the arithmetic average of term vectors of all articles in this topic
	 * String is term, Double is weight
	 */
	private HashMap<String,Double> wordVector; 
	
	private double life;//topic life, can be treated as "hotness"
	
	public Topic(Article article){
		
		//the param "article" is the first article detected for this new topic
		
		articles = new ArrayList<Article>();
		wordVector = new HashMap<String,Double>();
		life=0.1;
		
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
		
		//update life
		double energy = EnergyFunction.lifeToEnergy(life);
		energy += EnergyFunction.getEnergy(article, this);
		life = EnergyFunction.energyToLife(energy);
	}
	
	public void printInfo(Calendar calendar){
		
		System.out.println("topic life: "+this.life);
		System.out.println("topic size: "+this.articles.size());
		for(Article article:this.articles){
			if(article.getCalendar().DAY_OF_YEAR==calendar.DAY_OF_YEAR)
			System.out.println(article.getTitle());
		}
		
	}
	
	



	public ArrayList<Article> getArticles() {
		return articles;
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



	

}

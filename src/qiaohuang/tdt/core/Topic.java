package qiaohuang.tdt.core;

import java.util.ArrayList;
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
	
	private double life;//话题的“生命值”，也可以理解为话题的“热度”
	
	public Topic(){
		articles = new ArrayList<Article>();
		wordVector = new HashMap<String,Double>();
		life=0;
	}
	
	public void Update(Article article){
		
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
	}



	public ArrayList<Article> getArticles() {
		return articles;
	}


	public HashMap<String, Double> getWordVector() {
		return wordVector;
	}



	

}

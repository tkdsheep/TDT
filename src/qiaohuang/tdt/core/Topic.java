package qiaohuang.tdt.core;

import java.util.List;
import java.util.Map;

/**
 * @author qiaohuang
 *
 */
public class Topic {
	
	List<Article> articles;
	
	Map<String,Double> wordVector; 
	/*
	 * wordVector is defined as the arithmetic average of term vectors of all articles in this topic
	 * String is term, Double is weight
	 */
	
	int size;//number of articles in this topic
	
	
	
	public void Update(){
		
	}
	
	

}

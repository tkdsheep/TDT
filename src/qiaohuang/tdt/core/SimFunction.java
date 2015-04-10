package qiaohuang.tdt.core;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author qiaohuang
 *
 */

public class SimFunction {
	
	
	//cosine similarity between two articles
	public static double cosSim(Article a, Article b){
		
		double dist=0;
		
		Iterator<Entry<String, WordInfo>> it=a.getWords().entrySet().iterator(); 
		while(it.hasNext()){
			Map.Entry<String,WordInfo> entry = (Map.Entry<String,WordInfo>)it.next();
			String word = entry.getKey();
			if(!b.getWords().containsKey(word))
				continue;
			double aweight=entry.getValue().getWeight();
			double bweight=b.getWords().get(word).getWeight();
			dist+=aweight*bweight;
		}
		
		return dist;
	}
	
	
	//cosine similarity between article and topic
	public static double cosSim(Article a,Topic b){
		double dist = 0;
		
		Iterator<Entry<String, WordInfo>> it=a.getWords().entrySet().iterator(); 
		while(it.hasNext()){
			Map.Entry<String,WordInfo> entry = (Map.Entry<String,WordInfo>)it.next();
			String word = entry.getKey();
			if(!b.getWordVector().containsKey(word))
				continue;
			double aweight=entry.getValue().getWeight();
			double bweight=b.getWordVector().get(word);
			dist+=aweight*bweight;
		}
		
		return dist;
	}

}

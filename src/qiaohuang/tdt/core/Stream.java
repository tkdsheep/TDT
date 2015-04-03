package qiaohuang.tdt.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Stream {
	
	List<Article> articles;//the latest article stream
	
	HashMap<String,Integer> df;//document frequency for each word
	
	public void buildDocFreq(){
		for(Article article:articles){
			
			Iterator<Entry<String, WordInfo>> it=article.getWords().entrySet().iterator(); 
			while(it.hasNext()){
				Map.Entry<String,WordInfo> entry = (Map.Entry<String,WordInfo>)it.next();
				String word = entry.getKey();
				
				if(df.containsKey(word))
					df.put(word, df.get(word)+1);
				else df.put(word, 1);				
			}	
			
		}
		
		for(Article article:articles){
			Iterator<Entry<String, WordInfo>> it=article.getWords().entrySet().iterator(); 
			while(it.hasNext()){
				Map.Entry<String,WordInfo> entry = (Map.Entry<String,WordInfo>)it.next();
				WordInfo word = entry.getValue();
				
			}
			
		}
	}
	
	public void buildBurstiness(){
		
	}
	
	public void buildTfIdf(){
		
		
		
	}

}

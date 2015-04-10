package qiaohuang.tdt.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author qiaohuang
 *
 */
public class History {
	
	private static int size;//number of articles in the past
	private static HashMap<String,Integer> df;//document frequency for each word in the past
	
	public History(){
		size=0;
		df = new HashMap<String,Integer>();
	}
	
	public void updateHistory(Stream stream){
		size+=stream.getArticles().size();
		for(Article article:stream.getArticles()){
			
			Iterator<Entry<String, WordInfo>> it=article.getWords().entrySet().iterator(); 
			while(it.hasNext()){
				Map.Entry<String,WordInfo> entry = (Map.Entry<String,WordInfo>)it.next();
				String word = entry.getKey();
				
				if(df.containsKey(word))
					df.put(word, df.get(word)+1);
				else df.put(word, 1);				
			}	
		}		
	}


	public static HashMap<String, Integer> getDf() {
		return df;
	}

	public static int getSize() {
		return size;
	}

	
	
	

	
	
	

}

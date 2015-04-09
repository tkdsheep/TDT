package qiaohuang.tdt.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Stream {
	
	private ArrayList<Article> articles;//the latest article stream
	private HashMap<String,Integer> df;//document frequency for each word
	private HashMap<String,Double> burst;
	
	public Stream(){
		articles = new ArrayList<Article>();
		df = new HashMap<String,Integer>();
		burst = new HashMap<String,Double>();
		
	}
	
	public void build(){
		
		this.buildDocFreq();
		this.buildBurstiness();
		this.buildTfIdf();
		
	}
	
	private void buildDocFreq(){
		
		//统计当前新闻数据流中每个词语的df值（文档频率）
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
		
		/*
		 * 加上历史的df值，参考paper里的公式：df(w,i) = df(w,i-1) + df(w,si)
		 * df(w,i-1)是历史数据集里w的df值，df(w,si)是当前数据流中w的df值
		 */
		Iterator<Entry<String, Integer>> it=df.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String,Integer> entry = (Map.Entry<String,Integer>)it.next();
			String word = entry.getKey();
			Integer count = History.getDf().get(word);
			
			if(count==null)
				continue;//历史数据集没有出现过这个词
			
			entry.setValue(entry.getValue()+count);//更新df
		}
		
		
		
		for(Article article:articles){
			Iterator<Entry<String, WordInfo>> iter=article.getWords().entrySet().iterator(); 
			while(iter.hasNext()){
				Map.Entry<String,WordInfo> entry = (Map.Entry<String,WordInfo>)iter.next();
				WordInfo wordInfo = entry.getValue();
				wordInfo.setDf(df.get(entry.getKey()));
				
			}
			
		}
	}
	
	private void buildBurstiness(){
		
		
		/*
		 * 参照paper里的公式
		 * 		i	i'
		 * 	w	A	B
		 * 	w'	C	D
		 * 
		 * A is the count of stories that contain term w in time slot i;
		 * B is the count of stories that contain term w outside time slot i;
		 * C is the count of stories that don’t contain term w in time slot i;
		 * D is the count of stories that don’t contain term w outside time slot i.
		 * 
		 */	
		
		//统一计算数据流中所有词的burstiness
		double A, B, C, D;
		Iterator<Entry<String, Integer>> it = df.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) it.next();
			String word = entry.getKey();
			int count = entry.getValue();

			A = count;
			if (History.getDf().containsKey(word))
				B = History.getDf().get(word);
			else
				B = 0;
			C = this.articles.size() - A;
			D = History.getSize() - B;
			
			double burstiness = (A+B+C+D)*(A*D-B*C)*(A*D-B*C) / ((A+B)*(C+D)*(A+C)*(B+D));
			burst.put(word, burstiness);
			
		}
		
		//更新每篇文章的wordInfo
		for(Article article:articles){
			Iterator<Entry<String, WordInfo>> iter=article.getWords().entrySet().iterator(); 
			while(iter.hasNext()){
				Map.Entry<String,WordInfo> entry = (Map.Entry<String,WordInfo>)iter.next();
				WordInfo wordInfo = entry.getValue();
				wordInfo.setBurstiness(burst.get(entry.getKey()));
			}
		}
		
		
		
	}
	
	private void buildTfIdf(){
		
		for(Article article:articles){
			Iterator<Entry<String, WordInfo>> iter=article.getWords().entrySet().iterator(); 
			while(iter.hasNext()){
				Map.Entry<String,WordInfo> entry = (Map.Entry<String,WordInfo>)iter.next();
				WordInfo wordInfo = entry.getValue();
				
				
				int N = this.articles.size()+History.getSize();
				
				double weight = wordInfo.getTf()* Math.log((N+1)/(wordInfo.getDf()+0.5))*wordInfo.getBurstiness();
				
			}
		}
		
		
	}

	public ArrayList<Article> getArticles() {
		return articles;
	}

	public HashMap<String, Integer> getDf() {
		return df;
	}

	
	
	
	

}

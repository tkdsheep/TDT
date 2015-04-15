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
public class Stream {
	
	private ArrayList<Article> articles;//the latest article stream
	private HashMap<String,Integer> df;//document frequency for each word
	private HashMap<String,Double> burst;//burstiness for each word
	
	public Stream(){
		articles = new ArrayList<Article>();
		df = new HashMap<String,Integer>();
		burst = new HashMap<String,Double>();
		
	}
	
	public void build(){
		
		/*
		 * ArticleReader only "read" basic information of an article (title, content and so on)
		 * After reading, remember to call this function to calculate values of parameters for each word
		 */
		
		this.buildDocFreq();
		this.buildBurstiness();
		this.buildTfIdf();
		
	}
	
	private void buildDocFreq(){
		
		//calculate document frequency for each word in this stream
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
		 * Refer to the formula in the paper (CIKM'08)
		 * 
		 * df(w,i) = df(w,i-1) + df(w,si)
		 * 
		 * df(w,i-1)is the df value for each word w in history data
		 * df(w,si) is the df value for each word w in this new stream
		 * df(w,i) is the final df value for each word w
		 * 
		 */
		Iterator<Entry<String, Integer>> it=df.entrySet().iterator();
		while(it.hasNext()){//for each word in this stream
			Map.Entry<String,Integer> entry = (Map.Entry<String,Integer>)it.next();
			String word = entry.getKey();
			Integer count = History.getDf().get(word);
			
			if(count==null)
				continue;//this word didn't appear in history
			
			entry.setValue(entry.getValue()+count);//update df of this word
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
		 * Refer to the formula in the paper (CIKM'08)
		 * 
		 * 		i	i'
		 * 	w	A	B
		 * 	w'	C	D
		 * 
		 * A is the count of stories that contain term w in time slot i;
		 * B is the count of stories that contain term w outside time slot i;
		 * C is the count of stories that don't contain term w in time slot i;
		 * D is the count of stories that don't contain term w outside time slot i.
		 * 
		 */	
		
		//calculate burstiness for each word in this stream
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
			
			//debug
			//if(word.equals("ÂüµÂÀ­"))
				//System.out.println(A+" "+B+" "+C+" "+D+" "+burstiness);
			
		}
		
		//update wordInfo of each word in each article
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
			
			double total=0;//prepared for normalization
			
			Iterator<Entry<String, WordInfo>> iter=article.getWords().entrySet().iterator(); 
			while(iter.hasNext()){
				Map.Entry<String, WordInfo> entry = (Map.Entry<String, WordInfo>) iter.next();
				WordInfo wordInfo = entry.getValue();

				int N = this.articles.size() + History.getSize();

				double weight = wordInfo.getTf()
						* Math.log((N + 1) / (wordInfo.getDf() + 0.5))
						* wordInfo.getBurstiness();
				
				wordInfo.setWeight(weight);
				total+=weight*weight;
			}
			
			total = Math.sqrt(total);
			
			//normalization
			iter=article.getWords().entrySet().iterator(); 
			while(iter.hasNext()){
				Map.Entry<String, WordInfo> entry = (Map.Entry<String, WordInfo>) iter.next();
				WordInfo wordInfo = entry.getValue();
				wordInfo.setWeight(wordInfo.getWeight()/total);		
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

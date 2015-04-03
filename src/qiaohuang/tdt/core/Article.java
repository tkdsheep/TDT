package qiaohuang.tdt.core;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import qiaohuang.tdt.util.WordFilter;





/**
 * @author qiaohuang
 * 
 *
 */

public class Article {
	
	private String title, content, url, source;// source may be null
	private Calendar calendar;
	private HashMap<String,WordInfo> words;
	
	

	public Article(){
		
	}
	
	public void segmentTerms(){
		
		List<Term> parse = NlpAnalysis.parse(this.content);
		for(Term token:parse){
			
			String name = token.getName();			
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(name);
            name = m.replaceAll("");
      
            //Filter stop words, noisy words, punctuation and so on...
            if(WordFilter.filterWord(name)) 
            	continue;
            
            if(words.containsKey(name)){
            	WordInfo word = words.get(name);
            	word.setTf(word.getTf()+1);
            }
            else{          
            	WordInfo word = new WordInfo(name);
            	word.setDf(1);
            	words.put(name,word);
            }
		}
		
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	
	public HashMap<String, WordInfo> getWords() {
		return words;
	}


	
	

}

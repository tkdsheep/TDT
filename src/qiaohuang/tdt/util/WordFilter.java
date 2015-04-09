package qiaohuang.tdt.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.domain.Term;

import qiaohuang.tdt.conf.PathConfig;



public class WordFilter {
	
	
	HashSet<String> stopWords;//停用词表
	HashSet<String> noiseWords;//噪音词表
	
	public WordFilter(){
		stopWords = new HashSet<String>();
		noiseWords = new HashSet<String>();
		
		//读取停用词表
		try{
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(PathConfig.StopWordsDicPath),"UTF-8"));  
			String line = null;  
			while ((line = br.readLine()) != null) {  
				line = line.trim();
				stopWords.add(line);  
				//System.out.println(line);
			}
			br.close();
		}
		catch (Exception e){
			
		}
		
		//System.out.println("停用词个数： "+stopWords.size());
		
		
		
		//读取噪音词表
		try{
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(PathConfig.NoiseWordsDicPath),"UTF-8"));  
			String line = null;  
			while ((line = br.readLine()) != null) {  
				line = line.trim();
				noiseWords.add(line);  
				//System.out.println(line);
			}
			br.close();
		}
		catch (Exception e){
			
		}
		
		//add new noise words according to certain dataset
		//额外过滤“某年”、“某月”、“某日”这种形式的噪音词
				
		for(int i=1500;i<=2015;i++)
			noiseWords.add(new Integer(i).toString()+"年");
		for(int i=1;i<=12;i++)
			noiseWords.add(new Integer(i).toString()+"月");
		for(int i=1;i<=31;i++)
			noiseWords.add(new Integer(i).toString()+"日");
		for(int i=0;i<26;i++){
			noiseWords.add(""+(char)('a'+i));
			noiseWords.add(""+(char)('A'+i));
		}
			
		
		//System.out.println("噪音词个数："+noiseWords.size());
		
	
	}
	
	private boolean isStopWord(String word){
		if(stopWords.contains(word))
			return true;
		return false;		
	}
	
	private boolean isNoiseWord(String word){
		if(noiseWords.contains(word))
			return true;
		return false;	
	}	
	
	
	public void addStopWord(String word){
		//允许手动添加停用词 (新发现的噪音词也手动添加到这里）
		stopWords.add(word);
	}
	
	
	
	public String filterWord(Term token){
		
		if(token.getNatureStr().charAt(0)=='w')//this is punctuation
			return null;
		
		String word = token.getName();			
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(word);
        word = m.replaceAll("");
		
		if(isStopWord(word))
			return null;
		if(isNoiseWord(word))
			return null;
		if(word.length()<2)//过滤长度为0或1的词
			return null;		
		
		return word;
	}

}

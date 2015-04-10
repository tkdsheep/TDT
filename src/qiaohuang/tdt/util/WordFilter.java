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
	
	
	HashSet<String> stopWords;//鍋滅敤璇嶈〃
	HashSet<String> noiseWords;//鍣煶璇嶈〃
	
	public WordFilter(){
		stopWords = new HashSet<String>();
		noiseWords = new HashSet<String>();
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
		
		System.out.println("停用词个数： "+stopWords.size());
		
		//add new noise words according to certain dataset
		
		
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
		
		
		
		System.out.println("噪音词个数："+noiseWords.size());
		
	
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
	
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	
	
	public void addStopWord(String word){
		stopWords.add(word);
	}
	
	
	
	public String filterWord(Term token){
		
		if(token.getNatureStr().charAt(0)=='w')//this is punctuation
			return null;
		
		String word = token.getName();			
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(word);
        word = m.replaceAll("");
		
        if(word.length()<2)
			return null;
        if(isNumeric(word))
			return null;
		if(isStopWord(word))
			return null;
		if(isNoiseWord(word))
			return null;
		
		
		
		
		return word;
	}

}

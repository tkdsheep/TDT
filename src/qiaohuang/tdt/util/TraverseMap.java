package qiaohuang.tdt.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import qiaohuang.tdt.core.WordInfo;

/**
 * @author qiaohuang
 *
 */
public class TraverseMap {
	
	
	
	public static ArrayList<Entry<String,Double>> traverseDouble(HashMap<String,Double> map){
		
		ArrayList<Entry<String,Double>> list = new ArrayList<Entry<String,Double>>();
		
		
		Iterator<Entry<String,Double>> it=map.entrySet().iterator(); 
		while(it.hasNext()){
			Entry<String,Double> entry = it.next();
			list.add(entry);
		}	
		return list;	
	}
	
	public static ArrayList<Entry<String,WordInfo>> traverseWordInfo(HashMap<String,WordInfo> map){
		
		ArrayList<Entry<String,WordInfo>> list = new ArrayList<Entry<String,WordInfo>>();
		
		
		Iterator<Entry<String,WordInfo>> it=map.entrySet().iterator(); 
		while(it.hasNext()){
			Entry<String,WordInfo> entry = it.next();
			list.add(entry);
		}	
		return list;	
	}
	
	
	
	

}

package qiaohuang.tdt.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ToString {
	
	public static String toString(Calendar calendar){
	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(calendar.getTime());
		
	}

}

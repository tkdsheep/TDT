package qiaohuang.tdt.util;

import java.util.Comparator;

import qiaohuang.tdt.core.Topic;

public class TopicComparator implements Comparator<Topic>{
	
	@Override
	public int compare(Topic t1, Topic t2) {
		
		if(t2.getLife()==t1.getLife())
			return 0;
		return t2.getLife()>t1.getLife()?1:-1;

	}

}

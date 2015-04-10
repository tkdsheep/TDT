package qiaohuang.tdt.util;

import java.util.Comparator;

import qiaohuang.tdt.core.Topic;

public class TopicComparator implements Comparator<Topic>{
	
	@Override
	public int compare(Topic t1, Topic t2) {
		
		if(t2.getLife()/t2.getArticles().size()==t1.getLife()/t1.getArticles().size())
			return 0;
		return t2.getLife()/t2.getArticles().size()>t1.getLife()/t1.getArticles().size()?1:-1;

	}

}

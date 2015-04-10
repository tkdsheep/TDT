package qiaohuang.tdt.util;

import java.util.Comparator;

import qiaohuang.tdt.core.Article;


public class ArticleComparator implements Comparator<Article>{
	
	@Override
	public int compare(Article t1, Article t2) {
		
		if(t2.getSim()==t1.getSim())
			return 0;
		return t2.getSim()>t1.getSim()?1:-1;

	}
}

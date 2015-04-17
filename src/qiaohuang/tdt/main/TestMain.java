package qiaohuang.tdt.main;

import qiaohuang.tdt.util.DBConnection;

/**
 * @author qiaohuang
 *
 */
public class TestMain {
	//just for test
	
	public static void main(String Args[]){
		//DBConnection.getResult();
		
		double A,B,C,D;
		
		A=20;
		B=100;
		C=80;
		D=900;
		
		double b = (A+B+C+D)*(A*D-B*C)*(A*D-B*C) / ((A+B)*(C+D)*(A+C)*(B+D));
		System.out.println(b);
	}
}

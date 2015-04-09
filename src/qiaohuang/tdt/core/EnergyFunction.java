package qiaohuang.tdt.core;

import qiaohuang.tdt.conf.GlobalParam;


/**
 * @author qiaohuang
 *
 */
public class EnergyFunction {

	
	public static double getEnergy(Article article,Topic topic){
		
		return SimFunction.cosSim(article, topic);
	}
	
	public static double energyToLife(double energy){
		
		return energy/(energy+1);
	}
	
	public static double lifeToEnergy(double life){
		
		return life/(1-life);
	}
	
	public static double lifeDecay(double life){
		
		return life - GlobalParam.lifeDecay;
		
	}
	
}

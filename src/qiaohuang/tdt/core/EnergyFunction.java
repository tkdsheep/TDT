package qiaohuang.tdt.core;


/**
 * @author qiaohuang
 *
 */
public class EnergyFunction {

	
	public double getEnergy(Article article,Topic topic){
		
		double energy = 0;
		
		//todo energy = sim(article,topic)
		
		return energy;
	}
	
	public double energyToLife(double energy){
		double life=0;
		
		//todo: life = f(energy)
		
		return life;
	}
	
	public double lifeToEnergy(double life){
		double energy=0;
		
		//todo: energy = g(life)
		
		return energy;
	}
	
	public double lifeDecay(double life){
		
		//todo: life = life - beta
		
		return life;
		
	}
	
}

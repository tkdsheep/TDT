package qiaohuang.tdt.core;

/**
 * @author qiaohuang
 *
 */
public class WordInfo {
	private String name;
	private int tf;//term frequency (in a single article)
	private int df;//document frequency (number of articles that contain this term)
	private double burstiness,weight;
	
	public WordInfo(String name) {
		this.name = name;
		this.tf = this.df = 0;
		this.burstiness = this.weight = 0;
	}
	
	public String getName() {
		return name;
	}
	public int getTf() {
		return tf;
	}
	public void setTf(int tf) {
		this.tf = tf;
	}
	public int getDf() {
		return df;
	}
	public void setDf(int df) {
		this.df = df;
	}
	public double getBurstiness() {
		return burstiness;
	}
	public void setBurstiness(double burstiness) {
		this.burstiness = burstiness;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
}

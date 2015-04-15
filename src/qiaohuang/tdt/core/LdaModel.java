package qiaohuang.tdt.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import qiaohuang.tdt.conf.GlobalParam;

public class LdaModel {
	
	int [][] doc;//word index array
	int V, K, M;//vocabulary size, topic number, document number
	int [][] z;//topic label array
	double alpha; //doc-topic dirichlet prior parameter 
	double beta; //topic-word dirichlet prior parameter
	int [][] nmk;//given document m, count times of topic k. M*K
	int [][] nkt;//given topic k, count times of term t. K*V
	int [] nmkSum;//Sum for each row in nmk
	int [] nktSum;//Sum for each row in nkt
	double [][] phi;//Parameters for topic-word distribution K*V
	double [][] theta;//Parameters for doc-topic distribution M*K
	int iterations;//Times of iterations
	Map<String, Integer> wordToIndexMap;//Map each different word to an identical number
	ArrayList<String> indexToWordMap;//reverse map of wordToIndexMap
	Calendar calendar;
	
	public LdaModel(int K,int iterations) {
		
		this.K = K;
		this.iterations = iterations;
		this.beta = 0.1;
		this.alpha = 50.0/K;
		
	}
	
	public ArrayList<Topic> run(Stream stream){
		
		initializeModel(stream);
		inferenceModel(stream);
		return saveTopic(stream);
		
	}

	private void initializeModel(Stream stream) {
		
		wordToIndexMap = new HashMap<String, Integer>();
		indexToWordMap = new ArrayList<String>();
		calendar = stream.getArticles().get(0).getCalendar();
		
		//firstly, build wordToIndexMap and indexToWordMap
		for (Article article : stream.getArticles()) {
			
			for(String word:article.getWordList()){
				if (!wordToIndexMap.containsKey(word)) {
					int newIndex = wordToIndexMap.size();
					wordToIndexMap.put(word, newIndex);
					indexToWordMap.add(word);
				}
			}
			
		}
		
		
		M = stream.getArticles().size();
		V = wordToIndexMap.size();
		nmk = new int [M][K];
		nkt = new int[K][V];
		nmkSum = new int[M];
		nktSum = new int[K];
		phi = new double[K][V];
		theta = new double[M][K];
		
		
		//initialize documents index array
		doc = new int[M][];
		for(int m = 0; m < M; m++){
			//Notice the limit of memory
			int N = stream.getArticles().get(m).getWordList().size();
			doc[m] = new int[N];
			for(int n = 0; n < N; n++){
				String word = stream.getArticles().get(m).getWordList().get(n);
				doc[m][n] = wordToIndexMap.get(word);
			}
		}
		
		//initialize topic lable z for each word
		z = new int[M][];
		for(int m = 0; m < M; m++){
			int N = stream.getArticles().get(m).getWordList().size();
			z[m] = new int[N];
			for(int n = 0; n < N; n++){
				int initTopic = (int)(Math.random() * K);// From 0 to K - 1
				z[m][n] = initTopic;
				//number of words in doc m assigned to topic initTopic add 1
				nmk[m][initTopic]++;
				//number of terms doc[m][n] assigned to topic initTopic add 1
				nkt[initTopic][doc[m][n]]++;
				// total number of words assigned to topic initTopic add 1
				nktSum[initTopic]++;
			}
			 // total number of words in document m is N
			nmkSum[m] = N;
		}
	}

	public void inferenceModel(Stream stream){
		
		
		for(int i = 0; i < iterations; i++){
			
			//output iteration times
			//System.out.println("Iteration " + i);
			
			//Use Gibbs Sampling to update z[][]
			for(int m = 0; m < M; m++){
				int N = stream.getArticles().get(m).getWordList().size();
				for(int n = 0; n < N; n++){
					// Sample from p(z_i|z_-i, w)
					int newTopic = sampleTopicZ(m, n);
					z[m][n] = newTopic;
				}
			}
		}
		
		//inference ended, save phi and theta
		updateEstimatedParameters();
		
	}
	
	private void updateEstimatedParameters() {
		
		for(int k = 0; k < K; k++){
			for(int t = 0; t < V; t++){
				phi[k][t] = (nkt[k][t] + beta) / (nktSum[k] + V * beta);
			}
		}
		
		for(int m = 0; m < M; m++){
			for(int k = 0; k < K; k++){
				theta[m][k] = (nmk[m][k] + alpha) / (nmkSum[m] + K * alpha);
			}
		}
	}

	private int sampleTopicZ(int m, int n) {
		
		// Sample from p(z_i|z_-i, w) using Gibbs upde rule
		
		//Remove topic label for w_{m,n}
		int oldTopic = z[m][n];
		nmk[m][oldTopic]--;
		nkt[oldTopic][doc[m][n]]--;
		nmkSum[m]--;
		nktSum[oldTopic]--;
		
		//Compute p(z_i = k|z_-i, w)
		double [] p = new double[K];
		for(int k = 0; k < K; k++){
			p[k] = (nkt[k][doc[m][n]] + beta) / (nktSum[k] + V * beta) * (nmk[m][k] + alpha) / (nmkSum[m] + K * alpha);
		}
		
		//Sample a new topic label for w_{m, n} like roulette
		//Compute cumulated probability for p
		for(int k = 1; k < K; k++){
			p[k] += p[k - 1];
		}
		double u = Math.random() * p[K - 1]; //p[] is unnormalised
		int newTopic;
		for(newTopic = 0; newTopic < K; newTopic++){
			if(u < p[newTopic]){
				break;
			}
		}
		
		//Add new topic label for w_{m, n}
		nmk[m][newTopic]++;
		nkt[newTopic][doc[m][n]]++;
		nmkSum[m]++;
		nktSum[newTopic]++;
		return newTopic;
	}
	
	private ArrayList<Topic> saveTopic(Stream stream){
		
		ArrayList<Topic> topics = new ArrayList<Topic>();
		
		int relatedTopic[] = new int[M];//if relatedTopic[i] == j , then article i belongs to topic j
		Arrays.fill(relatedTopic, -1);
		
		for(int i=0;i<M;i++)//for each article
			for(int j=0;j<K;j++){//for each topic
				if(theta[i][j]<GlobalParam.ldaSimThreshold)
					continue;
				if(relatedTopic[i]==-1) 
					relatedTopic[i]=j;
				else if(theta[i][j]>theta[i][relatedTopic[i]])
					relatedTopic[i]=j;
			}
		
		for(int i=0;i<K;i++){//for each topic, save topic info			
			
			Integer tmp = calendar.get(Calendar.YEAR) * 10000
					+ (calendar.get(Calendar.MONTH) + 1) * 100
					+ calendar.get(Calendar.DAY_OF_MONTH);
			String id = tmp.toString()+new Integer(i).toString();
			//System.out.println("Topic id: "+id);
			
			Topic topic = new Topic(id);
			
			for(int j=0;j<M;j++){//for each article
				if(relatedTopic[j]==i){//this article belongs to this topic
					//add this article to topic
					topic.insertArticle(stream.getArticles().get(j));
					//System.out.println(stream.getArticles().get(j).getTitle());
				}			
			}
			//System.out.println("---------------------");
			
			
			
			if(!topic.getArticles().isEmpty()){
				
				topic.updateWordVector();
				
				//TODO the paper use alpha param
				double energy = 0;
				for(Article article:topic.getArticles()){
					energy+=GlobalParam.tdtAlpha*SimFunction.cosSim(article, topic);
				}
				topic.setLife(EnergyFunction.energyToLife(energy));
				
				topics.add(topic);
			}
		}
		
		return topics;
		
	}
	
	
}

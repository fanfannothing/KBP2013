package thu.nlp.kbp.rank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class TrainLinearNeuralModel {


	//assign each object in the list a score according to current weight vector w
	//not for user to use it 	
	public static double[] score(double[] w, List<double[]> x){
		double[] f = new double[x.size()];
		for(int i = 0; i < x.size(); i++){
			f[i] = 0;
			for(int j = 0; j < w.length; j++){
				f[i] += w[j]*x.get(i)[j];
			}
		}
		return f;
	}
	
	//calculate delta W according the current differential of the Loss fuction
	//not for user to use it, but if you want to use your own score of training data, you should rewrite the function
	//the score list here we assumed is (10,0,0,...,0)
	public static int counter = 100;
	static double[] deltaW(double[] f, List<double[]> x){
		//the bonus means the score of the top 1 is 10
//		for (int i = 0; i < f.length; i++) {
//			System.out.print(f[i] + "\t");
//		}
//		System.out.println("");
//		System.exit(0);
		int bonus = 10;
		double[] deltaW = new double[x.get(0).length];
		double normZ = 0;double normP = 0; 
		for(int k = 0; k < deltaW.length; k++){
			deltaW[k] = 0;
		}
		for(int j = 0; j < x.size(); j++){
			normZ += Math.exp(f[j]);
		}
		normP = Math.exp(bonus)+x.size()-1;
		for(int j = 0; j < x.size(); j++){
			double pj = 1.0/normP;
			if(j == 0){
				pj = pj * Math.exp(bonus);
			}
			for(int k = 0; k < deltaW.length; k++){
				deltaW[k] = deltaW[k] -x.get(j)[k]*pj + 1.0/normZ*Math.exp(f[j])*x.get(j)[k];
			}
			
		}
		
//		double sum = 0.0;
//		for (int i = 0; i < f.length; i++) {
//			sum += f[i] * f[i];
//		}
//		System.out.println(Math.sqrt(sum));
		
//		System.out.println(deltaW[0]);
//		counter--;
//		if (counter == 0) System.exit(0);
		return deltaW;
	}
	
	//trainData, the data used for training, each list in trainData represents a query, and the first element in a query's list should be top1
	//ita, the learning rate in each iteration
	//T, the maximun training time
	//The function mean to train a data one time according to special ita.
	//if used in other application, the function should be rewritten
	public static double[] train(List<List<double[]>> trainData,double ita,int T){
		//initialize the weight w
		double w[] = new double[trainData.get(0).get(0).length];
		for(int i = 0; i < w.length; i++){
			w[i] = 1;
		}
		
		for(int t = 0; t < T; t++){
			for(int i = 0; i < trainData.size(); i++){
				//calculate the score list z(i) with current w
				double f[] = score(w,trainData.get(i));
				//calculate the delta W
				double deltaW[] = deltaW(f,trainData.get(i));
				//adjust w
				for(int k = 0; k < w.length; k++){
					w[k] = w[k] - deltaW[k]*ita;
				}
			}
//			System.out.println(Arrays.toString(w));
		}
		return w;
	}
	
	//trainData, the data used for training, each list in trainData represents a query, and the first element in a query's list should be top1
	//if used in other application, the function should be rewritten
	//you are supposed to use this function to train with a data in memory
	public static double[] train(List<List<double[]>> trainData){
		double bestW[] = null;
		double bestIta = 0;
		double bestAcc = 0;
		//the starting learning rate is ita = 0.1, the max iteration num should be T = 100
		double ita = 0.1;
		int T = 100;
		
		while(ita <= 1){
			//train w with a learning rate ita
			double[] w = train(trainData,ita,T);
//			System.out.println(Arrays.toString(w));
			//evaluate the current weight w with top 1 accuracy on given trainData, you can rewrite this part if you need to.
			int correct = 0;
			for(int i = 0; i < trainData.size(); i++){
//				System.out.println(PredictLinearNeuralModel.predict(w, trainData.get(i)));
				if(PredictLinearNeuralModel.predict(w, trainData.get(i))==0)
					correct ++;
			}
			double acc = 1.0 * correct / trainData.size();
			if(acc > bestAcc){
				bestAcc = acc;
				bestIta = ita;
				bestW = w;
			}
			System.out.println("ListNet: " + ita + "\t" + acc);
			ita = ita + 0.1;
		}
		return bestW;
	}
	
	//file, a file with special format
	//return the training data
	public static List<List<double[]>> read(String file){
		List<List<double[]>> trainData = new ArrayList<List<double[]>>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null){
				int k = Integer.parseInt(line.trim());
				List<double[]> x = new ArrayList<double[]>();
				for(int i = 0; i < k; i++){
					/*
					 * This a few lines are added to ignore the content after '#'
					 * by wangyan
					 */
					String temp = reader.readLine();
					if (temp.contains("#")) {
						temp = (temp.split("#"))[0];
					}
					String[] feature = temp.trim().split(",");
					
//					String[] feature = reader.readLine().trim().split(",");
					double[] features = new double[feature.length-1];
					for(int j = 0; j < features.length; j++){
						features[j] = Double.parseDouble(feature[j+1]);
					}
					if(feature[0].startsWith("T")){
						//System.out.println(feature[0]+"\t"+features[features.length-1]);
						x.add(0, features);
					}else{
						x.add(features);
					}				
				}
				PredictLinearNeuralModel.NormalizeData(x);
				trainData.add(x);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return trainData;
	}
	
	//train with the trainData in the file with special format	
	public static double[] train(String file){
		List<List<double[]>> trainData = read(file);
		double[] w = train(trainData);
		return w;
	}
	
	// this is a test function, you can learn how to use this to predict answers
	public static HashMap<Integer, Integer> test(String file, double[] w){
		List<List<double[]>> trainData = read(file);
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		int correct = 0;
		for(int i = 0; i < trainData.size(); i++){
			int predict = PredictLinearNeuralModel.predict(w, trainData.get(i));
			if(predict == 0)
				correct ++;
			else{
//				System.out.println();
				for(int k = 0; k < w.length; k++){
//					System.out.print(trainData.get(i).get(predict)[k]+"\t");
				}
//				System.out.println();
				for(int k = 0; k < w.length; k++){
//					System.out.print(trainData.get(i).get(0)[k]+"\t");
				}
//				System.out.println();
//				System.out.println("The result of query " + i + " is " + predict + " rather than 0");
				result.put(new Integer(i), new Integer(predict));
			}
		}
		double acc = 1.0 * correct / trainData.size();
		System.out.println(acc);
		for(int i = 0; i < w.length; i++){
//			System.out.print(w[i]+"\t");
		}		
		return result;
	}
}

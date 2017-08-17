package com.lwt.tools;

import com.lwt.util.ToPrint;

public class LSMethod {
	double[][] data;
	int max_j;
	
	double[][] power;
	public double[] b;
	
	public double[][] matrix;
	
	public static void main(String[] args) {
		double[][] data = {{1.1, 2.3}, {2.0, 3.1}, {2.2, 1.2}, {3.1, 3.3}};
		LSMethod test = new LSMethod(data);
		test.com(3);
		System.out.println(ToPrint.array2str(test.data));
		System.out.println(ToPrint.array2str(test.power));
		System.out.println(ToPrint.array2str(test.b));
		System.out.println(ToPrint.array2str(test.matrix));
	}
	public LSMethod(double[][] data){
		this.max_j = data.length;
		for(int i=0; i<max_j; i++){//只能计算二维的样本
			if(2 != data[i].length){
				throw new IllegalArgumentException("样本点只能为2维！");
			}
		}
		this.data = data;
	}
	
	public void com(int w){
		if(w > max_j){
			throw new IllegalArgumentException("样本量过少，模型复杂度过大，方程欠定！");
		}
		
		power = new double[max_j+1][2*w-1];
		for(int i=0; i<max_j; i++){
			for(int j=0; j<=2*w-2; j++){
				power[i][j] = Math.pow(data[i][0], j);
				power[max_j][j] += power[i][j];
			}
		}
		
		b = new double[w];
		for(int i=0; i<w; i++){
			for(int ii=0; ii<max_j; ii++){
				b[i] += power[ii][i] * data[ii][1];
			}
		}
		
		matrix = new double[w][w];
		for(int i=0; i<w; i++){
			for(int j=0; j<w; j++){
				matrix[i][j] = power[max_j][i+j];
			}
		}
	}
}

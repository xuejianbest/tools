package com.lwt.tools;

import com.lwt.util.Debug;
import com.lwt.util.ToPrint;
import com.sun.xml.txw2.IllegalAnnotationException;

/**
 * @author liu
 * 此类采用二维数组的形式描述行列式，
 */
public class Determinant {
	public final double[][] determinant;
	public double[][] det;
	public int n;
	
	public Determinant(double[][] determinant){
		this.n = determinant.length;
		for(int i=0; i<n; i++){
			if(n != determinant[i].length){
				throw new IllegalAnnotationException("行列式的第" + i+1 + "行元素个数错误！");
			}
		}
		this.determinant = determinant;
	}
	
	public double det(){
		getDet();
		double res = 1;
		for(int i=0; i<n; i++){
			res *= det[i][i];
		}
		return res;
	}

	public double[][] getDet(){
		double[][] det_ = new double[n][n];
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				det_[i][j] = determinant[i][j];
			}
		}
		setDet(det_, det_.length);
		det = det_;
		return det;
	}
	//在原行列式基础上做变换
private void setDet(double[][] det_, int k) {
	if(k == 1){
		return;
	}else{
		//如果行列式右下角元素为0，则通过减去一列保证其不为0
		if(det_[k-1][k-1] == 0){
			for(int j=0; j<k-1; j++){
				if(det_[k-1][j] != 0){
					for(int i=0; i<k; i++){
						det_[i][k-1] -= det_[i][j];
					}
					break;
				}
			}
		}
		
		double last = det_[k-1][k-1];
		for(int j=0; j<k-1; j++){
			double div = -det_[k-1][j] / last;
			for(int i=0; i<k; i++){
				det_[i][j] += div * det_[i][k-1];
			}
		}
		setDet(det_, k-1);
	}
}

	public static void main(String[] args) {
		double[][] matrix = {{3,5,1},{1,2,3},{4,2,0}};
		Determinant det = new Determinant(matrix);
		String t1=ToPrint.array2str(det.determinant);
		System.out.println(t1);
		det.getDet();
		String t2=ToPrint.array2str(det.det);
		System.out.println(t2);
		Debug.printi(det.det());
	}
}

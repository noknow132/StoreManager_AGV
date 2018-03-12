/**  
 * Project Name:RegeditCode  
 * File Name:RowCloums.java  
 * Package Name:com.code  
 * Date:2018年2月9日上午9:21:32  
 * Copyright (c) 2018, chenzhou1025@126.com All Rights Reserved.  
 *  
*/  
  
package com.entity;

import java.util.ArrayList;
import java.util.List;

public class RowCloums {

	public static void main(String[] args) {
		String id = "";
		int i=3;
		int j=5;
		int start = 0;
		
		String a[][] = new String[i][j];
//		if(start==1){
//			i=i+1;
//			j=j+1;
//			a = new String[i+start][j+start];
//		}
		System.out.println("-------------------");
		for (int k = i-1; k >= start; k--) {
			for (int k2 = start; k2 < j; k2++) {
				
				a[k][k2] = (k<10?("0"+k):k) + "" + (k2<10?("0"+k2):k2);
				System.out.print("A1"+a[k][k2]+"\t");
			}
			System.out.println();
		}
		
		System.out.println("-------------------");
		
//		String b[][] = new String[j][i];
//		for (int k = j-1; k >= start; k--) {
//			for (int k2 = start; k2 < i; k2++) {
//				b[k][k2] = k + "-" + k2;
//				System.out.print(b[k][k2]+"\t");
//			}
//			System.out.println();
//		}
		String b[][] = new String[a[0].length][a.length];
		System.out.println("--------方案1：直接全部对调-----------");
		for (int k = a[0].length-1; k >= start; k--) {
			for (int k2 = start; k2 < a.length; k2++) {
				//1.
				b[k][k2] = a[k2][k];
				//2
				//3
				
				System.out.print("A1"+b[k][k2]+"\t");
			}
			System.out.println();
		}
		
		System.out.println("-------------------");
		System.out.println("--------方案2：逐一对调-----------");
		
		String p = "A10305";
		
        StringBuilder temp = new StringBuilder();  
        for (int k = 0; k < p.length(); k++) {  
            if (k % 2 == 0 && k > 0) {  
                temp.append("-");  
            }  
            temp.append(p.charAt(k));  
        }  
        String[] str = temp.toString().split("-");  
		for (int k = 0; k < str.length; k++) {
			System.out.println(str[k]);
		}
		
		String c = "abc";
		char[] ch = c.toCharArray();
		System.out.println(ch[0]);
	}
}
  

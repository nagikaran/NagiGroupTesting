package com.NagiGroup.utility;

public class SumCalculator {
	 public static void main(String[] args) {
	        double[] values = {
	        		300.00,
	        		250.00,
	        		700.00,
	        		600.00,
	        		600.00,
	        		600.00,
	        		600.00
};

	        double sum = calculateSum(values);
	        System.out.printf("Total Sum = %.2f\n", sum);
	    }

	    public static double calculateSum(double[] numbers) {
	        double sum = 0;
	        for (double num : numbers) {
	            sum += num;
	        }
	        return sum;
	    }
}

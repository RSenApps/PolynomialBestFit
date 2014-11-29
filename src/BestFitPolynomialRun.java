import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import Jama.Matrix;

/**
 * This file will start the frame and get input. It does not perform any actual calculations
 * @author Ryan
 *
 */

public class BestFitPolynomialRun {
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String file = getFile(input); //gets the file text based on user inpug
		double[][] points = getPointsFromFile(file); //processes the file to retrieve the points
		int polynomialOrder = getPolynomialOrder(input, points.length-1); //gets the polynomial order from the input
		input.close();
		//run the frame
		BestFitPolynomial demo = new BestFitPolynomial(points, polynomialOrder);
        demo.pack();
        demo.setVisible(true);
	}
	private static double[][] getPointsFromFile(String file) {
		String[] lines = file.split("\n");
		double[][] points = new double[lines.length][2];
		int lineNumber = 0;
		for (String line : lines)
		{
			String[] data = line.split("\t");
			points[lineNumber][0] = Double.valueOf(data[0]);
			points[lineNumber][1] = Double.valueOf(data[1]);
			lineNumber++;
		}
		return points;
	}
	private static String getFile(Scanner input) {
		System.out.print("Please enter file path: ");
		while (true) { // keep asking for file path until it has succeeded
			String path = input.next();
			try {
				byte[] encoded = Files.readAllBytes(Paths.get(path));
				return new String(encoded);
			} catch (Exception e) {
				System.out.print("No file found, please try again: ");
			}
		}
	}
	private static int getPolynomialOrder(Scanner input, int max) {
		System.out.print("Please enter polynomial fit order: ");
		while (true) { // keep asking for file path until it has succeeded
			
			String orderString = input.next();
			try {
				
				int order = Integer.valueOf(orderString);
				if (order <= max)
				{
					return order;
				}
				else {
					System.out.print("Sorry, you don't have enough points to calculate a polynomial of this order.\nPlease try a lower order: ");
				}
				 
			} catch (Exception e) {
				System.out.print("Please enter a valid number: ");
			}
		}
	}
}

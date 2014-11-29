import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.*;
import javax.swing.JFrame;
import javax.swing.text.AbstractDocument.BranchElement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.data.function.PolynomialFunction2D;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import Jama.Matrix;

/**
 * This file will actually do the calculations
 * @author Ryan
 *
 */
public class BestFitPolynomial extends JFrame {
	static JFreeChart chart;
	private final static double POLY_STEP = .2; //delta x to graph polynomial
	 
	public BestFitPolynomial(double[][] points, int polynomialOrder) {
		Matrix matrix1 = createMatrix1(points, polynomialOrder); //creates the right-hand side matrix 
		Matrix matrix2 = createMatrix2(points, polynomialOrder); //creates the left-hand side matrix
		Matrix resultMatrix = matrix2.inverse().times(matrix1); //divides the matrices to find the a, b, c... matrix
		String polynomial = printResults(resultMatrix); //turns the matrix into a printable result and prints the result to the console
       
		XYSeriesCollection result = new XYSeriesCollection();
        XYSeries series = new XYSeries("data"); //the points given
        
        for (double[] point: points)
        {
        	series.add(point[0], point[1]);
        }
        
        result.addSeries(series);
        XYSeries regression = new XYSeries("regression"); //the polynomial calculated
       
        double[] coefficients = new double[resultMatrix.getRowDimension()]; //must be in x^0->x^n order
       	for (int row = 0; row < resultMatrix.getRowDimension(); row++)
       	{
       		coefficients[resultMatrix.getRowDimension()-1-row] = resultMatrix.get(row, 0);
       	}
       	PolynomialFunction2D function = new PolynomialFunction2D(coefficients);
       	for (double x = series.getMinX(); x <= series.getMaxX(); x+=POLY_STEP)
       	{
       		regression.add(x, function.getValue(x));
       	}
       	result.addSeries(regression);
       	
		chart = ChartFactory.createScatterPlot(polynomial, "x", "y", result);
		
         ChartPanel chartPanel = new ChartPanel(chart);
         // default size
         chartPanel.setPreferredSize(new java.awt.Dimension(1000, 500));
         setContentPane(chartPanel);
	}

	private static Matrix createMatrix1(double[][] points, int polynomialOrder) {
		Matrix matrix = new Matrix(polynomialOrder+1, 1);
		for (int row = 0; row < polynomialOrder+1; row++)
		{
			double value = 0;
			for (int i = 0; i < points.length; i++)
			{
				value += Math.pow(points[i][0], row) * points[i][1];
			}
			matrix.set(row, 0, value);
		}
		return matrix;
	}
	private static Matrix createMatrix2(double[][] points, int polynomialOrder) {
		Matrix matrix = new Matrix(polynomialOrder+1, polynomialOrder+1);
		for (int column = 0; column < polynomialOrder+1; column++)
		{
			for (int row = 0; row < polynomialOrder+1; row++)
			{
				double value = 0;
				for (int i = 0; i < points.length; i++)
				{
					value += Math.pow(points[i][0], (polynomialOrder - column) + row);
				}
				matrix.set(row, column, value);
			}
		}
		return matrix;
	}
	private static String printResults(Matrix result) {
		String polynomial = "y = ";
		for (int row = 0; row < result.getRowDimension(); row++)
		{
			polynomial += result.get(row, 0) + "x^" + (result.getRowDimension() - 1 - row)+" + ";
		}
		polynomial = polynomial.substring(0, polynomial.length()-3);
		System.out.println(polynomial);
		return polynomial;
	}
	
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.awt.Color;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author KSEET_000
 */
public class LineChart extends JFrame {

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
    public LineChart(String title, String xLabel, String yLabel) {
        //dataset.addValue(0, "Time", "0");
        //dataset.addValue(0, "Estimated Time", "0");
        JFreeChart chart = ChartFactory.createLineChart(title, xLabel, yLabel, dataset);
        chart.setBackgroundPaint(Color.YELLOW);
        chart.getTitle().setPaint(Color.RED);
        CategoryPlot p = chart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.BLUE);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setSize(450, 350);
        //chartPanel.setPreferredSize(new java.awt.Dimension(500, 700));
        setContentPane(chartPanel);
    }

    public void addValue(double value, String label, String xValue) {
        dataset.setValue(value, label, xValue);
    }
    
    public void clear() {
        dataset.clear();
        //dataset.addValue(0, "Time", "0");
        //dataset.addValue(0, "Estimated Time", "0");
    }
}

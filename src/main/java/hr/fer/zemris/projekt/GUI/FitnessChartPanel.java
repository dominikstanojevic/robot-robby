package hr.fer.zemris.projekt.GUI;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class FitnessChartPanel extends JPanel {

    private XYSeries maxFitnessSeries;
    private XYSeries avgFitnessSeries;
    private XYSeriesCollection dataset;

    private JFreeChart chart;

    public FitnessChartPanel() {
        super();
        this.maxFitnessSeries = new XYSeries("Maximal standard fitness");
        this.avgFitnessSeries = new XYSeries("Average standard fitness");
        dataset = new XYSeriesCollection();
        dataset.addSeries(maxFitnessSeries);
        dataset.addSeries(avgFitnessSeries);
        final JFreeChart chart = createChart(dataset);

        final ChartPanel chartPanel = new ChartPanel(chart);
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);

    }

    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createXYLineChart("Training progress", "Iterations",
                "Standard fitness", dataset, PlotOrientation.VERTICAL, true, true, false);
        final XYPlot plot = result.getXYPlot();
        result.setBackgroundPaint(getBackground());

        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setRange(0, 200);
        axis = plot.getRangeAxis();
        axis.setRange(-1, 1.0);

        return result;
    }

    private int currIteration = 0;

    public void addValue(double maxFitnessValue, double avgFitnessValue) {
        this.maxFitnessSeries.add(currIteration, maxFitnessValue);
        this.avgFitnessSeries.add(currIteration, avgFitnessValue);
        currIteration++;
    }

    public void clearGraph() {
        currIteration = 0;
        this.maxFitnessSeries.clear();
        this.avgFitnessSeries.clear();
    }
}

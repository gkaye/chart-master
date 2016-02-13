import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.ui.Layer;

public class CmManager {
	private int passed = 0;
	private int failed = 0;
	private int skipped = 0;
	private int historyRange = 0;
	private int predictionDistance = 0;
	private int dataLocation = 0;
	private boolean onQuestion = true;
	private DefaultHighLowDataset datasetData = null;
	private JFreeChart chart = null;
	// Data storage
	private ArrayList<Date> dateData = new ArrayList<Date>();
	private ArrayList<Double> highData = new ArrayList<Double>();
	private ArrayList<Double> lowData = new ArrayList<Double>();
	private ArrayList<Double> openData = new ArrayList<Double>();
	private ArrayList<Double> closeData = new ArrayList<Double>();
	private ArrayList<Double> volumeData = new ArrayList<Double>();

	// Creates CmManager from a data file
	public CmManager(File data, int historyRange, int predictionDistance)
			throws Exception {
		processFile(data);
		setHistoryRange(historyRange);
		setPredictionDistance(predictionDistance);
		next();
	}

	// Sets range of historical data to display in graph
	public void setHistoryRange(int historyRange) {
		this.historyRange = historyRange;
	}

	// Sets the distance which the user is supposed to predict to
	public void setPredictionDistance(int predictionDistance) {
		this.predictionDistance = predictionDistance;
	}

	public int getHistoryRange() {
		return historyRange;
	}

	public int getLocation() {
		return dataLocation;
	}

	public int getPredictionDistance() {
		return predictionDistance;
	}

	public int getPassed() {
		return passed;
	}

	public int getFailed() {
		return failed;
	}

	public int getSkipped() {
		return skipped;
	}

	private void incrementFailed() {
		failed++;
	}

	private void incrementPassed() {
		passed++;
	}

	private void incrementSkipped() {
		skipped++;
	}

	// Resets tracked statistics
	public void resetStatistics() {
		failed = 0;
		passed = 0;
		skipped = 0;
	}

	public double getPercentPassed() {
		if (failed + passed > 0)
			return (((double) passed / ((double) failed + (double) passed)) * 100.0);
		else
			return 0.0;
	}

	// Processes data out of file and stores in corresponding
	private void processFile(File dataFile) throws Exception {
		BufferedReader b = new BufferedReader(new FileReader(dataFile));
		String line = "";
		Calendar calendar = Calendar.getInstance();
		calendar.set(2000, 0, 0);

		// Ignore first line
		b.readLine();
		while ((line = b.readLine()) != null) {
			String[] values = line.split(",");
			// Fill hash
			dateData.add(calendar.getTime());
			openData.add(Double.parseDouble(values[1]));
			highData.add(Double.parseDouble(values[2]));
			lowData.add(Double.parseDouble(values[3]));
			closeData.add(Double.parseDouble(values[4]));
			volumeData.add(Double.parseDouble(values[5]));
			calendar.add(Calendar.DATE, 1);
		}

		if (dateData.size() < (getHistoryRange() + getPredictionDistance() * 100)) {
			b.close();
			throw new Exception("Data file not of adequate size");
		}
		b.close();
	}

	// Regenerate chart based upon data values
	private void refreshChart() {
		if (onQuestion)
			generateChartQuestion();
		else
			generateChartAnswer();
	}

	private void generateChartQuestion() {
		int preRange = dataLocation - historyRange;
		int postRange = dataLocation + 1;

		Object[] datePreArray = dateData.subList(preRange,
				postRange + predictionDistance * 2).toArray();
		Object[] highPreArray = highData.subList(preRange, postRange).toArray();
		Object[] lowPreArray = lowData.subList(preRange, postRange).toArray();
		Object[] openPreArray = openData.subList(preRange, postRange).toArray();
		Object[] closePreArray = closeData.subList(preRange, postRange)
				.toArray();
		Object[] volumePreArray = volumeData.subList(preRange, postRange)
				.toArray();

		Date[] date = (Date[]) Arrays.copyOf(datePreArray, datePreArray.length,
				Date[].class);
		Double[] high = (Double[]) Arrays.copyOf(highPreArray,
				highPreArray.length, Double[].class);
		Double[] low = (Double[]) Arrays.copyOf(lowPreArray,
				lowPreArray.length, Double[].class);
		Double[] open = (Double[]) Arrays.copyOf(openPreArray,
				openPreArray.length, Double[].class);
		Double[] close = (Double[]) Arrays.copyOf(closePreArray,
				closePreArray.length, Double[].class);
		Double[] volume = (Double[]) Arrays.copyOf(volumePreArray,
				volumePreArray.length, Double[].class);

		datasetData = new DefaultHighLowDataset("", date, ArrayUtils.addAll(
				doubleToPrimative(high), new double[predictionDistance * 2]),
				ArrayUtils.addAll(doubleToPrimative(low),
						new double[predictionDistance * 2]), ArrayUtils.addAll(
						doubleToPrimative(open),
						new double[predictionDistance * 2]), ArrayUtils.addAll(
						doubleToPrimative(close),
						new double[predictionDistance * 2]), ArrayUtils.addAll(
						doubleToPrimative(volume),
						new double[predictionDistance * 2]));
		constructChart();
	}

	private void generateChartAnswer() {
		int preRange = dataLocation - historyRange;
		int postRange = dataLocation + predictionDistance * 2 + 1;

		Object[] datePreArray = dateData.subList(preRange, postRange).toArray();
		Object[] highPreArray = highData.subList(preRange, postRange).toArray();
		Object[] lowPreArray = lowData.subList(preRange, postRange).toArray();
		Object[] openPreArray = openData.subList(preRange, postRange).toArray();
		Object[] closePreArray = closeData.subList(preRange, postRange)
				.toArray();
		Object[] volumePreArray = volumeData.subList(preRange, postRange)
				.toArray();

		Date[] date = (Date[]) Arrays.copyOf(datePreArray, datePreArray.length,
				Date[].class);
		Double[] high = (Double[]) Arrays.copyOf(highPreArray,
				highPreArray.length, Double[].class);
		Double[] low = (Double[]) Arrays.copyOf(lowPreArray,
				lowPreArray.length, Double[].class);
		Double[] open = (Double[]) Arrays.copyOf(openPreArray,
				openPreArray.length, Double[].class);
		Double[] close = (Double[]) Arrays.copyOf(closePreArray,
				closePreArray.length, Double[].class);
		Double[] volume = (Double[]) Arrays.copyOf(volumePreArray,
				volumePreArray.length, Double[].class);

		datasetData = new DefaultHighLowDataset("", date,
				doubleToPrimative(high), doubleToPrimative(low),
				doubleToPrimative(open), doubleToPrimative(close),
				doubleToPrimative(volume));
		constructChart();
	}

	private void constructChart() {
		chart = ChartFactory.createCandlestickChart(null, "", "Price",
				datasetData, false);
		chart.setBackgroundPaint(Color.BLACK);
		chart.getPlot().setBackgroundPaint(Color.BLACK);
		XYPlot plot = (XYPlot) chart.getPlot();

		// Add prediction region
		long d1 = datasetData.getXDate(0, historyRange).getTime() - 86400000 / 2;
		long d2 = datasetData.getXDate(0, historyRange + predictionDistance)
				.getTime() + 86400000 / 2;
		IntervalMarker region1 = new IntervalMarker(d1, d2);
		region1.setAlpha(0.3f);
		region1.setPaint(new Color(50, 50, 50));
		plot.addDomainMarker(region1, Layer.BACKGROUND);

		// Add answer region
		if (!onQuestion) {
			double l1 = closeData.get(dataLocation);
			double l2 = closeData.get(dataLocation + predictionDistance);
			IntervalMarker region2 = null;
			boolean isUp = ((l1 - l2) < 0);
			if (l1 != l2) {
				if (isUp) {
					region2 = new IntervalMarker(l1, l2);
				} else {
					region2 = new IntervalMarker(l2, l1);
				}
				region2.setAlpha(0.2f);
				region2.setPaint(isUp ? Color.GREEN : Color.RED);
				plot.addRangeMarker(region2, Layer.FOREGROUND);
			}
		}

		// Configure miscellaneous chart parameters
		ValueAxis y = (ValueAxis) chart.getXYPlot().getRangeAxis();
		Axis x = chart.getXYPlot().getDomainAxis();
		((CandlestickRenderer) plot.getRenderer()).setVolumePaint(new Color(50,
				50, 50));
		double[] range = calculateRange();
		y.setRange(range[1], range[0]);
		x.setTickLabelsVisible(false);
		x.setTickMarksVisible(false);
		x.setAxisLineVisible(false);
		plot.setDomainGridlinesVisible(false);
		plot.setDomainMinorGridlinesVisible(false);
		plot.setDomainCrosshairVisible(false);
		plot.setDomainCrosshairLockedOnData(false);
		plot.setDomainZeroBaselineVisible(false);
		plot.setRangeGridlinesVisible(false);
		plot.setRangeMinorGridlinesVisible(false);
		plot.setRangeCrosshairVisible(false);
		plot.setRangeCrosshairLockedOnData(false);
		plot.setRangeZeroBaselineVisible(false);
	}

	private double[] calculateRange() {
		double highestPoint = 0;
		double lowestPoint = 0;
		int start = dataLocation-historyRange;
		int question = dataLocation + 1;
		int end = dataLocation + predictionDistance * 2 + 1;
		
		if (onQuestion) {
			highestPoint = calculateHighestValue(highData.subList(start, question));
			lowestPoint = calculateLowestValue(lowData.subList(start, question));	
		} else {
			highestPoint = calculateHighestValue(highData.subList(start, end));
			lowestPoint = calculateLowestValue(lowData.subList(start, end));
		}
		
		double difference = highestPoint - lowestPoint;

		double top = highestPoint + (difference * 0.15);
		double bottom = lowestPoint - (difference * 0.15);

		return (new double[] { top, bottom });
	}

	private double calculateHighestValue(List<Double> array) {
		Double max = array.get(0);
		for (int i = 1; i < array.size(); i++) {
			if (array.get(i) > max) {
				max = array.get(i);
			}
		}
		return (max);
	}

	private double calculateLowestValue(List<Double> array) {
		Double min = array.get(0);
		for (int i = 1; i < array.size(); i++) {
			if (array.get(i) < min) {
				min = array.get(i);
			}
		}
		return (min);
	}

	private double[] doubleToPrimative(Double[] data) {
		double[] tempArray = new double[data.length];
		int i = 0;
		for (Double d : data) {
			tempArray[i] = (double) d;
			i++;
		}
		return (tempArray);
	}

	// Returns true if higher, else lower
	private boolean Answer() {
		return (!((closeData.get(dataLocation) > closeData.get(dataLocation
				+ predictionDistance)))) ? true : false;
	}

	private void setOnQuestion() {
		onQuestion = true;
	}

	private void setOnAnswer() {
		onQuestion = false;
	}

	// Generates and returns chart
	public JFreeChart getChart() {
		return chart;
	}

	public int getTotalQuestions() {
		return (highData.size() / (historyRange + predictionDistance * 2));
	}

	// Submits user response and performs corresponding statistics processing
	// and chart generation actions, returns validity of choice
	public boolean voteUp() {
		if (Answer())
			incrementPassed();
		else
			incrementFailed();
		setOnAnswer();
		refreshChart();
		if (!Answer())
			chart.setBackgroundPaint(new Color(25, 0, 0));
		else
			chart.setBackgroundPaint(new Color(0, 25, 0));
		return Answer();
	}

	// Submits user response and performs corresponding statistics processing
	// and chart generation actions, returns validity of choice
	public boolean voteDown() {
		if (!Answer())
			incrementPassed();
		else
			incrementFailed();
		setOnAnswer();
		refreshChart();
		if (!Answer())
			chart.setBackgroundPaint(new Color(0, 25, 0));
		else
			chart.setBackgroundPaint(new Color(25, 0, 0));
		return !Answer();
	}

	// Submits user response and performs corresponding statistics processing
	// and chart generation actions, returns validity of choice
	public void voteSkip() throws Exception {
		incrementSkipped();
		next();
	}

	// Moves to next question if on answer
	public void next() throws Exception {
		setOnQuestion();
		int n = highData.size() - historyRange - predictionDistance * 2 - 1;
		if (n < 1)
			throw new Exception(
					"History range or prediction distance are too small for size of historical data");
		dataLocation = historyRange + (new SecureRandom()).nextInt(n);
		refreshChart();
	}

	public boolean onQuestion() {
		return onQuestion;
	}
}

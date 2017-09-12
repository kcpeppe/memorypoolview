/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodewerk.visualvm.memorypoolview.gc;

import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author kirk
 */
class GarbageCollectionFrequencyPanel extends JPanel implements GarbageCollectorModelListener {
    
    public final static long ONE_MEGABYTE_SIZE = 1048576;
    
    private SimpleXYChartSupport chart;

    public GarbageCollectionFrequencyPanel() {
        setLayout(new BorderLayout());
        SimpleXYChartDescriptor description = SimpleXYChartDescriptor.decimal(ONE_MEGABYTE_SIZE, false, 1000);
        
        description.addLineItems("GC Frequency");
        description.setDetailsItems(new String[]{ "Current Frequency" });
        
        init( description);
    }
    
    public void init( SimpleXYChartDescriptor description) {
        chart = ChartFactory.createSimpleXYChart(description);
        add(chart.getChart(), BorderLayout.CENTER);
    }
    
    protected SimpleXYChartSupport getChart() { return chart; }

    @Override
    public void garbageCollectorUpdated(GarbageCollectionModel model) {
        long[] dataPoints = new long[1];
        dataPoints[0] = (long)model.getFrequency();
        chart.addValues(System.currentTimeMillis(), dataPoints);

        String[] details = new String[1];
        details[0] =  Collection.formatNumber((long)model.getFrequency()) + " collections per second";
        getChart().updateDetails(details);
    }    
}

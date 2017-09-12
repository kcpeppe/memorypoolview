/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodewerk.visualvm.memorypoolview.gc;

import static com.kodewerk.visualvm.memorypoolview.gc.GarbageCollectorDurationPanel.ONE_MEGABYTE_SIZE;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import java.awt.BorderLayout;

/**
 *
 * @author kirk
 */
public class GarbageCollectorAnalyticPanel extends GarbageCollectorDurationPanel {
    
    public GarbageCollectorAnalyticPanel() {
        setLayout(new BorderLayout());
        SimpleXYChartDescriptor description = SimpleXYChartDescriptor.decimal(ONE_MEGABYTE_SIZE, false, 1000);

        description.addLineItems("Last GC duration");
        description.addLineItems("Indicator (sample size: " + GarbageCollectorAnalyticModel.MOVING_AVERAGE_PERIOD + ")");
        description.setDetailsItems(new String[]{"Last duration", "Number of collections", "Total time in GC",
                "Indicator"});
        
        super.init( description);
    }
    
    @Override
    public void garbageCollectorUpdated(GarbageCollectionModel model) {
        long[] dataPoints = new long[2];
        dataPoints[0] = model.getLastDuration();
        dataPoints[1] = ((GarbageCollectorAnalyticModel)model).getMovingAverage();
        super.getChart().addValues(System.currentTimeMillis(), dataPoints);

        String[] details = new String[4];
        details[0] = Collection.formatNumber(model.getLastDuration()) + " ms";
        details[1] = String.valueOf(model.getCount());
        details[2] = Collection.formatNumber(model.getTotalDuration()) + " ms";
        details[3] = String.valueOf(((GarbageCollectorAnalyticModel)model).getMovingAverage());
        getChart().updateDetails(details);
    }    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodewerk.visualvm.memorypoolview;

/**
 *
 * @author kirk
 */
public class MemoryPoolAnalyticPanel extends MemoryPoolPanel implements MemoryPoolModelListener {
    
    public MemoryPoolAnalyticPanel() {
        super( "Indicator (sample size: " + MemoryPoolAnalyticModel.MOVING_AVERAGE_PERIOD + ")", "Indicator");        
    }
    
    public void memoryPoolUpdated(MemoryPoolModel model) {
        long[] dataPoints = new long[3];
        dataPoints[0] = model.getCommitted();
        dataPoints[1] = model.getUsed();
        dataPoints[2] = ((MemoryPoolAnalyticModel)model).getMovingAverage();

        String[] details = new String[4];
        details[0] = formatBytes(model.getCommitted());
        details[1] = formatBytes(model.getUsed());
        details[2] = formatBytes(model.getMax());
        details[3] = formatBytes(((MemoryPoolAnalyticModel)model).getMovingAverage());
        updateChart( dataPoints, details);
    } 
    
}

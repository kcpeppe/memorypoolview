/*
 * Copyright (c) 2011-2013, Kirk Pepperdine.
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the license at http://www.opensource.org/licenses/CDDL-1.0.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 */
package com.kodewerk.visualvm.memorypoolview;

import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;

import javax.swing.*;
import java.awt.*;

/**
 * @author kirk
 */
public class MemoryPoolPanel extends JPanel implements MemoryPoolModelListener {
    private final static long ONE_MEGABYTE_SIZE = 1048576;

    private SimpleXYChartSupport chart;

    public MemoryPoolPanel() {
        this( new String[]{ "Memory Pool Size", "Memory Pool Used" }, new String[]{"Size", "Used", "Max"});
    }
    
    public MemoryPoolPanel( String additionalLineItem, String additionalDetails) {
        this( new String[]{ "Memory Pool Size", "Memory Pool Used", additionalLineItem }, new String[]{"Size", "Used", "Max", additionalDetails});                
    }
    
    MemoryPoolPanel( String[] lineItems, String[] details) {
        setLayout(new BorderLayout());
        SimpleXYChartDescriptor description = SimpleXYChartDescriptor.bytes(ONE_MEGABYTE_SIZE, false, 1000);

        for ( String lineItem : lineItems)
            description.addLineItems( lineItem);

        description.setDetailsItems( details);
        
        chart = ChartFactory.createSimpleXYChart(description);
        add(chart.getChart(), BorderLayout.CENTER);
    }
    
    public String formatBytes( long value) {
        return chart.formatBytes(value);
    }
    
    protected void updateChart( long[] values, String[] details) {
        chart.addValues( System.currentTimeMillis(), values);
        chart.updateDetails(details);
    }
 
    @Override
    public void memoryPoolUpdated(MemoryPoolModel model) {
        long[] dataPoints = new long[2];
        dataPoints[0] = model.getCommitted();
        dataPoints[1] = model.getUsed();

        String[] details = new String[3];
        details[0] = formatBytes(model.getCommitted());
        details[1] = formatBytes(model.getUsed());
        details[2] = formatBytes(model.getMax());
        updateChart( dataPoints, details);
    }
}

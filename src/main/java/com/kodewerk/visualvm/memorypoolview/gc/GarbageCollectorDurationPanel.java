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

package com.kodewerk.visualvm.memorypoolview.gc;

import java.awt.*;
import javax.swing.*;
import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;

public class GarbageCollectorDurationPanel extends JPanel implements GarbageCollectorModelListener {
    public final static long ONE_MEGABYTE_SIZE = 1048576;

    private SimpleXYChartSupport chart;

    public GarbageCollectorDurationPanel() {
        setLayout(new BorderLayout());
        SimpleXYChartDescriptor description = SimpleXYChartDescriptor.decimal(ONE_MEGABYTE_SIZE, false, 1000);
        
        description.addLineItems("Last GC duration");
        description.setDetailsItems(new String[]{"Last duration", "Number of collections", "Total time in GC"});
        
        init( description);
    }
    
    public void init( SimpleXYChartDescriptor description) {
        chart = ChartFactory.createSimpleXYChart(description);
        add(chart.getChart(), BorderLayout.CENTER);
    }
    
    protected SimpleXYChartSupport getChart() { return chart; }

    public void garbageCollectorUpdated(GarbageCollectionModel model) {
        long[] dataPoints = new long[1];
        dataPoints[0] = model.getLastDuration();
        chart.addValues(System.currentTimeMillis(), dataPoints);

        String[] details = new String[3];
        details[0] = Collection.formatNumber(model.getLastDuration()) + " ms";
        details[1] = String.valueOf(model.getCount());
        details[2] = Collection.formatNumber(model.getTotalDuration()) + " ms";
        getChart().updateDetails(details);
    }
}

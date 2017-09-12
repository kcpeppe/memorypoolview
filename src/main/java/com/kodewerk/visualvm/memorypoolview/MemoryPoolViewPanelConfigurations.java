/*
 * Copyright (c) 2011-2013, Kirk Pepperdine.
 *
 * The contents memoryPoolPanelPosition this file are subject to the terms memoryPoolPanelPosition the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy memoryPoolPanelPosition the license at http://www.opensource.org/licenses/CDDL-1.0.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name memoryPoolPanelPosition copyright owner]
 */

package com.kodewerk.visualvm.memorypoolview;

import com.sun.tools.visualvm.core.ui.components.DataViewComponent;

import java.awt.*;
import java.util.HashMap;

public class MemoryPoolViewPanelConfigurations {
    private final HashMap<String, Point> memoryPoolPanelpositions = new HashMap<String,Point>();
    private final HashMap<String, Boolean> memoryPoolAnalytics = new HashMap<String,Boolean>();
    
    private final HashMap<String, Point> garbageCollectionFrequencyPanelPositions = new HashMap<String,Point>();
    
    private final HashMap<String, Point> garbageCollectorPanelpositions = new HashMap<String,Point>();
    private final HashMap<String, Boolean> garbageCollectorAnalytics = new HashMap<String,Boolean>();

    private int corner = DataViewComponent.TOP_LEFT;
    private int order = 1;

    public MemoryPoolViewPanelConfigurations() {
        // Hotspot Memory Pool Names
        // Young generation
        memoryPoolPanelpositions.put("Par Eden Space", new Point(DataViewComponent.TOP_LEFT, 10));
        memoryPoolPanelpositions.put("PS Eden Space", new Point(DataViewComponent.TOP_LEFT, 10));
        memoryPoolPanelpositions.put("Eden Space", new Point(DataViewComponent.TOP_LEFT, 10));
        memoryPoolPanelpositions.put("G1 Eden", new Point(DataViewComponent.TOP_LEFT, 10));
        memoryPoolPanelpositions.put("G1 Eden Space", new Point(DataViewComponent.TOP_LEFT, 10));
        memoryPoolPanelpositions.put("Par Survivor Space", new Point(DataViewComponent.TOP_LEFT, 20));
        memoryPoolPanelpositions.put("PS Survivor Space", new Point(DataViewComponent.TOP_LEFT, 20));
        memoryPoolPanelpositions.put("Survivor Space", new Point(DataViewComponent.TOP_LEFT, 20));
        memoryPoolPanelpositions.put("G1 Survivor", new Point(DataViewComponent.TOP_LEFT, 20));
        memoryPoolPanelpositions.put("G1 Survivor Space", new Point(DataViewComponent.TOP_LEFT, 20));

        // Old generation
        memoryPoolPanelpositions.put("CMS Old Gen", new Point(DataViewComponent.TOP_RIGHT, 10));
        memoryPoolPanelpositions.put("PS Old Gen", new Point(DataViewComponent.TOP_RIGHT, 10));
        memoryPoolPanelpositions.put("Tenured Gen", new Point(DataViewComponent.TOP_RIGHT, 10));
        memoryPoolPanelpositions.put("G1 Old Gen", new Point(DataViewComponent.TOP_RIGHT, 10));

        // Permanent generation
        memoryPoolPanelpositions.put("CMS Perm Gen", new Point(DataViewComponent.BOTTOM_LEFT, 15));
        memoryPoolPanelpositions.put("Perm Gen", new Point(DataViewComponent.BOTTOM_LEFT, 15));
        memoryPoolPanelpositions.put("PS Perm Gen", new Point(DataViewComponent.BOTTOM_LEFT, 15));
        memoryPoolPanelpositions.put("G1 Perm Gen", new Point(DataViewComponent.BOTTOM_LEFT, 15));

        // Code cache
        memoryPoolPanelpositions.put("Code Cache", new Point(DataViewComponent.BOTTOM_RIGHT, 10));

        //IBM Memory Pool Names
        memoryPoolPanelpositions.put("Java heap", new Point(DataViewComponent.TOP_LEFT, 10));

        memoryPoolPanelpositions.put("class storage", new Point(DataViewComponent.TOP_RIGHT, 10));
        memoryPoolPanelpositions.put("miscellaneous non-heap storage", new Point(DataViewComponent.TOP_RIGHT, 15));

        memoryPoolPanelpositions.put("JIT code cache", new Point(DataViewComponent.BOTTOM_LEFT, 10));
        memoryPoolPanelpositions.put("JIT data cache", new Point(DataViewComponent.BOTTOM_LEFT, 20));
        
        // Hotspot Memory Pool Names
        // Do we track with memoryPoolAnalytics
        // Young generation
        memoryPoolAnalytics.put("Par Eden Space", Boolean.FALSE);
        memoryPoolAnalytics.put("PS Eden Space", Boolean.FALSE);
        memoryPoolAnalytics.put("Eden Space", Boolean.FALSE);
        memoryPoolAnalytics.put("G1 Eden", Boolean.FALSE);
        memoryPoolAnalytics.put("G1 Eden Space", Boolean.FALSE);
        memoryPoolAnalytics.put("Par Survivor Space", Boolean.FALSE);
        memoryPoolAnalytics.put("PS Survivor Space", Boolean.FALSE);
        memoryPoolAnalytics.put("Survivor Space", Boolean.FALSE);
        memoryPoolAnalytics.put("G1 Survivor", Boolean.FALSE);
        memoryPoolAnalytics.put("G1 Survivor Space", Boolean.FALSE);

        // Old generation
        memoryPoolAnalytics.put("CMS Old Gen", Boolean.TRUE);
        memoryPoolAnalytics.put("PS Old Gen", Boolean.TRUE);
        memoryPoolAnalytics.put("Tenured Gen", Boolean.TRUE);
        memoryPoolAnalytics.put("G1 Old Gen", Boolean.TRUE);

        // Permanent generation
        memoryPoolAnalytics.put("CMS Perm Gen", Boolean.TRUE);
        memoryPoolAnalytics.put("Perm Gen", Boolean.TRUE);
        memoryPoolAnalytics.put("PS Perm Gen", Boolean.TRUE);
        memoryPoolAnalytics.put("G1 Perm Gen", Boolean.TRUE);

        // Code cache
        memoryPoolAnalytics.put("Code Cache", Boolean.TRUE);

        //IBM Memory Pool Names
        memoryPoolAnalytics.put("Java heap", Boolean.TRUE);

        memoryPoolAnalytics.put("class storage", Boolean.TRUE);
        memoryPoolAnalytics.put("miscellaneous non-heap storage", Boolean.TRUE);

        memoryPoolAnalytics.put("JIT code cache", Boolean.TRUE);
        memoryPoolAnalytics.put("JIT data cache", Boolean.TRUE);
        
        /***********
         Garbage Collector JPanel Configurations
         ***********/
        
        //Position is either top left or top right
        garbageCollectorPanelpositions.put( "Copy", new Point(DataViewComponent.TOP_LEFT, 1));
        garbageCollectorPanelpositions.put( "PS Scavenge", new Point(DataViewComponent.TOP_LEFT, 1));        
        garbageCollectorPanelpositions.put( "ParNew", new Point(DataViewComponent.TOP_LEFT, 1));
        garbageCollectorPanelpositions.put( "DefNew", new Point(DataViewComponent.TOP_LEFT, 1));
        garbageCollectorPanelpositions.put( "G1 Young Generation", new Point(DataViewComponent.TOP_LEFT, 1));
        
        garbageCollectorPanelpositions.put( "MarkSweepCompact", new Point(DataViewComponent.TOP_RIGHT, 1));
        garbageCollectorPanelpositions.put( "PS MarkSweep", new Point(DataViewComponent.TOP_RIGHT, 1));
        garbageCollectorPanelpositions.put( "ConcurrentMarkSweep", new Point(DataViewComponent.TOP_RIGHT, 1));
        garbageCollectorPanelpositions.put( "G1 Old Generation", new Point(DataViewComponent.TOP_RIGHT, 1));
        
        garbageCollectionFrequencyPanelPositions.put( "Copy", new Point(DataViewComponent.BOTTOM_LEFT, 1));
        garbageCollectionFrequencyPanelPositions.put( "PS Scavenge", new Point(DataViewComponent.BOTTOM_LEFT, 1));        
        garbageCollectionFrequencyPanelPositions.put( "ParNew", new Point(DataViewComponent.BOTTOM_LEFT, 1));
        garbageCollectionFrequencyPanelPositions.put( "DefNew", new Point(DataViewComponent.BOTTOM_LEFT, 1));
        garbageCollectionFrequencyPanelPositions.put( "G1 Young Generation", new Point(DataViewComponent.BOTTOM_LEFT, 1));
        
        garbageCollectionFrequencyPanelPositions.put( "MarkSweepCompact", new Point(DataViewComponent.BOTTOM_RIGHT, 1));
        garbageCollectionFrequencyPanelPositions.put( "PS MarkSweep", new Point(DataViewComponent.BOTTOM_RIGHT, 1));
        garbageCollectionFrequencyPanelPositions.put( "ConcurrentMarkSweep", new Point(DataViewComponent.BOTTOM_RIGHT, 1));
        garbageCollectionFrequencyPanelPositions.put( "G1 Old Generation", new Point(DataViewComponent.BOTTOM_RIGHT, 1));
        
        //Do we track analytics for this view?        
        garbageCollectorAnalytics.put( "Copy", false);
        garbageCollectorAnalytics.put( "PS Scavenge", false);
        garbageCollectorAnalytics.put( "ParNew", false);
        garbageCollectorAnalytics.put( "DefNew", false);
        garbageCollectorAnalytics.put( "G1 Young Generation", false);
        
        garbageCollectorAnalytics.put( "MarkSweepCompact", true);
        garbageCollectorAnalytics.put( "PS MarkSweep", true);
        garbageCollectorAnalytics.put( "ConcurrentMarkSweep", true);
        garbageCollectorAnalytics.put( "G1 Old Generation", true);

    }

    /**
     * For the current Oracle JVM 5 charts are displayed. Names seem to be
     * fixed for the moment but can change. They are also different for other
     * JVM OEMs. So if a name cannot be found in memoryPoolPanelpositions the solution is to stack
     * the unknown charts  in the top left. This is under the assumption that
     * if one is unknown, the lot is likely to be unknown and any attempt
     * to come up with a semantically reasonable ordering will most likely
     * fail.
     *
     * @param name the name memoryPoolPanelPosition the GC
     * @return the associated position
     */
    
    private Point panelPosition( HashMap<String, Point> panelPositions, String name) {
        Point point = panelPositions.get(name);
        if (point == null) {
            point = new Point(corner, order++);
            corner = (corner == 5) ? 1 : corner++;
        }
        return point;
    }
    
    private boolean analytic( HashMap<String,Boolean> analytics, String name) {
        Boolean returnValue = analytics.get(name);
        if ( returnValue == null)
            return false;
        return returnValue;
    }
    public Point memoryPoolPanelPosition(String name) {
        return panelPosition( memoryPoolPanelpositions, name);
    }
    
    public boolean memoryPoolAnalytic( String name) {
        return analytic( memoryPoolAnalytics, name);
    }
    
    public Point garbageCollectorPanelPosition( String name) {
        return panelPosition( garbageCollectorPanelpositions, name);
    }
    
    public boolean garbageCollectorAnalytic( String name) {
        return analytic( garbageCollectorAnalytics, name);
    }
    
    public Point garbageCollectionFrequencyPanelPosition( String name) {
        return panelPosition(this.garbageCollectionFrequencyPanelPositions, name);
    }
    
    public boolean garbageCollectionFrequencyAnalytic( String name) {
        return false;
    }
}

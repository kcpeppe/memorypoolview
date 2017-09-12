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

import com.sun.tools.visualvm.tools.jmx.CachedMBeanServerConnectionFactory;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.MBeanCacheListener;
import org.openide.util.Exceptions;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GarbageCollectionModel implements MBeanCacheListener {

    private final Set<GarbageCollectorModelListener> listeners = new HashSet<GarbageCollectorModelListener>();
    private final ObjectName mbeanName;
    private final MBeanServerConnection mbeanServerConnection;
    private final String name;

    private Collection currentCollection = new Collection();
    private Collection previousCollection = new Collection();
    private long timeOfPreviousObservation = System.currentTimeMillis();
    private long timeOfCurrentObservation = System.currentTimeMillis() + 1;
   

    public GarbageCollectionModel(final ObjectName mbeanName, final JmxModel model, final MBeanServerConnection mbeanServerConnection) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException {
        this.mbeanName = mbeanName;
        this.mbeanServerConnection = mbeanServerConnection;
        this.name = mbeanServerConnection.getAttribute(mbeanName, "Name").toString();
        CachedMBeanServerConnectionFactory.getCachedMBeanServerConnection(model, 2000).addMBeanCacheListener(this);
    }

    /**
     *             Long collectionCount = (Long) mbeanServerConnection.getAttribute(mbeanName, "CollectionCount");
            if ( collectionCount != currentCollection.getCount()) {
                Long collectionTime = (Long) mbeanServerConnection.getAttribute(mbeanName, "CollectionTime");
                Long lastDuration = extractLastDuration();
                this.previousCollection = currentCollection;
                currentCollection = new Collection( collectionCount, collectionTime, lastDuration);
            }
     */
    @Override
    public void flushed() {
        try {
            Long collectionTime = (Long) mbeanServerConnection.getAttribute(mbeanName, "CollectionTime");
            Long collectionCount = (Long) mbeanServerConnection.getAttribute(mbeanName, "CollectionCount");
            Long lastDuration = extractLastDuration();
            
            this.previousCollection = currentCollection;
            this.timeOfPreviousObservation = this.timeOfCurrentObservation;
            
            /* only create a new sample if the data is different */
            if ( collectionCount != currentCollection.getCount())
                this.currentCollection = new Collection(collectionCount, collectionTime, lastDuration);
            this.timeOfCurrentObservation = System.currentTimeMillis();
            
            tickleListeners();
        } catch (Throwable t) {
            Exceptions.attachMessage(t, "Exception recovering data from GarbageCollectorMXBean");
        }
    }

    private Long extractLastDuration() throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException {
        try {
            return extractLastDurationFromHotspot();
        } catch (AttributeNotFoundException e) {
            return extractLastDurationFromJ9();
        }
    }

    private Long extractLastDurationFromHotspot() throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException {
        CompositeData lastGcInfo = (CompositeData) mbeanServerConnection.getAttribute(mbeanName, "LastGcInfo");
        return (Long) lastGcInfo.get("duration");
    }

    private Long extractLastDurationFromJ9() throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException {
        Long lastCollectionStartTime = (Long) mbeanServerConnection.getAttribute(mbeanName, "LastCollectionStartTime");
        Long lastCollectionEndTime = (Long) mbeanServerConnection.getAttribute(mbeanName, "LastCollectionEndTime");
        if (lastCollectionStartTime == null || lastCollectionEndTime == null) {
            throw new AttributeNotFoundException("Last collection duration could not be retrieved. " +
                    "lastCollectionStartTime=[" + lastCollectionStartTime +
                    "], lastCollectionEndTime=[" + lastCollectionEndTime +
                    "]");
        } else {
            return lastCollectionEndTime - lastCollectionStartTime;
        }
    }

    private void tickleListeners() {
        for (GarbageCollectorModelListener listener : listeners) {
            listener.garbageCollectorUpdated(this);
        }
    }

    public long getCount() {
        return currentCollection.getCount();
    }

    public long getTotalDuration() {
        return currentCollection.getTotalDuration();
    }

    public long getLastDuration() {
        return currentCollection.getLastDuration();
    }

    /*
     * x per time unit (variable)
     */
    public long getFrequency() {
        System.out.println("getFrequency called");
        return 1000 * (currentCollection.getCount() - previousCollection.getCount()) / (timeOfCurrentObservation - timeOfPreviousObservation);
    }

    public void registerView(GarbageCollectorModelListener listener) {
        System.out.println("new listener " + listener.toString());
        listeners.add(listener);
    }

    public String getName() {
        return name;
    }
}

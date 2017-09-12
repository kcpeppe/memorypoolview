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

import com.sun.tools.visualvm.tools.jmx.CachedMBeanServerConnectionFactory;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.MBeanCacheListener;
import org.openide.util.Exceptions;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author kirk
 */
public class MemoryPoolModel implements MBeanCacheListener {

    private final Set<MemoryPoolModelListener> listeners = new HashSet<MemoryPoolModelListener>();
    private final String name;
    private final String type;
    private final ObjectName mbeanName;
    private final MBeanServerConnection mbeanServerConnection;

    private MemoryUsage memoryUsage;

    public MemoryPoolModel(final ObjectName mbeanName, final JmxModel model, final MBeanServerConnection mbeanServerConnection) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException {
        this.mbeanName = mbeanName;
        this.mbeanServerConnection = mbeanServerConnection;
        CachedMBeanServerConnectionFactory.getCachedMBeanServerConnection(model, 2000).addMBeanCacheListener(this);
        name = mbeanServerConnection.getAttribute(mbeanName, "Name").toString();
        type = mbeanServerConnection.getAttribute(mbeanName, "Type").toString();
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public long getCommitted() {
        return this.memoryUsage.getCommitted();
    }

    public long getMax() {
        return this.memoryUsage.getMax();
    }

    public long getUsed() {
        return this.memoryUsage.getUsed();
    }

    public void registerView(MemoryPoolModelListener listener) {
        listeners.add(listener);
    }

    public Iterator<MemoryPoolModelListener> views() {
        return listeners.iterator();
    }

    protected void tickleListeners() {
        for (MemoryPoolModelListener listener : listeners) {
            listener.memoryPoolUpdated(this);
        }
    }

    @Override
    public void flushed() {
        try {
            CompositeData poolStatistics = (CompositeData) mbeanServerConnection.getAttribute(mbeanName, "Usage");
            if (poolStatistics != null) {
                memoryUsage = MemoryUsage.from(poolStatistics);
                tickleListeners();
            }
        } catch (Throwable t) {
            Exceptions.attachMessage(t, "Exception recovering data from MemoryPoolMXBean ");
        }
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(this.getName());
        buffer.append(" : ").append(this.getType());
        buffer.append(" : ").append(this.getUsed()).append(" : ").append(this.getCommitted());
        return buffer.toString();
    }
}

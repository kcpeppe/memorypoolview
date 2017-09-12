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

import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.DataSourceViewProvider;
import com.sun.tools.visualvm.core.ui.DataSourceViewsManager;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;
import org.openide.util.Exceptions;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.io.IOException;
import java.util.Set;

/**
 * @author kirk
 */
class MemoryPoolViewProvider extends DataSourceViewProvider<Application> {

    private static DataSourceViewProvider<Application> instance = new MemoryPoolViewProvider();

    @Override
    public boolean supportsViewFor(final Application application) {
        JmxModel jmx = JmxModelFactory.getJmxModelFor(application);
        if (jmx != null && jmx.getConnectionState() == JmxModel.ConnectionState.CONNECTED) {
            MBeanServerConnection connection = jmx.getMBeanServerConnection();
            try {
                Set<ObjectName> objectNames = connection.queryNames(ObjectName.WILDCARD, null);
                return !objectNames.isEmpty();
            } catch (IOException e) {
                Exceptions.printStackTrace(e);
            }
        }
        return false;
    }

    @Override
    public synchronized DataSourceView createView(final Application application) {
        return new MemoryPoolView(application);

    }

    static void initialize() {
        DataSourceViewsManager.sharedInstance().addViewProvider(instance, Application.class);
    }

    static void unregister() {
        DataSourceViewsManager.sharedInstance().removeViewProvider(instance);
    }
}

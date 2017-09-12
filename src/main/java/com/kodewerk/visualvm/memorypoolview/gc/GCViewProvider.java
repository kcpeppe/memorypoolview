/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodewerk.visualvm.memorypoolview.gc;

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
public class GCViewProvider extends DataSourceViewProvider<Application> {

    private static DataSourceViewProvider<Application> instance = new GCViewProvider();

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
        return new GCView(application);

    }

    public static void initialize() {
        DataSourceViewsManager.sharedInstance().addViewProvider(instance, Application.class);
    }

    public static void unregister() {
        DataSourceViewsManager.sharedInstance().removeViewProvider(instance);
    }

}

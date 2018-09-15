/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kodewerk.visualvm.memorypoolview.gc;

import com.kodewerk.visualvm.memorypoolview.MemoryPoolViewPanelConfigurations;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author kirk
 */
public class GCView extends DataSourceView {

    private final static ObjectName GARBAGE_COLLECTOR_WILDCARD_PATTERN = createGarbageCollectorWilcardPattern();
    private static final String IMAGE_PATH = "com/kodewerk/visualvm/memorypoolview/bin.png";

    private final Set<GarbageCollectionModel> garbageCollectorModels = new HashSet<GarbageCollectionModel>();
    private DataViewComponent dvc;

    public static MBeanServerConnection getMBeanServerConnection(Application application) {
        JmxModel jmx = JmxModelFactory.getJmxModelFor(application);
        return jmx == null ? null : jmx.getMBeanServerConnection();
    }

    private static ObjectName createGarbageCollectorWilcardPattern() {
        try {
            return new ObjectName("java.lang:type=GarbageCollector,name=*");
        } catch (Exception ignored) {
            return null;
        }
    }

    public GCView(Application application) {
        super(application, "Garbage Collector Activity", new ImageIcon(ImageUtilities.loadImage(IMAGE_PATH, true)).getImage(), 60, false);
    }

    @Override
    protected DataViewComponent createComponent() {
        //Data area for master view:
        JEditorPane generalDataArea = new JEditorPane();
        generalDataArea.setEditable(false);
        generalDataArea.setBorder(BorderFactory.createEmptyBorder(7, 8, 7, 8));

        //Master view:
        DataViewComponent.MasterView masterView = new DataViewComponent.MasterView("Garbage Collector statistics", "View of statistics", generalDataArea);

        //Configuration of master view:
        DataViewComponent.MasterViewConfiguration masterConfiguration = new DataViewComponent.MasterViewConfiguration(false);

        //Add the master view and configuration view to the component:
        dvc = new DataViewComponent(masterView, masterConfiguration);

        findGarbageCollectorsAndCreatePanels();

        return dvc;
    }

    private void findGarbageCollectorsAndCreatePanels() {
        MemoryPoolViewPanelConfigurations configuration = new MemoryPoolViewPanelConfigurations();
        try {
            MBeanServerConnection conn = getMBeanServerConnection((Application) super.getDataSource());
            for (ObjectName name : conn.queryNames(GARBAGE_COLLECTOR_WILDCARD_PATTERN, null)) {
                if ( configuration.garbageCollectorAnalytic(name.getKeyProperty("name"))) {
                    GarbageCollectorAnalyticModel model = initializeGarbageCollectorAnalyticModel( name, conn);
                    configureGarbageCollectorDurationPanelFor( new GarbageCollectorAnalyticPanel(), model, configuration);
                    configureGarbageCollectionFrequencyPanelFor( new GarbageCollectionFrequencyPanel(), model, configuration);
                } else {
                    GarbageCollectionModel model = initializeGarbageCollectorModel( name, conn);
                    configureGarbageCollectorDurationPanelFor( new GarbageCollectorDurationPanel(), model, configuration);
                    configureGarbageCollectionFrequencyPanelFor( new GarbageCollectionFrequencyPanel(), model, configuration);
                }
            }
        } catch (IOException e) {
            Exceptions.printStackTrace(e);
        }
    }
    
        private GarbageCollectorAnalyticModel initializeGarbageCollectorAnalyticModel(ObjectName mbeanName, MBeanServerConnection conn) {
        GarbageCollectorAnalyticModel model = null;
        try {
            model = new GarbageCollectorAnalyticModel( mbeanName, JmxModelFactory.getJmxModelFor((Application) super.getDataSource()), conn);
            garbageCollectorModels.add(model);
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
        return model;
    }

    private GarbageCollectionModel initializeGarbageCollectorModel(ObjectName mbeanName, MBeanServerConnection conn) {
        GarbageCollectionModel model = null;
        try {
            model = new GarbageCollectionModel(mbeanName, JmxModelFactory.getJmxModelFor((Application) super.getDataSource()), conn);
            garbageCollectorModels.add(model);
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
        return model;
    }
    
    private void configureGarbageCollectorDurationPanelFor(GarbageCollectorDurationPanel panel, GarbageCollectionModel model, MemoryPoolViewPanelConfigurations configuration) {
        model.registerView(panel);
        Point position = configuration.garbageCollectorPanelPosition(model.getName());
        DataViewComponent.DetailsView detailsView = new DataViewComponent.DetailsView(
                model.getName(), "garbage collector metrics", position.y, panel, null);
        dvc.addDetailsView(detailsView, position.x);
    }
    
    private void configureGarbageCollectionFrequencyPanelFor(GarbageCollectionFrequencyPanel panel, GarbageCollectionModel model, MemoryPoolViewPanelConfigurations configuration) {
        model.registerView(panel);
        Point position = configuration.garbageCollectionFrequencyPanelPosition(model.getName());
        DataViewComponent.DetailsView detailsView = new DataViewComponent.DetailsView(
                model.getName(), "GC Frequency", position.y, panel, null);
        dvc.addDetailsView(detailsView, position.x);
    } 
}

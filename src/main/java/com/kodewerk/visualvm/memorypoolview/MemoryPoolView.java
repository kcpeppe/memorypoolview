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
import java.util.ArrayList;

/**
 * @author kirk
 */
class MemoryPoolView extends DataSourceView {

    private final static ObjectName MEMORY_POOL_WILDCARD_PATTERN = createMemoryPoolWilcardPattern();
    private static final String IMAGE_PATH = "com/kodewerk/visualvm/memorypoolview/memory.png";

    private final ArrayList<MemoryPoolModel> memoryPoolModels = new ArrayList<MemoryPoolModel>();
    private DataViewComponent dvc;

    public static MBeanServerConnection getMBeanServerConnection(Application application) {
        JmxModel jmx = JmxModelFactory.getJmxModelFor(application);
        return jmx == null ? null : jmx.getMBeanServerConnection();
    }

    private static ObjectName createMemoryPoolWilcardPattern() {
        try {
            return new ObjectName("java.lang:type=MemoryPool,name=*");
        } catch (Exception ignored) {
            return null;
        }
    }

    public MemoryPoolView(Application application) {
        super(application, "Memory Pools", new ImageIcon(ImageUtilities.loadImage(IMAGE_PATH, true)).getImage(), 60, false);
    }

    @Override
    protected DataViewComponent createComponent() {
        //Data area for master view:
        JEditorPane generalDataArea = new JEditorPane();
        generalDataArea.setEditable(false);
        generalDataArea.setBorder(BorderFactory.createEmptyBorder(7, 8, 7, 8));

        //Master view:
        DataViewComponent.MasterView masterView = new DataViewComponent.MasterView("Memory Pools", "View of Memory Pools", generalDataArea);

        //Configuration memoryPoolPanelPosition master view:
        DataViewComponent.MasterViewConfiguration masterConfiguration = new DataViewComponent.MasterViewConfiguration(false);

        //Add the master view and configuration view to the component:
        dvc = new DataViewComponent(masterView, masterConfiguration);

        findMemoryPoolsAndCreatePanels();

        return dvc;
    }

    private void findMemoryPoolsAndCreatePanels() {
        MemoryPoolViewPanelConfigurations configuration = new MemoryPoolViewPanelConfigurations();
        try {
            MBeanServerConnection conn = getMBeanServerConnection((Application) super.getDataSource());
            for (ObjectName name : conn.queryNames(MEMORY_POOL_WILDCARD_PATTERN, null)) {
                if ( configuration.memoryPoolAnalytic( name.getKeyProperty("name"))) {
                    MemoryPoolAnalyticModel model = initializeMemoryPoolAnalyticModel(name, conn);
                    configureMemoryPoolPanelFor( new MemoryPoolAnalyticPanel(), model, configuration);
                } else {
                    MemoryPoolModel model = initializeMemoryPoolModel(name, conn);
                    configureMemoryPoolPanelFor( new MemoryPoolPanel(), model, configuration);
                }
            }
        } catch (IOException e) {
            Exceptions.printStackTrace(e);
        }
    }
    
    private void configureMemoryPoolPanelFor(MemoryPoolPanel panel, MemoryPoolModel model, MemoryPoolViewPanelConfigurations configuration) {        
        model.registerView(panel);
        Point position = configuration.memoryPoolPanelPosition(model.getName());
        DataViewComponent.DetailsView detailsView = new DataViewComponent.DetailsView(
                model.getName(), "memory pool metrics", position.y, panel, null);
        dvc.addDetailsView(detailsView, position.x);
    }

    private MemoryPoolAnalyticModel initializeMemoryPoolAnalyticModel(ObjectName mbeanName, MBeanServerConnection conn) {
        MemoryPoolAnalyticModel model = null;
        try {
            model = new MemoryPoolAnalyticModel( mbeanName, JmxModelFactory.getJmxModelFor((Application) super.getDataSource()), conn);
            memoryPoolModels.add(model);
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
        return model;
    }
    
    private MemoryPoolModel initializeMemoryPoolModel(ObjectName mbeanName, MBeanServerConnection conn) {
        MemoryPoolModel model = null;
        try {
            model = new MemoryPoolModel(mbeanName, JmxModelFactory.getJmxModelFor((Application) super.getDataSource()), conn);
            memoryPoolModels.add( model);
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
        return model;
    }
}

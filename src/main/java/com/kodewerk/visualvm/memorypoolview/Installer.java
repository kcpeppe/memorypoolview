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

import com.kodewerk.visualvm.memorypoolview.gc.GCViewProvider;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's life-cycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void validate() throws IllegalStateException {
    }

    @Override
    public void restored() {
        MemoryPoolViewProvider.initialize();
        GCViewProvider.initialize();
    }

    @Override
    public void uninstalled() {
        MemoryPoolViewProvider.unregister();
        GCViewProvider.unregister();
    }

}

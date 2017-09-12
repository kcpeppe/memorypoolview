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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class Collection {
    private final long count;
    private final long totalDuration;
    private final long lastDuration;

    public Collection() {
        this.count = 0;
        this.totalDuration = 0;
        this.lastDuration = 0;
    }

    public Collection(long count, long totalDuration, long lastDuration) {
        this.count = count;
        this.totalDuration = totalDuration;
        this.lastDuration = lastDuration;
    }

    public long getCount() {
        return count;
    }

    public long getTotalDuration() {
        return totalDuration;
    }


    public long getLastDuration() {
        return lastDuration;
    }

    public boolean differFrom(Long collectionTime, Long collectionCount, Long lastDuration) {
        return collectionTime != null && collectionCount != null && lastDuration != null;
    }

    public static String formatNumber(long number) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(new Locale("en_US"));
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        return formatter.format(number);
    }

    public static long calculateDeltaBetween(Collection before, Collection after) {
        return (after.lastDuration - before.lastDuration) / 2;
    }
}

package com.kodewerk.visualvm.memorypoolview.gc.arithmetic;

import java.util.Arrays;

public class Analytics {
    
    private final int period;
    private final long samples[];

    private long sum = 0;
    private int currentIndex;

    public Analytics(int period) {
        this.period = period;
        this.samples = new long[period];
        this.currentIndex = period - 1;

        Arrays.fill(this.samples, 0);
    }
    
    public long getSampleAt( int index) {
        return this.samples[(index + period) % period];
    }

    public void add(long newSample) {
        this.sum = this.sum - this.samples[currentIndex] + newSample;
        currentIndex = (currentIndex + 1) % period;
        this.samples[this.currentIndex] = newSample;
    }
    
    public double getSimpleMomentum() {
        long divisor = getSampleAt(currentIndex - 1);
        if ( divisor == 0) return 0.0d;
        return ((double)getSampleAt( currentIndex) / (double)divisor) * 100.0d;
    }

    public long getMovingAverage() {
        return sum / period;
    }
}

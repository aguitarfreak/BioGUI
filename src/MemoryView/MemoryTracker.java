/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MemoryView;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

/**
 *
 * @author ahwan
 */
public class MemoryTracker extends java.util.TimerTask{
    final long inKb = 1024L;
    
    LongProperty totalMemory = new SimpleLongProperty(0);
    LongProperty freeMemory = new SimpleLongProperty(0);
    LongProperty maxMemory = new SimpleLongProperty(0);
    LongProperty usedMemory = new SimpleLongProperty(0);
    Runtime rt = Runtime.getRuntime();

    @Override
    public void run() {

        totalMemory.set(rt.totalMemory()/inKb);
        freeMemory.set(rt.freeMemory()/inKb);
        maxMemory.set(rt.maxMemory()/inKb);
        usedMemory.set(totalMemory.get()-freeMemory.get());
        
        
        //System.out.println("T = " + totalMemory + ", F = " + freeMemory + ", M = " + maxMemory + ", U = " + usedMemory);
    }
    
}

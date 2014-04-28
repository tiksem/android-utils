package com.utilsframework.android.events;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * User: Tikhonenko.S
 * Date: 29.11.13
 * Time: 14:07
 */
public class PostExecutor {
    private Set<Runnable> runnables = new LinkedHashSet<Runnable>();

    public void runPost(Runnable runnable){
        runnables.add(runnable);
    }

    public void removeCallback(Runnable runnable){
        runnables.remove(runnable);
    }

    public void processQueue(){
        for(Runnable runnable : runnables){
            runnable.run();
        }
        
        runnables.clear();
    }
}

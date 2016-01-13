package com.utilsframework.android.view;

import android.view.View;

import java.util.*;

/**
 * User: Tikhonenko.S
 * Date: 16.12.13
 * Time: 17:05
 */
public class ViewDisablingClickManager {
    private Set<View> views;
    private List<View> viewsToEnable;

    public ViewDisablingClickManager(Collection<View> views) {
        this.views = new HashSet<View>(views);
    }

    public ViewDisablingClickManager(View... views){
        this(Arrays.asList(views));
    }

    public void enableViews(){
        if(viewsToEnable == null){
            return;
        }

        for(View view : viewsToEnable){
            if(view.isEnabled()){
                throw new RuntimeException("view has been enabled by another object");
            }

            view.setEnabled(true);
        }

        viewsToEnable = null;
    }

    public void setViewEnabled(View view, boolean value){
        if(!views.contains(view)){
            throw new NoSuchElementException("view has not been registered in ViewDisablingClickManager");
        }

        if(value){
            if(viewsToEnable == null || !viewsToEnable.contains(view)){
                view.setEnabled(value);
            }
        } else {
            if (viewsToEnable != null) {
                viewsToEnable.remove(view);
            }
            view.setEnabled(value);
        }
    }

    public void disableViews(){
        if(viewsToEnable != null){
            return;
        }

        viewsToEnable = new ArrayList<View>();

        for(View view : views){
            if(view.isEnabled()){
                viewsToEnable.add(view);
                view.setEnabled(false);
            }
        }
    }
}


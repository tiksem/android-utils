package com.utilsframework.android;

/**
 * User: Tikhonenko.S
 * Date: 31.03.14
 * Time: 14:44
 */
public interface Pauseable {
    public void pause();
    public void resume();
    public boolean canPause();
    public boolean isPaused();
}

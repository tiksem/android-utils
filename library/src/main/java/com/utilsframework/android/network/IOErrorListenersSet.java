package com.utilsframework.android.network;

import com.utils.framework.io.IOExceptionListener;
import com.utilsframework.android.IOErrorListener;

/**
 * Created by CM on 6/21/2015.
 */
public interface IOErrorListenersSet {
    void addIOErrorListener(IOErrorListener listener);
    void removeIOErrorListener(IOErrorListener listener);
}

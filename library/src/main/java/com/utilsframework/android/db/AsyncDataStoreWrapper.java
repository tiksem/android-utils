package com.utilsframework.android.db;

import android.os.Handler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: Tikhonenko.S
 * Date: 19.12.13
 * Time: 18:36
 */
public class AsyncDataStoreWrapper<T> implements AsyncDataStore<T> {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private DataStore<T> dataStore;
    private Handler handler = new Handler();

    public AsyncDataStoreWrapper(DataStore<T> dataStore) {
        this.dataStore = dataStore;
    }

    private <Result> void executeOnResult(final OnResult<Result> onResult, final Result result){
        handler.post(new Runnable() {
            @Override
            public void run() {
                onResult.onResult(result);
            }
        });
    }

    private void executeOnFinish(final OnFinish onFinish){
        if (onFinish != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onFinish.onFinish();
                }
            });
        }
    }

    @Override
    public void getElementByField(final String fieldName, final Object value, final OnResult<T> onResult) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                T elementByField = dataStore.getElementByField(fieldName, value);
                executeOnResult(onResult, elementByField);
            }
        });
    }

    @Override
    public void getElementsByField(final String fieldName, final Object value, final OnResult<List<T>> onResult) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<T> elementsByField = dataStore.getElementsByField(fieldName, value);
                executeOnResult(onResult, elementsByField);
            }
        });
    }

    @Override
    public void getElements(final OnResult<List<T>> onResult) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                executeOnResult(onResult, dataStore.getElements());
            }
        });
    }

    @Override
    public void getElementById(final Object id, final OnResult<T> onResult) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                executeOnResult(onResult, dataStore.getElementById(id));
            }
        });
    }

    @Override
    public void addOrReplace(final T object, final OnFinish onFinish) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dataStore.addOrReplace(object);
                executeOnFinish(onFinish);
            }
        });
    }

    @Override
    public void add(final T object, final OnFinish onFinish) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dataStore.add(object);
                executeOnFinish(onFinish);
            }
        });
    }

    @Override
    public void remove(final T object, final OnFinish onFinish) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dataStore.remove(object);
                executeOnFinish(onFinish);
            }
        });
    }

    @Override
    public void removeObjectWithId(final Object id, final OnFinish onFinish) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dataStore.removeObjectWithId(id);
                executeOnFinish(onFinish);
            }
        });
    }

    @Override
    public void removeObjectsByField(final String fieldName, final Object fieldValue, final OnFinish onFinish) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dataStore.removeObjectsByField(fieldName, fieldValue);
                executeOnFinish(onFinish);
            }
        });
    }

    @Override
    public void clear(final OnFinish onFinish) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dataStore.clear();
                executeOnFinish(onFinish);
            }
        });
    }
}

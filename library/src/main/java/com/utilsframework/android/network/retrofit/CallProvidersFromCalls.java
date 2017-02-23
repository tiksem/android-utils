package com.utilsframework.android.network.retrofit;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by stikhonenko on 2/23/17.
 */
public class CallProvidersFromCalls {
    public static List<CallProvider> create(final Call... calls) {
        return new AbstractList<CallProvider>() {
            @Override
            public CallProvider get(final int location) {
                return new CallProvider() {
                    @Override
                    public Call getCall() {
                        return calls[location];
                    }
                };
            }

            @Override
            public int size() {
                return calls.length;
            }
        };
    }
}

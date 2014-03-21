package com.dbbest.android.patterns;

/**
 * User: Tikhonenko.S
 * Date: 19.03.14
 * Time: 18:54
 */
public class NoSuchStateException extends RuntimeException{
    public NoSuchStateException(int stateId){
        super("Could not find state with " + stateId + " id");
    }
}

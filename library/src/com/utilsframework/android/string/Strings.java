package com.utilsframework.android.string;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tikhonenko.S
 * Date: 21.10.13
 * Time: 12:48
 * To change this template use File | Settings | File Templates.
 */
public class Strings {
    public static int length(List<CharSequence> sequences){
        int size = 0;
        for(CharSequence charSequence : sequences){
            size += charSequence.length();
        }
        return size;
    }

    public static int length(CharSequence[] sequences){
        return length(Arrays.asList(sequences));
    }

    public static StringBuilder join(CharSequence separator, List<CharSequence> parts){
        int size = length(parts) + separator.length() * parts.size() - 1;
        StringBuilder result = new StringBuilder(size);
        int index = 0;
        for(CharSequence part : parts){
            result.append(part);
            index++;
            if(index != parts.size()){
                result.append(separator);
            }
        }

        return result;
    }

    public static StringBuilder join(CharSequence separator, CharSequence[] parts){
        return join(separator, Arrays.asList(parts));
    }
}

package com.utilsframework.android.string;

import com.utils.framework.Reflection;
import com.utils.framework.strings.Capitalizer;
import com.utils.framework.strings.FirstLetterLowerCaseMaker;
import com.utils.framework.strings.TransformingString;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.ArrayList;
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

    public static StringBuilder joinObjects(CharSequence separator, final List<Object> parts){
        return join(separator, new AbstractList<CharSequence>() {
            @Override
            public CharSequence get(int location) {
                return parts.get(location).toString();
            }

            @Override
            public int size() {
                return parts.size();
            }
        });
    }

    public static StringBuilder joinObjects(CharSequence separator, final Object... parts){
        return joinObjects(separator, Arrays.asList(parts));
    }

    public static StringBuilder join(CharSequence separator, CharSequence[] parts){
        return join(separator, Arrays.asList(parts));
    }

    public static String joinObjectFields(Object object, String separator){
        return joinObjects(separator, Reflection.objectToPropertiesArray(object)).toString();
    }

    public static List<String> getObjectFieldValuesAsStringList(Object object){
        List<Field> fields = Reflection.getAllFields(object);
        List<String> result = new ArrayList<String>(fields.size());
        for(Field field : fields){
            Object value = Reflection.getValueOfField(object, field);
            result.add(value.toString());
        }

        return result;
    }

    public static CharSequence capitalize(CharSequence charSequence){
        return new TransformingString(charSequence, new Capitalizer());
    }

    public static CharSequence makeFirstLetterLowerCase(CharSequence charSequence){
        return new TransformingString(charSequence, new FirstLetterLowerCaseMaker());
    }

    public static String replaceAllIfNotSuccessNull(String replacement, String from, String replaceTo,
                                                    boolean ignoreSpaces){
        if(from.contains(replacement)){
            return from.replace(replacement, replaceTo);
        } else if(ignoreSpaces) {
            replacement = replacement.replaceAll(" ", "");
            if(from.contains(replacement)){
                return from.replace(replacement, replaceTo);
            }
        }

        return null;
    }

    public static final String setCharAt(String string, int index, char ch){
        char[] array = string.toCharArray();
        array[index] = ch;
        return array.toString();
    }
}

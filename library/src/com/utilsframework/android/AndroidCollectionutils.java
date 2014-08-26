package com.utilsframework.android;

import android.database.Cursor;
import android.database.MatrixCursor;
import com.utils.framework.*;
import com.utils.framework.collections.SetWithPredicates;
import com.utils.framework.collections.TimSort;

import java.util.*;

/**
 * User: Tikhonenko.S
 * Date: 26.08.14
 * Time: 19:36
 */
public class AndroidCollectionUtils {
    public static int calculateHashCode(Iterable iterable){
        int hashCode = 1;
        Iterator i = iterable.iterator();

        while (i.hasNext()) {
            Object o = i.next();
            hashCode = 31 * hashCode + (o == null ? 0 : o.hashCode());
        }

        return hashCode;
    }

    private static boolean moveIteratorsAndCheckForEquality(Iterator iter1, Iterator iter2){
        while(iter1.hasNext()){
            Object object1 = iter1.next();
            Object object2 = iter2.next();

            if(!object1.equals(object2)){
                return false;
            }
        }

        return true;
    }

    public static boolean containerEquals(Collection a, Collection b){
        if(a.size() != b.size()){
            return false;
        }

        Iterator iter1 = a.iterator();
        Iterator iter2 = b.iterator();

        return moveIteratorsAndCheckForEquality(iter1, iter2);
    }

    public static boolean containerEquals(Iterable a, Iterable b){
        if(a instanceof Collection && b instanceof Collection){
            return containerEquals((Collection)a, (Collection)b);
        }

        Iterator iter1 = a.iterator();
        Iterator iter2 = b.iterator();

        return moveIteratorsAndCheckForEquality(iter1, iter2) && !iter2.hasNext();
    }

    public static <T> List<T> iterableToList(Iterable<T> iterable){
        List list;
        if(iterable instanceof List){
            list = (List)iterable;
        }
        else {
            list = new ArrayList();
            for(Object i : iterable){
                list.add(i);
            }
        }

        return list;
    }

    public static void addCursorDataToList(Cursor cursor, List list){
        if(!cursor.moveToFirst()){
            return;
        }

        do {
            for(int i = 0; i < cursor.getColumnCount(); i++){
                String data = cursor.getString(0);
                list.add(data);
            }
        } while (cursor.moveToNext());
    }

    public static List<String> cursorToList(Cursor cursor){
        List<String> result = new ArrayList<String>();
        addCursorDataToList(cursor, result);
        return result;
    }

    public static Cursor listToCursor(List list){
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id"});
        for(Object i : list){
            cursor.addRow(new String[]{i.toString()});
        }

        return cursor;
    }

    public static <T> List<T> unique(List<T> list, Equals<T> equals, HashCodeProvider<T> hashCodeProvider){
        Set<T> set = new SetWithPredicates<T>(new LinkedHashSet(), equals, hashCodeProvider);
        return new ArrayList<T>(set);
    }

    public static <T> List<T> unique(List<T> list, Equals<T> equals){
        return unique(list, equals, null);
    }

    public static <T> List<T> unique(List<T> list){
        Set<T> set = new LinkedHashSet<T>(list);
        return new ArrayList<T>(set);
    }

    public static <T> void addAllInReverseOrder(Collection<T> destination, List<T> source){
        for(int i = source.size() - 1; i >= 0; i--){
            T object = source.get(i);
            destination.add(object);
        }
    }

    public static <T> Iterator<T> getReverseIterator(final List<T> list){
        return new Iterator<T>() {
            int index = list.size() - 1;
            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            public T next() {
                return list.get(index--);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <T> void addAll(Collection<T> destination, Iterator<T> source){
        while (source.hasNext()) {
            destination.add(source.next());
        }
    }

    public static <T> T getOrCreate(T[] array, int index, T valueToCreate){
        T value = array[index];
        if(value == null){
            array[index] = valueToCreate;
            return valueToCreate;
        }

        return value;
    }

    public static <T extends Collection> int getGeneralSize(Collection<T> collections){
        int result = 0;
        for(T collection : collections){
            result += collection.size();
        }
        return result;
    }

    public static <T> int getGeneralSizeOfArrays(Collection<T[]> arrays){
        int result = 0;
        for(T[] array : arrays){
            result += array.length;
        }
        return result;
    }

    public static <T> List<T> mergeToSingleListSequentially(List<List<T>> lists){
        int size = getGeneralSize(lists);
        List<T> result = new ArrayList<T>(size);
        List<List<T>> listsClone = new ArrayList<List<T>>(lists);
        List<Integer> indexInLists = new ArrayList(Collections.nCopies(listsClone.size(),0));

        for(int i = 0; i < size;){
            int listIndex = i % listsClone.size();
            int indexInList = CollectionUtils.incrementAndGet(indexInLists, listIndex);
            List<T> list = listsClone.get(listIndex);
            if(list == null){
                throw new NullPointerException("list in lists should not be null");
            }

            if(indexInList >= list.size()){
                listsClone.remove(listIndex);
                indexInLists.remove(listIndex);
                continue;
            }

            T object = list.get(indexInList);
            result.add(object);

            i++;
        }

        return result;
    }

    public static <T> void sortUsingSeveralComparators(List<T> list, Comparator<T>... comparators){
        if(comparators.length == 0){
            throw new IllegalArgumentException();
        }

        Collections.sort(list, Comparators.comparatorCombination(comparators));
    }

    public static <T> void sortUsingListItemComparator(T[] list, ListItemComparator<T> comparator){
        TimSort.sort(list, comparator);
    }

    public static <T> void sortUsingListItemComparator(List<T> list, ListItemComparator<T> comparator){
        Object[] a = list.toArray();
        sortUsingListItemComparator(a, (ListItemComparator)comparator);
        ListIterator i = list.listIterator();
        for (int j=0; j<a.length; j++) {
            i.next();
            i.set(a[j]);
        }
    }
}

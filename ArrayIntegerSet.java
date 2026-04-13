/*
 * File: ArrayIntegerSet.java
 * Class: CS 375
 * Author: djskrien
 * Date: February 2018
 * 
 * Updated by mbender September 2024
 * 
 * edited by Alice Myhran, Lucy Barest, and Dean Hickman
 * for CS375 fall 2024
 */

/**
 * This class models a finite set of integers. This implies that
 * duplicate values are not allowed in an ArrayIntegerSet.
 * It uses an array to store the integers of the set.
 */
public class ArrayIntegerSet {
    /** stores the integers of this set. */
    private int[] data;

    /*
     * CLASS INVARIANTS
     * 1. The order of the data in the array is irrelevant. That is, it
     * is okay to move elements around if it allows for a more efficient
     * implementation.
     * 2. The array is always full. That is, the size of the set equals
     * the length of the array.
     */

    /**
     * Time complexity: Theta(1)
     * 
     * This is the time complexity because all it does is initialize, which we know to be constant time. 
     * 
     * Creates a new empty ArrayIntegerSet
     */
    public ArrayIntegerSet() {
        data = new int[0];
    }

    /**
     * Time complexity: Theta(s+n)
     * 
     * where s is the size of "array" and n is the size of the set AFTER we add the elements from the array. 
     * This is the time complexity when s is the input array's size and n is the set after adding the array's elements to it. 
     * The reason this is the runtime is because the addAll() function goes through the whole array (size s).
     * The addAll() can take Theta(n) amount of time worst case, as it may end up going through the entire thing. 
     * Therefore, it is Theta(s+n)
     * 
     * Creates a new ArrayIntegerSet containing the integers in the array.
     * Duplicated values are added only once.
     * It creates an empty ArrayIntegerSet if array is null.
     * 
     * @param array the array of integers to be added to the set.
     */
    public ArrayIntegerSet(int[] array) {
        data = new int[0];
        if (array != null)
            addAll(array);
    }

    /**
     * Time complexity: Theta(n)
     * 
     * This is the complexity, where n is the size of the set, because in this method we use !contains(), which will be linear time.
     * It is linear time because it either is found, or it resizes which takes linear time. 
     * 
     * Adds a new integer to this set.
     * If the value is already in the set, nothing happens.
     * 
     * @param value the integer to be added to the set
     */
    public void add(int value) {
        if (!contains(value)) {
            incrementArraySize();
            data[data.length - 1] = value;
        }
    }

    /**
     * Time complexity: Theta(s*n)
     * 
     * This is where s is the size of the array that is inputted (called "array"), and n is the set's size. 
     * This is the time complexity because it iterates across the input array (s) and calls on add() for each element, which takes theta(n).
     * Therefore combined, it is Theta(s+n)
     * 
     * Adds the integers in an array to this set.
     * If any of the values in the array are already in the set,
     * those values are ignored.
     * If array is null, nothing happens.
     * 
     * @param array the array of integers to be added to the set
     */
    public void addAll(int[] array) {
        if (array == null)
            return;
        for (int x : array) {
            add(x);
        }
    }

    /**
     * Time complexity: Theta(s*n)
     * 
     * Where s is otherSet and n is the size of this set.
     * It is (s*n) because it first needs to convert otherSet to an array, which should take the same runtime as adding from an array, which was what we did in the previous method. 
     * 
     * Adds the values in another ArrayIntegerSet to this set, ignoring
     * duplicates. If otherSet is null, nothing happens.
     * 
     * @param otherSet the ArrayIntegerSet containing the values to be added to
     *                 this set.
     */
    public void addAll(ArrayIntegerSet otherSet) {
        if (otherSet != null)
            addAll(otherSet.toArray());
    }

    /**
     * Time complexity: Theta(n)
     * 
     * This method is linear becaue the remove method uses the indexOf() function, which finds elements in linear runtime. 
     * If it is found, and the array needs to be resized, that is Theta(n).
     * 
     * Deletes an int value from the set.
     * If the value is not in this set, then nothing happens.
     * 
     * @param value the integer to be deleted from this set.
     */
    public void remove(int value) {
        int index = indexOf(value);
        if (index != -1) {
            data[index] = data[data.length - 1];
            decrementArraySize();
        }
    }

    /**
     * Time complexity: Theta(s*n)
     * 
     * where s is otherSet's size, and n is the size of this set. 
     * This is the time complexity becasue we have to iterate through otherSet to call remove on each element. 
     * Since remove() itself already takes Theta(n), when we have this iterating over the whole set, we have Theta(s*n)
     * 
     * Removes all the values in otherSet from this set.
     * If some of the values are not in this set, nothing happens
     * regarding those values.
     * 
     * @param otherSet the ArrayIntegerSet whose values are to be removed
     *                 from this set.
     */
    public void removeAll(ArrayIntegerSet otherSet) {
        removeAll(otherSet.toArray());
    }

    /**
     * Time complexity: Theta(s*n)
     * 
     * where s is the size of input array and n is the size of this set. 
     * This is the time complexity because, same as above, it needs to go over item in the input array and call remove. 
     * 
     * Removes all the values in an integer array from this set.
     * If some of the values are not in this set, nothing happens
     * regarding those values.
     * 
     * @param array the integer array whose values are to be removed
     *              from this set.
     */
    public void removeAll(int[] array) {
        for (int x : array) {
            remove(x);
        }
    }

    /**
     * Time complexity: Theta(n)
     * 
     * This is linear because the indexOf() method searches for each element in an array, which is linear time. 
     * 
     * Determines whether an integer is stored in this set.
     * 
     * @param value the integer to be tested for membership
     * @return true if the value is a member of this set.
     */
    public boolean contains(int value) {
        return (indexOf(value) >= 0);
    }

    /**
     * Time complexity: Theta(s*n)
     * 
     * where s is the size of otherSet and n is the size of this set. 
     * This is the time complexity becasue it checks if the elements are all in otherSet by converting otherSet to an arrya, then calling the contains method.
     * Contains method is linear, and since we have to covert to an array and check every element, it ends up being Theta(s*n)
     * 
     * Determines whether all the integers in another ArrayIntegerSet are in this
     * set. That is, it tells whether the other set is a subset of this set.
     * If otherSet is null, it returns true.
     * 
     * @param otherSet the ArrayIntegerSet whose values are to be tested for
     *                 membership in this set.
     * @return true if this set contains all the elements of otherCollection.
     */
    public boolean containsAll(ArrayIntegerSet otherSet) {
        if (otherSet == null)
            return true;
        else
            return containsAll(otherSet.toArray());
    }

    /**
     * Time complexity: Theta(s*n)
     * 
     * where s is the size of the input array called "array", n is the size of this set
     * This is the time complexity becasue it has to iterate through the input array and call on constains() method for each item.
     * This means the overall complexity has to be Theta(s*n)
     * 
     * Determines whether all the integers in an integer array are in this
     * set.
     * If array is null, it returns true.
     * 
     * @param array the array whose values are to be tested for
     *              membership in this set.
     * @return true if this set contains all the elements of the array.
     */
    public boolean containsAll(int[] array) {
        if (array == null)
            return true;
        for (int x : array) {
            if (!contains(x)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Time complexity: Theta(s*n)
     * 
     * where s is the size of otherSet and n is the size of this set. 
     * This is the time complexity because it has to clone the current set which takes linear time, and then has to add all of the elements from the otherSet.
     * Overall this comes out to be Theta(s*n)
     * 
     * Creates a new ArrayIntegerSet that is the union of this set and another
     * ArrayIntegerSet.
     * If otherSet is null, it returns a clone of this set.
     * 
     * @param otherSet the ArrayIntegerSet to be unioned with this set.
     * @return a new ArrayIntegerSet containing all the elements of this set and
     *         otherSet.
     */
    public ArrayIntegerSet union(ArrayIntegerSet otherSet) {
        ArrayIntegerSet unionSet = (ArrayIntegerSet) this.clone();
        unionSet.addAll(otherSet);
        return unionSet;
    }

    /**
     * Time complexity: Theta(s*n)
     * 
     * where s is the size of otherSet, and n is the size of this set. 
     * This is the time complexity because it iterates through the set and then sees if each element in the set also exists in otherSet. 
     * That means it will take Theta(s*n) amount of time. 
     * 
     * Creates a new ArrayIntegerSet that is the intersection of this set and
     * another ArrayIntegerSet.
     * If otherSet is null, it returns null.
     * 
     * @param otherSet the ArrayIntegerSet to be intersected with this set.
     * @return a new ArrayIntegerSet containing the common elements of this
     *         set and otherSet.
     */
    public ArrayIntegerSet intersection(ArrayIntegerSet otherSet) {
        if (otherSet == null)
            return null;
        ArrayIntegerSet intersectionSet = new ArrayIntegerSet();
        for (int i = 0; i < data.length; i++) {
            if (otherSet.contains(data[i]))
                intersectionSet.add(data[i]);
        }
        return intersectionSet;
    }

    /**
     * Time complexity: Theta(n)
     * 
     * This is linear time because it just copies the elements into a new array, which takes Theta(n) amount of time. 
     * 
     * Creates an array containing the elements of this set.
     * The order of the elements in the array is arbitrary.
     * The length of the array is equal to the size of this set.
     * 
     * @return a new integer array with the elements of this set.
     */
    public int[] toArray() {
        int[] newArray = new int[data.length];
        System.arraycopy(data, 0, newArray, 0, newArray.length);
        return newArray;
    }

    /**
     * Time complexity: Theta(s*n)
     * 
     * where s is the size of otherSet and n is the size of this set
     * This is the time complexity becasue it calls on the containsAll() function twice. 
     * 
     * Determines the equality of two ArrayIntegerSets, where equality
     * means having exactly the same integers.
     * 
     * @param otherSet the ArrayIntegerSet to be tested against this set.
     * @return true if this set and otherSet both have exactly the
     *         same elements.
     */
    public boolean equals(ArrayIntegerSet otherSet) {
        if (otherSet == null)
            return false;
        else
            return this.containsAll(otherSet) && otherSet.containsAll(this);
    }

    /**
     * Time complexity: Theta(n*s)
     * 
     * where n is the size of this set and s is the size of otherSet
     * This is the time complexity because the method does a very similar thing to the previous method, and takes up the same amount of time.
     * Therefore, it is Theta(n*s)
     * 
     * Determines the equality of two ArrayIntegerSets, where equality
     * means having exactly the same integers.
     * 
     * @param otherSet the Object to be tested against this set.
     * @return true if otherSet is an ArrayIntegerSet and
     *         this set and otherSet both have exactly the
     *         same elements.
     */
    public boolean equals(Object otherSet) {
        if (otherSet == null || !(otherSet instanceof ArrayIntegerSet))
            return false;
        return equals((ArrayIntegerSet) otherSet);
    }

    /**
     * Time complexity: Theta(1)
     * 
     * This method just checks to see if the length of the array is 0, which is constant (it either is or isn't 0, only need to check 1 thing)
     * 
     * Determines whether this set has any elements.
     * 
     * @return true if this set has at least one integer.
     */
    public boolean isEmpty() {
        return data.length == 0;
    }

    /**
     * Time complexity: Theta(1)
     * 
     * This is constant runtime because it just sets the array data to an empty array. Doing this is always constant time.
     * 
     * Removes all integers from this set.
     */
    public void clear() {
        data = new int[0];
    }

    /**
     * Time complexity: Theta(1)
     * 
     * This is constant time because it just returns the length of the array, which is always just constant time. 
     * 
     * @return the number of elements in this set.
     */
    public int size() {
        return data.length;
    }

    /**
     * Time complexity: Theta(n)
     * 
     * This is linear because it creates a new arrayIntegerSet, which has a linear complexity due to the intersection() being used. 
     * 
     * @return a new ArrayIntegerSet containing the same elements as this set.
     */
    public Object clone() {
        return this.intersection(this);
    }

    /**
     * Time complexity: Theta(n)
     * 
     * This is linear becasue it it iterates through the whole array to make the string, which takes linear time. 
     * 
     * Creates a string containing all elements of this set
     * surrounded by braces. For example, if the
     * set stores the integers 1, 2, and 3, the string would be
     * "{1, 2, 3}" or a similar string with the integers in a different
     * order. The order in which the elements are listed is irrelevant.
     */
    public String toString() {
        String result = "{";
        for (int i = 0; i < data.length - 1; i++) {
            result += data[i] + ", ";
        }
        if (data.length > 0)
            result += data[data.length - 1];
        result += "}";
        return result;
    }

    // ------- private auxiliary methods ---------
    /**
     * Time complexity: Theta(n)
     * 
     * This is linear time because it has to iterate through the entire data array to find the element it is indexing. 
     * 
     * Determines whether an integer is in the data array
     * 
     * @param value the integer to be tested for inclusion
     * @return the index of the value in the array. If the value is not in
     *         the array, then -1 is returned.
     */
    private int indexOf(int value) {
        for (int i = 0; i < data.length; i++) {
            if (value == data[i])
                return i;
        }
        return -1;
    }

    /**
     * Time complexity: Theta(n)
     * 
     * This is linear time because it creates a new array, which is one array longer than the data array. 
     * Then, it copies over the contents of the data array, which means this method is linear in time complexity. 
     * 
     * Replaces the data array with a new array one size larger, using the same
     * data in the same slots and with a 0 in the new slot.
     */
    private void incrementArraySize() {
        int[] newData = new int[data.length + 1];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    /**
     * Time complexity: Theta(n)
     * 
     * This is linear because it creates a new array that is one element smaller than the data array. It also copies all of the elements from data into the new smaller array, taking linear time. 
     * 
     * Replaces the data array with a new array one size smaller, using the same
     * data in the same slots except the last slot of the old array, which is lost.
     */
    private void decrementArraySize() {
        int[] newData = new int[data.length - 1];
        System.arraycopy(data, 0, newData, 0, data.length - 1);
        data = newData;
    }

    /**
     * A non-exhaustive unit test
     * 
     * @param args unused
     */
    public static void main(String[] args) {
        ArrayIntegerSet set1 = new ArrayIntegerSet();
        System.out.println("set1: " + set1);
        int[] array = { 1, 2, 3 };
        ArrayIntegerSet set2 = new ArrayIntegerSet(array);
        System.out.println("set2: " + set2);
        set1.add(3);
        System.out.println("set1 after adding 3: " + set1);
        set1.addAll(set1);
        System.out.println("set1 after adding set1: " + set1);
        set1.addAll(set2);
        System.out.println("set1 after adding set2: " + set1);
        set1.clear();
        System.out.println("set 1 after clearing: " + set1);
        set1 = (ArrayIntegerSet) set2.clone();
        System.out.println("set 1 as clone of set2: " + set1);
        System.out.println("set1.contains(3): " + set1.contains(3));
        System.out.println("set1.contains(4): " + set1.contains(4));
        System.out.println("set1.containsAll(set2): " + set1.containsAll(set2));
        System.out.println("set1.equals(set2): " + set1.equals(set2));
        System.out.println("set1.equals(new ArrayIntegerSet()): " +
                set1.equals(new ArrayIntegerSet()));
        System.out.println("index of 3 in set1: " + set1.indexOf(3));
        System.out.println("index of 4 in set1: " + set1.indexOf(4));
        System.out.println("size of set1: " + set1.size());
        System.out.println("set1 union set2:" + set1.union(set2));
        set1.remove(4);
        System.out.println("set1 after removing 4: " + set1);
        set1.remove(2);
        System.out.println("set1 after removing 2: " + set1);
        set2.removeAll(set1);
        System.out.println("set2 after removing all of set1: " + set2);
    }

}
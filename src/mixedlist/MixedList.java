package mixedlist;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class MixedList<E> implements List<E> {
    static final int MAXIMUM_ARRAY_LENGTH = 1 << 16;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 6;

    private Node<E>[] array;
    private Node<E> head;
    private Node<E> tail;
    private int size;
    private int length;

    public MixedList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public MixedList(int initialCapacity) {
        if (initialCapacity < 0) throw new IllegalArgumentException("Illegal initial length: " + initialCapacity);
        length = 8;
        while (length * length < initialCapacity) {
            length = length * 2;
            if (length >= MAXIMUM_ARRAY_LENGTH) break;
        }
        array = (Node<E>[]) new Node[length];
    }




    /**
     * Returns the number of elements in this list.  If this list contains
     * more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if this list contains no elements.
     *
     * @return {@code true} if this list contains no elements
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns {@code true} if this list contains the specified element.
     * More formally, returns {@code true} if and only if this list contains
     * at least one element {@code e} such that
     * {@code Objects.equals(o, e)}.
     *
     * @param o element whose presence in this list is to be tested
     * @return {@code true} if this list contains the specified element
     * @throws ClassCastException   if the type of the specified element
     *                              is incompatible with this list
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              list does not permit null elements
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new MixedListIterator(0);
    }

    /**
     * Returns an array containing all of the elements in this list in proper
     * sequence (from first to last element).
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this list.  (In other words, this method must
     * allocate a new array even if this list is backed by an array).
     * The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this list in proper
     * sequence
     * @see Arrays#asList(Object[])
     */
    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        Node<E> x = head;
        for (int i = 0; i < size; i++) {
            res[i] = x.value;
            x = x.next;
        }
        return res;
    }

    /**
     * Returns an array containing all of the elements in this list in
     * proper sequence (from first to last element); the runtime type of
     * the returned array is that of the specified array.  If the list fits
     * in the specified array, it is returned therein.  Otherwise, a new
     * array is allocated with the runtime type of the specified array and
     * the size of this list.
     *
     * <p>If the list fits in the specified array with room to spare (i.e.,
     * the array has more elements than the list), the element in the array
     * immediately following the end of the list is set to {@code null}.
     * (This is useful in determining the length of the list <i>only</i> if
     * the caller knows that the list does not contain any null elements.)
     *
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     *
     * <p>Suppose {@code x} is a list known to contain only strings.
     * The following code can be used to dump the list into a newly
     * allocated array of {@code String}:
     *
     * <pre>{@code
     *     String[] y = x.toArray(new String[0]);
     * }</pre>
     * <p>
     * Note that {@code toArray(new Object[0])} is identical in function to
     * {@code toArray()}.
     *
     * @param a the array into which the elements of this list are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the elements of this list
     * @throws ArrayStoreException  if the runtime type of the specified array
     *                              is not a supertype of the runtime type of every element in
     *                              this list
     * @throws NullPointerException if the specified array is null
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    /**
     * Appends the specified element to the end of this list (optional
     * operation).
     *
     * <p>Lists that support this operation may place limitations on what
     * elements may be added to this list.  In particular, some
     * lists will refuse to add null elements, and others will impose
     * restrictions on the type of elements that may be added.  List
     * classes should clearly specify in their documentation any restrictions
     * on what elements may be added.
     *
     * @param element element to be appended to this list
     * @return {@code true} (as specified by {@link Collection#add})
     * @throws UnsupportedOperationException if the {@code add} operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of the specified element
     *                                       prevents it from being added to this list
     * @throws NullPointerException          if the specified element is null and this
     *                                       list does not permit null elements
     * @throws IllegalArgumentException      if some property of this element
     *                                       prevents it from being added to this list
     */
    @Override
    public boolean add(E element) {
        if (size >= length * length) resizeUp();
        Node<E> newNode = new Node<>(null, element, null);
        if (size == 0) {
            array[0] = tail = head = newNode;
            size++;
            return true;
        }
        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
        int row = size / length;
        int col = size % length;
        if (col == 0) array[row] = newNode;
        size++;
        return true;
    }

    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present (optional operation).  If this list does not contain
     * the element, it is unchanged.  More formally, removes the element with
     * the lowest index {@code i} such that
     * {@code Objects.equals(o, get(i))}
     * (if such an element exists).  Returns {@code true} if this list
     * contained the specified element (or equivalently, if this list changed
     * as a result of the call).
     *
     * @param o element to be removed from this list, if present
     * @return {@code true} if this list contained the specified element
     * @throws ClassCastException            if the type of the specified element
     *                                       is incompatible with this list
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified element is null and this
     *                                       list does not permit null elements
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws UnsupportedOperationException if the {@code remove} operation
     *                                       is not supported by this list
     */
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index < 0) return false;
        E removed = remove(index);
        return (removed != null);
    }

    /**
     * Returns {@code true} if this list contains all of the elements of the
     * specified collection.
     *
     * @param c collection to be checked for containment in this list
     * @return {@code true} if this list contains all of the elements of the
     * specified collection
     * @throws ClassCastException   if the types of one or more elements
     *                              in the specified collection are incompatible with this
     *                              list
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified collection contains one
     *                              or more null elements and this list does not permit null
     *                              elements
     *                              (<a href="Collection.html#optional-restrictions">optional</a>),
     *                              or if the specified collection is null
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the specified
     * collection's iterator (optional operation).  The behavior of this
     * operation is undefined if the specified collection is modified while
     * the operation is in progress.  (Note that this will occur if the
     * specified collection is this list, and it's nonempty.)
     *
     * @param c collection containing elements to be added to this list
     * @return {@code true} if this list changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this list
     * @throws NullPointerException          if the specified collection contains one
     *                                       or more null elements and this list does not permit null
     *                                       elements, or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this list
     * @see #add(Object)
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list at the specified position (optional operation).  Shifts the
     * element currently at that position (if any) and any subsequent
     * elements to the right (increases their indices).  The new elements
     * will appear in this list in the order that they are returned by the
     * specified collection's iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the
     * operation is in progress.  (Note that this will occur if the specified
     * collection is this list, and it's nonempty.)
     *
     * @param index index at which to insert the first element from the
     *              specified collection
     * @param c     collection containing elements to be added to this list
     * @return {@code true} if this list changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this list
     * @throws NullPointerException          if the specified collection contains one
     *                                       or more null elements and this list does not permit null
     *                                       elements, or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this list
     * @throws IndexOutOfBoundsException     if the index is out of range
     *                                       ({@code index < 0 || index > size()})
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    /**
     * Removes from this list all of its elements that are contained in the
     * specified collection (optional operation).
     *
     * @param c collection containing elements to be removed from this list
     * @return {@code true} if this list changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of an element of this list
     *                                       is incompatible with the specified collection
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this list contains a null element and the
     *                                       specified collection does not permit null elements
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    /**
     * Retains only the elements in this list that are contained in the
     * specified collection (optional operation).  In other words, removes
     * from this list all of its elements that are not contained in the
     * specified collection.
     *
     * @param c collection containing elements to be retained in this list
     * @return {@code true} if this list changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of an element of this list
     *                                       is incompatible with the specified collection
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this list contains a null element and the
     *                                       specified collection does not permit null elements
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    /**
     * Replaces each element of this list with the result of applying the
     * operator to that element.  Errors or runtime exceptions thrown by
     * the operator are relayed to the caller.
     *
     * @param operator the operator to apply to each element
     * @throws UnsupportedOperationException if this list is unmodifiable.
     *                                       Implementations may throw this exception if an element
     *                                       cannot be replaced or if, in general, modification is not
     *                                       supported
     * @throws NullPointerException          if the specified operator is null or
     *                                       if the operator result is a null value and this list does
     *                                       not permit null elements
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @implSpec The default implementation is equivalent to, for this {@code list}:
     * <pre>{@code
     *     final ListIterator<E> li = list.listIterator();
     *     while (li.hasNext()) {
     *         li.set(operator.apply(li.next()));
     *     }
     * }</pre>
     * <p>
     * If the list's list-iterator does not support the {@code set} operation
     * then an {@code UnsupportedOperationException} will be thrown when
     * replacing the first element.
     * @since 1.8
     */
    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        List.super.replaceAll(operator);
    }

    /**
     * Sorts this list according to the order induced by the specified
     * {@link Comparator}.  The sort is <i>stable</i>: this method must not
     * reorder equal elements.
     *
     * <p>All elements in this list must be <i>mutually comparable</i> using the
     * specified comparator (that is, {@code c.compare(e1, e2)} must not throw
     * a {@code ClassCastException} for any elements {@code e1} and {@code e2}
     * in the list).
     *
     * <p>If the specified comparator is {@code null} then all elements in this
     * list must implement the {@link Comparable} interface and the elements'
     * {@linkplain Comparable natural ordering} should be used.
     *
     * <p>This list must be modifiable, but need not be resizable.
     *
     * @param c the {@code Comparator} used to compare list elements.
     *          A {@code null} value indicates that the elements'
     *          {@linkplain Comparable natural ordering} should be used
     * @throws ClassCastException            if the list contains elements that are not
     *                                       <i>mutually comparable</i> using the specified comparator
     * @throws UnsupportedOperationException if the list's list-iterator does
     *                                       not support the {@code set} operation
     * @throws IllegalArgumentException      (<a href="Collection.html#optional-restrictions">optional</a>)
     *                                       if the comparator is found to violate the {@link Comparator}
     *                                       contract
     * @implSpec The default implementation obtains an array containing all elements in
     * this list, sorts the array, and iterates over this list resetting each
     * element from the corresponding position in the array. (This avoids the
     * n<sup>2</sup> log(n) performance that would result from attempting
     * to sort a linked list in place.)
     * @implNote This implementation is a stable, adaptive, iterative mergesort that
     * requires far fewer than n lg(n) comparisons when the input array is
     * partially sorted, while offering the performance of a traditional
     * mergesort when the input array is randomly ordered.  If the input array
     * is nearly sorted, the implementation requires approximately n
     * comparisons.  Temporary storage requirements vary from a small constant
     * for nearly sorted input arrays to n/2 object references for randomly
     * ordered input arrays.
     *
     * <p>The implementation takes equal advantage of ascending and
     * descending order in its input array, and can take advantage of
     * ascending and descending order in different parts of the same
     * input array.  It is well-suited to merging two or more sorted arrays:
     * simply concatenate the arrays and sort the resulting array.
     *
     * <p>The implementation was adapted from Tim Peters's list sort for Python
     * (<a href="http://svn.python.org/projects/python/trunk/Objects/listsort.txt">
     * TimSort</a>).  It uses techniques from Peter McIlroy's "Optimistic
     * Sorting and Information Theoretic Complexity", in Proceedings of the
     * Fourth Annual ACM-SIAM Symposium on Discrete Algorithms, pp 467-474,
     * January 1993.
     * @since 1.8
     */
    @Override
    public void sort(Comparator<? super E> c) {
        List.super.sort(c);
    }

    /**
     * Removes all of the elements from this list (optional operation).
     * The list will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *                                       is not supported by this list
     */
    @Override
    public void clear() {
        Node<E> pointer = head;
        while (pointer.next != null) {
            Node<E> next = pointer.next;
            pointer.prev = null;
            pointer.value = null;
            pointer.next = null;
            pointer = next;
        }
        head = tail = null;
        size = 0;
        Arrays.fill(array, null);
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   ({@code index < 0 || index >= size()})
     */
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index " + index + " is out of range");
        return node(index).value;
    }


    private Node<E> node(int index) {
        int row = index / length;
        int col = index % length;
        Node<E> node;
        if (col < length / 2) {
            node = array[row];
            for (int i = 0; i < col; i++) node = node.next;
        } else if ( size - index > length - col ) {
            node = array[row + 1];
            for (int i = length; i > col; i--) node = node.prev;
        } else {
            node = tail;
            for (int i = size - 1; i > index; i--) node = node.prev;
        }
        return node;
    }



    /**
     * Replaces the element at the specified position in this list with the
     * specified element (optional operation).
     *
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws UnsupportedOperationException if the {@code set} operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of the specified element
     *                                       prevents it from being added to this list
     * @throws NullPointerException          if the specified element is null and
     *                                       this list does not permit null elements
     * @throws IllegalArgumentException      if some property of the specified
     *                                       element prevents it from being added to this list
     * @throws IndexOutOfBoundsException     if the index is out of range
     *                                       ({@code index < 0 || index >= size()})
     */
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index " + index + " is out of range");
        Node<E> node = node(index);
        E res = node.value;
        node.value = element;
        return res;
    }

    /**
     * Inserts the specified element at the specified position in this list
     * (optional operation).  Shifts the element currently at that position
     * (if any) and any subsequent elements to the right (adds one to their
     * indices).
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws UnsupportedOperationException if the {@code add} operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of the specified element
     *                                       prevents it from being added to this list
     * @throws NullPointerException          if the specified element is null and
     *                                       this list does not permit null elements
     * @throws IllegalArgumentException      if some property of the specified
     *                                       element prevents it from being added to this list
     * @throws IndexOutOfBoundsException     if the index is out of range
     *                                       ({@code index < 0 || index > size()})
     */
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("index " + index + " is out of range");
        if (size == 0 || index == size) {  add(element);  return;  }
        if (index == 0) {  addFirst(element);  return;  }
        if (size >= length * length) resizeUp();
        Node<E> newNode = new Node<>(null, element, null);
        int row = index / length;
        int col = index % length;
        Node<E> oldNode = array[row];
        for (int i = 0; i < col; i++) oldNode = oldNode.next;
        newNode.prev = oldNode.prev;
        newNode.next = oldNode;
        newNode.prev.next = newNode;
        oldNode.prev = newNode;
        if (col == 0) array[row] = newNode;
        int lastRow = size / length;
        for (int i = row + 1; i <= lastRow; i++) {
            if (array[i] != null) array[i] = array[i].prev;
            else array[i] = tail;
        }
        size++;
    }

    /**
     * Increases length of storage
     * @return {@code true} if length is increased, {@code false} if MAXIMUM_ARRAY_LENGTH is already reached
     */
    private boolean resizeUp() {
        if (length == MAXIMUM_ARRAY_LENGTH) return false;
        length = length * 2;
        Node<E>[] newArray = (Node<E>[]) new Node[length];
        for (int i = 0; i * 2 < array.length; i++) {
            newArray[i] = array[i * 2];
        }
        array = newArray;
        return true;
    }

    /**
     * Removes the element at the specified position in this list (optional
     * operation).  Shifts any subsequent elements to the left (subtracts one
     * from their indices).  Returns the element that was removed from the
     * list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws UnsupportedOperationException if the {@code remove} operation
     *                                       is not supported by this list
     * @throws IndexOutOfBoundsException     if the index is out of range
     *                                       ({@code index < 0 || index >= size()})
     */
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index " + index + " is out of range");
        if (index == 0) return removeFirst();
        if (index == size - 1) return removeLast();
        int row = index / length;
        int col = index % length;
        Node<E> oldNode = array[row];
        for (int i = 0; i < col; i++) oldNode = oldNode.next;
        oldNode.prev.next = oldNode.next;
        oldNode.next.prev = oldNode.prev;
        if (col == 0) array[row] = oldNode.next;
        int lastRow = (size - 1) / length;
        for (int i = row + 1; i <= lastRow; i++) {
            array[i] = array[i].next;
        }
        size--;
        return oldNode.value;
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index {@code i} such that
     * {@code Objects.equals(o, get(i))},
     * or -1 if there is no such index.
     *
     * @param o element to search for
     * @return the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element
     * @throws ClassCastException   if the type of the specified element
     *                              is incompatible with this list
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              list does not permit null elements
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public int indexOf(Object o) {
        Node<E> node = head;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, node.value)) return i;
            node = node.next;
        }
        return -1;
    }

    /**
     * Returns the index of the last occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the highest index {@code i} such that
     * {@code Objects.equals(o, get(i))},
     * or -1 if there is no such index.
     *
     * @param o element to search for
     * @return the index of the last occurrence of the specified element in
     * this list, or -1 if this list does not contain the element
     * @throws ClassCastException   if the type of the specified element
     *                              is incompatible with this list
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              list does not permit null elements
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public int lastIndexOf(Object o) {
        Node<E> node = tail;
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(o, node.value)) return i;
            node = node.prev;
        }
        return -1;
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * @return a list iterator over the elements in this list (in proper
     * sequence)
     */
    @Override
    public ListIterator<E> listIterator() {
        return new MixedListIterator(0);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * The specified index indicates the first element that would be
     * returned by an initial call to {@link ListIterator#next next}.
     * An initial call to {@link ListIterator#previous previous} would
     * return the element with the specified index minus one.
     *
     * @param index index of the first element to be returned from the
     *              list iterator (by a call to {@link ListIterator#next next})
     * @return a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   ({@code index < 0 || index > size()})
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        return new MixedListIterator(index);
    }

    /**
     * Returns a view of the portion of this list between the specified
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.  (If
     * {@code fromIndex} and {@code toIndex} are equal, the returned list is
     * empty.)  The returned list is backed by this list, so non-structural
     * changes in the returned list are reflected in this list, and vice-versa.
     * The returned list supports all of the optional list operations supported
     * by this list.<p>
     * <p>
     * This method eliminates the need for explicit range operations (of
     * the sort that commonly exist for arrays).  Any operation that expects
     * a list can be used as a range operation by passing a subList view
     * instead of a whole list.  For example, the following idiom
     * removes a range of elements from a list:
     * <pre>{@code
     *      list.subList(from, to).clear();
     * }</pre>
     * Similar idioms may be constructed for {@code indexOf} and
     * {@code lastIndexOf}, and all of the algorithms in the
     * {@code Collections} class can be applied to a subList.<p>
     * <p>
     * The semantics of the list returned by this method become undefined if
     * the backing list (i.e., this list) is <i>structurally modified</i> in
     * any way other than via the returned list.  (Structural modifications are
     * those that change the size of this list, or otherwise perturb it in such
     * a fashion that iterations in progress may yield incorrect results.)
     *
     * @param fromIndex low endpoint (inclusive) of the subList
     * @param toIndex   high endpoint (exclusive) of the subList
     * @return a view of the specified range within this list
     * @throws IndexOutOfBoundsException for an illegal endpoint index value
     *                                   ({@code fromIndex < 0 || toIndex > size ||
     *                                   fromIndex > toIndex})
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return List.of();
    }

    /**
     * Creates a {@link Spliterator} over the elements in this list.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#SIZED} and
     * {@link Spliterator#ORDERED}.  Implementations should document the
     * reporting of additional characteristic values.
     *
     * @return a {@code Spliterator} over the elements in this list
     * @implSpec The default implementation creates a
     * <em><a href="Spliterator.html#binding">late-binding</a></em>
     * spliterator as follows:
     * <ul>
     * <li>If the list is an instance of {@link RandomAccess} then the default
     *     implementation creates a spliterator that traverses elements by
     *     invoking the method {@link List#get}.  If such invocation results or
     *     would result in an {@code IndexOutOfBoundsException} then the
     *     spliterator will <em>fail-fast</em> and throw a
     *     {@code ConcurrentModificationException}.
     *     If the list is also an instance of {@link AbstractList} then the
     *     spliterator will use the list's {@link AbstractList#modCount modCount}
     *     field to provide additional <em>fail-fast</em> behavior.
     * <li>Otherwise, the default implementation creates a spliterator from the
     *     list's {@code Iterator}.  The spliterator inherits the
     *     <em>fail-fast</em> of the list's iterator.
     * </ul>
     * @implNote The created {@code Spliterator} additionally reports
     * {@link Spliterator#SUBSIZED}.
     * @since 1.8
     */
    @Override
    public Spliterator<E> spliterator() {
        return List.super.spliterator();
    }

    /**
     * {@inheritDoc}
     *
     * @param element
     * @throws NullPointerException          {@inheritDoc}
     * @throws UnsupportedOperationException {@inheritDoc}
     * @implSpec The implementation in this interface calls {@code add(0, e)}.
     * @since 21
     */
    @Override
    public void addFirst(E element) {
        if (size == 0) {
            add(element);
            return;
        }
        if (size >= length * length) resizeUp();
        Node<E> newNode = new Node<>(null, element, null);
        head.prev = newNode;
        newNode.next = head;
        array[0] = head = newNode;
        int lastRow = size / length;
        for (int i = 1; i <= lastRow; i++) {
            if (array[i] != null) array[i] = array[i].prev;
            else array[i] = tail;
        }
        size++;
    }

    /**
     * {@inheritDoc}
     *
     * @param element
     * @throws NullPointerException          {@inheritDoc}
     * @throws UnsupportedOperationException {@inheritDoc}
     * @implSpec The implementation in this interface calls {@code add(element)}.
     * @since 21
     */
    @Override
    public void addLast(E element) {
        add(element);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException {@inheritDoc}
     * @implSpec If this List is not empty, the implementation in this interface returns the result
     * of calling {@code get(0)}. Otherwise, it throws {@code NoSuchElementException}.
     * @since 21
     */
    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException("Calling first element of empty List");
        return head.value;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException {@inheritDoc}
     * @implSpec If this List is not empty, the implementation in this interface returns the result
     * of calling {@code get(size() - 1)}. Otherwise, it throws {@code NoSuchElementException}.
     * @since 21
     */
    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException("Calling last element of empty List");
        return tail.value;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException        {@inheritDoc}
     * @throws UnsupportedOperationException {@inheritDoc}
     * @implSpec If this List is not empty, the implementation in this interface returns the result
     * of calling {@code remove(0)}. Otherwise, it throws {@code NoSuchElementException}.
     * @since 21
     */
    @Override
    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException("Removing first element of empty List");
        E value = head.value;
        if (size == 1) {
            array[0] = head = tail = null;
            size = 0;
            return value;
        }
        head = head.next;
        head.prev = null;
        int lastRow = (size - 1) / length;
        for (int i = 0; i <= lastRow; i++) {
            array[i] = array[i].next;
        }
        size--;
        return value;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException        {@inheritDoc}
     * @throws UnsupportedOperationException {@inheritDoc}
     * @implSpec If this List is not empty, the implementation in this interface returns the result
     * of calling {@code remove(size() - 1)}. Otherwise, it throws {@code NoSuchElementException}.
     * @since 21
     */
    @Override
    public E removeLast() {
        if (size == 0) throw new NoSuchElementException("Removing last element of empty List");
        E value = tail.value;
        if (size == 1) {
            array[0] = head = tail = null;
            size = 0;
            return value;
        }
        tail = tail.prev;
        tail.next = null;
        int row = (size - 1) / length;
        int col = (size - 1) % length;
        if (col == 0) array[row] = null;
        size--;
        return value;
    }

    /**
     * {@inheritDoc}
     *
     * @return a reverse-ordered view of this collection, as a {@code List}
     * @implSpec The implementation in this interface returns a reverse-ordered List
     * view. The {@code reversed()} method of the view returns a reference
     * to this List. Other operations on the view are implemented via calls to
     * public methods on this List. The exact relationship between calls on the
     * view and calls on this List is unspecified. However, order-sensitive
     * operations generally delegate to the appropriate method with the opposite
     * orientation. For example, calling {@code getFirst} on the view results in
     * a call to {@code getLast} on this List.
     * @since 21
     */
    @Override
    public List<E> reversed() {
        return List.super.reversed();
    }






    /**
     * Basic chain node
     */
    private static class Node<E> {
        Node<E> prev;
        E value;
        Node<E> next;

        private Node(Node<E> prev, E value, Node<E> next) {
            this.prev = prev;
            this.value = value;
            this.next = next;
        }

        public final E getValue() {         return value;        }

        public final void setValue(E value) {        this.value = value;       }

        @Override
        public String toString() {    return value.toString();     }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {      return Objects.hash(value);     }
    }






    private class MixedListIterator implements ListIterator<E> {
        private int index;
        private int returned = -1;
        private Node<E> curNode;
        private Node<E> retNode;

        MixedListIterator(int index) {
            this.index = index;
            if (index < size) curNode = node(index);
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public E next() {
            if (index >= size) throw new NoSuchElementException();
            returned = index;  retNode = curNode;
            index++;  curNode = curNode.next;
            return retNode.value;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public E previous() {
            if (index <= 0) throw new NoSuchElementException();
            index--;  curNode = (curNode == null) ? tail : curNode.prev;
            returned = index;  retNode = curNode;
            return retNode.value;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            if (returned < 0) throw new IllegalStateException();
            MixedList.this.remove(returned);
            if (index > 0 && returned != index) index--;
            curNode = node(index);
            returned = -1;  retNode = null;
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            while (hasNext()) {
                action.accept(next());
            }
        }

        @Override
        public void set(E e) {
            if (returned < 0) throw new IllegalStateException();
            retNode.value = e;
        }

        @Override
        public void add(E e) {
            MixedList.this.add(index, e);
            index++;
            returned = -1; retNode = null;
        }
    }



}

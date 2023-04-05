package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node.  Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E>
{
    public static void main(String[] args) {
        StoutList<String> s = new StoutList();
        s.add("a");
        s.add("b");
        s.add("c");
        s.add("d");

        StoutList.NodeInfo n = s.find(3);
        StoutList.Node tempNode = n.node;
        tempNode.addItem("r");

        n = s.find(0);
        tempNode = n.node;
        tempNode.addItem("x");
        tempNode.addItem("y");
        tempNode.addItem("z");

        ListIterator<String> iter = s.listIterator();
        System.out.println(s.toStringInternal(iter));
        iter.next();
        System.out.println(s.toStringInternal(iter));
    }


  /**
   * Default number of elements that may be stored in each node.
   */
  private static final int DEFAULT_NODESIZE = 4;
  
  /**
   * Number of elements that can be stored in each node.
   */
  private final int nodeSize;
  
  /**
   * Dummy node for head.  It should be private but set to public here only  
   * for grading purpose.  In practice, you should always make the head of a 
   * linked list a private instance variable.  
   */
  public Node head;
  //test
  
  /**
   * Dummy node for tail.
   */
  private Node tail;
  
  /**
   * Number of elements in the list.
   */
  private int size;
  
  /**
   * Constructs an empty list with the default node size.
   */
  public StoutList()
  {
    this(DEFAULT_NODESIZE);
  }

  /**
   * Constructs an empty list with the given node size.
   * @param nodeSize number of elements that may be stored in each node, must be 
   *   an even number
   */
  public StoutList(int nodeSize)
  {
    if (nodeSize <= 0 || nodeSize % 2 != 0) throw new IllegalArgumentException();
    
    // dummy nodes
    head = new Node();
    tail = new Node();
    head.next = tail;
    tail.previous = head;
    this.nodeSize = nodeSize;
  }
  
  /**
   * Constructor for grading only.  Fully implemented. 
   * @param head
   * @param tail
   * @param nodeSize
   * @param size
   */
  public StoutList(Node head, Node tail, int nodeSize, int size)
  {
	  this.head = head; 
	  this.tail = tail; 
	  this.nodeSize = nodeSize; 
	  this.size = size; 
  }

  @Override
  public int size()
  {
    return size;
  }

    /**
     * Inserts newNode into the list, doesn't update the size.
     * @param current -
     * @param newNode -
     */
    public void link(Node current, Node newNode) {
        if (current != null && newNode != null) {
            newNode.previous = current;
            newNode.next = current.next;
            current.next.previous = newNode;
            current.next = newNode;
        }
    }


    /**
     * removes current from the list without updating the size.
     * @param current
     */
    public void unlink(Node current) {
        current.previous.next = current.next;
        current.next.previous = current.previous;
    }
  
  @Override
  public boolean add(E item) {
        if (item == null) {
        throw new NullPointerException();
    }
        Node temp = new Node();
        temp.addItem(item);
        link(tail.previous, temp);
        size++;
        return true;
  }

  private Node findNodeByIndex(int pos) {
        if (pos == -1) {
            return head;
        }
        if (pos == size) {
            return tail;
        }
        Node current = head.next;
        int counter = 0;
        while (counter < pos) {
            current = current.next;
            counter++;
        }
        return current.next;
  }

  @Override
  public void add(int pos, E item)
  {
      if (item == null) {
          throw new NullPointerException();
      }
      if (pos > 0 || pos < size) {
          throw new IndexOutOfBoundsException("" + pos);
      }
      Node temp = new Node();
      temp.addItem(item);
      Node predecessor = findNodeByIndex(pos-1);
      link(predecessor, temp);
      size++;
  }

  @Override
  public E remove(int pos) {
        Node temp = findNodeByIndex(pos);
        unlink(temp);
        size--;
        return temp.data[0];
  }

  /**
   * Sort all elements in the stout list in the NON-DECREASING order. You may do the following. 
   * Traverse the list and copy its elements into an array, deleting every visited node along 
   * the way.  Then, sort the array by calling the insertionSort() method.  (Note that sorting 
   * efficiency is not a concern for this project.)  Finally, copy all elements from the array 
   * back to the stout list, creating new nodes for storage. After sorting, all nodes but 
   * (possibly) the last one must be full of elements.  
   *  
   * Comparator<E> must have been implemented for calling insertionSort().    
   */
  public void sort()
  {
	  // TODO 
  }
  
  /**
   * Sort all elements in the stout list in the NON-INCREASING order. Call the bubbleSort()
   * method.  After sorting, all but (possibly) the last nodes must be filled with elements.  
   *  
   * Comparable<? super E> must be implemented for calling bubbleSort(). 
   */
  public void sortReverse() 
  {
	  // TODO 
  }
  
  @Override
  public Iterator<E> iterator() {
      return new StoutListIterator();
  }

  @Override
  public ListIterator<E> listIterator() {
      return new StoutListIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index) {
      return new StoutListIterator(index);
  }
  
  /**
   * Returns a string representation of this list showing
   * the internal structure of the nodes.
   */
  public String toStringInternal()
  {
    return toStringInternal(null);
  }

  /**
   * Returns a string representation of this list showing the internal
   * structure of the nodes and the position of the iterator.
   *
   * @param iter
   *            an iterator for this list
   */
  public String toStringInternal(ListIterator<E> iter) 
  {
      int count = 0;
      int position = -1;
      if (iter != null) {
          position = iter.nextIndex();
      }

      StringBuilder sb = new StringBuilder();
      sb.append('[');
      Node current = head.next;
      while (current != tail) {
          sb.append('(');
          E data = current.data[0];
          if (data == null) {
              sb.append("-");
          } else {
              if (position == count) {
                  sb.append("| ");
                  position = -1;
              }
              sb.append(data.toString());
              ++count;
          }

          for (int i = 1; i < nodeSize; ++i) {
             sb.append(", ");
              data = current.data[i];
              if (data == null) {
                  sb.append("-");
              } else {
                  if (position == count) {
                      sb.append("| ");
                      position = -1;
                  }
                  sb.append(data.toString());
                  ++count;

                  // iterator at end
                  if (position == size && count == size) {
                      sb.append(" |");
                      position = -1;
                  }
             }
          }
          sb.append(')');
          current = current.next;
          if (current != tail)
              sb.append(", ");
      }
      sb.append("]");
      return sb.toString();
  }


  /**
   * Node type for this list.  Each node holds a maximum
   * of nodeSize elements in an array.  Empty slots
   * are null.
   */
  private class Node
  {
    /**
     * Array of actual data elements.
     */
    // Unchecked warning unavoidable.
    public E[] data = (E[]) new Comparable[nodeSize];
    
    /**
     * Link to next node.
     */
    public Node next;
    
    /**
     * Link to previous node;
     */
    public Node previous;
    
    /**
     * Index of the next available offset in this node, also 
     * equal to the number of elements in this node.
     */
    public int count;

    /**
     * Adds an item to this node at the first available offset.
     * Precondition: count < nodeSize
     * @param item element to be added
     */
    void addItem(E item)
    {
      if (count >= nodeSize)
      {
        return;
      }
      data[count++] = item;
      //useful for debugging
//            System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
    }
  
    /**
     * Adds an item to this node at the indicated offset, shifting
     * elements to the right as necessary.
     * 
     * Precondition: count < nodeSize
     * @param offset array index at which to put the new element
     * @param item element to be added
     */
    void addItem(int offset, E item)
    {
      if (count >= nodeSize)
      {
    	  return;
      }
      for (int i = count - 1; i >= offset; --i)
      {
        data[i + 1] = data[i];
      }
      ++count;
      data[offset] = item;
      //useful for debugging 
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
    }

    /**
     * Deletes an element from this node at the indicated offset, 
     * shifting elements left as necessary.
     * Precondition: 0 <= offset < count
     * @param offset
     */
    void removeItem(int offset)
    {
      E item = data[offset];
      for (int i = offset + 1; i < nodeSize; ++i)
      {
        data[i - 1] = data[i];
      }
      data[count - 1] = null;
      --count;
    }    
  }

  private class StoutListIterator implements ListIterator<E>
  {
	// constants you possibly use ...   
	  private static final int BEHIND = -1;
      private static final int AHEAD = 1;
      private static final int NONE = 0;
	// instance variables ...
      private Node cursor;
      private int index;
      private int direction;
	  
    /**
     * Default constructor 
     */
    public StoutListIterator()
    {
    	this(0);
    }

    /**
     * Constructor finds node at a given position.
     * @param pos
     */
    public StoutListIterator(int pos)
    {
    	if (pos < 0 || pos > (size * nodeSize)) {
            throw new IndexOutOfBoundsException("" + pos);
        }
        cursor = find(pos).node;
//        cursor = find(pos).node;
        index = pos;
        direction = NONE;
    }

    @Override
    public boolean hasNext()
    {
    	return index < (size * nodeSize);
    }

    @Override
    public E next()
    {
    	if (!hasNext()) {
            throw new NoSuchElementException();
        }
        E ret = cursor.data[index];
        index++;
//        cursor = cursor.next;
        direction = BEHIND;
        return ret;
    }

      /**
       * Returns {@code true} if this list iterator has more elements when
       * traversing the list in the reverse direction.  (In other words,
       * returns {@code true} if {@link #previous} would return an element
       * rather than throwing an exception.)
       *
       * @return {@code true} if the list iterator has more elements when
       * traversing the list in the reverse direction
       */
      @Override
      public boolean hasPrevious() {
          return false;
      }

      /**
       * Returns the previous element in the list and moves the cursor
       * position backwards.  This method may be called repeatedly to
       * iterate through the list backwards, or intermixed with calls to
       * {@link #next} to go back and forth.  (Note that alternating calls
       * to {@code next} and {@code previous} will return the same
       * element repeatedly.)
       *
       * @return the previous element in the list
       * @throws NoSuchElementException if the iteration has no previous
       *                                element
       */
      @Override
      public E previous() {
          return null;
      }

      /**
       * Returns the index of the element that would be returned by a
       * subsequent call to {@link #next}. (Returns list size if the list
       * iterator is at the end of the list.)
       *
       * @return the index of the element that would be returned by a
       * subsequent call to {@code next}, or list size if the list
       * iterator is at the end of the list
       */
      @Override
      public int nextIndex() {
          return index;
      }

      /**
       * Returns the index of the element that would be returned by a
       * subsequent call to {@link #previous}. (Returns -1 if the list
       * iterator is at the beginning of the list.)
       *
       * @return the index of the element that would be returned by a
       * subsequent call to {@code previous}, or -1 if the list
       * iterator is at the beginning of the list
       */
      @Override
      public int previousIndex() {
          return index-1;
      }

      @Override
    public void remove()
    {
    	// TODO 
    }

      /**
       * Replaces the last element returned by {@link #next} or
       * {@link #previous} with the specified element (optional operation).
       * This call can be made only if neither {@link #remove} nor {@link
       * #add} have been called after the last call to {@code next} or
       * {@code previous}.
       *
       * @param e the element with which to replace the last element returned by
       *          {@code next} or {@code previous}
       * @throws UnsupportedOperationException if the {@code set} operation
       *                                       is not supported by this list iterator
       * @throws ClassCastException            if the class of the specified element
       *                                       prevents it from being added to this list
       * @throws IllegalArgumentException      if some aspect of the specified
       *                                       element prevents it from being added to this list
       * @throws IllegalStateException         if neither {@code next} nor
       *                                       {@code previous} have been called, or {@code remove} or
       *                                       {@code add} have been called after the last call to
       *                                       {@code next} or {@code previous}
       */
      @Override
      public void set(E e) {

      }

      /**
       * Inserts the specified element into the list (optional operation).
       * The element is inserted immediately before the element that
       * would be returned by {@link #next}, if any, and after the element
       * that would be returned by {@link #previous}, if any.  (If the
       * list contains no elements, the new element becomes the sole element
       * on the list.)  The new element is inserted before the implicit
       * cursor: a subsequent call to {@code next} would be unaffected, and a
       * subsequent call to {@code previous} would return the new element.
       * (This call increases by one the value that would be returned by a
       * call to {@code nextIndex} or {@code previousIndex}.)
       *
       * @param e the element to insert
       * @throws UnsupportedOperationException if the {@code add} method is
       *                                       not supported by this list iterator
       * @throws ClassCastException            if the class of the specified element
       *                                       prevents it from being added to this list
       * @throws IllegalArgumentException      if some aspect of this element
       *                                       prevents it from being added to this list
       */
      @Override
      public void add(E e) {

      }

      // Other methods you may want to add or override that could possibly facilitate
    // other operations, for instance, addition, access to the previous element, etc.
    // 
    // ...
    //

  }

  

  /**
   * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order. 
   * @param arr   array storing elements from the list 
   * @param comp  comparator used in sorting 
   */
  private void insertionSort(E[] arr, Comparator<? super E> comp)
  {
	  // TODO
  }
  
  /**
   * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
   * description of bubble sort please refer to Section 6.1 in the project description. 
   * You must use the compareTo() method from an implementation of the Comparable 
   * interface by the class E or ? super E. 
   * @param arr  array holding elements from the list
   */
  private void bubbleSort(E[] arr)
  {
	  // TODO
  }

    private class NodeInfo {
        public Node node;
        public int offset;

        public NodeInfo(Node node, int offset) {
            this.node = node;
            this.offset = offset;
        }
    }

    public NodeInfo find(int pos) {
        if (pos > (size*nodeSize)) {
            throw new IndexOutOfBoundsException();
        }
        Node current = head.next;
        if (pos < current.count) {
            return new NodeInfo(current, pos);
        }
        int counter = current.count;
        while (counter <= pos && current.next != tail) {
            current = current.next;
            counter += current.count;
            if (counter > pos) {
                return new NodeInfo(current, counter-pos);
            }

        }
        return null;
    }



}
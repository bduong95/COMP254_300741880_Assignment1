/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package linkedlists;

/**
 * A basic singly linked list implementation.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class SinglyLinkedList<E> implements Cloneable {
  //---------------- nested Node class ----------------
  /**
   * Node of a singly linked list, which stores a reference to its
   * element and to the subsequent node in the list (or null if this
   * is the last node).
   */
  private static class Node<E> {

    /** The element stored at this node */
    private E element;            // reference to the element stored at this node

    /** A reference to the subsequent node in the list */
    private Node<E> next;         // reference to the subsequent node in the list

    /**
     * Creates a node with the given element and next node.
     *
     * @param e  the element to be stored
     * @param n  reference to a node that should follow the new node
     */
    public Node(E e, Node<E> n) {
      element = e;
      next = n;
    }

    // Accessor methods
    /**
     * Returns the element stored at the node.
     * @return the element stored at the node
     */
    public E getElement() { return element; }

    /**
     * Returns the node that follows this one (or null if no such node).
     * @return the following node
     */
    public Node<E> getNext() { return next; }

    // Modifier methods
    /**
     * Sets the node's next reference to point to Node n.
     * @param n    the node that should follow this one
     */
    public void setNext(Node<E> n) { next = n; }
    
  } //----------- end of nested Node class -----------

  // instance variables of the SinglyLinkedList
  /** The head node of the list */
  private Node<E> head = null;               // head node of the list (or null if empty)

  /** The last node of the list */
  private Node<E> tail = null;               // last node of the list (or null if empty)

  /** Number of nodes in the list */
  private int size = 0;                      // number of nodes in the list

  /** Constructs an initially empty list. */
  public SinglyLinkedList() { }              // constructs an initially empty list

  // access methods
  /**
   * Returns the number of elements in the linked list.
   * @return number of elements in the linked list
   */
  public int size() { return size; }

  /**
   * Tests whether the linked list is empty.
   * @return true if the linked list is empty, false otherwise
   */
  public boolean isEmpty() { return size == 0; }

  /**
   * Returns (but does not remove) the first element of the list
   * @return element at the front of the list (or null if empty)
   */
  public E first() {             // returns (but does not remove) the first element
    if (isEmpty()) return null;
    return head.getElement();
  }

  /**
   * Returns (but does not remove) the last element of the list.
   * @return element at the end of the list (or null if empty)
   */
  public E last() {              // returns (but does not remove) the last element
    if (isEmpty()) return null;
    return tail.getElement();
  }

  // update methods
  /**
   * Adds an element to the front of the list.
   * @param e  the new element to add
   */
  public void addFirst(E e) {                // adds element e to the front of the list
    head = new Node<>(e, head);              // create and link a new node
    if (size == 0)
      tail = head;                           // special case: new node becomes tail also
    size++;
  }

  /**
   * Adds an element to the end of the list.
   * @param e  the new element to add
   */
  public void addLast(E e) {                 // adds element e to the end of the list
    Node<E> newest = new Node<>(e, null);    // node will eventually be the tail
    if (isEmpty())
      head = newest;                         // special case: previously empty list
    else
      tail.setNext(newest);                  // new node after existing tail
    tail = newest;                           // new node becomes the tail
    size++;
  }

  /**
   * Removes and returns the first element of the list.
   * @return the removed element (or null if empty)
   */
  public E removeFirst() {                   // removes and returns the first element
    if (isEmpty()) return null;              // nothing to remove
    E answer = head.getElement();
    head = head.getNext();                   // will become null if list had only one node
    size--;
    if (size == 0)
      tail = null;                           // special case as list is now empty
    return answer;
  }

  @SuppressWarnings({"unchecked"})
  public boolean equals(Object o) {
    if (o == null) return false;
    if (getClass() != o.getClass()) return false;
    SinglyLinkedList other = (SinglyLinkedList) o;   // use nonparameterized type
    if (size != other.size) return false;
    Node walkA = head;                               // traverse the primary list
    Node walkB = other.head;                         // traverse the secondary list
    while (walkA != null) {
      if (!walkA.getElement().equals(walkB.getElement())) return false; //mismatch
      walkA = walkA.getNext();
      walkB = walkB.getNext();
    }
    return true;   // if we reach this, everything matched successfully
  }

  @SuppressWarnings({"unchecked"})
  public SinglyLinkedList<E> clone() throws CloneNotSupportedException {
    // always use inherited Object.clone() to create the initial copy
    SinglyLinkedList<E> other = (SinglyLinkedList<E>) super.clone(); // safe cast
    if (size > 0) {                    // we need independent chain of nodes
      other.head = new Node<>(head.getElement(), null);
      Node<E> walk = head.getNext();      // walk through remainder of original list
      Node<E> otherTail = other.head;     // remember most recently created node
      while (walk != null) {              // make a new node storing same element
        Node<E> newest = new Node<>(walk.getElement(), null);
        otherTail.setNext(newest);     // link previous node to this one
        otherTail = newest;
        walk = walk.getNext();
      }
    }
    return other;
  }

  public int hashCode() {
    int h = 0;
    for (Node walk=head; walk != null; walk = walk.getNext()) {
      h ^= walk.getElement().hashCode();      // bitwise exclusive-or with element's code
      h = (h << 5) | (h >>> 27);              // 5-bit cyclic shift of composite code
    }
    return h;
  }

  /**
   * Produces a string representation of the contents of the list.
   * This exists for debugging purposes only.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder("(");
    Node<E> walk = head;
    while (walk != null) {
      sb.append(walk.getElement());
      if (walk != tail)
        sb.append(", ");
      walk = walk.getNext();
    }
    sb.append(")");
    return sb.toString();
  }
  
  // Assignment 1 - Question 2
  // -----------------------------------------------------------------------------------------------------
  @SuppressWarnings({"unchecked"})
  public void swapNodes(E node1, E node2)
  {
	  // Check if node1 and node2 are the same
	  if (node1 == node2)
	  {
		  System.out.println("Both nodes provided are the same");
		  return;
	  }
	  
	  // Check if the size of the array is at least 2
	  if (this.size() < 2)
	  {
		  System.out.println("Not enough elements in the LinkedList to switch nodes");
		  return;
	  }
	  
	  // Create nodes for traversing and storing previous/current
	  Node currentNode1 = head;
	  Node previousNode1 = null;
	 	  
	  // Search for node1 by traversing the list until node1 found or reaches end of the list
	  while (currentNode1 != null && currentNode1.element != node1)
	  {
		  previousNode1 = currentNode1;
		  currentNode1 = currentNode1.getNext();
	  }

//	  
	  // Create nodes for traversing and storing previous/current
	  Node currentNode2 = head;
	  Node previousNode2 = null;
	  System.out.println(currentNode2.getNext().element);
	  // Search for node2 by traversing the list until node2 found or reaches end of the list
	  while (currentNode2 != null && currentNode2.element != node2)
	  {
		  previousNode2 = currentNode2;
		  currentNode2 = currentNode2.getNext();
	  }

	  // Check if node1 and node2 is null (Both were not found)
	  if (currentNode1 == null || currentNode2 == null)
	  {
		  System.out.println("One of the elements are not found in the list");
		  return;
	  }
	  
	  // node1 is NOT head
	  if (previousNode1 != null)
	  {
		  previousNode1.setNext(currentNode2);
	  }
	  else
	  {
		  // Make node2 the head
		  head = currentNode2;
	  }
	  
	  // node2 is NOT head
	  if (previousNode2 != null)
	  {
		  previousNode2.setNext(currentNode1);
	  }
	  else
	  {
		  // Make node2 the head
		  head = currentNode1;
	  }

	  // SWAP the remaining nodes to point correctly
	  Node temp = currentNode1.getNext();
	  currentNode1.setNext(currentNode2.getNext());
	  currentNode2.setNext(temp);
	  System.out.println("Nodes are swapped");
  }
  
  //main method
  public static void main(String[] args)
  {	  
	  SinglyLinkedList<String> list = new SinglyLinkedList<String>();
	  list.addFirst("MSP");
	  list.addLast("ATL");
	  list.addLast("BOS");
	  //
	  list.addFirst("LAX");
	  System.out.println(list);
	  
	  // Call swap the swapNodes
	  list.swapNodes("LAX","MSP");
	  System.out.println(list);
	  
	  try {
	        SinglyLinkedList<String> clonedList = list.clone();
	        System.out.println("Cloned List: " + clonedList);
	        clonedList.swapNodes("LAX","MSP");
	        System.out.println("Cloned List: " + clonedList);
	    } catch (CloneNotSupportedException e) {
	        System.out.println("Cloning not supported: " + e.getMessage());
	    }
	  
	  System.out.println(list);
	  //
  }
  
}

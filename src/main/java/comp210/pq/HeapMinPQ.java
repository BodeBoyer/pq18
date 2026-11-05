package comp210.pq;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A min-priority queue backed by a binary min-heap stored in an array.
 *
 * <p>The heap lives in {@code data[0]} through {@code data[size - 1]}, with the root at index 0.
 * Every parent is smaller than its children.
 *
 * <p><b>RULE: every key comparison must go through {@link #less(int, int)}.</b> Never call {@code
 * compareTo} on two keys yourself. The tests and the driver count comparisons, so a comparison
 * that skips the helper makes the counts wrong. (Comparing indexes, like {@code i < size}, is
 * fine.)
 *
 * <p>Keys are unique. {@link #peekMin()} and {@link #pollMin()} throw {@link
 * NoSuchElementException} when the queue is empty.
 */
public class HeapMinPQ<K extends Comparable<K>> implements MinPriorityQueue<K> {

  private static final int INITIAL_CAPACITY = 4;

  private K[] data = newArray(INITIAL_CAPACITY);
  private int size;
  private long comparisons;

  /**
   * Returns true if the key at index {@code i} is smaller than the key at index {@code j}, and
   * counts the comparison.
   */
  private boolean less(int i, int j) {
    comparisons++;
    return data[i].compareTo(data[j]) < 0;
  }

  /**
   * Makes an empty array of keys. Java cannot do {@code new K[n]} for a type parameter, so this
   * makes an array of {@code Comparable} and casts it.
   */
  @SuppressWarnings("unchecked")
  private static <K extends Comparable<K>> K[] newArray(int capacity) {
    return (K[]) new Comparable[capacity];
  }

  // ---------------------------------------------------------------------
  // Student-owned methods
  // ---------------------------------------------------------------------

  /** Adds a key to the heap. */
  @Override
  public void add(K key) {
    if (size == data.length) {
      grow();
    }

    data[size] = key;
    size++;
    percolateUp(size-1);

  }

  /** Returns the smallest key without removing it; throws NoSuchElementException if empty. */
  @Override
  public K peekMin() {
    if (size == 0) {
      throw new NoSuchElementException();
    }
    return data[0];
  }

  /** Removes and returns the smallest key; throws NoSuchElementException if empty. */
  @Override
  public K pollMin() {
    if (size == 0) {
      throw new UnsupportedOperationException("HeapMinPQ.pollMin is not implemented yet");
    }
    K minimum = data[0];

    size--;
    data[0] = data[size];
    data[size] = null;

    if (size > 0) {
      percolateDown(0);
    }
    return minimum;
  }

  /** Moves the key at index {@code i} up until its parent is smaller. */
  private void percolateUp(int i) {
    while (i > 0) {
      int p = parent(i);

      if (!less(i,p)) {
        break
      }

      swap(i, p);
      i = p;
    }
  }

  /** Moves the key at index {@code i} down until both children are larger. */
  private void percolateDown(int i) {
    while (left(i) < size) {
      int smallerChild = left(i);
      int rightChild = right(i);

      if (rightChild < size && less(rightChild,smallerChild)) {
        smallerChild = rightChild;
      }
    }
  }

  // ---------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------

  // AI-permitted: you may ask LearnWithAI to write this.
  /** Returns the index of the parent of index {@code i}. */
  private int parent(int i) {
    throw new UnsupportedOperationException("HeapMinPQ.parent is not implemented yet");
  }

  // AI-permitted: you may ask LearnWithAI to write this.
  /** Returns the index of the left child of index {@code i}. */
  private int left(int i) {
    throw new UnsupportedOperationException("HeapMinPQ.left is not implemented yet");
  }

  // AI-permitted: you may ask LearnWithAI to write this.
  /** Returns the index of the right child of index {@code i}. */
  private int right(int i) {
    throw new UnsupportedOperationException("HeapMinPQ.right is not implemented yet");
  }

  // AI-permitted: you may ask LearnWithAI to write this.
  /** Swaps the keys at indexes {@code i} and {@code j}. */
  private void swap(int i, int j) {
    throw new UnsupportedOperationException("HeapMinPQ.swap is not implemented yet");
  }

  // AI-permitted: you may ask LearnWithAI to write this.
  /** Doubles the capacity of {@code data}, keeping the keys already stored. */
  private void grow() {
    throw new UnsupportedOperationException("HeapMinPQ.grow is not implemented yet");
  }

  // AI-permitted: you may ask LearnWithAI to write this.
  /** Returns the heap's keys in array order, for example {@code [1, 4, 2]}. */
  @Override
  public String toString() {
    return "HeapMinPQ (toString not written yet)";
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public long comparisons() {
    return comparisons;
  }

  // ---------------------------------------------------------------------
  // Test hooks. Do not change these; the tests use them to set up heaps
  // without calling your add method.
  // ---------------------------------------------------------------------

  /**
   * Builds a heap directly from an array. Trusts that {@code heap} is already a valid min-heap.
   * Leaves spare capacity so the next add does not need to grow.
   */
  static <K extends Comparable<K>> HeapMinPQ<K> fromArrayForTest(K[] heap) {
    HeapMinPQ<K> pq = new HeapMinPQ<>();
    pq.data = newArray(Math.max(INITIAL_CAPACITY, heap.length * 2));
    for (int i = 0; i < heap.length; i++) {
      pq.data[i] = heap[i];
    }
    pq.size = heap.length;
    return pq;
  }

  /** Returns the heap's keys in array order ({@code data[0]} to {@code data[size - 1]}). */
  List<K> snapshot() {
    List<K> copy = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      copy.add(data[i]);
    }
    return copy;
  }
}

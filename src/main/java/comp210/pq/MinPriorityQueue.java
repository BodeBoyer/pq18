package comp210.pq;

import java.util.NoSuchElementException;

/**
 * A priority queue where the smallest key comes out first.
 *
 * <p>Keys are {@link Comparable}, the same interface from L6: {@code a.compareTo(b)} is negative
 * when {@code a} comes before {@code b}, zero when they are equal, and positive when {@code a}
 * comes after {@code b}.
 *
 * <p>Contract for every implementation:
 *
 * <ul>
 *   <li>Keys are unique and never null. Nobody will ever add the same key twice.
 *   <li>{@link #peekMin()} and {@link #pollMin()} throw {@link NoSuchElementException} when the
 *       queue is empty.
 *   <li>{@link #comparisons()} counts every key comparison the queue has made.
 * </ul>
 */
public interface MinPriorityQueue<K extends Comparable<K>> {

  /** Adds a key to the queue. The key is not already in the queue. */
  void add(K key);

  /**
   * Returns the smallest key without removing it.
   *
   * @throws NoSuchElementException if the queue is empty
   */
  K peekMin();

  /**
   * Removes and returns the smallest key.
   *
   * @throws NoSuchElementException if the queue is empty
   */
  K pollMin();

  /** Returns the number of keys in the queue. */
  int size();

  /** Returns how many key comparisons this queue has made since it was created. */
  long comparisons();
}

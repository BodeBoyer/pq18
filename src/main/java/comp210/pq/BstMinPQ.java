package comp210.pq;

import java.util.NoSuchElementException;

/**
 * A min-priority queue backed by a binary search tree (no balancing).
 *
 * <p><b>RULE: every key comparison must go through {@link #compare(Comparable, Comparable)}.</b>
 * Never call {@code compareTo} on two keys yourself. The tests and the driver count comparisons,
 * so a comparison that skips the helper makes the counts wrong. (Checking whether a node is {@code
 * null} is not a key comparison.)
 *
 * <p>Keys are unique. {@link #peekMin()} and {@link #pollMin()} throw {@link
 * NoSuchElementException} when the queue is empty.
 */
public class BstMinPQ<K extends Comparable<K>> implements MinPriorityQueue<K> {

  private BstNode<K> root;
  private int size;
  private long comparisons;

  /**
   * Compares two keys with {@code compareTo} and counts the comparison.
   *
   * @return a negative number if {@code a} comes before {@code b}, zero if they are equal, a
   *     positive number if {@code a} comes after {@code b}
   */
  private int compare(K a, K b) {
    comparisons++;
    return a.compareTo(b);
  }

  /** Adds a key to the tree, keeping the binary-search-tree order. */
  @Override
  public void add(K key) {
    if (root == null) {
      root = new BstNode<>(key);
      size++;
      return;
    }

    BstNode<K> current = root;

    while (true) {
      if (compare(key, current.key) < 0) {
        if (current.left == null) {
          current.left = new BstNode<>(key);
          break;
        }
        current = current.left;
      } else {
        if (current.right == null) {
          current.right = new BstNode<>(key);
          break;
        }
        current = current.right;
      }
    }
    size++;
  }

  /** Returns the smallest key without removing it; throws NoSuchElementException if empty. */
  @Override
  public K peekMin() {
    if (root == null) {
      throw new NoSuchElementException();
    }
    BstNode<K> current = root;
    while (current.left != null) {
      current = current.left;
    }
    return current.key;
  }

  /** Removes and returns the smallest key; throws NoSuchElementException if empty. */
  @Override
  public K pollMin() {
    if (root == null) {
      throw new NoSuchElementException();
    }
    BstNode<K> parent = null;
    BstNode<K> current = root;

    while (current.left != null) {
      parent = current;
      current = current.left;
    }

    if (parent == null) {
      root = current.right;
    } else {
      parent.left = current.right;
    }

    size--;
    return current.key;
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
  // Test hooks. Do not change these; the tests use them to set up trees
  // without calling your add method.
  // ---------------------------------------------------------------------

  /**
   * Replaces the whole tree. The tests promise {@code root} is a valid BST with {@code size} nodes.
   */
  void setRootForTest(BstNode<K> root, int size) {
    this.root = root;
    this.size = size;
  }

  /** Returns the root node so tests can inspect the tree's shape. */
  BstNode<K> getRootForTest() {
    return root;
  }
}

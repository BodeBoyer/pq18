package comp210.pq;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import org.junit.jupiter.api.function.Executable;

/**
 * Helpers shared by the tests. Fixture builders here never call student code, so a bug in add
 * cannot break the peek or poll tests.
 *
 * <p>Height convention: height is counted in EDGES. A single node has height 0 and an empty tree
 * has height -1.
 */
final class TestSupport {

  private TestSupport() {}

  // ------------------------------------------------------------------
  // BST inspection
  // ------------------------------------------------------------------

  /** Keys of the tree in in-order (left, node, right). Sorted if the tree is a valid BST. */
  static int[] inOrder(BstNode<Integer> root) {
    List<Integer> keys = new ArrayList<>();
    collectInOrder(root, keys);
    int[] result = new int[keys.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = keys.get(i);
    }
    return result;
  }

  private static void collectInOrder(BstNode<Integer> node, List<Integer> keys) {
    if (node == null) {
      return;
    }
    collectInOrder(node.left, keys);
    keys.add(node.key);
    collectInOrder(node.right, keys);
  }

  /** Height in edges: empty tree is -1, a single node is 0. */
  static int height(BstNode<?> node) {
    if (node == null) {
      return -1;
    }
    return 1 + Math.max(height(node.left), height(node.right));
  }

  // ------------------------------------------------------------------
  // Heap inspection
  // ------------------------------------------------------------------

  /**
   * True if data[0..size) satisfies the min-heap property (every parent smaller than its children).
   */
  static boolean isValidMinHeap(int[] data, int size) {
    for (int i = 1; i < size; i++) {
      if (data[(i - 1) / 2] > data[i]) {
        return false;
      }
    }
    return true;
  }

  // ------------------------------------------------------------------
  // Fixture builders (no student code involved)
  // ------------------------------------------------------------------

  /** Builds a BST by inserting the keys in the given order (plain insertion, no balancing). */
  static BstNode<Integer> bst(int... keys) {
    BstNode<Integer> root = null;
    for (int key : keys) {
      root = insert(root, key);
    }
    return root;
  }

  private static BstNode<Integer> insert(BstNode<Integer> node, int key) {
    if (node == null) {
      return new BstNode<>(key);
    }
    if (key < node.key) {
      node.left = insert(node.left, key);
    } else {
      node.right = insert(node.right, key);
    }
    return node;
  }

  /** A BstMinPQ whose tree is built from the keys in the given insertion order. */
  static BstMinPQ<Integer> bstQueue(int... keys) {
    BstMinPQ<Integer> pq = new BstMinPQ<>();
    pq.setRootForTest(bst(keys), keys.length);
    return pq;
  }

  /** A HeapMinPQ holding exactly this array, which must already be a valid min-heap. */
  static HeapMinPQ<Integer> heapQueue(int... heap) {
    assertTrue(
        isValidMinHeap(heap, heap.length),
        "test fixture is not a valid heap: " + Arrays.toString(heap));
    Integer[] boxed = new Integer[heap.length];
    for (int i = 0; i < heap.length; i++) {
      boxed[i] = heap[i];
    }
    return HeapMinPQ.fromArrayForTest(boxed);
  }

  /** The heap's keys in array order, as an int array for easy comparison. */
  static int[] snapshot(HeapMinPQ<Integer> pq) {
    List<Integer> keys = pq.snapshot();
    int[] result = new int[keys.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = keys.get(i);
    }
    return result;
  }

  /** Arranges the keys into a valid min-heap array (bottom-up heapify). */
  static int[] heapify(int[] keys) {
    int[] heap = Arrays.copyOf(keys, keys.length);
    for (int i = heap.length / 2 - 1; i >= 0; i--) {
      int j = i;
      while (2 * j + 1 < heap.length) {
        int child = 2 * j + 1;
        if (child + 1 < heap.length && heap[child + 1] < heap[child]) {
          child++;
        }
        if (heap[j] < heap[child]) {
          break;
        }
        int temp = heap[j];
        heap[j] = heap[child];
        heap[child] = temp;
        j = child;
      }
    }
    return heap;
  }

  // ------------------------------------------------------------------
  // Keys and assertions
  // ------------------------------------------------------------------

  /** {@code count} distinct keys from 1..(10 * count) in a random order fixed by {@code seed}. */
  static int[] randomUniqueKeys(int count, long seed) {
    int range = 10 * count;
    int[] pool = new int[range];
    for (int i = 0; i < range; i++) {
      pool[i] = i + 1;
    }
    Random random = new Random(seed);
    for (int i = range - 1; i > 0; i--) {
      int j = random.nextInt(i + 1);
      int temp = pool[i];
      pool[i] = pool[j];
      pool[j] = temp;
    }
    return Arrays.copyOf(pool, count);
  }

  /** The keys 1, 2, ..., n. */
  static int[] ascending(int n) {
    int[] keys = new int[n];
    for (int i = 0; i < n; i++) {
      keys[i] = i + 1;
    }
    return keys;
  }

  static int[] sortedCopy(int[] keys) {
    int[] copy = Arrays.copyOf(keys, keys.length);
    Arrays.sort(copy);
    return copy;
  }

  /**
   * Asserts that the call throws NoSuchElementException. If the method is still a stub, its
   * UnsupportedOperationException is passed through unchanged so the test reads as "not implemented
   * yet" rather than a confusing mismatch.
   */
  static void assertThrowsNoSuchElement(Executable call) {
    try {
      call.execute();
    } catch (NoSuchElementException expected) {
      return;
    } catch (UnsupportedOperationException notYetImplemented) {
      throw notYetImplemented;
    } catch (Throwable other) {
      fail("expected NoSuchElementException, but got " + other, other);
    }
    fail("expected NoSuchElementException on an empty queue, but nothing was thrown");
  }
}

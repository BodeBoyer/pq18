package comp210.pq;

import static comp210.pq.TestSupport.assertThrowsNoSuchElement;
import static comp210.pq.TestSupport.heapQueue;
import static comp210.pq.TestSupport.heapify;
import static comp210.pq.TestSupport.isValidMinHeap;
import static comp210.pq.TestSupport.randomUniqueKeys;
import static comp210.pq.TestSupport.snapshot;
import static comp210.pq.TestSupport.sortedCopy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Rungs 4-6: the array min-heap queue. @Order values match the activity's test numbers (15-29).
 * Rung 4 checks add through snapshot(). Rungs 5 and 6 build their heaps with test fixtures, so they
 * do not depend on your add method.
 */
@Order(2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TimeLimitExtension.class)
class HeapMinPQTest {

  // ------------------------------------------------------------------
  // Rung 4: add
  // ------------------------------------------------------------------

  @Test
  @Order(15)
  @DisplayName("R4.1 add one key: heap is [k]")
  void addOneKey() {
    HeapMinPQ<Integer> pq = new HeapMinPQ<>();
    pq.add(7);

    assertEquals(1, pq.size(), "size");
    assertArrayEquals(new int[] {7}, snapshot(pq), "heap array");
  }

  @Test
  @Order(16)
  @DisplayName("R4.2 add larger than parent, no swap: add 5, 9 gives [5, 9]")
  void addLargerNoSwap() {
    HeapMinPQ<Integer> pq = new HeapMinPQ<>();
    pq.add(5);
    pq.add(9);

    assertEquals(2, pq.size(), "size");
    assertArrayEquals(new int[] {5, 9}, snapshot(pq), "heap array");
  }

  @Test
  @Order(17)
  @DisplayName("R4.3 add smaller than parent, one swap: add 5, 2 gives [2, 5]")
  void addSmallerOneSwap() {
    HeapMinPQ<Integer> pq = new HeapMinPQ<>();
    pq.add(5);
    pq.add(2);

    assertEquals(2, pq.size(), "size");
    assertArrayEquals(new int[] {2, 5}, snapshot(pq), "heap array");
  }

  @Test
  @Order(18)
  @DisplayName("R4.4 [2, 4, 6, 8, 10] add 1: percolates to the root, giving [1, 4, 2, 8, 10, 6]")
  void addSiftsAllTheWayUp() {
    HeapMinPQ<Integer> pq = heapQueue(2, 4, 6, 8, 10);
    pq.add(1);

    assertEquals(6, pq.size(), "size");
    assertArrayEquals(new int[] {1, 4, 2, 8, 10, 6}, snapshot(pq), "heap array");
  }

  @Test
  @Order(19)
  @DisplayName("R4.5 add 100 random keys: heap invariant holds and the array grows past capacity 4")
  void addHundredRandomKeys() {
    int[] keys = randomUniqueKeys(100, 1019);
    HeapMinPQ<Integer> pq = new HeapMinPQ<>();
    for (int key : keys) {
      pq.add(key);
    }

    int[] heap = snapshot(pq);
    assertEquals(100, pq.size(), "size");
    assertTrue(isValidMinHeap(heap, heap.length), "not a valid min-heap: " + Arrays.toString(heap));
    assertArrayEquals(
        sortedCopy(keys), sortedCopy(heap), "heap should hold exactly the keys that were added");
  }

  // ------------------------------------------------------------------
  // Rung 5: peekMin
  // ------------------------------------------------------------------

  @Test
  @Order(20)
  @DisplayName("R5.1 peekMin on empty queue throws NoSuchElementException")
  void peekEmptyThrows() {
    HeapMinPQ<Integer> pq = new HeapMinPQ<>();
    assertThrowsNoSuchElement(pq::peekMin);
  }

  @Test
  @Order(21)
  @DisplayName("R5.2 peekMin returns the smallest key, size and array unchanged")
  void peekReturnsSmallest() {
    HeapMinPQ<Integer> pq = heapQueue(3, 5, 4, 9, 6);

    assertEquals(3, pq.peekMin(), "smallest key");
    assertEquals(5, pq.size(), "size after peek");
    assertArrayEquals(new int[] {3, 5, 4, 9, 6}, snapshot(pq), "heap array after peek");
  }

  // ------------------------------------------------------------------
  // Rung 6: pollMin
  // ------------------------------------------------------------------

  @Test
  @Order(22)
  @DisplayName("R6.1 pollMin on empty queue throws NoSuchElementException")
  void pollEmptyThrows() {
    HeapMinPQ<Integer> pq = new HeapMinPQ<>();
    assertThrowsNoSuchElement(pq::pollMin);
  }

  @Test
  @Order(23)
  @DisplayName("R6.2 pollMin on size 1: returns key, size 0")
  void pollSizeOne() {
    HeapMinPQ<Integer> pq = heapQueue(7);

    assertEquals(7, pq.pollMin(), "returned key");
    assertEquals(0, pq.size(), "size");
    assertArrayEquals(new int[] {}, snapshot(pq), "heap array");
  }

  @Test
  @Order(24)
  @DisplayName("R6.3 [1, 5] pollMin: returns 1, leaves [5]")
  void pollTwo() {
    HeapMinPQ<Integer> pq = heapQueue(1, 5);

    assertEquals(1, pq.pollMin(), "returned key");
    assertArrayEquals(new int[] {5}, snapshot(pq), "heap array");
  }

  @Test
  @Order(25)
  @DisplayName("R6.4 [1, 3, 2] pollMin: returns 1, leaves [2, 3]")
  void pollThree() {
    HeapMinPQ<Integer> pq = heapQueue(1, 3, 2);

    assertEquals(1, pq.pollMin(), "returned key");
    assertArrayEquals(new int[] {2, 3}, snapshot(pq), "heap array");
  }

  @Test
  @Order(26)
  @DisplayName("R6.5 [1, 2, 3, 4] pollMin: returns 1, leaves [2, 4, 3] (smaller child is left)")
  void pollSmallerChildLeft() {
    HeapMinPQ<Integer> pq = heapQueue(1, 2, 3, 4);

    assertEquals(1, pq.pollMin(), "returned key");
    assertArrayEquals(new int[] {2, 4, 3}, snapshot(pq), "heap array");
  }

  @Test
  @Order(27)
  @DisplayName("R6.6 [1, 3, 2, 4] pollMin: returns 1, leaves [2, 3, 4] (smaller child is right)")
  void pollSmallerChildRight() {
    HeapMinPQ<Integer> pq = heapQueue(1, 3, 2, 4);

    assertEquals(1, pq.pollMin(), "returned key");
    assertArrayEquals(new int[] {2, 3, 4}, snapshot(pq), "heap array");
  }

  @Test
  @Order(28)
  @DisplayName("R6.7 [1..8] pollMin: returns 1, leaves [2, 4, 3, 8, 5, 6, 7] (percolates two levels)")
  void pollSiftsTwoLevels() {
    HeapMinPQ<Integer> pq = heapQueue(1, 2, 3, 4, 5, 6, 7, 8);

    assertEquals(1, pq.pollMin(), "returned key");
    assertArrayEquals(new int[] {2, 4, 3, 8, 5, 6, 7}, snapshot(pq), "heap array");
  }

  @Test
  @Order(29)
  @DisplayName("R6.8 poll all of 200 random keys: output sorted, valid heap after every poll")
  void pollAllRandom() {
    int[] keys = randomUniqueKeys(200, 1029);
    int[] sorted = sortedCopy(keys);
    HeapMinPQ<Integer> pq = heapQueue(heapify(keys));

    for (int i = 0; i < sorted.length; i++) {
      assertEquals(sorted[i], pq.pollMin(), "poll #" + (i + 1));
      int[] heap = snapshot(pq);
      assertEquals(sorted.length - i - 1, pq.size(), "size after poll #" + (i + 1));
      assertTrue(
          isValidMinHeap(heap, heap.length),
          "not a valid min-heap after poll #" + (i + 1) + ": " + Arrays.toString(heap));
    }
  }
}

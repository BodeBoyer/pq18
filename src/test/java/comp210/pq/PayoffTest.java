package comp210.pq;

import static comp210.pq.TestSupport.ascending;
import static comp210.pq.TestSupport.randomUniqueKeys;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Payoff: both queues side by side. @Order values match the activity's test numbers (30-31). These
 * use your add and pollMin, so finish the earlier rungs first.
 */
@Order(3)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TimeLimitExtension.class)
class PayoffTest {

  // Expected counts assume ONE compare call per node visited in BST add, and
  // ONE less call per step of heap percolateUp. Sorted input 1..100 then costs
  // 0 + 1 + ... + 99 = 4950 for the BST (each key walks the whole right spine)
  // and 99 for the heap (each new key checks its parent once and stops).
  // If this proves brittle in piloting, loosen to a range.
  @Test
  @Order(30)
  @DisplayName("P.1 add 1..100 in sorted order: BST makes 4950 comparisons, heap makes 99")
  void sortedInputComparisons() {
    BstMinPQ<Integer> bst = new BstMinPQ<>();
    HeapMinPQ<Integer> heap = new HeapMinPQ<>();
    for (int key : ascending(100)) {
      bst.add(key);
    }
    for (int key : ascending(100)) {
      heap.add(key);
    }

    assertEquals(
        4950,
        bst.comparisons(),
        "BST comparisons (use exactly one compare call per node you visit)");
    assertEquals(
        99, heap.comparisons(), "heap comparisons (use exactly one less call per percolateUp step)");
  }

  @Test
  @Order(31)
  @DisplayName("P.2 same random input through both queues: drained outputs are identical")
  void sameOutputThroughInterface() {
    int[] keys = randomUniqueKeys(500, 1031);
    MinPriorityQueue<Integer> bst = new BstMinPQ<>();
    MinPriorityQueue<Integer> heap = new HeapMinPQ<>();

    assertArrayEquals(drain(bst, keys), drain(heap, keys), "BST output vs heap output");
  }

  private static int[] drain(MinPriorityQueue<Integer> pq, int[] keys) {
    for (int key : keys) {
      pq.add(key);
    }
    int[] out = new int[keys.length];
    for (int i = 0; i < out.length; i++) {
      out[i] = pq.pollMin();
    }
    assertEquals(0, pq.size(), "size after draining");
    return out;
  }
}

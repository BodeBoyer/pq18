package comp210.pq;

import static comp210.pq.TestSupport.ascending;
import static comp210.pq.TestSupport.assertThrowsNoSuchElement;
import static comp210.pq.TestSupport.bstQueue;
import static comp210.pq.TestSupport.height;
import static comp210.pq.TestSupport.inOrder;
import static comp210.pq.TestSupport.randomUniqueKeys;
import static comp210.pq.TestSupport.sortedCopy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Rungs 1-3: the BST-backed queue. @Order values match the activity's test numbers (1-14). Rung 1
 * checks add through the tree's shape. Rungs 2 and 3 build their trees with test fixtures, so they
 * do not depend on your add method.
 */
@Order(1)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TimeLimitExtension.class)
class BstMinPQTest {

  // ------------------------------------------------------------------
  // Rung 1: add
  // ------------------------------------------------------------------

  @Test
  @Order(1)
  @DisplayName("R1.1 add one key: size 1, root holds key")
  void addOneKey() {
    BstMinPQ<Integer> pq = new BstMinPQ<>();
    pq.add(42);

    BstNode<Integer> root = pq.getRootForTest();
    assertEquals(1, pq.size(), "size");
    assertNotNull(root, "root should not be null after one add");
    assertEquals(42, root.key, "root key");
    assertNull(root.left, "root.left");
    assertNull(root.right, "root.right");
  }

  @Test
  @Order(2)
  @DisplayName("R1.2 add smaller then larger: smaller is left child, larger is right child")
  void addLeftAndRightChild() {
    BstMinPQ<Integer> pq = new BstMinPQ<>();
    pq.add(50);
    pq.add(30);
    pq.add(70);

    BstNode<Integer> root = pq.getRootForTest();
    assertEquals(3, pq.size(), "size");
    assertEquals(50, root.key, "root key");
    assertNotNull(root.left, "30 should be the left child of 50");
    assertEquals(30, root.left.key, "left child of 50");
    assertNotNull(root.right, "70 should be the right child of 50");
    assertEquals(70, root.right.key, "right child of 50");
  }

  @Test
  @Order(3)
  @DisplayName("R1.3 add 20 random keys: in-order is sorted, size 20")
  void addTwentyRandomKeys() {
    int[] keys = randomUniqueKeys(20, 1001);
    BstMinPQ<Integer> pq = new BstMinPQ<>();
    for (int key : keys) {
      pq.add(key);
    }

    assertEquals(20, pq.size(), "size");
    assertArrayEquals(
        sortedCopy(keys),
        inOrder(pq.getRootForTest()),
        "in-order traversal should list every key in sorted order; added " + Arrays.toString(keys));
  }

  @Test
  @Order(4)
  @DisplayName("R1.4 add 1..100 in sorted order: tree degenerates to height 99 (edges)")
  void addSortedKeysDegenerates() {
    BstMinPQ<Integer> pq = new BstMinPQ<>();
    for (int key : ascending(100)) {
      pq.add(key);
    }

    assertEquals(100, pq.size(), "size");
    assertArrayEquals(ascending(100), inOrder(pq.getRootForTest()), "in-order traversal");
    assertEquals(
        99,
        height(pq.getRootForTest()),
        "height in edges: sorted input should make a 100-node chain");
  }

  // ------------------------------------------------------------------
  // Rung 2: peekMin
  // ------------------------------------------------------------------

  @Test
  @Order(5)
  @DisplayName("R2.1 peekMin on empty queue throws NoSuchElementException")
  void peekEmptyThrows() {
    BstMinPQ<Integer> pq = new BstMinPQ<>();
    assertThrowsNoSuchElement(pq::peekMin);
  }

  @Test
  @Order(6)
  @DisplayName("R2.2 peekMin on a single node returns its key")
  void peekSingleNode() {
    BstMinPQ<Integer> pq = bstQueue(42);
    assertEquals(42, pq.peekMin());
  }

  @Test
  @Order(7)
  @DisplayName("R2.3 peekMin finds the min several levels down the left spine")
  void peekDeepLeftSpine() {
    //            50
    //          /    \
    //        30      70
    //       /  \
    //     20    40
    //    /  \
    //  10    25
    //    \
    //     15
    BstMinPQ<Integer> pq = bstQueue(50, 30, 70, 20, 40, 10, 25, 15);
    assertEquals(10, pq.peekMin());
  }

  @Test
  @Order(8)
  @DisplayName("R2.4 peekMin does not change size or the tree")
  void peekDoesNotChangeSize() {
    BstMinPQ<Integer> pq = bstQueue(50, 30, 70, 20, 40, 10, 25, 15);
    pq.peekMin();
    pq.peekMin();

    assertEquals(8, pq.size(), "size after two peeks");
    assertArrayEquals(
        new int[] {10, 15, 20, 25, 30, 40, 50, 70},
        inOrder(pq.getRootForTest()),
        "tree contents after two peeks");
  }

  // ------------------------------------------------------------------
  // Rung 3: pollMin
  // ------------------------------------------------------------------

  @Test
  @Order(9)
  @DisplayName("R3.1 pollMin on empty queue throws NoSuchElementException")
  void pollEmptyThrows() {
    BstMinPQ<Integer> pq = new BstMinPQ<>();
    assertThrowsNoSuchElement(pq::pollMin);
  }

  @Test
  @Order(10)
  @DisplayName("R3.2 pollMin on a single node: returns key, size 0, root null")
  void pollSingleNode() {
    BstMinPQ<Integer> pq = bstQueue(42);

    assertEquals(42, pq.pollMin(), "returned key");
    assertEquals(0, pq.size(), "size");
    assertNull(pq.getRootForTest(), "root should be null once the tree is empty");
  }

  @Test
  @Order(11)
  @DisplayName("R3.3 pollMin when the root is the min: its right child becomes the root")
  void pollRootWithRightSubtree() {
    //  10
    //    \
    //     30
    //    /  \
    //  20    40
    BstMinPQ<Integer> pq = bstQueue(10, 30, 20, 40);

    assertEquals(10, pq.pollMin(), "returned key");
    assertEquals(3, pq.size(), "size");
    BstNode<Integer> root = pq.getRootForTest();
    assertNotNull(root, "root");
    assertEquals(30, root.key, "new root should be the old root's right child");
    assertArrayEquals(new int[] {20, 30, 40}, inOrder(root), "remaining keys in order");
  }

  @Test
  @Order(12)
  @DisplayName("R3.4 pollMin when the min is a deeper leaf: its parent's left becomes null")
  void pollDeepLeaf() {
    //        50
    //      /    \
    //    30      70
    //   /  \
    // 10    40
    BstMinPQ<Integer> pq = bstQueue(50, 30, 70, 10, 40);

    assertEquals(10, pq.pollMin(), "returned key");
    assertEquals(4, pq.size(), "size");
    BstNode<Integer> root = pq.getRootForTest();
    assertEquals(50, root.key, "root should not change");
    assertNull(root.left.left, "30's left child should be null after removing 10");
    assertArrayEquals(new int[] {30, 40, 50, 70}, inOrder(root), "remaining keys in order");
  }

  @Test
  @Order(13)
  @DisplayName(
      "R3.5 pollMin when the deeper min has a right subtree: parent's left becomes that subtree")
  void pollDeepMinWithRightSubtree() {
    //          50
    //        /    \
    //      30      70
    //     /
    //   10
    //     \
    //      20
    //     /  \
    //   15    25
    BstMinPQ<Integer> pq = bstQueue(50, 30, 70, 10, 20, 15, 25);

    assertEquals(10, pq.pollMin(), "returned key");
    assertEquals(6, pq.size(), "size");
    BstNode<Integer> thirty = pq.getRootForTest().left;
    assertEquals(30, thirty.key, "50's left child should still be 30");
    assertNotNull(thirty.left, "30's left child should be 10's old right subtree (rooted at 20)");
    assertEquals(20, thirty.left.key, "30's left child");
    assertEquals(15, thirty.left.left.key, "20's left child");
    assertEquals(25, thirty.left.right.key, "20's right child");
    assertArrayEquals(
        new int[] {15, 20, 25, 30, 50, 70},
        inOrder(pq.getRootForTest()),
        "remaining keys in order");
  }

  @Test
  @Order(14)
  @DisplayName("R3.6 poll all of 50 random keys: output sorted, in-order valid after every poll")
  void pollAllRandom() {
    int[] keys = randomUniqueKeys(50, 1014);
    int[] sorted = sortedCopy(keys);
    BstMinPQ<Integer> pq = bstQueue(keys);

    for (int i = 0; i < sorted.length; i++) {
      assertEquals(sorted[i], pq.pollMin(), "poll #" + (i + 1));
      assertEquals(sorted.length - i - 1, pq.size(), "size after poll #" + (i + 1));
      assertArrayEquals(
          Arrays.copyOfRange(sorted, i + 1, sorted.length),
          inOrder(pq.getRootForTest()),
          "remaining keys in order after poll #" + (i + 1));
    }
  }
}

package comp210.pq;

import java.util.Random;

/**
 * Runs both priority queues on the same inputs and prints how many key comparisons each one makes.
 * Run it any time; parts you have not written yet show up as "not yet implemented".
 */
public class PQDriver {

  private static final int N = 1000;
  private static final long SEED = 42;

  public static void main(String[] args) {
    int[] sorted = new int[N];
    for (int i = 0; i < N; i++) {
      sorted[i] = i + 1;
    }
    int[] random = shuffledCopy(sorted, new Random(SEED));

    System.out.println("Comparisons for N = " + N + " keys");
    System.out.println();
    runInput("Random input", random);
    runInput("Sorted input (1, 2, 3, ...)", sorted);
  }

  private static void runInput(String title, int[] keys) {
    System.out.println("== " + title + " ==");
    runOne("BST ", new BstMinPQ<>(), keys);
    runOne("Heap", new HeapMinPQ<>(), keys);
    System.out.println();
  }

  private static void runOne(String name, MinPriorityQueue<Integer> pq, int[] keys) {
    String addResult;
    String pollResult = "not yet implemented";
    boolean addWorked = false;

    try {
      for (int key : keys) {
        pq.add(key);
      }
      addResult = String.valueOf(pq.comparisons());
      addWorked = true;
    } catch (UnsupportedOperationException e) {
      addResult = "not yet implemented";
    } catch (RuntimeException e) {
      addResult = "error: " + e;
    }

    String heightResult = "";
    if (pq instanceof BstMinPQ<Integer> bst) {
      String shown = addWorked ? height(bst.getRootForTest()) + " edges" : addResult;
      heightResult = "   height: " + shown;
    }

    // Polling only makes sense once add works.
    if (addWorked) {
      try {
        long before = pq.comparisons();
        while (pq.size() > 0) {
          pq.pollMin();
        }
        pollResult = String.valueOf(pq.comparisons() - before);
      } catch (UnsupportedOperationException e) {
        pollResult = "not yet implemented";
      } catch (RuntimeException e) {
        pollResult = "error: " + e;
      }
    }

    System.out.println(
        name + "   add-all: " + addResult + "   poll-all: " + pollResult + heightResult);
  }

  /** Height in edges: a single node has height 0, an empty tree has height -1. */
  private static int height(BstNode<?> node) {
    if (node == null) {
      return -1;
    }
    return 1 + Math.max(height(node.left), height(node.right));
  }

  private static int[] shuffledCopy(int[] keys, Random random) {
    int[] copy = new int[keys.length];
    for (int i = 0; i < keys.length; i++) {
      copy[i] = keys[i];
    }
    for (int i = copy.length - 1; i > 0; i--) {
      int j = random.nextInt(i + 1);
      int temp = copy[i];
      copy[i] = copy[j];
      copy[j] = temp;
    }
    return copy;
  }
}

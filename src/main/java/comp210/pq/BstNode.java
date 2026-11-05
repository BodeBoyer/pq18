package comp210.pq;

/** One node of a binary search tree. */
public class BstNode<K> {
  K key;
  BstNode<K> left;
  BstNode<K> right;

  public BstNode(K key) {
    this.key = key;
  }
}

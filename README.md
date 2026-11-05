# In-class activity: BST vs. min-heap priority queue

**Before you start:** install the LearnWithAI plugin for IntelliJ, your AI tutor:
https://christhisway.github.io/learnwithai-intellij/

Keep the Canvas quiz you got this repo from open while you work. It has the reflection questions
you'll answer at the end. The quiz only covers the reflection: your code is graded on Gradescope.

**DON'T FORGET TO SUBMIT YOUR CODE TO GRADESCOPE.**

Imagine you're building the scheduler for a print server or an ER triage desk: jobs
arrive constantly, and you always need the most urgent one next. That's a priority
queue. Today you'll build one two ways, with a **binary search tree** and with an
**array-based min-heap**, and then measure which one holds up when the input stops
being friendly.

## Goal

You will implement the same **min-priority queue** (smallest key comes out first) twice:

1. `BstMinPQ`, backed by a binary search tree.
2. `HeapMinPQ`, backed by an array min-heap.

Then you will run a driver that counts how many key comparisons each one makes.
Watch what happens to the BST when the input is already sorted.

## What you write

| File | Methods |
|---|---|
| `BstMinPQ.java` | `add`, `peekMin`, `pollMin` (plus any private helpers you want) |
| `HeapMinPQ.java` | `percolateUp`, `percolateDown`, `add`, `peekMin`, `pollMin` |

Every method you need to write currently throws `UnsupportedOperationException`.
Do not change `MinPriorityQueue`, `BstNode`, the `compare`/`less` helpers, or the
methods marked as test hooks.

### Helpers you may ask LearnWithAI to write

In `HeapMinPQ`, these helpers are marked `// AI-permitted: you may ask LearnWithAI to write this.`:

`parent(int)`, `left(int)`, `right(int)`, `swap(int, int)`, `grow()`, `toString()`

You may ask LearnWithAI to write those for you. For everything else, LearnWithAI will help you
think it through, but you write the code.

## Rules

- Keys are `Comparable`: both queues take any key type `K extends Comparable<K>`, the
  interface from L6. `a.compareTo(b)` is negative when `a` comes first, zero when they are
  equal, and positive when `a` comes second. The tests and the driver use `Integer` keys.
- Every key comparison goes through the helper: `compare(a, b)` in `BstMinPQ`,
  `less(i, j)` in `HeapMinPQ`. Both call `compareTo` for you. Never call `compareTo` on two keys
  yourself.
  The helpers count comparisons, and the tests check those counts.
  Comparing indexes (like `i < size`) or checking for `null` is fine.
- Empty queue: `peekMin()` and `pollMin()` throw `java.util.NoSuchElementException`
  when the queue is empty.
- Keys are unique: no key is ever added twice, so you never need to handle duplicates.
- The heap's array starts with capacity 4. `add` must grow it when it is full.

## Reflection

The reflection is in the Canvas quiz, the same one that gave you the link to this repo. When the
tests pass and you've run the driver, answer its questions. One question asks you to upload a
screenshot of IntelliJ with the LearnWithAI panel open and your conversation visible, so take it
before you close the conversation. If you run out of time in class, finish the quiz before it
closes.

Submitting the quiz does not submit your code.

## Grading and submitting

**DON'T FORGET TO SUBMIT YOUR CODE TO GRADESCOPE.**

Submit on Gradescope by choosing **GitHub** and selecting your fork of this repo. The autograder
runs the same 31 tests you have here, which together are worth 4 points, split evenly. The
reflection questions in the Canvas quiz are worth the 5th point.

## Running the tests

The tests are a ladder. Work through them in order; each rung is your progress bar.

| Rung | Tests | What it checks |
|---|---|---|
| R1 | `BstMinPQTest` | BST `add` |
| R2 | `BstMinPQTest` | BST `peekMin` |
| R3 | `BstMinPQTest` | BST `pollMin` |
| R4 | `HeapMinPQTest` | heap `add` (and `percolateUp`) |
| R5 | `HeapMinPQTest` | heap `peekMin` |
| R6 | `HeapMinPQTest` | heap `pollMin` (and `percolateDown`) |
| P | `PayoffTest` | both queues side by side |

The peek and poll tests build their trees and heaps without calling your `add`,
so you can make progress on them even if `add` still has a bug.

**In IntelliJ:** open the folder (IntelliJ detects `pom.xml` and sets up Maven), then
right-click `src/test/java` and choose **Run 'All Tests'**, or click the green arrow next
to a single test class.

**From a terminal:**

```
./mvnw test             # macOS / Linux
mvnw.cmd test           # Windows
```

A test that fails with `UnsupportedOperationException` just means you haven't written
that method yet. Each test has a 3-second time limit, so an infinite loop shows up as a
failed test instead of freezing (the limit is off while you use the debugger).

## Running the driver

`PQDriver` adds 1000 keys to each queue, then removes them all, for random input
and for sorted input. It prints the comparison counts and the BST's height.
Anything you haven't written yet prints `not yet implemented`.

**In IntelliJ:** open `PQDriver.java` and click the green arrow next to `main`.

**From a terminal:**

```
./mvnw compile exec:java       # macOS / Linux
mvnw.cmd compile exec:java     # Windows
```

Requires Java 17 or newer, the same JDK you use for the assignments.

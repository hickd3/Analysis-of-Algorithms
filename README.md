# Analysis-of-Algorithms

**Contributors:** Alice Myhran, Lucy Barest, Dean Hickman  
**Original scaffolding:** djskrien (Feb 2018), updated mbender (Sep 2024)

---

## Project Thesis

This repository contains five standalone Java implementations developed for CS 375, covering three distinct algorithmic paradigms: array-backed abstract data type design (`ArrayIntegerSet.java`), bottom-up dynamic programming for string edit distance (`EditDistance.java`, `SpellChecker.java`, `improvedSpellChecker.java`), and 0/1 knapsack optimization via DP table construction with traceback (`MaxJoyDP.java`). The codebase also contains an explicit performance improvement experiment: `improvedSpellChecker.java` introduces a character-overlap pre-filter designed to reduce the number of full Levenshtein computations performed during spell checking. No machine learning, external frameworks, or build systems are used. All logic is implemented from scratch in plain Java.

---

## Repository Structure

```
.
├── ArrayIntegerSet.java
├── EditDistance.java
├── MaxJoyDP.java
├── SpellChecker.java
└── improvedSpellChecker.java
```

---

## File-by-File Architecture

### `ArrayIntegerSet.java`

**What it is:** A set-of-integers ADT backed by a tightly-packed, always-full `int[]` array (field: `data`). There is no backing ArrayList, no HashSet, and no capacity buffer — the array is resized by exact allocation on every insertion and deletion.

**Class invariants (explicitly stated in source):**
1. Order of elements in `data` is irrelevant (unordered set semantics).
2. `data.length` always equals the logical size of the set — there are no unused slots.

**Key methods and their documented complexities:**

| Method | Complexity | Mechanism |
|---|---|---|
| `ArrayIntegerSet()` | Θ(1) | Allocates `new int[0]` |
| `ArrayIntegerSet(int[] array)` | Θ(s + n) | Delegates to `addAll(int[])` |
| `add(int value)` | Θ(n) | Calls `contains()` → `indexOf()` (linear scan), then `incrementArraySize()` |
| `addAll(int[] array)` | Θ(s·n) | Iterates over `s` elements, calls `add()` (Θ(n)) each time |
| `addAll(ArrayIntegerSet)` | Θ(s·n) | Calls `toArray()` then `addAll(int[])` |
| `remove(int value)` | Θ(n) | `indexOf()` to locate, swap-with-last, then `decrementArraySize()` |
| `removeAll(ArrayIntegerSet)` | Θ(s·n) | Delegates via `toArray()` to `removeAll(int[])` |
| `contains(int value)` | Θ(n) | Delegates to `indexOf()` (linear scan) |
| `containsAll(ArrayIntegerSet)` | Θ(s·n) | Iterates over `s` items, calls `contains()` (Θ(n)) each |
| `union(ArrayIntegerSet)` | Θ(s·n) | `clone()` then `addAll(otherSet)` |
| `intersection(ArrayIntegerSet)` | Θ(s·n) | Iterates `data`, calls `otherSet.contains()` per element |
| `clone()` | Θ(n) | Returns `this.intersection(this)` — self-intersect as copy |
| `equals(ArrayIntegerSet)` | Θ(s·n) | Bidirectional `containsAll` |
| `size()` | Θ(1) | Returns `data.length` |
| `isEmpty()` | Θ(1) | Checks `data.length == 0` |
| `clear()` | Θ(1) | Reassigns `data = new int[0]` |
| `toArray()` | Θ(n) | `System.arraycopy` into fresh array |
| `toString()` | Θ(n) | Iterates `data`, builds `{a, b, c}` format string |

**Private helpers:**

- `indexOf(int value)` — linear scan of `data`, returns index or -1. All membership tests funnel through this.
- `incrementArraySize()` — allocates `data.length + 1` array, `System.arraycopy`s old contents, reassigns `data`. Called on every `add`.
- `decrementArraySize()` — allocates `data.length - 1` array, `System.arraycopy`s `data[0..length-2]`, reassigns `data`. Called on every successful `remove`.

**Design note:** The swap-with-last in `remove` (`data[index] = data[data.length - 1]`) exploits the unordered invariant to avoid an O(n) shift, making the swap itself Θ(1) before the Θ(n) resize.

**`main` method:** Provides a non-exhaustive manual smoke test covering construction, `add`, `addAll`, `clear`, `clone`, `contains`, `containsAll`, `equals`, `indexOf`, `size`, `union`, `remove`, and `removeAll`. Not a JUnit test suite; no assertions are made — correctness is verified by visual inspection of stdout.

---

### `EditDistance.java`

**What it is:** A standalone implementation of the Wagner–Fischer algorithm for computing Levenshtein edit distance between two strings.

**Algorithm:** Bottom-up DP over a 2D array `A[m+1][n+1]`, where `m = s1.length()`, `n = s2.length()`.

**Recurrence implemented in `editDist(String s1, String s2)`:**

```
A[i][0] = i          (delete all chars of s1 prefix)
A[0][j] = j          (insert all chars of s2 prefix)

if s1[i-1] == s2[j-1]:
    A[i][j] = A[i-1][j-1]
else:
    A[i][j] = 1 + min(A[i][j-1],       // insert
                      A[i-1][j],        // delete
                      A[i-1][j-1])      // replace
```

**Return value:** `A[m][n]` — the minimum edit distance between the full strings.

**Side effect:** The method prints the full `(m+1) × (n+1)` DP matrix to stdout before returning.

**`main` method:** Hard-codes `s1 = "dean"`, `s2 = "alice"`, calls `editDist`, and prints the result with a wall-clock millisecond timer using `System.currentTimeMillis()`.

**Time complexity:** Θ(m·n). **Space complexity:** Θ(m·n) — no space optimization is applied.

---

### `MaxJoyDP.java`

**What it is:** A 0/1 knapsack solver framed as a "candy shopping" problem. Selects a subset of items to maximize total joy subject to a budget constraint, with no item reuse.

**Hard-coded inputs in `main`:**
- `budget = 90`
- `prices = {16, 42, 5, 33, 7, 23, 6, 20, 50, 48}` (10 items)
- `joys  = {67, 19, 1, 18, 6, 17, 3, 14, 24, 23}` (10 items)

**DP table:** `dp[n+1][budget+1]` where `dp[i][b]` = maximum joy achievable using the first `i` items with budget `b`.

**Recurrence:**
```
if prices[i-1] > b:
    dp[i][b] = dp[i-1][b]           // cannot afford item i
else:
    dp[i][b] = max(dp[i-1][b],
                   dp[i-1][b - prices[i-1]] + joys[i-1])   // include or exclude
```

**Traceback (in `main`):** After filling the table, walks backward from `dp[n][budget]`. At each row `i`, if `dp[i][b] != dp[i-1][b]`, item `i-1` (0-indexed) was included; its price is subtracted from the remaining budget `b`. Included item indices are stored in `optimalSubset` (an `ArrayList<Integer>`).

**Output:** Prints the full DP table via `Arrays.toString` per row, the maximum joy value, and the item indices + (price, joy) pairs for the optimal selection.

**Time complexity:** Θ(n · budget). **Space complexity:** Θ(n · budget).

---

### `SpellChecker.java`

**What it is:** A spell checker that identifies out-of-vocabulary words in an input text and ranks dictionary candidates by Levenshtein edit distance.

**`editDistance(String w1, String w2)`:** Identical Wagner–Fischer implementation to `EditDistance.java` — full `(m+1)×(n+1)` DP table, same recurrence, returns `dp[m][n]`. No stdout side effects here.

**`spellCheck(String T, Set<String> D)`:**
1. Tokenizes input text `T` by whitespace: `T.split("\\s+")`.
2. For each token `w`: if `w` is absent from dictionary `D` (exact `HashSet` lookup):
   - Computes `editDistance(w, dictWord)` against **every word in `D`**.
   - Stores results as `List<Map.Entry<String, Integer>>` (word → distance pairs).
   - Sorts the list by distance ascending via `Comparator.comparingInt(Map.Entry::getValue)`.
   - Takes the top 5 (or fewer if `D` is smaller) as suggestions.
3. Returns `Map<String, List<String>>` mapping each misspelled token to its suggestion list.

**`loadDictionary(String path, Set<String> dictionary)`:** Reads a file line-by-line, trims and lowercases each entry, adds to the passed-in `HashSet<String>`.

**`main` method:** Requires exactly 3 CLI arguments: two dictionary file paths and one text file path. Loads both dictionaries into a single shared `HashSet<String> D`, reads the text file into a `StringBuilder`, calls `spellCheck`, and prints suggestions to stdout.

**Time complexity of `spellCheck`:** Θ(|w_T| · |D| · max\_word\_length²) in the worst case, since every token not in `D` triggers a full scan of `D` with an O(m·n) edit distance per pair.

**Runtime:** `java SpellChecker <dictFile1> <dictFile2> <textFile>`

---

### `improvedSpellChecker.java`

**What it is:** A modified version of `SpellChecker.java` that introduces a pre-filtering heuristic to reduce the number of full edit distance computations.

**Changes from `SpellChecker.java` (annotated in source as "code we added"):**

1. **New method `hasAtLeastHalfLetters(String word, String dictWord)`:**
   - Builds a `HashSet<Character>` from the characters of `word`.
   - Counts how many characters in `dictWord` appear in that set (with repetition — iterates over `dictWord.toCharArray()`, not a set).
   - Returns `true` if `commonLetters >= word.length() / 2` (integer division).
   - This is used as a fast filter: if a dictionary word shares fewer than half the distinct characters of the query word, skip the edit distance computation entirely.

2. **Modified `spellCheck`:** Before calling `editDistance`, gates each `dictWord` through `hasAtLeastHalfLetters(w, dictWord)`. Words that fail the filter are silently excluded from candidate ranking.

**Behavioral difference from `SpellChecker.java`:** The filter can cause valid close-edit-distance suggestions to be omitted if they fail the character-overlap threshold — precision may decrease for suggestions, but throughput improves for large dictionaries.

**Hard-coded inputs in `main`:**
- Dictionary: reads `"DomainSpecificDict.csv"` from the current working directory (single file, no CLI args).
- Test text: `T = "meximal anautjos"` (two tokens, both expected to be OOV).

**No CLI argument handling** — unlike `SpellChecker.java`, this class is not parameterized at runtime.

**`editDistance`:** Identical to `SpellChecker.java`.

---

## Runtime Execution Flow

### `ArrayIntegerSet` (via `main`)
```
main()
  → new ArrayIntegerSet()              // data = int[0]
  → new ArrayIntegerSet(int[])        // addAll(array) → add() × s
      → add(x) → contains(x) → indexOf(x)
                → incrementArraySize() → System.arraycopy
  → set1.add(3)
  → set1.addAll(set1)                 // toArray() then add() for each
  → set1.addAll(set2)
  → set1.clear()                      // data = new int[0]
  → set2.clone()                      // this.intersection(this)
  → contains / containsAll / equals / indexOf / size / union / remove / removeAll
  → [all results printed to stdout]
```

### `EditDistance` (via `main`)
```
main()
  → s1 = "dean", s2 = "alice"
  → startTime = System.currentTimeMillis()
  → editDist(s1, s2)
      → allocate A[5][6]
      → fill base cases: A[i][0]=i, A[0][j]=j
      → nested loop i=1..4, j=1..5: fill recurrence
      → print full A matrix to stdout
      → return A[4][5]
  → endTime = System.currentTimeMillis()
  → print result and elapsed ms
```

### `MaxJoyDP` (via `main`)
```
main()
  → budget=90, prices[10], joys[10] hardcoded
  → allocate dp[11][91]
  → nested loop i=1..10, b=0..90: fill knapsack recurrence
  → print dp table (Arrays.toString per row)
  → print dp[10][90] as max joy
  → traceback: i=10..1, reduce b by prices[i-1] when dp[i][b] != dp[i-1][b]
  → print optimalSubset indices and (price, joy) pairs
```

### `SpellChecker` (via `main`)
```
main(args[0], args[1], args[2])
  → loadDictionary(args[0], D)    // HashSet<String>
  → loadDictionary(args[1], D)
  → read args[2] into StringBuilder
  → spellCheck(text, D)
      → split text by "\\s+"
      → for each token w:
          → D.contains(w)?  // HashSet O(1) lookup
              → NO: for each dictWord in D:
                  → editDistance(w, dictWord)  // O(|w|·|dictWord|)
              → sort by distance
              → take top 5
  → print "Suggestions for 'w': ..." per misspelled token
```

### `improvedSpellChecker` (via `main`)
```
main()
  → read "DomainSpecificDict.csv" into HashSet D
  → T = "meximal anautjos"
  → spellCheck(T, D)
      → split T by "\\s+"
      → for each token w:
          → D.contains(w)?
              → NO: for each dictWord in D:
                  → hasAtLeastHalfLetters(w, dictWord)?  // char-set intersection
                      → YES: editDistance(w, dictWord)
                      → NO:  skip
              → sort by distance
              → take top 5
  → print suggestions
```

---

## Key Algorithms and Why They Are Used Here

### Wagner–Fischer (Levenshtein) DP — `EditDistance.java`, `SpellChecker.java`, `improvedSpellChecker.java`

Used to quantify string similarity for spell correction. The bottom-up tabulation avoids exponential recomputation of overlapping subproblems inherent in the naive recursive formulation. The choice of a full `(m+1)×(n+1)` table (rather than a space-optimized two-row version) is consistent across all three files — this is likely a pedagogical decision to make the DP structure visible, consistent with the `EditDistance.java` explicit matrix printout.

### 0/1 Knapsack DP — `MaxJoyDP.java`

The 0/1 constraint (each item used at most once) is enforced by indexing the previous row `dp[i-1]` when considering inclusion, preventing reuse of item `i` within the same budget `b`. The traceback correctly reconstructs the optimal item set by backward iteration comparing `dp[i][b]` to `dp[i-1][b]`.

### Array-resize Set ADT — `ArrayIntegerSet.java`

The always-full invariant trades memory efficiency (no wasted capacity) for Θ(n) per insertion/deletion cost. This is appropriate for a course context illustrating ADT design tradeoffs. The swap-with-last trick in `remove` preserves the unordered-set invariant while avoiding an O(n) element shift.

### Character-overlap Pre-filter — `improvedSpellChecker.java`

`hasAtLeastHalfLetters` uses a `HashSet<Character>` for O(|word|) construction and O(1) per-character lookup, then counts character occurrences in `dictWord` against that set. The threshold `>= word.length() / 2` is a hard-coded heuristic with no empirical justification in the source. **Note:** The method counts occurrences in `dictWord` with repetition (iterates `dictWord.toCharArray()`), not distinct characters — a `dictWord` with repeated characters matching the query can inflate `commonLetters` beyond `word.length()`.

---

## Evaluation Methodology

**No formal evaluation framework is present in this repository.**

- `ArrayIntegerSet.main` performs manual smoke tests with no assertions; pass/fail is determined by visual inspection of stdout.
- `EditDistance.main` includes a `System.currentTimeMillis()` wall-clock timer but runs a single fixed input pair (`"dean"` vs `"alice"`). No benchmark suite, no parameterized timing across input sizes.
- `MaxJoyDP.main` runs a single hard-coded instance. No correctness validation against a known optimum.
- `SpellChecker` and `improvedSpellChecker` have no comparison of suggestion quality between the two implementations. The improvement introduced in `improvedSpellChecker` is not benchmarked for either speed or recall.

---

## Reproducible Run Instructions

### Prerequisites
- Java 8+ (`javac`, `java`)
- No external dependencies or build system

### Compile all files
```bash
javac ArrayIntegerSet.java
javac EditDistance.java
javac MaxJoyDP.java
javac SpellChecker.java
javac improvedSpellChecker.java
```

### Run each class

**ArrayIntegerSet** (self-contained smoke test):
```bash
java ArrayIntegerSet
```

**EditDistance** (hard-coded inputs: `"dean"` vs `"alice"`):
```bash
java EditDistance
```

**MaxJoyDP** (hard-coded: budget=90, 10 items):
```bash
java MaxJoyDP
```

**SpellChecker** (requires 2 dictionary files + 1 text file as CLI args):
```bash
java SpellChecker <dictionaryFile1> <dictionaryFile2> <textFile>
# Example:
java SpellChecker dict1.txt dict2.txt input.txt
```
Dictionary files: one word per line, plain text.  
Text file: whitespace-delimited words.

**improvedSpellChecker** (requires `DomainSpecificDict.csv` in current directory; text is hard-coded):
```bash
# Place DomainSpecificDict.csv in the working directory, then:
java improvedSpellChecker
```
`DomainSpecificDict.csv`: one word per line. The test input `"meximal anautjos"` is hard-coded in `main`.

---

## Known Limitations and Design Notes

- **`addAll` complexity comment mismatch in `ArrayIntegerSet.java`:** The Javadoc for `addAll(int[])` states Θ(s·n) in the description body but Θ(s+n) in the opening line. The correct complexity, given that `add()` is Θ(n) and is called `s` times, is Θ(s·n).
- **`improvedSpellChecker` filter semantics:** `hasAtLeastHalfLetters` counts character occurrences in `dictWord` with multiplicity against the distinct character set of `word`. This means a `dictWord` containing many repeated characters that appear in `word` can pass the filter even if character overlap is semantically low.
- **No shared dictionary file is committed.** `improvedSpellChecker` expects `DomainSpecificDict.csv` at runtime; its contents are not in this repository. They can be provided upon request, or you can set your own csv with the same title.
- **No test harness.** JUnit or equivalent is not present in any file.

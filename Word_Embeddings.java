import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class Node {
public String word;
public double dist;

public Node(String word, double dist) {
this.word = word;
this.dist = dist;
}
}

public class HashedHeaps {
private Node[][] words; // the hashtable
private int m; // size of hashtable
private int itemsInHash;// number of hashtable entries
private int k; // size of heap
private int[] itemsInHeap; // number of items in each Heap

// sz1: Hashtable size, sz2: Heap size
public HashedHeaps(int sz1, int sz2) {
m = sz1;
k = sz2;
words = new Node[m][k];
itemsInHash = 0;
itemsInHeap = new int[m];
}

// Parses the file: sFile line-by-line, and makes 2 calls to method: insert() for each line
// The format of each line is: word1,word2,dist
public void load(String sFile) {
String sLine = "", w1 = "", w2 = "";
double dist;
try {
BufferedReader br = new BufferedReader(new FileReader(sFile));
while ((sLine=br.readLine()) != null) {
sLine = sLine.trim();
String[] arLine = sLine.split(",");
w1 = arLine[0];
w2 = arLine[1];
dist = Double.parseDouble(arLine[2]);
insert(w1, w2, dist);
insert(w2, w1, dist);
}
br.close();
} catch (FileNotFoundException e) {
e.printStackTrace();
} catch (IOException ioe) {
ioe.printStackTrace();
}
}

// Return the hash value [0...m) for a given word: w
private int hashFunc(String w) {
int key = Math.abs(w.hashCode());
int hash = key % this.m;
return hash;
}





// Inserts the entry: (w1, w2, dist) to the data structure.
// If: w1 does not exist, insert it in the hashtable and create heap with two nodes:
// (w1, MIN_VALUE) and (w2, dist)
// If: w1 exists, just add (w2, dist) to the heap
public void insert(String w1, String w2, double dist) {
int hashValue = hashFunc(w1);
   
    // Check if the word w1 already exists in the hashtable
    boolean wordExists = false;
    int heapIndex = -1;

    for (int i = 0; i < itemsInHash; i++) {
        if (words[i][0].word.equals(w1)) {
            wordExists = true;
            heapIndex = i;
            break;
        }
    }

    // If word w1 does not exist in the hashtable
    if (!wordExists) {
        // Insert Node(w1, -1) at the root of the heap
        words[itemsInHash][0] = new Node(w1, -1);
        itemsInHeap[itemsInHash] = 1; // One element in the heap
        heapIndex = itemsInHash;
        itemsInHash++;
    }

    // Insert Node(w2, dist) in the heap corresponding to w1
    Node newNode = new Node(w2, dist);
    int heapSize = itemsInHeap[heapIndex];

    // Insert the new node at the end of the heap
    words[heapIndex][heapSize] = newNode;
    itemsInHeap[heapIndex]++;

    // Restore the heap property (if necessary) by bubbling up the new node
    int currentIndex = heapSize;
    while (currentIndex > 0) {
        int parentIndex = (currentIndex - 1) / 2;

        if (words[heapIndex][currentIndex].dist < words[heapIndex][parentIndex].dist) {
            // Swap nodes if the new node has smaller distance than its parent
            Node temp = words[heapIndex][currentIndex];
            words[heapIndex][currentIndex] = words[heapIndex][parentIndex];
            words[heapIndex][parentIndex] = temp;
        } else {
            // Stop if the heap property is restored
            break;
        }

        currentIndex = parentIndex;
    }
}




// Retrieves the most similar word to: w
public String findMostSimilarWord(String w) {
	 int hashValue = hashFunc(w);

	    for (int i = 0; i < itemsInHash; i++) {
	        if (words[i][0].word.equals(w)) {
	            // Found the word in the hashtable
	            // Return the word with the smallest distance (if any)
	            if (itemsInHeap[i] > 1) {
	                return words[i][1].word; // The first element in the heap (smallest distance)
	            } else {
	                // The word exists in the hashtable, but no similar words in the heap
	                return null;
	            }
	        }
	    }

	    // Word not found in the hashtable
	    return null;
}

// Removes the most similar word to: w
public String removeMostSimilarWord(String w) {
    int hashValue = hashFunc(w);

    for (int i = 0; i < itemsInHash; i++) {
    	
    	 // Found the word in the hashtable
        if (words[i][0].word.equals(w)) {
           
            if (itemsInHeap[i] > 1) {
                // Remove the word with the smallest distance (second element in the heap)
                String removedWord = words[i][1].word;

                // Replace the second element with the last element in the heap
                words[i][1] = words[i][itemsInHeap[i] - 1];
                itemsInHeap[i]--;

                // Restore the heap property (if necessary) by bubbling down the replaced node
                int currentIndex = 1;
                while (true) {
                    int leftChildIndex = 2 * currentIndex + 1;
                    int rightChildIndex = 2 * currentIndex + 2;
                    int smallestChildIndex = -1;

                    // Find the index of the smallest child
                    if (leftChildIndex < itemsInHeap[i]) {
                        smallestChildIndex = leftChildIndex;
                    }
                    
                    
                    if (rightChildIndex < itemsInHeap[i] &&
                        words[i][rightChildIndex].dist < words[i][leftChildIndex].dist) {
                        smallestChildIndex = rightChildIndex;
                    }

                    // Check if the heap property is violated
                    if (smallestChildIndex != -1 &&
                        words[i][currentIndex].dist > words[i][smallestChildIndex].dist) {
                        // Swap nodes if the current node has a larger distance than the smallest child
                        Node temp = words[i][currentIndex];
                        words[i][currentIndex] = words[i][smallestChildIndex];
                        words[i][smallestChildIndex] = temp;
                    } else {
                        // Stop if the heap property is restored
                        break;
                    }

                    currentIndex = smallestChildIndex;
                }

                return removedWord;
            } else {
                // The word exists in the hashtable, but no similar words in the heap
                return null;
            }
        }
    }

    // Word not found in the hashtable
    return null;
}


// Returns true if words: w1 and w2 have at least one common similar word in the top-n
public boolean haveCommonSimilarWord(String w1, String w2, int n) {
	   int hashValue1 = hashFunc(w1);
	    int hashValue2 = hashFunc(w2);

	    // Find the heap index for w1
	    int heapIndex1 = -1;
	    for (int i = 0; i < itemsInHash; i++) {
	        if (words[i][0].word.equals(w1)) {
	            heapIndex1 = i;
	            break;
	        }
	    }

	    // Find the heap index for w2
	    int heapIndex2 = -1;
	    for (int i = 0; i < itemsInHash; i++) {
	        if (words[i][0].word.equals(w2)) {
	            heapIndex2 = i;
	            break;
	        }
	    }

	    // Check if both words exist in the hashtable
	    if (heapIndex1 == -1 || heapIndex2 == -1) {
	        return false;
	    }

	    // Check if w1 contains w2 in the top-n similar words
	    for (int i = 1; i < Math.min(n + 1, itemsInHeap[heapIndex1]); i++) {
	        if (words[heapIndex1][i].word.equals(w2)) {
	            return true;
	        }
	    }

	    // Check if w2 contains w1 in the top-n similar words
	    for (int i = 1; i < Math.min(n + 1, itemsInHeap[heapIndex2]); i++) {
	        if (words[heapIndex2][i].word.equals(w1)) {
	            return true;
	        }
	    }

	    // No common similar word found
	    return false;
	}


public static void main(String[] args) {
	
	
	System.out.println("\t\t Word Embeddings Project \n");
	// Create an object of HashedHeaps
    HashedHeaps hashedHeaps = new HashedHeaps(2000, 1000);
    
    // Import data from the file
    hashedHeaps.load("data.txt");

    // Input words
    hashedHeaps.insert("word1", "word2", 3.14);
    hashedHeaps.insert("word1", "word3", 2.71);
    hashedHeaps.insert("word4", "word5", 1.23);
    
    // Test the FindMostSimilarWord method
    System.out.println("This is the procedure of FindMostSimilarWord Method");
    String similarWord = hashedHeaps.findMostSimilarWord("word1");
    System.out.println("Most similar word to 'word': " + similarWord+"\n");
    
 // Test the removeMostSimilarWord method
    System.out.println("This is the procedure of removeMostSimilarWord Method");
    String removedWord = hashedHeaps.removeMostSimilarWord("word1");
    System.out.println("Removed most similar word to 'word1': " + removedWord + "\n");

    // Test the haveCommonSimilarWord method
    System.out.println("This is the procedure of haveCommonSimilarWord Method");
    boolean haveCommonSimilarWord = hashedHeaps.haveCommonSimilarWord("word2", "word3", 2);
    System.out.println("Do 'word2' and 'word3' have a common similar word? " + haveCommonSimilarWord);
}
}

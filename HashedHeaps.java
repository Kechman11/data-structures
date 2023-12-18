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
	private Node[][] words;	// the hashtable
	private int m;			// size of hashtable
	private int itemsInHash;// number of hashtable entries 
	private int k;			// size of heap
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
	//		(w1, MIN_VALUE) and (w2, dist)
	// If: w1 exists, just add (w2, dist) to the heap
	public void insert(String w1, String w2, double dist) {	
		/* your code here */
	}

	// Retrieves the most similar word to: w
	public String findMostSimilarWord(String w) {
		/* your code here */
	}

	// Removes the most similar word to: w
	public String removeMostSimilarWord(String w) {
		/* your code here */
	}

	// Returns true if words: w1 and w2 have at least one common similar word in the top-n
	public boolean haveCommonSimilarWord(String w1, String w2, int n) {
		/* your code here */
	}

	public static void main(String[] args) {
	}
}

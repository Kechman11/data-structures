
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


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
	
	
	public void insert(String w1, String w2, double dist) {
		// Calculate hashValue for w1 from the hash function
	    int hashValue = hashFunc(w1);

	    // Check if the word w1 already exists in the hashtable
	    boolean wordExists = false;
	    
	    
	    int heapIndex = -1;
	    
	    // Access the hashTable to check if w1 exists
	    for (int i = 0; i < itemsInHash; i++) {
	        if (words[i][0].word.equals(w1)) {
	        	// There is w1 in the hashtable
	            wordExists = true;
	            heapIndex = i;
	            break;
	        }
	    }

	    // If word w1 does not exist in the hashtable
	    if (!wordExists) {
	        // Check if there is space in the hashtable
	        if (itemsInHash < m) {
	            // Insert Node(w1, -1) at the root of the heap
	            words[itemsInHash][0] = new Node(w1, -1);
	            itemsInHeap[itemsInHash] = 1; // One element in the heap
	            heapIndex = itemsInHash;
	            itemsInHash++;
	            System.out.println(w1);
	        } else {
	            // Handle the case where the hashtable is full
	            System.out.println("Hashtable is full for word: " + w1);
	            return;
	        }
	    }

	    // Insert Node(w2, dist) in the heap corresponding to w1
	    int heapSize = itemsInHeap[heapIndex];

	    // Check if there is space in the heap
	    if (heapSize < k) {
	        // Insert the new node at the end of the heap
	        words[heapIndex][heapSize] = new Node(w2, dist);
	        itemsInHeap[heapIndex]++;
	        // Rest of the code for restoring the heap property...
	    } else {
	        // Handle the case where the heap is full
	        System.out.println("Heap is full for word: " + w1);
	    }
	}




	public String findMostSimilarWord(String w) {
		  int hashValue = hashFunc(w);

		    // Check if the word w already exists in the hashtable
		    boolean wordExists = false;
		    int heapIndex = -1;

		    for (int i = 0; i < itemsInHash; i++) {
		        if (words[i][0].word.equals(w)) {
		            wordExists = true;
		            heapIndex = i;
		            break;
		        }
		    }
		    
		    // If word w exists in the hashtable
		    if (wordExists) {
		        // Find the word with the smallest distance in the corresponding heap
		        double minDist = Double.MAX_VALUE;
		        String mostSimilarWord = null;

		        for (int j = 1; j < itemsInHeap[heapIndex]; j++) {
		            if (words[heapIndex][j].dist < minDist) {
		                minDist = words[heapIndex][j].dist;
		                mostSimilarWord = words[heapIndex][j].word;
		            }
		        }

		        return mostSimilarWord;
		    } else {
		        System.out.println("Word '" + w + "' not found in the hashtable.");
		        return null;
		    }
	}

	// Removes the most similar word to: w
	public String removeMostSimilarWord(String w) {
		return w;
		/* your code here */
	}

	// Returns true if words: w1 and w2 have at least one common similar word in the top-n
	public boolean haveCommonSimilarWord(String w1, String w2, int n) {
		return false;
		/* your code here */
	}

	
	
	
	
	
	
	
	public static void main(String[] args) {
	    // Create an instance of HashedHeaps
		HashedHeaps hashedHeaps = new HashedHeaps(2000, 1000);  

	   
	    
	    System.out.println("Choose method you want to test:\n1.Insert method\n2.findMostSimilarWord\n3.removeMostSimilarWord\n4.haveCommonSimilarWord");
	    int answer;
	    Scanner input = new Scanner(System.in);
	    answer=input.nextInt();
	    
	    do {
	    if (answer!=1&&answer!=2&&answer!=3&&answer!=4)
	    {
	    	System.out.println("Please try again!");
	    }
	    
	    
	    else {
	    	if(answer==1) {
	    // Insert some sample data
	    hashedHeaps.insert("families", "world", 1.5);
	    hashedHeaps.insert("apple", "orange", 2.0);
	    hashedHeaps.insert("dog", "cat", 0.8);
	    hashedHeaps.load("data.txt");
	   
	    
	    	}
	    	else if(answer==2)
	    {
	    		String testWord1 = "dog";
	            String testWord2 = "nonexistentword";
	            // Call the findMostSimilarWord method for testWord1
	            String mostSimilarWord1 = hashedHeaps.findMostSimilarWord(testWord1);

	            // Display the result for testWord1
	            if (mostSimilarWord1 != null) {
	            	System.out.println("The most similar word to '" + testWord1 + "' is: " + mostSimilarWord1);
	            } else {
            System.out.println("No similar word found for '" + testWord1 + "'");
	            }	
	    }
	    	
	    }
	    
	    
	    }while(answer!=1&&answer!=2&&answer!=3&&answer!=4);

}
}

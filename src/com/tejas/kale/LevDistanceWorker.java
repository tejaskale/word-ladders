package com.tejas.kale;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Multithreaded transition calcualtor. Generates a list of 1edit transitions from wordlist.txt and writes to transitionlist.json
 * @author tkale
 *
 */
public class LevDistanceWorker implements Runnable {

	static String all[]; // The word list. Read only. Common to all threads.
	private int start,end;

	public HashMap<String, String[]> map;
	/**
	 * Take a chunk of words from start index to end index in the all array.
	 * @param all
	 * @param currentHash
	 * @param map
	 */
	public LevDistanceWorker(int start, int end) {
		this.start = start;
		this.end = end;
		this.map = new HashMap<String,String[]>(end-start);
	}

	/**
	 * Gathers all possible 1 distance transitions for the currentHash
	 */
	@Override
	public void run() {
		for(int i=start;i<end;i++){
			ArrayList<String> t = new ArrayList<String>(20);
			for (int j = 0; j < all.length; j++){
				if(i!=j && Math.abs(all[i].length()-all[j].length())<2 && getLevDistance(all[i], all[j]) ==1){
					t.add(all[j]);
				}
			}
			map.put(all[i],  t.toArray(new String[t.size()]));
		}
	}
	
	/**
	 * Implementation of the Wagner Fischer algorithm. Ref: https://en.wikipedia.org/wiki/Wagner%E2%80%93Fischer_algorithm
	 * For the purposes of this problem, substitution is equal to a delete and an add. Consequently the cost is 2.
	 * @param a
	 * @param b
	 * @return The Levhenstein Distance.
	 */

	public static int getLevDistance(String a, String b) {

		char[] a_chars = a.toCharArray();
		char[] b_chars = b.toCharArray();

		int len_a = a.length();
		int len_b = b.length();

		int[] current = new int[len_a + 1];
		int[] previous = new int[len_a + 1];

		// Initialize previous = {1 .. len_a};
		for (int i = 0; i <= len_a; i++)
			previous[i] = i;

		for (int i = 1; i <= len_b; i++) {
			current[0] = i;
			for (int j = 1; j <= len_a; j++) {
				if (b_chars[i - 1] == a_chars[j - 1]) {
					current[j] = previous[j - 1];
				} else {
					// penalize substitutions because in our problem definition
					// substitutions are not allowed.
					current[j] = min(previous[j], previous[j - 1] + 1,current[j - 1]) + 1;
				}
			}
			previous = current.clone();
		}

		return current[len_a];
	}

	private static int min(int a, int b, int c) {
		return (a < b) ? ((a < c) ? a : c) : (b < c) ? b : c;
	}
}

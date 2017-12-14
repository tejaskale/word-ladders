package com.tejas.kale;

import java.util.ArrayList;
import java.util.HashMap;

/* Finding the diameter of the graph.  Incomplete.
 * 
 */
public class Longest {
	public static void main(String[] args) throws Exception {
		BFS b = new BFS();
		HashMap<String, ArrayList<String>> longest = new HashMap<>(b.gen.wordFreq.size());
		
		int maxIter = 1;
		int sizeMax = 0;
		
		for (String string : b.gen.wordFreq.keySet()) {
			for (String string1 : b.gen.wordFreq.keySet()) {
				ArrayList<String> temp = b.getPath(string, string1, false);
				if( temp.size() >sizeMax){
					longest.put(string, temp);
					sizeMax = temp.size();
					System.out.println(temp);
				}
			}
			maxIter--;
			if(maxIter < 0)break;
		}
		for (String string : longest.keySet()) {
			System.out.println(longest.get(string));
		}
	}
}

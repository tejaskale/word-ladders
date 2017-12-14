package com.tejas.kale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Performs BFS search on the 1 edit distance transition graph.
 * @author tkale
 *
 */
public class BFS {

	 ChildrenGenerator gen;

	/**
	 * Reads wordlist and the precomputed transition list.
	 * @throws Exception
	 */
	public BFS() throws Exception {
		gen = new ChildrenGenerator();
//		gen.readWordList();
//		gen.readTransitions();
	}

	/**
	 * Get a path from start word to goal word. Anagrams are handled by using
	 * the first possible word.
	 * 
	 * @param startWord
	 * @param goalWord
	 * @param all
	 * @return {@link ArrayList<String>} The path from start word to goal Word.
	 *         Empty if no path exists.
	 * @throws Exception
	 *             File read exceptions from reading word and transition lists.
	 * 
	 */
	public ArrayList<String> getPath(String startWord, String goalWord, boolean all) throws Exception {

	

		String startHash = gen.getHash(startWord.toLowerCase());
		String goalHash = gen.getHash(goalWord.toLowerCase());

		ArrayList<String> path = new ArrayList<String>();
		/*
		 * Empty args, args not in word list --> output nothing.
		 */
		if ("".equals(startWord) || "".equals(goalWord))
			return path;
		// Node.gen = gen;
		HashMap<String, String> parents = new HashMap<String, String>(100000);
		LinkedList<String> queue = new LinkedList<String>();

		parents.put(startHash, null);
		queue.add(startHash);
		String currentParent = startHash;

		while (!goalHash.equals(currentParent) && !queue.isEmpty()) {
			// Add all the current nodes children to the queue.
			String children[] = gen.transitions.get(currentParent);
			if (null != children) {
				for (String string : children) {
					if (!parents.containsKey(string)) {
						parents.put(string, currentParent);
						queue.add(string);
					}
				}
			}
			// Update Current Parent.
			currentParent = queue.pop();
		}

		path.add(goalWord); // Ensure goal word is added and not one of its
							// synonyms.
		currentParent = parents.get(currentParent);
		while (null != currentParent) {
			if (all) {
				path.add(Arrays.toString(gen.wordFreq.get(currentParent).toArray()));
			} else {
				path.add(gen.wordFreq.get(currentParent).get(0));
			}
			currentParent = parents.get(currentParent);
		}
		Collections.reverse(path);
		path.set(0, startWord); //Ensure start word is added and not one of the anagrams.
		if (!queue.isEmpty()) {
			return path;
		} else
			return new ArrayList<String>(); // return empty arraylist incase of
											// no path found.
	}

	public static void main(String[] args) throws Exception {
		String startWord =  args[0];
		String goalWord =  args[1];
		

		BFS bfs = new BFS();
		

		ArrayList<String> path = bfs.getPath(startWord, goalWord, (args.length>2));
		for (String string : path) {
			System.out.println(string);
		}
	}
}

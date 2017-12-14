package com.tejas.kale;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Read Wordlist and generate node children.
 * 
 * @author tkale
 * 
 */
public class ChildrenGenerator {
	static final String WORDLIST = "data/wordList.txt";
	static final String TRANSITIONLIST = "data/transitions.json";
	static final int numWorkers = 4;
	static final int numWords = 120000;

	public String[] hashArray;
	public HashMap<String, ArrayList<String>> wordFreq;
	public HashMap<String, String[]> transitions; 

	public ChildrenGenerator() throws Exception {
		wordFreq = new HashMap<String, ArrayList<String>>(120000);
		transitions = new HashMap<String, String[]>(100000);
		readWordList();
		readTransitions();
	}

	public static void main(String[] args) throws Exception {

		ChildrenGenerator gen = new ChildrenGenerator();
//		gen.readWordList();
//		 gen.generateTransitions();
//		gen.readTransitions();

//		 System.out.println(gen.getHash("croissant"));
//		// System.out.println(getHash("baritone"));
//		// System.out.println(wordFreq.get(getHash("notaries")));
//		System.out.println(gen.wordFreq.get((gen.getHash("reader"))));
//		System.out.println(LevDistanceWorker.getLevDistance("croissant", "baritone"));
//		System.out.println(gen.getHash("reader"));
//		System.out.println(LevDistanceWorker.getLevDistance("acinorsst", "ainorsst"));

//		System.out.println(gen.getHash("croissant")+":\""+Arrays.toString(gen.transitions.get(gen.getHash("croissant")))+"\":"+LevDistanceWorker.getLevDistance("baritone", "croissant"));
//		System.out.println(gen.getHash("arsonist")+":\""+Arrays.toString(gen.transitions.get(gen.getHash("arsonist")))+"\""+LevDistanceWorker.getLevDistance("baritone", "arsonist"));
//		System.out.println(gen.getHash("aroints")+":\""+Arrays.toString(gen.transitions.get(gen.getHash("aroints")))+"\""+LevDistanceWorker.getLevDistance("baritone", "aroints"));
//		System.out.println(gen.getHash("notaries")+":\""+Arrays.toString(gen.transitions.get(gen.getHash("notaries")))+"\""+LevDistanceWorker.getLevDistance("baritone", "notaries"));
//		System.out.println(gen.getHash("baritones")+":\""+Arrays.toString(gen.transitions.get(gen.getHash("baritones")))+"\""+LevDistanceWorker.getLevDistance("baritone", "baritones"));
//		System.out.println(gen.getHash("baritone")+":\""+Arrays.toString(gen.transitions.get(gen.getHash("baritone")))+"\""+LevDistanceWorker.getLevDistance("baritone", "baritone"));

		
		// System.out.println(getLevDeistance("acinorsst", "abeinort"));
//		String t[] = "ainorsst:[acinorsst, aeinorsst, aginorsst, ainorss, ainorssst, ainorst, ainrsst, aiorsst]".split(":\\[|,\\s*|\\]");
//		System.out.println("");
	}

	public void generateTransitions() throws InterruptedException, IOException {
		LevDistanceWorker.all = hashArray;
		String[] work = new String[1000];
		for (int i = 0; i < 1000; i++) {
			work[i] = hashArray[i];
		}
		Thread supervisors[] = new Thread[numWorkers];
		LevDistanceWorker peasants[] = new LevDistanceWorker[numWorkers];
		int start = 0, end = 0;
		for (int k = 0; k < numWorkers; k++) {
			start = end;
			end += (hashArray.length / numWorkers) + 1;
			end = (end > hashArray.length - 1) ? hashArray.length - 1 : end;
			peasants[k] = new LevDistanceWorker(start, end);
			supervisors[k] = new Thread(peasants[k]);
			supervisors[k].start();
		}
		// Wait for all threads to finish.
		for (int k = 0; k < numWorkers; k++) {
			supervisors[k].join();
		}

		FileWriter fw = new FileWriter("transitions_threaded.json");
		HashMap<String, String[]> levDistances = new HashMap<String, String[]>();

		for (int k = 0; k < numWorkers; k++) {
			levDistances.putAll(peasants[k].map);
		}
		for (String string : hashArray) {
			String[] temp = levDistances.get(string);
			if (null != temp && temp.length > 0)
				fw.write(string + ":" + Arrays.toString(levDistances.get(string)) + "\n");
		}
		fw.close();
	}

	/**
	 * Since we can rearrange the letters at a step, we use the sorted letters as the key for a hashMap.
	 * @param word
	 * @return sorted letters of the word.
	 */
	public String getHash(String word) {
		char a[] = word.toCharArray();
		Arrays.sort(a);
		return new String(a);
	}

	public void readWordList() throws Exception {

		InputStream fin = BFS.class.getClassLoader().getResourceAsStream(WORDLIST);
		BufferedReader br = new BufferedReader(new InputStreamReader(fin));
		String line = "";
		ArrayList<String> wordlist = new ArrayList<String>(110000);
		wordFreq = new HashMap<String, ArrayList<String>>();

		/*
		 * Read file and make hashed map.
		 */
		while ((line = br.readLine()) != null) {
			wordlist.add(line);
			String hash = getHash(line);
			ArrayList<String> l = wordFreq.get(hash);
			if (null == l) {
				l = new ArrayList<String>();
				l.add(line);
				wordFreq.put(hash, l);
			} else {
				l.add(line);
			}
		}
		br.close();
		fin.close();
		hashArray = (String[]) wordFreq.keySet().toArray(new String[wordFreq.keySet().size()]);
		Arrays.sort(hashArray);
	}

	public void readTransitions() throws IOException {
		InputStream fin = BFS.class.getClassLoader().getResourceAsStream(TRANSITIONLIST);
		BufferedReader br = new BufferedReader(new InputStreamReader(fin));
		String line = "";
		while (null != (line = br.readLine()))  {
			String temp[] = line.split(":\\[|,\\s*|\\]");
			String temp2[]  = new String[temp.length-1];
			for (int i = 1; i < temp.length; i++) {
				temp2[i-1] = temp[i];
			}
			transitions.put(temp[0], temp2);
		}
		br.close();
		fin.close();
	}
}
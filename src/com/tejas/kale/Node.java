package com.tejas.kale;

/**
 * No longer used. 
 * @author tkale
 *
 */
@Deprecated
public class Node  {
	public String hash;
	public Node parent;
	public static String goalHash;
	public Node(String hash) {
		this.hash = hash;
	}

	@Override
	public String toString() {
		return this.hash+":"+LevDistanceWorker.getLevDistance(this.hash, goalHash);
	}
}

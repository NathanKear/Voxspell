package VoxspellPrototype;

import java.util.ArrayList;
import java.util.HashMap;

public class Level {

	private boolean _isUnlocked = false;
	private HashMap<String, int[]> _levelMap;
	private ArrayList<String> _currentlyFailedList = new ArrayList<String>();
	private ArrayList<String> _currentlyMasteredList = new ArrayList<String>();
	private String _levelName;

	/**
	 * Creates a new level with a name and HashMap storing all the words and their stats
	 * 
	 * @param levelName - name of level
	 * @param levelMap - HashMap with words and stats
	 */
	public Level(String levelName, HashMap<String, int[]> levelMap) {
		_levelName = levelName;
		_levelMap = levelMap;
	}

	/**
	 * Unlocks the level
	 */
	public void unlockLevel() {
		_isUnlocked = true;
	}

	/**
	 * This method returns whether the level is unlocked
	 * 
	 * @return whether the level is unlocked
	 */
	public boolean isUnlocked() {
		return _isUnlocked;
	}

	/**
	 * Returns the level HashMap
	 * 
	 * @return
	 */
	public HashMap<String, int[]> getMap() {
		return _levelMap;
	}
	
	/**
	 * Returns the size of the level map
	 * 
	 * @return
	 */
	public int Size() {
		return _levelMap.size();
	}

	/**
	 * Returns the name of the Level
	 * 
	 * @return
	 */
	public String levelName() {
		return _levelName;
	}

	/**
	 * Adds a word to the currently failed list
	 * 
	 * @param word - failed word
	 */
	public void addToFailed(String word){
		if(!_currentlyFailedList.contains(word)) {
			_currentlyFailedList.add(word);
		}
	}

	/**
	 * Removes a word from the currently failed list
	 * 
	 * @param word - faulted or mastered word
	 */
	public void removeFromFailed(String word){
		_currentlyFailedList.remove(word);
	}

	/**
	 * Returns the words which are currently failed for this Level
	 * 
	 * @return
	 */
	public ArrayList<String> getFailedWords() {
		return _currentlyFailedList;
	}
	
	/**
	 * Adds a word to the currently mastered list
	 * 
	 * @param word - mastered word
	 */
	public void addToMastered(String word){
		if(!_currentlyMasteredList.contains(word)) {
			_currentlyMasteredList.add(word);
		}
	}

	/**
	 * Removes a word from the currently mastered list
	 * 
	 * @param word - failed or faulted word
	 */
	public void removeFromMastered(String word){
		_currentlyMasteredList.remove(word);
	}

	/**
	 * Returns the current mastered words for this Level
	 * 
	 * @return
	 */
	public ArrayList<String> getMasteredWords() {
		return _currentlyMasteredList;
	}

	/**
	 * Clears the stats for this Level
	 */
	public void ClearStats() {
		for (int[] stats : _levelMap.values()) {
			for (int i = 0; i < stats.length; i++) {
				stats[i] = 0;
			}
		}
	}
}

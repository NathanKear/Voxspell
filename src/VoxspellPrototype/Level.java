package VoxspellPrototype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Level {

	private boolean _isUnlocked = false;
	private HashMap<String, List<Character>> _levelMap;
	private String _levelName;

	/**
	 * Creates a new level with a name and HashMap storing all the words and their stats
	 * 
	 * @param levelName - name of level
	 * @param levelMap - HashMap with words and stats
	 */
	public Level(String levelName, HashMap<String, List<Character>> levelMap) {
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
	public HashMap<String, List<Character>> getMap() {
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
	 * Add eiter a pass or fail to the words overall statistics.
	 * @param word Word concerned
	 * @param success TRUE if attempt was successful, FALSE if attempt was a failure.
	 */
	public void AddToWordStat(String word, boolean success) {
		List<Character> newStat = _levelMap.get(word);
		if (success) {
			newStat.add('p');
		} else {
			newStat.add('f');
		}
		_levelMap.put(word, newStat);
	}
	
	/**
	 * Get the statistics count for a given word
	 * 
	 * @param word Word to get stats of
	 * @param success Whether to count word success of word failures
	 * @param history How far back to look in the words history
	 * @return Number of successes or failures counted
	 */
	public int GetStatCount(String word, boolean success, int history) {
		List<Character> wordStats = _levelMap.get(word);
		int count = 0;
		
		for (int i = wordStats.size() - 1; i >= Math.max(0, wordStats.size() - history); i--) {
			if (success && wordStats.get(i) == 'p') {
				count++;
			} else if (!success && wordStats.get(i) == 'f') {
				count++;
			}
		}
		
		return count;
	}
	
	public int GetAttemptCount(String word) {
		return _levelMap.get(word).size();
	}
	
	public double GetStatSuccessRate(String word) {
		int successes = GetStatCount(word, true, Integer.MAX_VALUE);
		int totalAttempts = GetAttemptCount(word);
		
		return (double)successes / totalAttempts;
	}
	
	public String GetStatSuccessRateFormatted(String word) {
		int successes = GetStatCount(word, true, Integer.MAX_VALUE);
		int totalAttempts = GetAttemptCount(word);
		
		if (totalAttempts == 0)
			return "-";
		
		double rate = (double)successes / totalAttempts;
		rate *= 100.0;
		return String.format("%.1f", rate) + "%";
	}
	
	/**
	 * Get list of unique words to use for quiz.
	 * 
	 * @param wordCount Words to be in list
	 * @returnl
	 */
	public List<String> GetWordsBias(int wordCount) {
		if (_levelMap.size() <= wordCount) {
			return new ArrayList<String>(_levelMap.keySet());
		} else {
			List<String> pickList = new ArrayList<String>();
			List<String> choosenWords = new ArrayList<String>();
			
			for (String word : _levelMap.keySet()) {
				int entryCount = GetStatCount(word, false, 5) + 1;
				
				for (int i = entryCount; i > 0; i--) {
					pickList.add(word);
				}
			}
			
			Collections.shuffle(pickList);
			
			for (int i = 0; i < wordCount; i++) {
				String word = pickList.get(0);
				pickList.remove(word);
				
				choosenWords.add(word);
			}
			
			return choosenWords;
		}
	}
	
	/**
	 * Clears the stats for this Level
	 */
	public void ClearStats() {
		for (List<Character> stats : _levelMap.values()) {
			stats.clear();
		}
	}
}

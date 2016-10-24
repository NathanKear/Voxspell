package VoxspellPrototype.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author nathan kear & charles carey
 *
 */
public class LevelModel {
	
	private final int _goldThreshold = 18;
	private final int _silverThreshold = 14;
	private final int _bronzeThreshold = 10;
	
	public enum Medal {
		None,
		Bronze,
		Silver,
		Gold
	}

	private int _record;
	private boolean _isUnlocked = false;
	private HashMap<String, List<Character>> _levelMap;
	private String _levelName;

	/**
	 * Creates a new level with a name and HashMap storing all the words and their stats
	 * 
	 * @param levelName - name of level
	 * @param levelMap - HashMap with words and stats
	 */
	public LevelModel(String levelName, HashMap<String, List<Character>> levelMap) {
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
		
		// Count instances of 'p' and 'f' in word stats file
		for (int i = wordStats.size() - 1; i >= Math.max(0, wordStats.size() - history); i--) {
			if (success && wordStats.get(i) == 'p') {
				count++;
			} else if (!success && wordStats.get(i) == 'f') {
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Get overall % success rate for spelling words in level formatted as a string
	 * @return success rate formatted as %aa.a or - if zero attempts made
	 */
	public String GetStatSuccessRateFormattedOverall() {
		int successes = 0;
		int totalAttempts = 0;
		
		// Count total successful spelling attempts and the total attempts overall
		for (String word : _levelMap.keySet()) {
			successes += GetStatCount(word, true, 5);
			totalAttempts += GetAttemptCount(word);
		}
		
		// If zero total attempts return "-" rather than dividing by zero.
		if (totalAttempts == 0)
			return "-";
		
		double rate = (double)successes / totalAttempts;
		rate *= 100.0;
		return String.format("%.1f", rate) + "%";
	}
	
	public int GetAttemptCount(String word) {
		return _levelMap.get(word).size();
	}
	
	/**
	 * Get success rate for specified word
	 * @param word Word to get success rate of
	 * @return
	 */
	public double GetStatSuccessRate(String word) {
		int successes = GetStatCount(word, true, Integer.MAX_VALUE);
		int totalAttempts = GetAttemptCount(word);
		
		return (double)successes / totalAttempts;
	}
	
	/**
	 * Get success rate % for specified word formatted as a string
	 * @param word Word to get success rate of
	 * @return Success rate expressed as percentage %aa.a or - if zero attempts made
	 */
	public String GetStatSuccessRateFormatted(String word) {
		int successes = GetStatCount(word, true, Integer.MAX_VALUE);
		int totalAttempts = GetAttemptCount(word);
		
		// Return - to avoid dividing by zero
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
				pickList.removeAll(Collections.singleton(word));
				
				choosenWords.add(word);
			}
			
			return choosenWords;
		}
	}
	
	/**
	 * Get random set of unique words from wordlist
	 * @param wordCount Max unique words to return. 
	 * Note if wordCount is greater than the number of words in list then the function simply return all words in the list
	 * @return
	 */
	public List<String> GetWordsNonBias(int wordCount) {
		List<String> list  = new ArrayList<String>(_levelMap.keySet());
		Collections.shuffle(list);
		
		if (_levelMap.size() <= wordCount) { // Have to return whole list
			return list;
		} else { // Return part of list
			return list.subList(0, wordCount);
		}
	}
	
	/**
	 * Clears the stats for this Level
	 */
	public void ClearStats() {
		for (List<Character> stats : _levelMap.values()) {
			stats.clear();
		}
		
		_record = 0;
	}
	
	/**
	 * Submit the users results from time trial
	 * @param correct Total words correctly spelled
	 * @return true if new record, false if not a new record
	 */
	public boolean SubmitCorrectResponses(int correct) {
		if (correct > this._record) {
			this._record = correct;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Get the best medal that has currently been gained in this level
	 * @return
	 */
	public Medal GetMedal() {
		if (this._record >= _goldThreshold) {
			return Medal.Gold;
		} else if (this._record >= _silverThreshold) {
			return Medal.Silver;
		} else if (this._record >= _bronzeThreshold) {
			return Medal.Bronze;
		}
		
		return Medal.None;
	}
	
	/**
	 * Get current record spelling attempt for wordlist
	 * @return
	 */
	public int GetCurrentRecord() {
		return this._record;
	}
	
	/**
	 * Get number of correct answers needed to get gold medal
	 * @return
	 */
	public int GetGoldThreshold() {
		return this._goldThreshold;
	}
	
	/**
	 * Get number of correct answers needed to get silver medal
	 * @return
	 */
	public int GetSilverThreshold() {
		return this._silverThreshold;
	}
	
	/**
	 * Get number of correct answers needed to get bronze medal
	 * @return
	 */
	public int GetBronzeThreshold() {
		return this._bronzeThreshold;
	}
}

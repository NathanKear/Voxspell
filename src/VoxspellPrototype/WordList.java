package VoxspellPrototype;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WordList extends ArrayList<Level> {

	private static WordList _instance = null;

	private WordList() {
		super();
	}

	/**
	 * This constructor applies the singleton method so that there is one global WordList object
	 * @return the WordList
	 */
	public static WordList GetWordList() {
		if (_instance == null) {
			_instance = initialiseNathansAwesomeDataStructure("NZCER-spelling-lists.txt");
			loadStatsFromFile(_instance);
		}

		return _instance;
	}

	/**
	 * Puts the WordList back into the state it was at at start up
	 * 
	 * @return the reloaded WordList
	 */
	public WordList ReloadWordList() {
		//Reload the WordList by loading stats from file again
		_instance = initialiseNathansAwesomeDataStructure("NZCER-spelling-lists.txt");

		return _instance;
	}

	/**
	 * Clears the WordList and reads the stats from file again
	 */
	public void clearWordList() {
		this.clear();

		_instance = initialiseNathansAwesomeDataStructure("NZCER-spelling-lists.txt");
	}

	/**
	 * Clears all the stats currently saved in the WordList
	 */
	public void ClearStats() {
		for (Level level : this) {
			level.ClearStats();
		}
	}

	/**
	 * Unlock the lowest currently locked level.
	 * @return return the name of the unlocked level, otherwise null.
	 */
	public String UnlockNextLevel() {
		for (Level level : this) {
			if (!level.isUnlocked()) {
				level.unlockLevel();
				return level.levelName();
			}
		}

		return null;
	}

	/**
	 * Return highest level unlocked.
	 * @return Highest level unlocked. Null if none unlocked.
	 */
	public Level HighestUnlockedLevel() {
		for (int i = this.size() - 1; i >= 0; i--) {
			if (this.get(i).isUnlocked())
				return this.get(i);
		}

		return null;
	}

	/**
	 * Saves all the stats currently in the WordList to a text file
	 */
	public void saveWordListToDisk() {
		File f = new File("Word-Log");

		try {

			//Deleting and creating the file to write into it fresh
			f.delete();
			f.createNewFile();

			BufferedWriter textFileWriter = new BufferedWriter(new FileWriter(f));

			for(int i = 0; i < this.size(); i++) {
				//Getting a level from the hash map
				Level level = this.get(i);
				HashMap<String, int[]> levelMap = level.getMap();

				//Getting an iterator to go over all the words in the level hash map
				Iterator<Map.Entry<String, int[]>> wordIterator = levelMap.entrySet().iterator();

				//Looping through all the words in the level hash map using the iterator
				while(wordIterator.hasNext()) {
					Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) wordIterator.next();

					//Getting the stats array associated with the pair from the level hash map
					int[] stats = (int[]) pair.getValue();

					//Looping through the stats array
					for(int k = 0; k < 3; k++) {
						//If at any point a stat is not zero then the word has been tested so it should be saved to disk and 
						//the loop should be broken out of
						if(stats[k] != 0) {
							String word = (String) pair.getKey();
							textFileWriter.append(level.levelName() + " " + word + " " + stats[0] + " " + stats[1] + " " + stats[2] + "\n");
							break;
						}
					}
				}
			}

			//Saving which files are unlocked
			for(int i = 0; i < this.size(); i++) {
				//Getting a level from the hash map
				Level level = this.get(i);
				if(level.isUnlocked()) {
					textFileWriter.append("unlock " + level.levelName() + "\n");
				}
			}

			//Saving which words are currently failed
			for(int i = 0; i < this.size(); i++) {
				//Getting a level from the hash map
				Level level = this.get(i);
				List<String> failedWords = level.getFailedWords();
				for(int j = 0; j < failedWords.size(); j++) {
					textFileWriter.append("failed " + level.levelName() + " " + failedWords.get(j) + "\n");
				}
			}

			//Saving which words are currently mastered
			for(int i = 0; i < this.size(); i++) {
				//Getting a level from the hash map
				Level level = this.get(i);
				List<String> masteredWords = level.getMasteredWords();
				for(int j = 0; j < masteredWords.size(); j++) {
					textFileWriter.append("mastered " + level.levelName() + " " + masteredWords.get(j) + "\n");
				}
			}


			textFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads stats from the Word-Log file into the WordList object
	 * 
	 * @param wordList
	 * @return WordList
	 */
	private static WordList loadStatsFromFile(WordList wordList) {
		File savedWords = new File("Word-Log");

		WordList wordlist = WordList.GetWordList();

		try {

			//Reading words from file if they exist
			if(savedWords.exists()) {
				BufferedReader statsReader = new BufferedReader(new FileReader(savedWords));

				String line = "";
				while((line = statsReader.readLine()) != null) {
					//If it's a level which needs to be unlocked
					if(line.contains("unlock")) {
						line = line.replaceAll("unlock ", "");
						//Loop through all levels to find the right one and unlock it
						for(int i = 0 ; i < wordlist.size(); i++) {
							Level level = wordList.get(i);
							String levelName = level.levelName();
							if(levelName.equals(line)) {
								level.unlockLevel();
							}
						}
						//Else if it's a word which needs to be added to the failed list
					} else if(line.contains("failed")) {
						line = line.replaceAll("failed ", "");
						String[] splitLine = line.split("\\s+");
						String levelName = "";
						//Getting the level name
						for(int i = 0; i < splitLine.length - 1; i++) {
							if(i != splitLine.length - 2) {
								levelName += splitLine[i] + " ";
							} else {
								levelName += splitLine[i];	
							}
						}
						//Getting the level associated with the word and adding the word to its failed list
						Level level = wordlist.getLevelFromName(levelName);
						level.addToFailed(splitLine[splitLine.length - 1]);
						
						//Else if the word needs to be added to the mastered list
					} else if (line.contains("mastered")) {
						line = line.replaceAll("mastered ", "");
						String[] splitLine = line.split("\\s+");
						String levelName = "";
						
						//Getting the level name
						for(int i = 0; i < splitLine.length - 1; i++) {
							if(i != splitLine.length - 2) {
								levelName += splitLine[i] + " ";
							} else {
								levelName += splitLine[i];	
							}
						}
						//Getting the level associated with the word and adding the word to its mastered list
						Level level = wordlist.getLevelFromName(levelName);
						level.addToMastered(splitLine[splitLine.length - 1]);
						
						//Else its the stats for a word
					} else {
						//Splitting each line by spaces
						String[] wordAndStats = line.split("\\s+"); 

						//Getting the key for the level hash map
						int lengthOfLevelName = 1 + wordAndStats.length - 5;
						String levelName = "";
						for(int i = 0; i < lengthOfLevelName; i++) {
							if(i != lengthOfLevelName - 1) {
								levelName += wordAndStats[i] + " ";
							} else {
								levelName += wordAndStats[i];	
							}
						}
						levelName.trim();

						//Getting the word to use as a key in the level map
						String wordKey = wordAndStats[2];

						//Getting the level map
						Level level = wordlist.getLevelFromName(levelName);
						HashMap<String, int[]> levelMap = level.getMap();

						//Getting the stats paired with the word
						int[] stats = levelMap.get(wordKey);

						//Set each of the stats to be what they are from file
						stats[0] = Integer.parseInt(wordAndStats[5 - lengthOfLevelName]);
						stats[1] = Integer.parseInt(wordAndStats[5 - lengthOfLevelName + 1]);
						stats[2] = Integer.parseInt(wordAndStats[5 - lengthOfLevelName + 2]);

						//Hash the stats and word back into the hashmap
						levelMap.put(wordKey, stats);

					}
				}
				statsReader.close();
			}
		} catch (IOException e) {

		}


		return wordList;
	}

	/**
	 * Create the WordList with words from a file
	 * 
	 * @param fileName - the name of a file with words to be tested 
	 * @return WordList
	 */
	private static WordList initialiseNathansAwesomeDataStructure(String fileName) {
		//Creating the file to read from
		File wordList = new File(fileName);

		String line;
		int lvlCounter = 1;
		String levelName = "";
		boolean lastLineWasWord = false;

		//Initialising the data structures
		WordList nathansAwesomeDataStructure = new WordList();
		HashMap<String, int[]> levelHashMap = new HashMap<String, int[]>();

		try {
			//Creating the reader to loop through each line in the text file
			BufferedReader textFileReader = new BufferedReader(new FileReader(wordList));

			while((line = textFileReader.readLine()) != null) {

				//If the first char is % then its the name of the level
				if(line.charAt(0) == '%') {

					if(lastLineWasWord) {
						Level level = new Level(levelName, levelHashMap);
						nathansAwesomeDataStructure.add(level);
					}

					levelName = line.substring(1, line.length());

					//Create the hashmap for that level
					levelHashMap = new HashMap<String, int[]>();

					lastLineWasWord = false;

				} else {

					//Hashing each word to the level hashmap
					levelHashMap.put(line, new int[3]);

					lastLineWasWord = true;
				}

			}
			//Adding the last level in to the list
			Level level = new Level(levelName, levelHashMap);
			nathansAwesomeDataStructure.add(level);
			textFileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return nathansAwesomeDataStructure;
	}

	/**
	 * Returns random selection of words from the wordlist specified.
	 * @param wordlistName Name of the list to select random words
	 * @param listCount Number of words to return
	 * @return
	 */
	public List<String> GetRandomWords(String wordlistName, int listCount) {
		List<String> wordlist = null;

		// Get list of words from named wordlist
		HashMap<String, int[]> levelMap = getLevelFromName(wordlistName).getMap();

		Collection<String> wordset = levelMap.keySet();
		wordlist = new ArrayList<String>(wordset);

		// Shuffle list
		java.util.Collections.shuffle(wordlist);

		// Ensure we don't try to return more elements than exist in the list
		listCount = Math.min(listCount, wordlist.size());

		// Return first n elements from shuffled list (essentially n random elements)
		return wordlist.subList(0, listCount);
	}

	/**
	 * Getting a random assortment of the failed words from a level
	 * 
	 * @param wordlistName - name of the level
	 * @param listCount - number of words to return if there are that many words available
	 * @return -  a list of currently failed words
	 */
	public List<String> GetRandomFailedWords(String wordlistName, int listCount) {
		List<String> wordlist = null;

		// Get list of words from named wordlist
		Level level = getLevelFromName(wordlistName);

		Collection<String> wordset = level.getFailedWords();
		wordlist = new ArrayList<String>(wordset);

		// Shuffle list
		java.util.Collections.shuffle(wordlist);

		// Ensure we don't try to return more elements than exist in the list
		listCount = Math.min(listCount, wordlist.size());

		// Return first n elements from shuffled list (essentially n random elements)
		return wordlist.subList(0, listCount);
	}

	/**
	 * This method is called when a word is failed to update the stats
	 * 
	 * @param word - the failed words
	 * @param wordlistName - the level name
	 */
	public void failedWord(String word, String wordlistName) {

		Level level = getLevelFromName(wordlistName);

		//Get list of words from wordlist
		HashMap<String, int[]> levelMap = level.getMap();

		//Getting the stats array associated with the word
		int[] stats = levelMap.get(word);

		//Increasing the failed count by 1
		stats[0]++;	

		//Adding the word to the failed list in the level
		level.addToFailed(word);
		level.removeFromMastered(word);

		//Putting the word back in the table with the updated stats
		levelMap.put(word, stats);

	}

	/**
	 * This method is called when a word is faulted to update the stats
	 * 
	 * @param word - the failed words
	 * @param wordlistName - the level name
	 */
	public void faultedWord(String word, String wordlistName) {

		Level level = getLevelFromName(wordlistName);

		//Get list of words from wordlist
		HashMap<String, int[]> levelMap = level.getMap();

		//Getting the stats array associated with the word
		int[] stats = levelMap.get(word);

		//Increasing the faulted count by 1
		stats[1] += 1;	

		//Removing the word from the failed list if it is there
		level.removeFromFailed(word);
		level.removeFromMastered(word);

		//Putting the word back in the table with the updated stats
		levelMap.put(word, stats);

	}

	/**
	 * This method is called when a word is mastered to update the stats
	 * 
	 * @param word - the failed words
	 * @param wordlistName - the level name
	 */
	public void masteredWord(String word, String wordlistName) {

		Level level = getLevelFromName(wordlistName);

		//Get list of words from wordlist
		HashMap<String, int[]> levelMap = level.getMap();

		//Getting the stats array associated with the word
		int[] stats = levelMap.get(word);

		//Increasing the mastered count by 1
		stats[2] += 1;	

		//Removing the word from the failed list if it is there
		level.removeFromFailed(word);
		level.addToMastered(word);

		//Putting the word back in the table with the updated stats
		levelMap.put(word, stats);

	}

	/**
	 * This method is used to return a level associated with a name
	 * 
	 * @param name - the level's name
	 * @return a Level
	 */
	public Level getLevelFromName(String name) {
		Level level = null;
		for(int i = 0; i < this.size(); i++) {
			String levelName;
			if((levelName  = this.get(i).levelName()).equals(name)) {
				level = this.get(i);
			}
		}
		return level;
	}

	/**
	 * Adding a failed word to a Level's failed list
	 * 
	 * @param word - failed word
	 * @param levelName - name of the Level
	 */
	public void addToLevelsFailedList(String word, String levelName) {
		Level level = getLevelFromName(levelName);
		level.addToFailed(word);
	}

	/**
	 * Remove a word from a Level's failed list when it is faulted or mastered
	 * 
	 * @param word - word to be removed
	 * @param levelName - name of the Level
	 */
	public void removeFromLevelFailedList(String word, String levelName) {
		Level level = getLevelFromName(levelName);
		level.removeFromFailed(word);
	}
}

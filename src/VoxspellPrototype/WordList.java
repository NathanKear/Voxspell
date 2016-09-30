package VoxspellPrototype;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
				HashMap<String, List<Character>> levelMap = level.getMap();

				//Getting an iterator to go over all the words in the level hash map
				Iterator<Map.Entry<String, List<Character>>> wordIterator = levelMap.entrySet().iterator();

				//Looping through all the words in the level hash map using the iterator
				while(wordIterator.hasNext()) {
					Map.Entry<String, List<Character>> pair = (Map.Entry<String, List<Character>>) wordIterator.next();

					//Getting the stats array associated with the pair from the level hash map
					List<Character> stats = (List<Character>) pair.getValue();
					
					String word = (String) pair.getKey();
					
					String lineToWrite = level.levelName() + "#" + word;
					
					
					//Looping through the stats array
					for(int j = 0; j < stats.size(); j++) {	
						if (j == 0)
							lineToWrite = lineToWrite + "#";

						lineToWrite = lineToWrite + stats.get(j).toString();
					}
					
					textFileWriter.append(lineToWrite + "\n");
				}
			}

			//Saving which files are unlocked
			for(int i = 0; i < this.size(); i++) {
				//Getting a level from the hash map
				Level level = this.get(i);
				if(level.isUnlocked()) {
					textFileWriter.append("!unlock " + level.levelName() + "\n");
				}
			}

//			//Saving which words are currently failed
//			for(int i = 0; i < this.size(); i++) {
//				//Getting a level from the hash map
//				Level level = this.get(i);
//				List<String> failedWords = level.getFailedWords();
//				for(int j = 0; j < failedWords.size(); j++) {
//					textFileWriter.append("failed " + level.levelName() + " " + failedWords.get(j) + "\n");
//				}
//			}

//			//Saving which words are currently mastered
//			for(int i = 0; i < this.size(); i++) {
//				//Getting a level from the hash map
//				Level level = this.get(i);
//				List<String> masteredWords = level.getMasteredWords();
//				for(int j = 0; j < masteredWords.size(); j++) {
//					textFileWriter.append("mastered " + level.levelName() + " " + masteredWords.get(j) + "\n");
//				}
//			}


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
			if (savedWords.exists()) {
				BufferedReader statsReader = new BufferedReader(new FileReader(savedWords));
				
				String line = "";
				while ((line = statsReader.readLine()) != null) {
					// Line contains level to unlock
					if (line.contains("!unlock")) {
						
						// Trim line down to just the level to replace
						line = line.replaceAll("!unlock", "");
						
						// Check all levels if they need to be unlocked
						for (Level level : wordlist) {
							if (level.levelName().equals(line)) {
								// Unlock the level
								level.unlockLevel();
							}
						}
						
					} else {
						
						String level = line.split("#")[0];
						String word = line.split("#")[1];
						Level l = wordlist.getLevelFromName(level);
						
						if (line.split("#").length > 2) { 
							String stat = line.split("#")[2];
						
							List<Character> statList = new ArrayList<Character>();
							for (char c : stat.toCharArray()) {
								statList.add(c);
							}
							
							l.getMap().put(word, statList);
						} else {
							l.getMap().put(word, new ArrayList<Character>());
						}					
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return wordlist;
//
//		try {

//			//Reading words from file if they exist
//			if(savedWords.exists()) {
//				BufferedReader statsReader = new BufferedReader(new FileReader(savedWords));
//
//				String line = "";
//				while((line = statsReader.readLine()) != null) {
//					//If it's a level which needs to be unlocked
//					if(line.contains("unlock")) {
//						line = line.replaceAll("unlock ", "");
//						//Loop through all levels to find the right one and unlock it
//						for(int i = 0 ; i < wordlist.size(); i++) {
//							Level level = wordList.get(i);
//							String levelName = level.levelName();
//							if(levelName.equals(line)) {
//								level.unlockLevel();
//							}
//						}
//						//Else if it's a word which needs to be added to the failed list
//					} else if(line.contains("failed")) {
//						line = line.replaceAll("failed ", "");
//						String[] splitLine = line.split("\\s+");
//						String levelName = "";
//						//Getting the level name
//						for(int i = 0; i < splitLine.length - 1; i++) {
//							if(i != splitLine.length - 2) {
//								levelName += splitLine[i] + " ";
//							} else {
//								levelName += splitLine[i];	
//							}
//						}
//						//Getting the level associated with the word and adding the word to its failed list
//						Level level = wordlist.getLevelFromName(levelName);
//						level.addToFailed(splitLine[splitLine.length - 1]);
//						
//						//Else if the word needs to be added to the mastered list
//					} else if (line.contains("mastered")) {
//						line = line.replaceAll("mastered ", "");
//						String[] splitLine = line.split("\\s+");
//						String levelName = "";
//						
//						//Getting the level name
//						for(int i = 0; i < splitLine.length - 1; i++) {
//							if(i != splitLine.length - 2) {
//								levelName += splitLine[i] + " ";
//							} else {
//								levelName += splitLine[i];	
//							}
//						}
//						//Getting the level associated with the word and adding the word to its mastered list
//						Level level = wordlist.getLevelFromName(levelName);
//						level.addToMastered(splitLine[splitLine.length - 1]);
//						
//						//Else its the stats for a word
//					} else {
//						//Splitting each line by spaces
//						String[] wordAndStats = line.split("\\s+"); 
//
//						//Getting the key for the level hash map
//						int lengthOfLevelName = 1 + wordAndStats.length - 5;
//						String levelName = "";
//						for(int i = 0; i < lengthOfLevelName; i++) {
//							if(i != lengthOfLevelName - 1) {
//								levelName += wordAndStats[i] + " ";
//							} else {
//								levelName += wordAndStats[i];	
//							}
//						}
//						levelName.trim();
//
//						//Getting the word to use as a key in the level map
//						String wordKey = wordAndStats[2];
//
//						//Getting the level map
//						Level level = wordlist.getLevelFromName(levelName);
//						HashMap<String, int[]> levelMap = level.getMap();
//
//						//Getting the stats paired with the word
//						int[] stats = levelMap.get(wordKey);
//
//						//Set each of the stats to be what they are from file
//						stats[0] = Integer.parseInt(wordAndStats[5 - lengthOfLevelName]);
//						stats[1] = Integer.parseInt(wordAndStats[5 - lengthOfLevelName + 1]);
//						stats[2] = Integer.parseInt(wordAndStats[5 - lengthOfLevelName + 2]);
//
//						//Hash the stats and word back into the hashmap
//						levelMap.put(wordKey, stats);
//
//					}
//				}
//				statsReader.close();
//			}
//		} catch (IOException e) {
//
//		}
//
//
//		return wordList;
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
		HashMap<String, List<Character>> levelHashMap = new HashMap<String, List<Character>>();

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
					levelHashMap = new HashMap<String, List<Character>>();

					lastLineWasWord = false;

				} else {

					//Hashing each word to the level hashmap
					levelHashMap.put(line, new ArrayList<Character>());

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
		return getLevelFromName(wordlistName).GetWordsBias(listCount);
	}
	
	public void AddWordStat(String word, String list, boolean success) {
		getLevelFromName(list).AddToWordStat(word, success);
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
}

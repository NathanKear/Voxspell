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
	private static String _listFileName = VoxspellPrototype.TXT_FILE;

	private WordList() {
		super();
	}
	
	public static void SetWordFile(String filename) {
		_listFileName = filename;
	}

	/**
	 * This constructor applies the singleton method so that there is one global WordList object
	 * @return the WordList
	 */
	public static WordList GetWordList() {
		if (_instance == null) {
			_instance = initialiseNathansAwesomeDataStructure(_listFileName);
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
		_instance = initialiseNathansAwesomeDataStructure(_listFileName);

		return _instance;
	}

	/**
	 * Clears the WordList and reads the stats from file again
	 */
	public void clearWordList() {
		this.clear();

		_instance = initialiseNathansAwesomeDataStructure(_listFileName);
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
						} else if (line.split("#").length > 1) {
							l.getMap().put(word, new ArrayList<Character>());
						}					
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return wordlist;
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
				if(line.length() > 0 && line.charAt(0) == '%') {

					if(lastLineWasWord) {
						Level level = new Level(levelName, levelHashMap);
						level.unlockLevel();
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
			level.unlockLevel();
			nathansAwesomeDataStructure.add(level);
			
			textFileReader.close();
			
			if (nathansAwesomeDataStructure.size() == 0) {
				PopupWindow.DeployPopupWindow("No levels found within the text file. Are you sure a line begins with '%' to indicate a level.");
			}
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

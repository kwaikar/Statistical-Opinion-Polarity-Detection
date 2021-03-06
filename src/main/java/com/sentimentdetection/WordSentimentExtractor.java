package com.sentimentdetection;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.sentimentdetection.common.SentimentType;
import com.sentimentdetection.common.WordNetSentiment;

/**
 * This class returns polarity of polar words.
 * 
 * @author Kanchan Waikar Date Created : Mar 31, 2016 - 10:39:33 PM
 *
 */
public class WordSentimentExtractor {

	Map<String, WordNetSentiment> wordnetSentimentMap = new HashMap<String, WordNetSentiment>();

	/**
	 * This function loads WordNet as well as SentiWordNet in memory.
	 * 
	 * @param pathToSWN
	 * @throws IOException
	 */
	public void loadSentiments(String pathToSWN) throws IOException {

		String inputFile = FileUtils.readFileToString(new File(this.getClass().getResource(pathToSWN).getFile()));
		System.out.println(inputFile.length());
		for (String line : inputFile.split(DataPreparationHelper.LINE_TOKENIZER_REGEX)) {
	
			if (line.trim().length()>0 && !line.trim().startsWith("#")) {
				//System.out.println(line);
				String[] data = line.split("\t");
				if (Double.parseDouble(data[2]) != 0.0 && Double.parseDouble(data[3]) != 0.0) {
					String[] inputs = data[4].split(" ");
					for (String singleSynTerm : inputs) {

						WordNetSentiment sentiment = new WordNetSentiment(data[0], data[1], Double.parseDouble(data[2]),
								Double.parseDouble(data[3]), singleSynTerm.substring(0, singleSynTerm.indexOf('#')),
								data[5]);
						wordnetSentimentMap.put(singleSynTerm.substring(0, singleSynTerm.indexOf('#')), sentiment);
					}
				}
			}
		}
	}
 
	/**
	 * @param name
	 * @return
	 */
	public static Map<String, Integer> maintainQueue(Map<String,Integer>map,String name) {
		if(map.get(name)!=null)
		{
			 map.put(name, new Integer((map.get(name))+1));
		}
		else
		{
			map.put(name,1);
		}
		System.out.println(map);
		return map;
	}

	/**
	 * This method returns Sentiment type for given adjective in given sentence.
	 * 
	 * @param adjective
	 * @return
	 */
	public SentimentType getSentimentForAdjective(String adjective ) {
		WordNetSentiment sentiment = wordnetSentimentMap.get(adjective);
		if (sentiment != null) {
			if (sentiment.getPosScore() > sentiment.getNegativeScore()) {
				return SentimentType.POSITIVE;
			} else {
				return SentimentType.NEGATIVE;
			}
		} else {
			return SentimentType.NEUTRAL;
		}

	}
}

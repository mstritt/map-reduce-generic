/*
 *     Orbit, a versatile image analysis software for biological image-based quantification.
 *     Copyright (C) 2009 - 2016 Actelion Pharmaceuticals Ltd., Gewerbestrasse 16, CH-4123 Allschwil, Switzerland.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.actelion.research.mapReduceGeneric.examples;

import com.actelion.research.mapReduceGeneric.IMapReduce;
import com.actelion.research.mapReduceGeneric.executors.IMapReduceExecutor;
import com.actelion.research.mapReduceGeneric.executors.MapReduceExecutorLocalMultiCore;
import com.actelion.research.mapReduceGeneric.utils.Helpers;
import com.actelion.research.mapReduceGeneric.utils.KeyValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Wordcount demo (it seems that every map reduce framework must have s.th. like this).
 * It reads numURLs random wikipedia pages and outputs the 20 most frequent words.
 * <p>
 * Please don't run this too ofter with numURLs set to a high value to not block Wikipedia... !
 */
public class WordCount implements IMapReduce<String, String, Integer> {
    private static int numURLs = 100;
    private String[] stopWords = new String[]{"this", "not", "or", "do", "does", "you", "with", "from", "this", "was", "were", "for"};

    public List<KeyValue<String, Integer>> map(String element) {
        List<KeyValue<String, Integer>> wordList = new ArrayList<KeyValue<String, Integer>>();
        try {
            String content = getRedirectedContentStr(new URL(element));
            StringTokenizer tokenizer = new StringTokenizer(content, " ");
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken().trim();
                if (accept(word)) {
                    //System.out.println("word: "+word);
                    wordList.add(new KeyValue<String, Integer>(word, 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("WordCount error: " + e.getMessage());   // important: throw a RuntimeException here so that the mapReduce framework reschedules this job
        }
        return wordList;
    }

    public Integer reduce(String key, List<Integer> valueList) {
        int cnt = 0;
        for (Integer v : valueList) {
            cnt += v;
        }
        return cnt;
    }

    public Collection<String> parseParams(String s) {
        return Helpers.parseParamsString(s);
    }

    public String serializeParam(String element) {
        return element;
    }

    private boolean accept(String s) {
        if (s == null) return false;
        if (s.length() < 5) return false;
        if (s.contains("<") || s.contains(">") || s.contains("\"") || s.contains(":") || s.contains("=") || s.contains(",") || s.contains(";") || s.contains(".") || s.contains("/") || s.contains("\\") || s.contains("(") || s.contains(")"))
            return false;
        for (String stop : stopWords) {
            if (s.equalsIgnoreCase(stop)) return false;
        }
        return true;
    }

    public String getRedirectedContentStr(URL url) {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String newUrl = conn.getHeaderField("Location");   // the random page will redirect us...
            conn.disconnect();
            URL url2 = newUrl != null ? new URL(newUrl) : url;

            in = new BufferedReader(
                    new InputStreamReader(url2.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine + "\n");
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        List<String> urlList = new ArrayList<String>(numURLs);
        for (int i = 0; i < numURLs; i++) {
            urlList.add("http://en.wikipedia.org/wiki/Special:Random");
        }

        //IMapReduceExecutor<String,String,Integer> executor = new MapReduceExecutorLocal<String, String, Integer>();
        IMapReduceExecutor<String, String, Integer> executor = new MapReduceExecutorLocalMultiCore<String, String, Integer>();
        Map<String, Integer> wordCountMap = executor.execute(urlList, new WordCount());

        // output most frequent words
        List<KeyValue<String, Integer>> wordCountList = new ArrayList<KeyValue<String, Integer>>(wordCountMap.size());
        for (String s : wordCountMap.keySet()) {
            wordCountList.add(new KeyValue<String, Integer>(s, wordCountMap.get(s)));
        }
        Collections.sort(wordCountList, new Comparator<KeyValue<String, Integer>>() {
            public int compare(KeyValue<String, Integer> o1, KeyValue<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        System.out.println("Most frequent words:");
        for (int i = 0; i < 20; i++) {
            KeyValue<String, Integer> kv = wordCountList.get(i);
            System.out.println(kv.getKey() + ": " + kv.getValue());
        }
    }


}

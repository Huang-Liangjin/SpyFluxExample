package jp.co.drecom.spyfluxexample.util;

import java.util.ArrayList;
import java.util.List;

import jp.co.drecom.spyflux.util.SpyLog;
import jp.co.drecom.spyfluxexample.model.Word;

/**
 * Created by huang_liangjin on 2016/03/04.
 */
public class DummyDBHelper {
    public static final String TAG = "DummyDBHelper";
    public static synchronized List<Word> getTodayWords() {
        SpyLog.printLog(TAG, "");
        List<Word> wordList = new ArrayList<Word>();
//        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 500; i++) {
            Word word = new Word();
            word.id = i;
            word.word = "word" + i;
            word.skillLevel = 1;
            word.timesStudy = 0;
            word.grammar= "grammar" + i;
            word.meaning = "meaning" + i;
            word.pronounce = "pronounce" + i;
            word.sampleSentence = "sample sentence " + i;
            wordList.add(word);
        }
        return wordList;
    }

    public static void saveToDB(Word word) {
        SpyLog.printLog(TAG, "save to db is called");
    }
}

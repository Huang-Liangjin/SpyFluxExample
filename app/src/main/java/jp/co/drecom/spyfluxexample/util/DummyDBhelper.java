package jp.co.drecom.spyfluxexample.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.co.drecom.spyflux.util.SpyLog;
import jp.co.drecom.spyfluxexample.model.Word;

/**
 * Created by huang_liangjin on 2016/03/04.
 */
public class DummyDBhelper {
    public static final String TAG = "DummyDBHelper";
    public static synchronized List<Word> getTodayWords() {
        SpyLog.printLog(TAG, "");
        List<Word> wordList = new ArrayList<Word>();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 1; i <= 500; i++) {
            Word word = new Word();
            word.id = i;
            word.word = "word" + i;
            word.skillLevel = random.nextInt(10);
            word.timesStudy = random.nextInt();
            word.grammar= "grammar" + i;
            word.meaning = "meaning" + i;
            word.pronounce = "pronounce" + i;
            word.sampleSentance = "sample sentance " + i;
            wordList.add(word);
        }
        return wordList;
    }
}

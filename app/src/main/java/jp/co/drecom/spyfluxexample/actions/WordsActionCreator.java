package jp.co.drecom.spyfluxexample.actions;

import java.util.List;

import jp.co.drecom.spyflux.action.SpyAction;
import jp.co.drecom.spyflux.action.SpyActionCreator;
import jp.co.drecom.spyflux.util.SpyLog;
import jp.co.drecom.spyfluxexample.model.Word;
import jp.co.drecom.spyfluxexample.util.DummyDBhelper;

/**
 * Created by huang_liangjin on 2016/03/04.
 */
public class WordsActionCreator extends SpyActionCreator {

    public static final String TAG = "WordsActionCreator";

    private WordsActionCreator(){}

    public static WordsActionCreator getInstance() {
        return SingletonHolder.mInstance;
    }

    private static class SingletonHolder {
        private static WordsActionCreator mInstance = new WordsActionCreator();
    }

    public void getTodayWords() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpyLog.printLog(TAG, "getTodayWords");
                List<Word> wordList = DummyDBhelper.getTodayWords();
                SpyAction action = new SpyAction.Builder(ActionType.GET_WORDS_TODAY)
                        .bundle(ActionDataKey.WORDS_TODAY, wordList).build();
                post(action);
            }
        }).start();
        //1. すべてのDataを取得する
        //2. すべてのDataをActionに埋め込む
        //3. そのActionをpostする
    }

    public void getWordDetails(int wordId) {

    }
}

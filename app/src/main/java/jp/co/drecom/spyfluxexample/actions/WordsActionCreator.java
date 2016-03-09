package jp.co.drecom.spyfluxexample.actions;

import java.util.List;

import jp.co.drecom.spyflux.action.SpyStoreAction;
import jp.co.drecom.spyflux.action.SpyActionCreator;
import jp.co.drecom.spyflux.store.SpyStore;
import jp.co.drecom.spyflux.util.SpyLog;
import jp.co.drecom.spyfluxexample.model.Word;
import jp.co.drecom.spyfluxexample.util.DummyDBHelper;

/**
 * Created by huang_liangjin on 2016/03/04.
 *
 * WordsStoreの非同期操作はここで定義します。
 * 非同期操作終わったら、Store.onActionCreated()を呼び出します。
 */
public class WordsActionCreator extends SpyActionCreator {

    public static final String TAG = "WordsActionCreator";

    public WordsActionCreator(SpyStore store){
        super(store);
    }

    public void getTodayWords() {
        // もちろん、実際マルチスレッド作る時、直接thread使うことではなく、
        // ExecutorServiceを使った方がよいです。
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpyLog.printLog(TAG, "getTodayWords");
                List<Word> wordList = DummyDBHelper.getTodayWords();
                SpyStoreAction action = new SpyStoreAction.Builder(ActionType.GET_WORDS_TODAY)
                        .bundle(ActionDataKey.WORDS_TODAY, wordList).build();
                store.onActionCreated(action);
            }
        }).start();
    }
}

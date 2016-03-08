package jp.co.drecom.spyfluxexample.stores;

import java.util.ArrayList;
import java.util.List;

import jp.co.drecom.spyflux.action.SpyAction;
import jp.co.drecom.spyflux.dispatcher.SpyDispatcher;
import jp.co.drecom.spyflux.store.SpyStore;
import jp.co.drecom.spyflux.util.SpyLog;
import jp.co.drecom.spyfluxexample.actions.ActionDataKey;
import jp.co.drecom.spyfluxexample.actions.ActionType;
import jp.co.drecom.spyfluxexample.actions.WordsActionCreator;
import jp.co.drecom.spyfluxexample.model.Word;
import jp.co.drecom.spyfluxexample.util.DummyDBHelper;

/**
 * Created by huang_liangjin on 2016/03/04.
 *
 * Business Logicの実装は全てここでやります
 * データの倉庫。データはここしか保存しない。
 */
public class WordsStore implements SpyStore{

    public static WordsStore getInstance() {
        return SingletonHolder.mInstance;
    }

    private WordsStore() {}

    private static class SingletonHolder {
        private static WordsStore mInstance = new WordsStore();
    }

    private volatile List<Word> mWords = new ArrayList<Word>();

    private WordsActionCreator mWordsActionCreator = new WordsActionCreator(this);

    @Override
    public void onProcess(SpyAction viewAction) {
        SpyLog.printLog(TAG, "function call from UI");
        SpyAction action = viewAction;
        switch (viewAction.getType()) {
            case ActionType.GET_WORDS_TODAY:
                //非同期操作はActionCreator経由でやります
                mWordsActionCreator.getTodayWords();
                return;
            case ActionType.SAVE_STUDY_RESULT_AND_GET_NEXT_WORD:
                //同期操作はStoreでやります
                action = saveStudyResultAndShowNextWord(viewAction);
                break;
            default:
                return;
        }
        SpyDispatcher.notifyView(action);
    }

    @Override
    public void onActionCreated(SpyAction action) {
        SpyLog.printLog(TAG, "data received from ActionCreator");
        switch (action.getType()) {
            case ActionType.GET_WORDS_TODAY:
                mWords = (List<Word>) action.getData().get(ActionDataKey.WORDS_TODAY);
                break;
            default:
                return;
        }
        SpyDispatcher.notifyView(action);
    }

    private SpyAction saveStudyResultAndShowNextWord(SpyAction action) {
        int currentWordNum = action.getData().keyAt(0);
        int studyResult = (Integer)action.getData().get(currentWordNum);
        Word current = mWords.get(currentWordNum);
        current.timesStudy++;
        current.skillLevel = studyResult;
        DummyDBHelper.saveToDB(current);
        return new SpyAction.Builder(action.getType())
                .bundle(ActionDataKey.WORD_NEXT, get(currentWordNum + 1))
                .build();
    }

//    /**
//     * こちらのonReceive()関数は、ActionCreatorからのActionを受け取るための専用関数です。
//     * ViewからStoreにアクセスする際、これを使わない
//     * @param action
//     */
//    @Override
//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onReceive(SpyAction action) {
//        SpyLog.printLog(TAG, "data received from ActionCreator");
//        switch (action.getType()) {
//            case ActionType.GET_WORDS_TODAY:
//                mWords = (List<Word>) action.getData().get(ActionDataKey.WORDS_TODAY);
//                break;
//            default:
//                return;
//        }
//        onDataProcessed(new SpyProcessedAction(StoreId.WORDS_STORE, action));
//    }

//    @Override
//    public void cleanStore() {
//        mWords.clear();
//        SpyLog.printLog(TAG, "data is cleared.");
//    }

    // このメソッドを呼び出すのはUI thread、扱うデータを取得するのは別thread
    // synchronized に要注意
    public Word get(int serialNumber){
        Word word = null;
        if (serialNumber < 0) {
            serialNumber = 0;
        } else if (serialNumber > (mWords.size() -1 )) {
            serialNumber = mWords.size() - 1;
        }

        if (mWords != null) {
            try {
                word = mWords.get(serialNumber);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }
        return word;
    }

    public List<Word> getAll() {
        return mWords;
    }

    public boolean isEmpty() {
        if (mWords == null || mWords.isEmpty()) {
            return true;
        }
        return false;
    }

}

package jp.co.drecom.spyfluxexample.stores;

import java.util.ArrayList;
import java.util.List;

import jp.co.drecom.spyflux.action.SpyStoreAction;
import jp.co.drecom.spyflux.action.SpyViewAction;
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
 *
 * Singleton, lazy initialization.
 */
public class WordsStore implements SpyStore{

    public static WordsStore getInstance() {
        return SingletonHolder.mInstance;
    }

    private WordsStore() {}

    private static class SingletonHolder {
        private static WordsStore mInstance = new WordsStore();
    }

    /**
     * データはここに保存
     */
    private volatile List<Word> mWords = new ArrayList<>();

    /**
     * WordsStoreの非同期操作は、WordsActionCreatorに任せます。
     */
    private WordsActionCreator mWordsActionCreator = new WordsActionCreator(this);

    /**
     * Viewがこの関数を使って、ProcessリクエストをStoreに送ります。
     * @param viewAction UIからもらったActionです。
     */
    @Override
    public void onProcessViewRequest(SpyViewAction viewAction) {
        SpyLog.printLog(TAG, "function call from UI");
        SpyStoreAction action;
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
        //処理終わったら、Viewに通知します、処理させてデータはactionの中に内包します。
        //Store -> UIのActionは、SpyStoreActionです。
        SpyDispatcher.notifyView(action);
    }

    /**
     * ActionCreatorはAction生成したら、この関数を呼び出して、Storeに通知します。
     * StoreはこのActionをViewに転送します。
     *
     * @param action ActionCreatorが生成したactionです、データが含まれてます。
     */
    @Override
    public void onActionCreated(SpyStoreAction action) {
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

    private SpyStoreAction saveStudyResultAndShowNextWord(SpyViewAction action) {
        int currentWordNum = (Integer)action.getData().get(0);
        int studyResult = (Integer)action.getData().get(1);
        Word current = mWords.get(currentWordNum);
        current.timesStudy++;
        current.skillLevel = studyResult;
        DummyDBHelper.saveToDB(current);
        return new SpyStoreAction.Builder(action.getType())
                .bundle(ActionDataKey.WORD_NEXT, get(currentWordNum + 1))
                .build();
    }

    /**
     * こちらのメソッドはViewに使われています。
     * View -> Storeの通信方式は二つが有りまして
     * 1. ViewはSpyViewActionを作って、Storeに送って、StoreはそのActionを処理して、新しいSpyStoreAction
     *    を生成して、SpyDispatcherよりViewに送信します。
     * 2. Viewは直接Storeのメソッドを呼び出して、欲しいデータを取得します。
     *
     * Storeは基本的に同期メソッドしかない＋シングルートンですので、わざわざ一番目のやり方しなくても、
     * 二番目のやり方で十分です。
     * が、もしView側がStoreに処理してもらいたいデータが多く、又Viewが要求する処理は重く、非同期にする必要がある時に、
     * やはり一番目の通信方式がもっと適切だと思います。
     *
     * @param serialNumber
     * @return
     */
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
        return mWords == null || mWords.isEmpty();
    }

}

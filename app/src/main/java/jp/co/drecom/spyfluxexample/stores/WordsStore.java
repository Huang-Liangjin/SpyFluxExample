package jp.co.drecom.spyfluxexample.stores;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import jp.co.drecom.spyflux.action.SpyAction;
import jp.co.drecom.spyflux.action.SpyProcessedAction;
import jp.co.drecom.spyflux.store.SpyStore;
import jp.co.drecom.spyfluxexample.actions.ActionDataKey;
import jp.co.drecom.spyfluxexample.actions.ActionType;
import jp.co.drecom.spyfluxexample.model.Word;

/**
 * Created by huang_liangjin on 2016/03/04.
 *
 * Business Logicの実装は全てここでやります
 * データの倉庫。データはここしか保存しない。
 */
public class WordsStore extends SpyStore{

    private List<Word> mWords;

    @Override
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onReceive(SpyAction action) {
        switch (action.getType()) {
            case ActionType.GET_WORDS_TODAY:
                mWords = (List<Word>) action.getData().get(ActionDataKey.WORDS_TODAY);
                break;
            default:
                return;
        }
        onDataProcessed(new SpyProcessedAction(StoreId.WORDS_STORE, action));
    }

}

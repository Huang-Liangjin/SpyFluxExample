package jp.co.drecom.spyfluxexample.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import jp.co.drecom.spyflux.action.SpyProcessedAction;
import jp.co.drecom.spyflux.dispatcher.SpyDispatcher;
import jp.co.drecom.spyflux.ui.SpyView;
import jp.co.drecom.spyfluxexample.R;
import jp.co.drecom.spyfluxexample.actions.ActionDataKey;
import jp.co.drecom.spyfluxexample.model.Word;
import jp.co.drecom.spyfluxexample.stores.StoreId;

/**
 * A fragment representing a list of Items.
 */
public class WordsFragment extends Fragment implements SpyView{

    private WordRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WordsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new WordRecyclerViewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SpyDispatcher.getInstance().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SpyDispatcher.getInstance().unregister(this);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyChange(SpyProcessedAction action) {
        //TODO
        switch (action.getStoreId()) {
            case StoreId.WORDS_STORE:
                mAdapter.setData((List<Word>) action.getAction().getData().get(ActionDataKey.WORDS_TODAY));
                break;
            default:
                return;
        }

    }

}

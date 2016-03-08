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

import jp.co.drecom.spyflux.action.SpyAction;
import jp.co.drecom.spyflux.dispatcher.SpyDispatcher;
import jp.co.drecom.spyflux.ui.SpyView;
import jp.co.drecom.spyflux.util.SpyLog;
import jp.co.drecom.spyfluxexample.R;
import jp.co.drecom.spyfluxexample.actions.ActionDataKey;
import jp.co.drecom.spyfluxexample.actions.ActionType;
import jp.co.drecom.spyfluxexample.model.Word;
import jp.co.drecom.spyfluxexample.stores.WordsStore;

/**
 * A fragment representing a list of Items.
 */
public class WordsFragment extends Fragment implements SpyView, View.OnClickListener{

    public static final String TAG = "WordsFragment";

    private WordRecyclerViewAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WordsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new WordRecyclerViewAdapter(WordsStore.getInstance().getAll(), this);

        SpyLog.printLog(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        SpyLog.printLog(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setAdapter(mAdapter);

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SpyLog.printLog(TAG, "onAttach");
        mListener = (OnListFragmentInteractionListener) context;
        SpyDispatcher.register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SpyLog.printLog(TAG, "onDetach");
        mListener = null;
        SpyDispatcher.unregister(this);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveStoreMsg(SpyAction action) {
        //TODO
        switch (action.getType()) {
            case ActionType.GET_WORDS_TODAY:
                SpyLog.printLog(TAG, "UI received the notice from Store");
                mAdapter.setDatas((List<Word>) action.getData().get(ActionDataKey.WORDS_TODAY));
                break;
            default:
                return;
        }

    }

    /**
     * WordsFragmentの唯一のclick listener.
     * WrodsFragmentの全ての画面要素のクリックイベントはここで受け取ります
     * @param v
     */
    @Override
    public void onClick(View v) {
        int itemPosition = mRecyclerView.getChildAdapterPosition(v);
        if (itemPosition == RecyclerView.NO_POSITION) {
            //RecyclerView以外のViewのクリックイベント
            return;
        }
        mListener.onListFragmentInteraction(itemPosition);

        SpyLog.printLog(TAG, "word No. " + itemPosition + " is clicked");
    }

    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(int serialNumber);
    }
}

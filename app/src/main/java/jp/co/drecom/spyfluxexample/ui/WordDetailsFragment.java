package jp.co.drecom.spyfluxexample.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jp.co.drecom.spyflux.action.SpyAction;
import jp.co.drecom.spyflux.dispatcher.SpyDispatcher;
import jp.co.drecom.spyflux.ui.SpyView;
import jp.co.drecom.spyflux.util.SpyLog;
import jp.co.drecom.spyfluxexample.R;
import jp.co.drecom.spyfluxexample.actions.ActionDataKey;
import jp.co.drecom.spyfluxexample.actions.ActionType;
import jp.co.drecom.spyfluxexample.model.Word;
import jp.co.drecom.spyfluxexample.stores.WordsStore;
import jp.co.drecom.spyfluxexample.util.AnimationUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link WordDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordDetailsFragment extends Fragment implements SpyView, View.OnClickListener{

    public static final String TAG = "WordDetailsFragment";

    private static final String ARG_WORD_NUMBER = "wordSerialNumber";

    private AnimatorListenerAdapter mShowListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            AnimationUtil.hideRevealEffect(mAnimationView, mHideListener);
            updateData(mWordNum);

        }
    };

    private AnimatorListenerAdapter mHideListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mAnimationView.setVisibility(View.INVISIBLE);
            mBtnClickable = true;
        }
    };

    private int mWordNum;

    private TextView mTextViewWordDetails;
    private Button mBtnMastered;
    private Button mBtnConfused;
    private Button mBtnForgotten;
    private View mAnimationView;

    private boolean mBtnClickable = true;


    public WordDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param wordNum the serial number of the word which chosen to be show.
     * @return A new instance of fragment WordDetailsFragment.
     */
    public static WordDetailsFragment newInstance(int wordNum) {
        WordDetailsFragment fragment = new WordDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WORD_NUMBER, wordNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mWordNum = savedInstanceState.getInt(ARG_WORD_NUMBER);
        }
        if (getArguments() != null) {
            mWordNum = getArguments().getInt(ARG_WORD_NUMBER);
        }
        SpyLog.printLog(TAG, "onCreate, wordNum is " + mWordNum);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_WORD_NUMBER, mWordNum);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_word_details, container, false);
        mTextViewWordDetails = (TextView) view.findViewById(R.id.txt_word_details);
        mBtnMastered = (Button) view.findViewById(R.id.btn_mastered);
        mBtnConfused = (Button) view.findViewById(R.id.btn_confused);
        mBtnForgotten = (Button) view.findViewById(R.id.btn_forgotten);
        mAnimationView = view.findViewById(R.id.view_animation);
        mTextViewWordDetails.setOnClickListener(this);
        mBtnMastered.setOnClickListener(this);
        mBtnConfused.setOnClickListener(this);
        mBtnForgotten.setOnClickListener(this);
        mTextViewWordDetails.setText(WordsStore.getInstance().get(mWordNum).toString());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SpyDispatcher.register(this);
        SpyLog.printLog(TAG, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SpyDispatcher.unregister(this);
        SpyLog.printLog(TAG, "onDetach");
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveStoreMsg(SpyAction action) {
        switch (action.getType()) {
            case ActionType.SAVE_STUDY_RESULT_AND_GET_NEXT_WORD:
                Word word = (Word)action.getData().get(ActionDataKey.WORD_NEXT);
                SpyLog.printLog(TAG, "next word id is " + word.id);
                mWordNum = word.id;
                //do not update UI here, update UI in the onAnimationEnd() method.
//                updateData(word.id);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SpyLog.printLog(TAG, "onDestroy");
    }

    @Override
    public void onClick(View v) {
        if (!mBtnClickable) {
            return;
        }
        mBtnClickable = false;
        runAnimation(v);
        switch (v.getId()) {
            case R.id.txt_word_details:
                SpyLog.printLog(TAG, "textview is clicked");
                break;
            case R.id.btn_mastered:
                requestProcess(mWordNum, Word.STUDY_RESULT_MASTERED);
                break;
            case R.id.btn_confused:
                requestProcess(mWordNum, Word.STUDY_RESULT_CONFUSED);
                break;
            case R.id.btn_forgotten:
                requestProcess(mWordNum, Word.STUDY_RESULT_FORGOTTEN);
                break;
        }
    }

    private void requestProcess(int wordNum, int studyResult) {
        SpyAction action = new SpyAction.Builder(ActionType.SAVE_STUDY_RESULT_AND_GET_NEXT_WORD)
                .bundle(wordNum, studyResult)
                .build();
        WordsStore.getInstance().onProcess(action);
    }

    private void updateData(int wordNum) {
        //画面遷移動画
        SpyLog.printLog(TAG, "updateUI is called");
        mWordNum = wordNum;
        mTextViewWordDetails.setText(WordsStore.getInstance().get(mWordNum).toString());
    }

    private void runAnimation(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int x = location[0] + (view.getWidth()/2);
        // minus the action bar height.
        // should calculate it more carefully when developing the app.
        int y = location[1] - 160;
        AnimationUtil.showRevealEffect(mAnimationView, x, y, mShowListener);
    }

}

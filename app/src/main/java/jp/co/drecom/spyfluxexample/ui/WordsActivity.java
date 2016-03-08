package jp.co.drecom.spyfluxexample.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import jp.co.drecom.spyflux.action.SpyAction;
import jp.co.drecom.spyflux.util.SpyLog;
import jp.co.drecom.spyfluxexample.R;
import jp.co.drecom.spyfluxexample.actions.ActionType;
import jp.co.drecom.spyfluxexample.stores.WordsStore;

public class WordsActivity extends AppCompatActivity implements WordsFragment.OnListFragmentInteractionListener{
    public static final String TAG = "WordsActivity";
    private FragmentManager mFragmentManager  = getSupportFragmentManager();
    private WordsFragment mWordsFragment = null;
    private WordDetailsFragment mWordDetailsFragment = null;

    private WordsStore mWordsStore = WordsStore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpyLog.printLog(TAG, "onCreate");
//        mWordsStore.register();
        setContentView(R.layout.activity_words);
        if (WordsStore.getInstance().isEmpty()) {
            SpyAction action = new SpyAction.Builder(ActionType.GET_WORDS_TODAY)
                   .build();
            mWordsStore.onProcess(action);
//            WordsActionCreator.getInstance().getTodayWords();
        }

        mWordsFragment = (WordsFragment) mFragmentManager.findFragmentByTag(WordsFragment.TAG);
        if (mWordsFragment == null) {
            SpyLog.printLog(TAG, "WordsFragment is newed");
            mWordsFragment = new WordsFragment();
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, mWordsFragment, WordsFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mWordsStore.unregister();
        SpyLog.printLog(TAG, "onDestroy");

    }

    @Override
    public void onListFragmentInteraction(int serialNumber) {
        if (WordsStore.getInstance().get(serialNumber) != null) {
            showWordDetails(serialNumber);
        } else {
            Toast.makeText(this, "something wrong when finding the word", Toast.LENGTH_SHORT).show();
        }
    }

    private void showWordDetails(int serialNumber) {
        mWordDetailsFragment = (WordDetailsFragment) mFragmentManager.findFragmentByTag(WordDetailsFragment.TAG);
        if (mWordDetailsFragment == null) {
            SpyLog.printLog(TAG, "WordDetailsFragment is newed");
            mWordDetailsFragment = new WordDetailsFragment().newInstance(serialNumber);
            SpyLog.printLog(TAG, "WordDetailsFragment is not in layout");
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, mWordDetailsFragment, WordDetailsFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }
}

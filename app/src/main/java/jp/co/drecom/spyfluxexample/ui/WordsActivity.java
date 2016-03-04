package jp.co.drecom.spyfluxexample.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import jp.co.drecom.spyfluxexample.R;
import jp.co.drecom.spyfluxexample.actions.WordsActionCreator;
import jp.co.drecom.spyfluxexample.stores.WordsStore;

public class WordsActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager  = getSupportFragmentManager();
    private WordsFragment mWordsFragment = null;
    private WordDetailsFragment mWordDetailsFragment = null;

    private WordsStore mWordsStore = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mWordsStore == null) {
            mWordsStore = new WordsStore();
        }
        mWordsStore.register();
        setContentView(R.layout.activity_words);
        WordsActionCreator.getInstance().getTodayWords();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (mWordsFragment == null) {
            mWordsFragment = new WordsFragment();
        }
        fragmentTransaction.replace(R.id.container, mWordsFragment).addToBackStack(null).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWordsStore.unregister();

    }
}

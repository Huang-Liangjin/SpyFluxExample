package jp.co.drecom.spyfluxexample.actions;

/**
 * Created by huang_liangjin on 2016/03/03.
 *
 * SpyStoreActionとSpyViewActionで使われてるActionキー
 * アプリの全てのビジネスロジックの名前はここで定義します。
 */
public interface ActionType {
    //単語向けのAction type
    int GET_WORDS_TODAY =                           0X00010001;
    int SAVE_STUDY_RESULT_AND_GET_NEXT_WORD =       0X00010002;

}

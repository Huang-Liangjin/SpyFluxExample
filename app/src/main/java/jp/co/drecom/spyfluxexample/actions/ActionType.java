package jp.co.drecom.spyfluxexample.actions;

/**
 * Created by huang_liangjin on 2016/03/03.
 */
public interface ActionType {
    //単語向けのAction type
    int GET_WORDS_TODAY =   0X00010001;
    int GET_WORD =          0X00010002;
    int SHOW_WORD =         0X00010003;
    int MASTER_WORD =       0X00010004;
    int FUZZY_WORD =        0X00010005;
    int UNKNOWN_WORD =      0X00010006;
}

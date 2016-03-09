package jp.co.drecom.spyfluxexample.model;

/**
 * Created by huang_liangjin on 2016/03/04.
 * 単語情報
 *
 */
public class Word {
    //把握
    public static final int STUDY_RESULT_MASTERED = 3;
    //あやふや
    public static final int STUDY_RESULT_CONFUSED = 2;
    //知らない
    public static final int STUDY_RESULT_FORGOTTEN = 1;

    public int id;
    public String word;
    public String pronounce;
    public String meaning;
    public String sampleSentence;
    public String grammar;
    public int timesStudy;
    public int skillLevel; //熟練程度

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", pronounce='" + pronounce + '\'' +
                ", meaning='" + meaning + '\'' +
                ", sampleSentence='" + sampleSentence + '\'' +
                ", grammar='" + grammar + '\'' +
                ", timesStudy=" + timesStudy +
                ", skillLevel=" + skillLevel +
                '}';
    }

    public static String skillLevel(int skillLevel) {
        switch (skillLevel) {
            case STUDY_RESULT_MASTERED:
                return "master";
            case STUDY_RESULT_CONFUSED:
                return "confuse";
            case STUDY_RESULT_FORGOTTEN:
            default:
                return "unknown";
        }
    }
}

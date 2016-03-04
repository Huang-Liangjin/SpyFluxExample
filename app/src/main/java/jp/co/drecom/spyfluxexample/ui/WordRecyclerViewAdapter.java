package jp.co.drecom.spyfluxexample.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jp.co.drecom.spyflux.util.SpyLog;
import jp.co.drecom.spyfluxexample.R;
import jp.co.drecom.spyfluxexample.model.Word;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class WordRecyclerViewAdapter extends RecyclerView.Adapter<WordRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "WordRecyclerViewAdapter";

    private List<Word> mValues;
//    private final OnListFragmentInteractionListener mListener;

//    public WordRecyclerViewAdapter(List<Word> items, OnListFragmentInteractionListener listener) {
//        mValues = items;
//        mListener = listener;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mWord.setText(mValues.get(position).word);
        holder.mSkillLevel.setText(String.valueOf(mValues.get(position).skillLevel));
        //TODO
//        holder.mView.setOnClickListener();
    }

    @Override
    public int getItemCount() {
        if (mValues == null) {
            return 0;
        }
        return mValues.size();
    }

    public void setData(List<Word> data) {
        SpyLog.printLog(TAG, "setData is called");
        mValues = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mWord;
        public final TextView mSkillLevel;
        public Word mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mWord = (TextView) view.findViewById(R.id.id);
            mSkillLevel = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSkillLevel.getText() + "'";
        }
    }
}

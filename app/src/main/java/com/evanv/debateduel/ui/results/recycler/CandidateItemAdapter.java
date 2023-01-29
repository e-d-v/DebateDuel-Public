package com.evanv.debateduel.ui.results.recycler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evanv.debateduel.R;
import com.evanv.debateduel.ui.ClickListener;
import com.evanv.debateduel.ui.DownloadImageFromInternet;

import java.util.List;

public class CandidateItemAdapter extends RecyclerView.Adapter<CandidateItemAdapter.CandidateViewHolder> {
    public final List<CandidateItem> mCandidateItemList; // List of candidates
    private final Activity mActivity; // Context of current Activity
    private final ClickListener mListener; // Handles onClick

    /**
     * Constructs an adapter for MainActivity's recyclerview
     *
     * @param candidateItemList List of candidates to display
     */
    @SuppressWarnings("unused")
    public CandidateItemAdapter(List<CandidateItem> candidateItemList, Activity activity, ClickListener listener) {
        mCandidateItemList = candidateItemList;
        mActivity = activity;
        mListener = listener;
    }

    /**
     * Initialize an individual layout for MainActivity's recyclerview
     *
     * @param parent ViewGroup associated with the parent recyclerview
     * @param viewType not used, required by override
     * @return a DayViewHolder associated with the new layout
     */
    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates a new day_item, whose data will be filled by the DayViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.candidate_item, parent, false);

        return new CandidateViewHolder(view);
    }

    /**
     * Sets the date for the day_item and gives the component RecyclerViews their data
     *
     * @param holder DayViewHolder that represents the day to be changed
     * @param position Index in the dayItemList to be represented
     */
    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
        CandidateItem item = mCandidateItemList.get(position);
        holder.mCandidateName.setText(item.getCandidateName());
        if (item.getLastWeekBreakdown() == null) {
            holder.mNameLayout.setGravity(Gravity.CENTER_VERTICAL);
            holder.mCandidateScore.setVisibility(View.INVISIBLE);
            holder.mCandidateScore.setTextSize(0);
        }
        else {
            holder.mNameLayout.setGravity(Gravity.NO_GRAVITY);
            holder.mCandidateScore.setVisibility(View.VISIBLE);
            holder.mCandidateScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            holder.mCandidateScore.setText(item.getLastWeekBreakdown());
        }
        holder.mMoreInfoButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getCandidatePositionURL()));
            mActivity.startActivity(browserIntent);
        });

        if (mListener != null) {
            holder.mCandidateImage.setOnClickListener(v -> mListener.onClick(position));
        }

        (new DownloadImageFromInternet(holder.mCandidateImage, mActivity)).execute(item.getCandidateImg());
    }

    @Override
    public int getItemCount() {
        return mCandidateItemList.size();
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class CandidateViewHolder extends RecyclerView.ViewHolder {
        public ImageView mCandidateImage;
        public TextView mCandidateName;
        public TextView mCandidateScore;
        public ImageButton mMoreInfoButton;
        public LinearLayout mNameLayout;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);

            mCandidateImage = itemView.findViewById(R.id.candidateImageView);
            mCandidateName = itemView.findViewById(R.id.candidateNameTextView);
            mCandidateScore = itemView.findViewById(R.id.candidateScoreTextView);
            mMoreInfoButton = itemView.findViewById(R.id.chevronMoreInfo);
            mNameLayout = itemView.findViewById(R.id.nameLayout);
        }
    }

}

package com.evanv.debateduel.ui.leagues.recycler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evanv.debateduel.R;
import com.evanv.debateduel.ui.DownloadImageFromInternet;
import com.evanv.debateduel.ui.leagues.standings.LeagueStandingsActivity;

import java.io.InputStream;
import java.util.List;

public class LeagueItemAdapter extends RecyclerView.Adapter<LeagueItemAdapter.CandidateViewHolder> {
    public final List<LeagueItem> mLeagueItemList; // List of candidates
    private final Activity mActivity; // Context of current Activity

    /**
     * Constructs an adapter for MainActivity's recyclerview
     *
     * @param leagueItemList List of candidates to display
     */
    @SuppressWarnings("unused")
    public LeagueItemAdapter(List<LeagueItem> leagueItemList, Activity activity) {
        mLeagueItemList = leagueItemList;
        mActivity = activity;
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
        LeagueItem item = mLeagueItemList.get(position);
        holder.mLeagueName.setText(item.getLeagueName());
        holder.mUserPosition.setText(item.getUserPosition());
        holder.mMoreInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, LeagueStandingsActivity.class);
            intent.putExtra(LeagueStandingsActivity.LEAGUE_ID, position);
            mActivity.startActivity(intent);
        });
        (new DownloadImageFromInternet(holder.mLeagueImageView, mActivity)).execute(item.getLeagueImage());
    }

    @Override
    public int getItemCount() {
        return mLeagueItemList.size();
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class CandidateViewHolder extends RecyclerView.ViewHolder {
        public ImageView mLeagueImageView;
        public TextView mLeagueName;
        public TextView mUserPosition;
        public ImageButton mMoreInfoButton;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);

            mLeagueImageView = itemView.findViewById(R.id.candidateImageView);
            mLeagueName = itemView.findViewById(R.id.candidateNameTextView);
            mUserPosition = itemView.findViewById(R.id.candidateScoreTextView);
            mMoreInfoButton = itemView.findViewById(R.id.chevronMoreInfo);
        }
    }
}

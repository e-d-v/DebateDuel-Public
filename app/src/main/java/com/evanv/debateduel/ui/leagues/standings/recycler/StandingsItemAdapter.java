package com.evanv.debateduel.ui.leagues.standings.recycler;

import static com.evanv.debateduel.ui.picks.PicksFragment.convertDpIntoPx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.evanv.debateduel.R;
import com.evanv.debateduel.logic.LogicSubSystem;
import com.evanv.debateduel.ui.DownloadImageFromInternet;
import com.evanv.debateduel.ui.leagues.recycler.LeagueItem;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.InputStream;
import java.util.List;

public class StandingsItemAdapter extends RecyclerView.Adapter<StandingsItemAdapter.CandidateViewHolder> {
    public final List<StandingsItem> mStandingsItemList; // List of candidates
    private final FragmentActivity mActivity; // Context of current Activity
    private final int mLeagueID;

    private int leagueId;

    @SuppressWarnings("unused")
    public StandingsItemAdapter(List<StandingsItem> standingsItemsList, FragmentActivity activity, int leagueID) {
        mStandingsItemList = standingsItemsList;
        mActivity = activity;
        mLeagueID = leagueID;
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates a new day_item, whose data will be filled by the DayViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.candidate_item, parent, false);

        return new CandidateViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
        StandingsItem item = mStandingsItemList.get(position);
        holder.mUserName.setText(item.getUserName());
        holder.mUserScore.setText(item.getUserScore());
        (new DownloadImageFromInternet(holder.mUserIcon, mActivity)).execute(item.getUserImage());

        holder.mMoreInfoButton.setOnClickListener(v -> {
            final BottomSheetDialog diag = new BottomSheetDialog(mActivity);
            diag.setContentView(R.layout.last_week_info);

            FlexboxLayout flex = diag.findViewById(R.id.flexboxInfo);

            for (int i = 0; i < LogicSubSystem.getInstance().leagues.get(mLeagueID).USERS.get(position).LAST_WEEK_PICKS.size(); i++) {
                LinearLayout ll = new LinearLayout(mActivity);
                int dp10 = convertDpIntoPx(mActivity, 10);
                ll.setPadding(dp10, dp10, dp10, dp10);
                ll.setOrientation(LinearLayout.VERTICAL);
                CardView cardView = new CardView(mActivity);
                cardView.setRadius(convertDpIntoPx(mActivity, 60));
                int size = convertDpIntoPx(mActivity, 60);
                cardView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
                ImageView imageView = new ImageView(mActivity);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
                cardView.addView(imageView);
                ll.addView(cardView);
                TextView score = new TextView(mActivity);
                int position_id = Integer.parseInt(LogicSubSystem.getInstance().leagues.get(mLeagueID).USERS.get(position).LAST_WEEK_PICKS.get(i));
                (new DownloadImageFromInternet(imageView, mActivity)).execute(LogicSubSystem.getInstance().politicians.get(position_id).IMAGE);
                score.setText(LogicSubSystem.getInstance().politicians.get(position_id).CURRENT_SCORE + " points");
                ll.addView(score);
                flex.addView(ll);
            }
            diag.show();
        });
    }

    @Override
    public int getItemCount() {
        return mStandingsItemList.size();
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class CandidateViewHolder extends RecyclerView.ViewHolder {
        public ImageView mUserIcon;
        public TextView mUserName;
        public TextView mUserScore;
        public ImageButton mMoreInfoButton;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);

            mUserIcon = itemView.findViewById(R.id.candidateImageView);
            mUserName = itemView.findViewById(R.id.candidateNameTextView);
            mUserScore = itemView.findViewById(R.id.candidateScoreTextView);
            mMoreInfoButton = itemView.findViewById(R.id.chevronMoreInfo);
        }
    }
}

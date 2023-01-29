package com.evanv.debateduel.ui.picks;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evanv.debateduel.R;
import com.evanv.debateduel.databinding.FragmentPicksBinding;
import com.evanv.debateduel.logic.LogicSubSystem;
import com.evanv.debateduel.logic.Politician;
import com.evanv.debateduel.logic.User;
import com.evanv.debateduel.ui.ClickListener;
import com.evanv.debateduel.ui.DownloadImageFromInternet;
import com.evanv.debateduel.ui.results.recycler.CandidateItem;
import com.evanv.debateduel.ui.results.recycler.CandidateItemAdapter;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class PicksFragment extends Fragment implements ClickListener {

    private FragmentPicksBinding binding;
    private FlexboxLayout mFlexbox;
    private List<Integer> mPicks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PicksViewModel picksViewModel =
                new ViewModelProvider(this).get(PicksViewModel.class);

        binding = FragmentPicksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mFlexbox = root.findViewById(R.id.flexbox);

        mPicks = new ArrayList<>();

        // Call "onClick" for each of user picks
        User currentUser = LogicSubSystem.getInstance().getCurrentUser();
        while(currentUser==null) {
            currentUser = LogicSubSystem.getInstance().getCurrentUser();
        }

        for(String p : currentUser.PICKS) {
            if (p.isEmpty()) {
                break;
            }
            addToUI(Integer.parseInt(p));
        }

        List<CandidateItem> mCandidateItemList = new ArrayList<>();
        for (int i = 0; i < LogicSubSystem.getInstance().politicians.size(); i++) {
            Politician tmp = LogicSubSystem.getInstance().politicians.get(i);
            mCandidateItemList.add(new CandidateItem(tmp.NAME, tmp.BELIEFS, null, tmp.IMAGE));
        }

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        CandidateItemAdapter mAdapter = new CandidateItemAdapter(mCandidateItemList, getActivity(), this);
        RecyclerView recycler = root.findViewById(R.id.candidatesRecycler);
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(llm);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(int position) {
        if (mPicks.contains(position) || mPicks.size() >= 4) {
            return;
        }
        addToUI(position);
        addPick(position);
    }

    public void addToUI(int position) {
        CardView cardView = new CardView(requireContext());
        cardView.setRadius(convertDpIntoPx(requireContext(), 60));
        int size = convertDpIntoPx(requireContext(), 60);
        cardView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        ImageView imageView = new ImageView(requireContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        (new DownloadImageFromInternet(imageView, requireContext())).execute(LogicSubSystem.getInstance().politicians.get(position).IMAGE);
        cardView.addView(imageView);
        mFlexbox.addView(cardView);
        imageView.setOnClickListener(view -> {
            mFlexbox.removeView(cardView);
            removePick(position);
        });
    }

    public void addPick(int position) {
        User currentUser = LogicSubSystem.getInstance().getCurrentUser();
        currentUser.PICKS.add(""+position);
        mPicks.add(position);
    }

    public void removePick(int position) {
        User currentUser = LogicSubSystem.getInstance().getCurrentUser();
        currentUser.PICKS.remove(""+position);
        mPicks.remove(Integer.valueOf(position));
    }

    public static int convertDpIntoPx(Context mContext, float yourdpmeasure) {
        if (mContext == null) {
            return 0;
        }
        Resources r = mContext.getResources();
        int px = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, yourdpmeasure, r.getDisplayMetrics());
        return px;
    }
}
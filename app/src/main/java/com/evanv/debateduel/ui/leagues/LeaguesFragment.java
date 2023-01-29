package com.evanv.debateduel.ui.leagues;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evanv.debateduel.R;
import com.evanv.debateduel.databinding.FragmentLeaguesBinding;
import com.evanv.debateduel.logic.League;
import com.evanv.debateduel.logic.LogicSubSystem;
import com.evanv.debateduel.ui.leagues.recycler.LeagueItem;
import com.evanv.debateduel.ui.leagues.recycler.LeagueItemAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaguesFragment extends Fragment {

    /**
     * Instance fields
     */
    private FragmentLeaguesBinding binding;

    /**
     * onCreateView() method.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The View to be used.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LeaguesViewModel dashboardViewModel =
                new ViewModelProvider(this).get(LeaguesViewModel.class);

        binding = FragmentLeaguesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<LeagueItem> mLeagueItemList = new ArrayList<>();

        LogicSubSystem.getInstance().leagues.sort(new LeagueComparator());
        for (int i = 0; i < LogicSubSystem.getInstance().leagues.size(); i++) {
            League league = LogicSubSystem.getInstance().leagues.get(i);
            mLeagueItemList.add(new LeagueItem(league.NAME, league.USERS.size() + " members", league.PROFILE_PICTURE));
        }

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        LeagueItemAdapter mAdapter = new LeagueItemAdapter(mLeagueItemList, getActivity());
        RecyclerView recycler = root.findViewById(R.id.leagueRecycler);
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(llm);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

class LeagueComparator implements Comparator<League> {
    public int compare(League l1, League l2) {
        return l2.USERS.size() - l1.USERS.size();
    }
}
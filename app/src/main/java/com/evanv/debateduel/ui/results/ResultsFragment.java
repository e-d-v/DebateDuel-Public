package com.evanv.debateduel.ui.results;

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
import com.evanv.debateduel.databinding.FragmentResultsBinding;
import com.evanv.debateduel.logic.LogicSubSystem;
import com.evanv.debateduel.logic.Politician;
import com.evanv.debateduel.ui.results.recycler.CandidateItem;
import com.evanv.debateduel.ui.results.recycler.CandidateItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class ResultsFragment extends Fragment {

    private FragmentResultsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ResultsViewModel resultsViewModel =
                new ViewModelProvider(this).get(ResultsViewModel.class);

        binding = FragmentResultsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<CandidateItem> mCandidateItemList = new ArrayList<>();

        for (int i = 0; i < LogicSubSystem.getInstance().politicians.size(); i++) {
            Politician tmp = LogicSubSystem.getInstance().politicians.get(i);
            StringBuilder output = new StringBuilder();
            for (int j = 0; j  < tmp.SCORES.size(); j ++){
                output.append(tmp.SCORES.get(j).toString());
            }
            mCandidateItemList.add(new CandidateItem(tmp.NAME, tmp.BELIEFS, output.toString(),tmp.IMAGE));
        }

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        CandidateItemAdapter mAdapter = new CandidateItemAdapter(mCandidateItemList, getActivity(), null);
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


}
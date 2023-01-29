package com.evanv.debateduel.ui.results.recycler;

import java.net.URI;

public class CandidateItem {
    private String mLastWeekBreakdown;    // Point breakdown from last week
    private String mCandidatePositionURL; // Link to candidate positions
    private String mCandidateName;        // Name of Candidate
    private String mCandidateImg;         // String to URL

    public CandidateItem(String name, String position, String breakdown, String image) {
        mCandidateName = name;
        mCandidatePositionURL = position;
        mLastWeekBreakdown = breakdown;
        mCandidateImg = image;
    }

    public String getLastWeekBreakdown() {
        return mLastWeekBreakdown;
    }

    public String getCandidatePositionURL() {
        return mCandidatePositionURL;
    }

    public String getCandidateName() {
        return mCandidateName;
    }

    public String getCandidateImg() {
        return mCandidateImg;
    }
}

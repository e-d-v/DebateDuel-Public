package com.evanv.debateduel.ui.leagues.standings;

import static com.evanv.debateduel.ui.picks.PicksFragment.convertDpIntoPx;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evanv.debateduel.R;
import com.evanv.debateduel.logic.League;
import com.evanv.debateduel.logic.LogicSubSystem;
import com.evanv.debateduel.logic.User;
import com.evanv.debateduel.ui.leagues.standings.recycler.StandingsItem;
import com.evanv.debateduel.ui.leagues.standings.recycler.StandingsItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeagueStandingsActivity extends AppCompatActivity {
    public static String LEAGUE_ID = "com.evanv.debateduel.ui.leagues.standings.recycler.LeagueStandingsActivity.EXTRA_LEAGUE";

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_standings);

        int leagueIndex = getIntent().getIntExtra(LEAGUE_ID, -1);

        List<StandingsItem> mStandingsList = new ArrayList<>();

        League temp = LogicSubSystem.getInstance().leagues.get(leagueIndex);

        temp.USERS.sort(new UserComparator());
        for (int i = 0; i < temp.USERS.size() ; i++) {
            User user =  temp.USERS.get(i);
            mStandingsList.add(new StandingsItem(String.format("#%d %s", i+1, user.NAME), String.format("%d points", user.SCORE), user.IMAGE));
        }

        LinearLayoutManager llm = new LinearLayoutManager(this);
        StandingsItemAdapter mAdapter = new StandingsItemAdapter(mStandingsList, this, leagueIndex);
        RecyclerView recycler = findViewById(R.id.standingsRecycler);
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(llm);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("What user would you like to invite?")
                    .setTitle("Add User to League");
            EditText userNameET = new EditText(this);
            userNameET.setHint("Username");
            int dp10 = convertDpIntoPx(this, 10);
            builder.setView(userNameET)
                    .setPositiveButton("Add", (dialog, id) -> {
                        // TODO returns 404 if username does not exist, 502 if gateway error, 1 on success, 132 if username is already in the league.
                        if (inviteUser(userNameET.getText().toString()) == 1) {
                            return;
                        }
                        else {
                            Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
                        }
                    });
            builder.show();
        });
    }

    /**
     * Invite user to a league.
     * @param username Username to invite.
     * @return 404 if username does not exist, 502 if gateway error, 1 on success, 132 if username is already in the league.
     */
    private int inviteUser(String username){
        int leagueIndex = getIntent().getIntExtra(LEAGUE_ID, -1);
        League temp = LogicSubSystem.getInstance().leagues.get(leagueIndex);
        // Check if user exists
        int resultCode = LogicSubSystem.getInstance().checkForUser(username); // 1 if user exists, 404 otherwise, 502 on error

        if (resultCode == 1){
            // Check if user is in league already
            resultCode = LogicSubSystem.getInstance().checkLeagueForUser(temp.NAME, username);
            if(resultCode == 132 || resultCode == 502){
                return resultCode;
            }

            return LogicSubSystem.getInstance().updateUsersInviteStatus(username, LogicSubSystem.getInstance().getCurrentUser().NAME, temp.ID);
        }
        return resultCode; //error otherwise
    }
}

class UserComparator implements Comparator<User> {
    public int compare(User u1, User u2) {
        return u2.SCORE - u1.SCORE;
    }
}
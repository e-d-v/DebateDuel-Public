package com.evanv.debateduel;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.evanv.debateduel.databinding.ActivityMainBinding;
import com.evanv.debateduel.logic.League;
import com.evanv.debateduel.logic.LogicSubSystem;
import com.evanv.debateduel.logic.Politician;
import com.evanv.debateduel.logic.Score;
import com.evanv.debateduel.logic.User;
import com.evanv.debateduel.ui.DownloadImageFromInternet;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_FILE = "com.evanv.debateduel.ui.MainActivity.PREF_FILE";
    private MenuItem mAddLeague;
    private int mCurrNavDestination;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LogicSubSystem.getInstance().politicians.size() == 0) {
            loadData();
        }

        // Inflate the main layout using data binding
        // Binding for the main layout
        com.evanv.debateduel.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup navigation between fragments
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Define top level destinations for the navigation bar
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_picks, R.id.navigation_leagues, R.id.navigation_results)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (mAddLeague != null) {
                mAddLeague.setVisible(navDestination.getId() == R.id.navigation_leagues);
            }
            mCurrNavDestination = navDestination.getId();
        });

        mCurrNavDestination = R.id.navigation_picks;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu ) {
        getMenuInflater().inflate(R.menu.league_action_bar_menu, menu);
        mAddLeague = menu.findItem(R.id.add_league);
        if (mCurrNavDestination != R.id.navigation_leagues) {
            mAddLeague.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_league) {
            final BottomSheetDialog diag = new BottomSheetDialog(this);
            diag.setContentView(R.layout.dialog_add_league);

            EditText nameET = diag.findViewById(R.id.nameET);
            EditText imageET = diag.findViewById(R.id.imageET);

            diag.findViewById(R.id.submitButton).setOnClickListener(v -> {
                addLeague(nameET.getText().toString(), imageET.getText().toString());
                diag.hide();
            });

            diag.show();
        }
        else if (item.getItemId() == R.id.show_user) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.user_page, null);
            builder.setView(view);

            ImageView userIcon = view.findViewById(R.id.userImageView);
            TextView username = view.findViewById(R.id.userNameTextView);
            TextView userScore = view.findViewById(R.id.userScoreTextView);
            TextView emailLocation = view.findViewById(R.id.emailLocationTextView);
            FlexboxLayout flex = view.findViewById(R.id.flexboxInfo);

            // TODO: make this real
            (new DownloadImageFromInternet(userIcon, this)).execute("https://bipartisan-policy-center.imgix.net/wp-content/uploads/2020/10/John-Delaney_Headshot.png");
            username.setText("edv");
            userScore.setText("438 points this year");
            emailLocation.setText("Email: evanvoogd@gmail.com    Location: Minnesota");
            User currentUser;
//            for (int i = 0; i < currentUser.LAST_WEEK_PICKS.size(); i++) {
//                LinearLayout ll = new LinearLayout(this);
//                int dp10 = convertDpIntoPx(this, 10);
//                ll.setPadding(dp10, dp10, dp10, dp10);
//                ll.setOrientation(LinearLayout.VERTICAL);
//                CardView cardView = new CardView(this);
//                cardView.setRadius(convertDpIntoPx(this, 60));
//                int size = convertDpIntoPx(this, 60);
//                cardView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
//                ImageView imageView = new ImageView(this);
//                imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
//                cardView.addView(imageView);
//                ll.addView(cardView);
//                TextView score = new TextView(this);
//                int position_id = Integer.parseInt(currentUser.LAST_WEEK_PICKS.get(i));
//                (new DownloadImageFromInternet(imageView, this)).execute(LogicSubSystem.getInstance().politicians.get(position_id).IMAGE);
//                score.setText(LogicSubSystem.getInstance().politicians.get(position_id).CURRENT_SCORE + " points");
//                ll.addView(score);
//                flex.addView(ll);
//            }
            // TODO: End todo

            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addLeague(String name, String image) {
        // Check if League already exists: 1 if exists, 404 if not, 502 if bad gateway.
        int resultCode = LogicSubSystem.getInstance().checkLeagueName(name);
        if(resultCode == 1) {
            //TODO LEAGUE ALREADY EXISTS, POPUP
            return;
        } else if(resultCode == 502) {
            //TODO INTERNET ISSUE, POPUP THAT THEY'RE OFFLINE
            return;
        }

        int ID = LogicSubSystem.getInstance().leagues.size() == 0 ? 0 :
                LogicSubSystem.getInstance().leagues.get(LogicSubSystem.getInstance().leagues.size()-1).ID+1;
        League league = new League(ID, name);
        if (!image.isEmpty()) {
            league.updatePicture(image);
        }
        league.USERS.add(LogicSubSystem.getInstance().getCurrentUser());
        LogicSubSystem.getInstance().addLeague(league);
        // 1 on success, 502 if bad gateway.
        resultCode = LogicSubSystem.getInstance().userJoinsLeague(LogicSubSystem.getInstance().getCurrentUser().USERNAME, ""+ID);
        if(resultCode == 502) {
            //TODO INTERNET ISSUE, POPUP THAT THEY'RE OFFLINE
            return;
        }
    }

    private void showInvitations(List<Integer> toShow) {
        for (Integer index : toShow) {
            // TODO: make this real
            String inviter = "edv";
            String league = "League of Legends";
            // TODO: End todo
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(String.format("%s invited you to join %s.", inviter, league))
                    .setTitle("League Invitation")
                    .setPositiveButton("Accept", (dialog, id) -> {
                        // TODO: Add user to league
                    })
                    .setNegativeButton("Reject", (dialog, id) -> {});
            builder.show();
        }
    }

    /**
     * Loads data into the logic subsystem
     */
    private void loadData() {
        // Initialize politicians array
        ArrayList<Politician> politicians = new ArrayList<>();

        // Add politicians to the array
        politicians.add(new Politician("Donald Trump", "Republican", 76, "https://en.wikipedia.org/wiki/Donald_Trump", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Donald_Trump_official_portrait.jpg/1024px-Donald_Trump_official_portrait.jpg",0));
        politicians.add(new Politician("Ted Cruz", "Republican", 52, "https://en.wikipedia.org/wiki/Ted_Cruz", "https://upload.wikimedia.org/wikipedia/commons/9/95/Ted_Cruz_official_116th_portrait.jpg",1));
        politicians.add(new Politician("Marco Rubio", "Rebublican", 51, "https://en.wikipedia.org/wiki/Marco_Rubio", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Senator_Rubio_official_portrait.jpg/1024px-Senator_Rubio_official_portrait.jpg",2));
        politicians.add(new Politician("John Kasich", "Republican", 70, "https://en.wikipedia.org/wiki/John_Kasich", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Governor_John_Kasich.jpg/1024px-Governor_John_Kasich.jpg",3));
        politicians.add(new Politician("Hillary Clinton", "Democrat", 76, "https://en.wikipedia.org/wiki/Hillary_Clinton", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Hillary_Clinton_by_Gage_Skidmore_4_%28cropped%29.jpg/1024px-Hillary_Clinton_by_Gage_Skidmore_4_%28cropped%29.jpg",4));
        politicians.add(new Politician("Bernie Sanders", "Democrat", 81, "https://en.wikipedia.org/wiki/Bernie_Sanders", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/02/Bernie_Sanders_in_March_2020.jpg/1024px-Bernie_Sanders_in_March_2020.jpg",5));

        for (Politician politician : politicians) {
            LogicSubSystem.getInstance().addPolitician(politician);
        }
        ArrayList<Score> scores = new ArrayList<>();
        scores.add(new Score("Says \"We must build a wall\"", 3));
        scores.add(new Score("Says \"I love America\" first", -8));
        scores.add(new Score("Mentions taxes first", 12));
        scores.add(new Score("Name starts with the letter E", 1));
        scores.add(new Score("Sneezes", 2));
        scores.add(new Score("Yells at their opponent", -5));
        for (Score score : scores) {
            LogicSubSystem.getInstance().addScore(score);
        }

        ArrayList<League> leagues = new ArrayList<>();
        League LoVe = new League(0, "League of Villainous Evil");
        League LoL = new League(1, "League of Legends");
        leagues.add(LoVe);
        leagues.add(LoL);
        LoL.updatePicture("https://www.leagueoflegends.com/static/open-graph-2e582ae9fae8b0b396ca46ff21fd47a8.jpg");

        for (League league : leagues){
            LogicSubSystem.getInstance().addLeague(league);
        }

        //LogicSubSystem.getInstance().sign_up("Narek", "ohanyanarek@gmail.com", "IL", "SuperNar3k", "SwagMaster69");
        //LogicSubSystem.getInstance().sign_up("Ethan", "ethan.davis@gmail.com", "NE", "EthanD", "password");
        LogicSubSystem.getInstance().sign_in("edv", "69420");
        //LogicSubSystem.getInstance().sign_up("Jason", "jvb@gmail.com", "MN", "jvb", "password");
//        User narek = LogicSubSystem.getInstance().getUser("SuperNar3k");
//        narek.LastWeekPicks.add(0);
//        narek.LastWeekPicks.add(4);
//        User ethan = LogicSubSystem.getInstance().getUser("EthanD");
//        ethan.LastWeekPicks.add(1);
//        narek.LastWeekPicks.add(5);
//        User evan = LogicSubSystem.getInstance().getUser("edv");
//        evan.LastWeekPicks.add(2);
//        User jason = LogicSubSystem.getInstance().getUser("jvb");
//        jason.LastWeekPicks.add(3);
//        narek.IMAGE = "https://media.licdn.com/dms/image/D5603AQG__sc4QjAcnA/profile-displayphoto-shrink_800_800/0/1673715721556?e=1680739200&v=beta&t=amvga8tgu0jk384UP72bP9AgVG_F5lIljmF552TC5ss";
//        ethan.IMAGE = "https://static.wikia.nocookie.net/scratchpad/images/c/ce/Ethan_Davis.jpeg/revision/latest/scale-to-width-down/1000?cb=20200709210908";
//        jason.IMAGE = "https://static.wikia.nocookie.net/fridaythe13th/images/8/8e/JBOX6.png/revision/latest/scale-to-width-down/350?cb=20210617194843";
//        narek.addSCORE(69);
//        evan.addSCORE(-5);
//        ethan.addSCORE(332);
//        jason.addSCORE(69420);
//        LogicSubSystem.getInstance().leagues.get(0).addUser(narek);
//        LogicSubSystem.getInstance().leagues.get(0).addUser(ethan);
//        LogicSubSystem.getInstance().leagues.get(0).addUser(evan);
//        LogicSubSystem.getInstance().leagues.get(0).addUser(jason);
//        LogicSubSystem.getInstance().leagues.get(1).addUser(jason);
        LogicSubSystem.getInstance().politicians.get(1).SCORES.add(new Score("Says \"We must build a wall\"", 3));
        LogicSubSystem.getInstance().politicians.get(1).SCORES.add(scores.get(4));

        ArrayList<Integer> invites = new ArrayList<>();
        invites.add(0);
        showInvitations(invites);
    }
}
package com.evanv.debateduel.logic;

import java.util.ArrayList;

public class LogicSubSystem {
    private static volatile LogicSubSystem instance = null;
    // Object for managing secure data access
    private final Gatekeeper professionalSecurityExpert;

    public ArrayList<Politician> politicians;
    public ArrayList<Score> scoreList;
    public ArrayList<League> leagues;

    public LogicSubSystem() {
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        instance = this;
        politicians = new ArrayList<>();
        scoreList = new ArrayList<>();
        leagues = new ArrayList<>();
        // Initialize the secure data access object
        professionalSecurityExpert = new Gatekeeper();
    }

    /**
     * Gets the current logged-in user object.
     * @return User object.
     */
    public User getCurrentUser() {
        return professionalSecurityExpert.getCurrentUser();
    }

    /**
     * Signs the user in through the secure data access object.
     * @param username The username for the user.
     * @param password The password in plaintext string for the user.
     * @return 0 on success, 403 on incorrect password, 404 on username doesn't exist.
     */
    public int sign_in(String username, String password) {
        return professionalSecurityExpert.sign_in(username, password);
    }

    /**
     * Signs the user up for this awesome service! Through the secure data access object, of course.
     * @param name User's name.
     * @param email User's email.
     * @param state User's state (in USA).
     * @param username User's username.
     * @param password User's password in plaintext string.
     * @return 0 on success, 409 on username in use, or another code in exception.
     */
    public int sign_up(String name, String email, String state, String username, String password) {
        return professionalSecurityExpert.sign_up(name, email, state, username, password);
    }

    public static LogicSubSystem getInstance() {
        if (instance == null) {
            synchronized (LogicSubSystem.class) {
                if (instance == null) {
                    instance = new LogicSubSystem();
                }
            }
        }
        return instance;
    }

    public void addPolitician(Politician politician) {
        politicians.add(politician);
    }

    public void removePolitician(Politician politician) {
        politicians.remove(politician);
    }

    public void addScore(Score score) {
        scoreList.add(score);
    }

    public void removeScore(Score score) {
        scoreList.remove(score);
    }

    public void addLeague(League league){
        leagues.add(league);
    }
    public void deleteLeague(League league){
        leagues.remove(league);
    }

    /**
     * Makes all scores of politicians 0 and clears their score list and adds previous score to debate scores.
     */
    public void newDebate() {
        for (Politician p : politicians) {
            p.PREVIOUS_SCORES.add(p.CURRENT_SCORE);
            p.CURRENT_SCORE = 0;
            p.SCORES.clear();
        }

        User user = professionalSecurityExpert.getCurrentUser();
        user.LAST_WEEK_PICKS = user.PICKS;
//        for (League league : leagues) {
//            for (User user : league.USERS) {
//                user.LastWeekPicks = user.PICKS;
//            }
//        }
    }

    public int checkForUser(String username){
        return professionalSecurityExpert.checkUsername(username);
    }
    public int checkLeagueName(String username){
        return professionalSecurityExpert.checkLeagueName(username);
    }
    public int userJoinsLeague(String username, String leagueID){
        return professionalSecurityExpert.userJoinsLeague(username, leagueID);
    }
    public int updateUsersInviteStatus(String user, String inviteIssuer, int leagueID){
        return professionalSecurityExpert.updateUsersInviteStatus(user, inviteIssuer, leagueID);
    }

    public int checkLeagueForUser(String league, String username){
        return professionalSecurityExpert.checkLeagueForUser(league, username);
    }
}

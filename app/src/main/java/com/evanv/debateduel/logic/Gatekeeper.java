package com.evanv.debateduel.logic;

import android.os.AsyncTask;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

/**
 * Gatekeep, girlboss, gaslight?
 */
public class Gatekeeper {
    public boolean isLoggedIn;
    private User currentUser;
    private ConnectMySql dbConn;

    /**
     * Gatekeeper constructor.
     */
    public Gatekeeper() {
        this.isLoggedIn = false;
        this.currentUser = null;
    }

    /**
     * Gets the current logged-in user object.
     * @return User object.
     */
    public User getCurrentUser() {
        return this.currentUser;
    }

    /**
     * Log in to your favorite app!
     * @param username The username for the user.
     * @param password The password in plaintext string for the user.
     * @return 0 on success, 403 on incorrect password, 404 on username doesn't exist, 502 on bad gateway.
     */
    public int sign_in(String username, String password) {
        try {
            dbConn = new ConnectMySql("jdbc:mysql://208.109.40.126:3306/DuelDebatabase",
                    "redacted", "redacted");
            String attempt = dbConn.execute("sign_in", username, generateHash(password)).get();
            List<String> css = Arrays.asList(attempt.split(",", -1));
            // CHECK IF ERROR SIGNING IN
            if(css.get(0).equals("403") || css.get(0).equals("404") || css.get(0).equals("502")) {
                return Integer.parseInt(css.get(0));
            }
            // SUCCESS!
            this.isLoggedIn = true;
            this.currentUser = new User(css.get(0), css.get(1), css.get(2), css.get(3), css.get(4), css.get(5), css.get(6), css.get(7), css.get(8), css.get(9));
            return 0;
        } catch(Exception e){
            // BAD GATEWAY!
            System.out.println("Exception thrown: Bad Gateway! " + e);
            return 502;
        }
    }

    /**
     * Sign up for this awesome service!
     * @param name User's name.
     * @param email User's email.
     * @param state User's state (in USA).
     * @param username User's username.
     * @param password User's password in plaintext string.
     * @return 0 on success, 409 on username in use, or another code in execution.
     */
    public int sign_up(String name, String email, String state, String username, String password) {
        try {
            dbConn = new ConnectMySql("jdbc:mysql://208.109.40.126:3306/DuelDebatabase",
                    "redacted", "redacted");

            String res = dbConn.execute("sign_up", name, email, state, username, generateHash(password)).get();

            System.out.println(res);
            int i = Integer.parseInt(res);
            // CHECK IF ERROR SIGNING IN
            if(i != 0) {
                return i;
            }
            // SUCCESS!
            this.isLoggedIn = true;
            this.currentUser = new User(name, email, state, username);
            return 0;
        } catch(Exception e){
            // BAD GATEWAY!
            System.out.println("Exception thrown: Bad Gateway! " + e);
            return 502;
        }
    }

    /**
     * Checks to see if the username exists in the database.
     * @param username The username to check for.
     * @return 1 if username is found, 404 if not, 502 if bad gateway.
     */
    public int checkUsername(String username) {
        try {
            dbConn = new ConnectMySql("jdbc:mysql://208.109.40.126:3306/DuelDebatabase",
                    "redacted", "redacted");
            String res = dbConn.execute("check_username", username).get();
            return Integer.parseInt(res);
        } catch(Exception e){
            // BAD GATEWAY!
            System.out.println("Exception thrown: Bad Gateway! " + e);
            return 502;
        }
    }

    /**
     * Checks to see if the league name exists in the database.
     * @param name The name to check for.
     * @return 1 if league exists found, 404 if not, 502 if bad gateway.
     */
    public int checkLeagueName(String name) {
        try {
            dbConn = new ConnectMySql("jdbc:mysql://208.109.40.126:3306/DuelDebatabase",
                    "redacted", "redacted");
            String res = dbConn.execute("check_league_name", name).get();
            return Integer.parseInt(res);
        } catch(Exception e){
            // BAD GATEWAY!
            System.out.println("Exception thrown: Bad Gateway! " + e);
            return 502;
        }
    }

    /**
     * Generates hash of password
     * @param PASSWORD Plaintext
     * @return Cypher hash
     */
    public String generateHash(String PASSWORD) {
        return DigestUtils.md5Hex(PASSWORD);
    }

    /**
     * Updates a user's invite status.
     * @param targetUser Target username.
     * @param sourceUser Source username.
     * @param leagueID League identification.
     * @return 1 on success, 502 on gateway error.
     */
    public int updateUsersInviteStatus(String targetUser, String sourceUser, int leagueID) {
        try {
            dbConn = new ConnectMySql("jdbc:mysql://208.109.40.126:3306/DuelDebatabase",
                    "redacted", "redacted");
            String res = dbConn.execute("update_users_invite_status", targetUser, sourceUser, ""+leagueID).get();
            return Integer.parseInt(res);
        } catch (Exception e) {
            // BAD GATEWAY!
            System.out.println("Exception thrown: Bad Gateway! " + e);
            return 502;
        }
    }


    /**
     * Adds a username to a league.
     * @param username The username to check for.
     * @param leagueID The ID of the league to join.
     * @return 1 on success, 502 if bad gateway.
     */
    public int userJoinsLeague(String username, String leagueID) {
        try {
            dbConn = new ConnectMySql("jdbc:mysql://208.109.40.126:3306/DuelDebatabase",
                    "redacted", "redacted");
            String res = dbConn.execute("user_joins_league", username, leagueID).get();
            return Integer.parseInt(res);
        } catch (Exception e) {
            // BAD GATEWAY!
            System.out.println("Exception thrown: Bad Gateway! " + e);
            return 502;
        }
    }
    /**
     * Checks of a username exists in League.
     * @param league Name of the league to check for.
     * @param username Name of the username to check for.
     * @return 132 if username is in the league, 0 if not. 502 if gateway error.
     */
    public int checkLeagueForUser(String league, String username) {
        try {
            dbConn = new ConnectMySql("jdbc:mysql://208.109.40.126:3306/DuelDebatabase",
                    "redacted", "redacted");
            String res = dbConn.execute("check_league_for_user", league, username).get();
            return Integer.parseInt(res);
        } catch (Exception e) {
            // BAD GATEWAY!
            System.out.println("Exception thrown: Bad Gateway! " + e);
            return 502;
        }
    }

    private static class ConnectMySql extends AsyncTask<String, Void, String> {
        String url;
        String user;
        String pass;
        Connection con;
        Statement stmt;
        ResultSet rs;

        public ConnectMySql(String url, String user, String pass) {
            this.url = url;
            this.user = user;
            this.pass = pass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url, user, pass);
                stmt = con.createStatement();

                if(params.length != 0) {
                    switch (params[0]) {
                        case "sign_in":
                            return "" + sign_in(params[1], params[2]);
                        case "sign_up":
                            return "" + sign_up(params[1], params[2], params[3], params[4], params[5]);
                        case "check_username":
                            return "" + checkUsername(params[1]);
                        case "check_league_for_user":
                            return "" + checkLeagueForUser(params[1], params[2]);
                        case "check_league_name":
                            return "" + checkLeagueName(params[1]);
                        case "update_users_invite_status":
                            return "" + updateUsersInviteStatus(params[1], params[2], params[3]);
                        case "user_joins_league":
                            return "" + userJoinsLeague(params[1], params[2]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        /**
         * Log in to your favorite app!
         * @param username The username for the user.
         * @param passwordHash The password in plaintext string for the user.
         * @return 0 on success, 403 on incorrect password, 404 on username doesn't exist, 502 on bad gateway.
         */
        public String sign_in(String username, String passwordHash) {
            try {
                System.out.println("LINE 259");
                rs = stmt.executeQuery("SELECT * FROM all_users WHERE username = '" + username + "';");
                if(rs.next()) {
                    System.out.println("LINE 261");
                    if(rs.getString("password_hash").equals(passwordHash)) {
                        System.out.println("LINE 266");
                        return rs.getString("name")+","+rs.getString("email")+","+rs.getString("state")+","+
                                rs.getString("username")+","+rs.getString("picks")+","+rs.getString("last_weeks_picks")+","+
                                rs.getString("score")+","+rs.getString("image")+","+rs.getString("invites_to")+","+rs.getString("invites_from");
                        // SUCCESS!
                    } else {
                        //INCORRECT PASSWORD!
                        return "403";
                    }
                } else {
                    //USERNAME DOES NOT EXIST!
                    System.out.println("LINE 275");
                    return "404";

                }
            } catch(Exception e){
                // BAD GATEWAY!
                System.out.println("Exception thrown: Bad Gateway! " + e);
                return "502";
            }
        }

        /**
         * Sign up for this awesome service!
         * @param name User's name.
         * @param email User's email.
         * @param state User's state (in USA).
         * @param username User's username.
         * @param passwordHash User's password in plaintext string.
         * @return 0 on success, 409 on username in use, or another code in exception.
         */
        public int sign_up(String name, String email, String state, String username, String passwordHash) {
            try {
                int check = checkUsername(username);
                if(check == 1) {
                    // USERNAME ALREADY IN USE!
                    return 409;
                } else if(check == 404) {
                    // ADD NEW USER TO DATABASE!
                    stmt.executeUpdate("INSERT INTO `all_users` (`username`,`password_hash`, `state`," +
                            "`name`, `email`, `score`, `image`, `picks`, `last_weeks_picks`, `invites_to`, `invites_from`) VALUES ('" +
                            username +"', '" + passwordHash + "', '" + state + "', '" + name +
                            "', '" + email + "','0', 'https://debateduelbucket.s3.us-east-2.amazonaws.com/user_default.png', '', '', '', '');");
                    // SUCCESS!
                    System.out.println("SUCCESS ON LINE 305");
                    return 0;
                } else {
                    // BAD GATEWAY!
                    return check; //is 502 here
                }
            } catch(Exception e){
                // BAD GATEWAY!
                System.out.println("Exception thrown! " + e);
                return 502;
            }
        }

        /**
         * Checks to see if the username exists in the database.
         * @param username The username to check for.
         * @return 1 if username is found, 404 if not, 502 if bad gateway.
         */
        public int checkUsername(String username) {
            try {
                rs = stmt.executeQuery("SELECT * FROM all_users WHERE username = '" + username + "';");
                if(rs.next()) {
                    // USERNAME FOUND
                    return 1;
                }
                // USERNAME DOES NOT EXIST
                return 404;
            } catch(Exception e) {
                // BAD GATEWAY!
                System.out.println("Exception thrown! " + e);
                return 502;
            }
        }

        /**
         * Adds a username to a league.
         * @param username The username to check for.
         * @param leagueID The ID of the league to join.
         * @return 1 on success, 502 if bad gateway.
         */
        public int userJoinsLeague(String username, String leagueID) {
            try {
                rs = stmt.executeQuery("SELECT users FROM all_leagues WHERE ID = '" + leagueID + "';");
                String newUserList = rs.getString("users") + "," + username;
                stmt.executeUpdate("UPDATE all_leagues SET users = '" + newUserList + "' WHERE ID = '" + leagueID + "';");
                return 1;
            } catch(Exception e) {
                // BAD GATEWAY!
                System.out.println("Exception thrown! " + e);
                return 502;
            }
        }

        /**
         * Checks to see if the league name exists in the database.
         * @param name The name to check for.
         * @return 1 if league exists found, 404 if not, 502 if bad gateway.
         */
        public int checkLeagueName(String name) {
            try {
                rs = stmt.executeQuery("SELECT * FROM all_leagues WHERE name = '" + name + "';");
                if(rs.next()) {
                    // LEAGUE FOUND
                    return 1;
                }
                // LEAGUE DOES NOT EXIST
                return 404;
            } catch(Exception e) {
                // BAD GATEWAY!
                System.out.println("Exception thrown! " + e);
                return 502;
            }
        }

        /**
         * Updates a user's invite status.
         * @param targetUser Target username.
         * @param sourceUser Source username.
         * @param leagueID League identification.
         * @return 1 on success, or 502 on error.
         */
        public int updateUsersInviteStatus(String targetUser, String sourceUser, String leagueID) {
            try {
                stmt.executeUpdate("UPDATE [LOW PRIORITY] all_users SET invites_to = '" + leagueID +
                        "', invites_from = '" + sourceUser + "' [WHERE username = '" + targetUser + "'];");
                return 1;
            } catch(Exception e){
                // BAD GATEWAY!
                System.out.println("Exception thrown! " + e);
                return 502;
            }
        }

        /**
         * Checks of a username exists in League.
         * @param league Name of the league to check for.
         * @param username Name of the username to check for.
         * @return 132 if username is in the league, 0 if not. 502 if gateway error.
         */
        public int checkLeagueForUser(String league, String username) {
            try {
                rs = stmt.executeQuery("SELECT users FROM all_leagues WHERE name = '" + league + "';");
                if(rs == null) {
                    return 0;
                }
                List<String> usersInLeague = Arrays.asList(rs.getString("username").split(",", -1));
                if(usersInLeague.contains(username)) {
                    return 1;
                }
                return 0;
            } catch(Exception e){
                // BAD GATEWAY!
                System.out.println("Exception thrown! " + e);
                return 502;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Cry about it
        }
    }
}

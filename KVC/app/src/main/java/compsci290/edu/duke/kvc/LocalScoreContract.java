package compsci290.edu.duke.kvc;

/**
 * Created by Bao on 4/12/2017.
 */


import android.provider.BaseColumns;

public final class LocalScoreContract {
    private LocalScoreContract() {}

    public static class LocalScoreRecord implements BaseColumns {
        // user_id | character | score
        public static final String TABLE_NAME = "LocalScoreRecord";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_CHARACTER = "character";
        public static final String COLUMN_NAME_SCORE = "score";
    }

}

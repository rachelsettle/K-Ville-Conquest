package compsci290.edu.duke.kvc;

/**
 * Created by Bao on 4/12/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalScoreDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "K_Ville_Conquest.db";


    //CREATE TABLE LocalScoreRecord
    // (user_id TEXT PRIMARY KEY,
    //  character TEXT,
    //  score INT);


    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " +
                    LocalScoreContract.LocalScoreRecord.TABLE_NAME + "(" +
                    LocalScoreContract.LocalScoreRecord.COLUMN_NAME_USER_ID + " TEXT," +
                    LocalScoreContract.LocalScoreRecord.COLUMN_NAME_CHARACTER + " TEXT," +
                    LocalScoreContract.LocalScoreRecord.COLUMN_NAME_SCORE + " INTEGER," +
                    "PRIMARY KEY (user_id, character, score)" + ")";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + LocalScoreContract.LocalScoreRecord.TABLE_NAME;

    public LocalScoreDBHelper(Context c){
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}

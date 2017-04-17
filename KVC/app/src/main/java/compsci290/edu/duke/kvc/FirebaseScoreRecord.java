package compsci290.edu.duke.kvc;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Bao on 4/16/2017.
 */
@IgnoreExtraProperties
public class FirebaseScoreRecord {
    public String character;

    public FirebaseScoreRecord(){

    }

    public FirebaseScoreRecord(String character){
        this.character = character;
    }
}

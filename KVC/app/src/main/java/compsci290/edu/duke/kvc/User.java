package compsci290.edu.duke.kvc;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Bao on 4/17/2017.
 */

@IgnoreExtraProperties
public class User {

    public String firstName;
    public String lastName;
    public String email;

    public User(){

    }

    public User(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}

package compsci290.edu.duke.kvc;

import android.util.Log;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Created by Bao on 4/29/2017.
 */
public class SharedPrefTest {

    //see if there are an equal number of read and write methods
    //one read or write method on its own is not useful
    @Test
    public void equal_read_and_write(){
        Class sp = SharedPref.class;
        int numWrite = 0;
        int numRead = 0;

        for (Method m: sp.getMethods()) {
            if (m.getName().contains("write")){
                numWrite++;
            }

            else if (m.getName().contains("read")){
                numRead++;
            }

            else{
                continue;
            }
        }
        assertTrue(numRead == numWrite);
    }

}
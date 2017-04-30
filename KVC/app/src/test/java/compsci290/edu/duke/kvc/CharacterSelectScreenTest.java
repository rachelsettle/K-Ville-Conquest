package compsci290.edu.duke.kvc;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created by Bao on 4/29/2017.
 */
public class CharacterSelectScreenTest {

    //see if we properly extract only characters from the drawable file
    @Test
    public void only_characters_test(){
        Field[] ID_Fields = R.drawable.class.getFields();
        ArrayList<String> onlyChars = new ArrayList<>();
        HashSet<String> seenFiles = new HashSet<>();

        for (Field f: ID_Fields){
            if (f.getName().contains("character") && !seenFiles.contains(f.getName())){
                onlyChars.add(f.getName());
                seenFiles.add(f.getName());
                assertTrue(f.getName().contains("character"));
            }
        }
    }
}
package compsci290.edu.duke.kvc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

/*
 * Created by Bao on 4/8/2017.
 */

public class CharacterSelectScreen extends AppCompatActivity implements AdapterView.OnItemClickListener {


    public class NameIDPair{

        private String mImageName;
        private int mImageID;

        public NameIDPair(){
            this("", Integer.MIN_VALUE);
        }

        public NameIDPair(String name, int id){
            mImageName = name;
            mImageID = id;
        }

        public String getmImageName(){
            return mImageName;
        }

        public int getmImageID(){
            return mImageID;
        }
    }

    private ListView mCharacterChoices;
    private ArrayList<NameIDPair> mListOfChars;

    public static String[] sCharacterNames;
    public static int[] sCharacterIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_select_list);
        Context c = getApplicationContext();

        mCharacterChoices = (ListView) this.findViewById(R.id.characterSelect);

        ArrayList<String> dummyArrayList1 = new ArrayList<>();
        ArrayList<Integer> dummyArrayList2 = new ArrayList<>();

        mListOfChars = getmCharacters(c);

        for (NameIDPair pair : mListOfChars){
            String imageName = pair.getmImageName();
            //trim the _character part at the end of character names
            if (pair.getmImageName() != null){
                int endingIndex = imageName.indexOf("_character");
                imageName = imageName.substring(0, endingIndex);
            }

            dummyArrayList1.add(imageName);
            dummyArrayList2.add(pair.getmImageID());
        }

        sCharacterNames = new String[dummyArrayList1.size()];
        sCharacterNames = dummyArrayList1.toArray(sCharacterNames);
        sCharacterIDs = new int[dummyArrayList1.size()];

        for (int i = 0; i < sCharacterIDs.length; i++){
            sCharacterIDs[i] = dummyArrayList2.get(i).intValue();
        }

        CharacterSelectAdapter adapter = new CharacterSelectAdapter(this, sCharacterNames, sCharacterIDs);
        mCharacterChoices.setAdapter(adapter);
        mCharacterChoices.setOnItemClickListener(this);

    }

    private ArrayList<NameIDPair> getmCharacters(Context c) throws IllegalArgumentException{
        Field[] ID_Fields = R.drawable.class.getFields();
        ArrayList<NameIDPair> result = new ArrayList<>();
        HashSet<String> seenFiles = new HashSet<>();
        for (Field f: ID_Fields){
            try{
                String fileName = f.getName();

                if (fileName.contains("character") && !(seenFiles.contains(fileName))){
                    seenFiles.add(fileName);
                    result.add(new NameIDPair(fileName,
                            CharacterSelectScreen.this.getResources().getIdentifier(fileName,
                                    "drawable",
                                    CharacterSelectScreen.this.getPackageName())));
                }
            }
            catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }

        return result;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
        Intent gameStart = new Intent(CharacterSelectScreen.this, GameScreen.class);
        SharedPref.initialize(CharacterSelectScreen.this.getApplicationContext());
        SharedPref.write("charID", sCharacterIDs[position]);
        SharedPref.write("charName", sCharacterNames[position]);
        startActivity(gameStart);
    }

}
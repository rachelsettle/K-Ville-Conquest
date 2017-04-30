package compsci290.edu.duke.kvc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
    static final int PICTURE_RESULT = 1;
    private Bitmap pic;
    private Uri f;

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
            String imageName = "";
            //replace the file name of the image with
            //a more user friendly name
            String[] names = pair.getmImageName().split("_");

            for (String name : names){
                if (!name.contains("character")){
                    //capitalize the word
                    imageName = imageName + name.substring(0,1).toUpperCase() + name.substring(1)
                                + " ";
                }
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

    // Customizable tent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {
            try {
                pic = MediaStore.Images.Media.getBitmap(getContentResolver(),f);
                Log.d("FILES", "getting the Bitmap");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                pic.compress(Bitmap.CompressFormat.JPEG, 0, stream);
                byte[] bytes = stream.toByteArray();
                Log.d("FILES", "compressed the Bitmap");
                Intent gameStart = new Intent(CharacterSelectScreen.this, GameScreen.class);
                gameStart.putExtra("BMP",bytes);
                Log.d("FILES", "added the compression");
                SharedPref.initialize(CharacterSelectScreen.this.getApplicationContext());
                SharedPref.write("position", 0);
                SharedPref.write("charName", sCharacterNames[0]);
                SharedPref.write("charID",0);
                startActivity(gameStart);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
        if (position != 0){
            Intent gameStart = new Intent(CharacterSelectScreen.this, GameScreen.class);
            SharedPref.initialize(CharacterSelectScreen.this.getApplicationContext());
            SharedPref.write("charID", sCharacterIDs[position]);
            SharedPref.write("charName", sCharacterNames[position]);
            SharedPref.write("position", position);
            startActivity(gameStart);
        }
        else{
            Log.d("CHARACTER", "clicked customizable");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Log.d("CHARACTER", "made camera intent");
            File file = null;
            try {
                file = File.createTempFile("customizable_tent_character",".jpg",this.getCacheDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("PHOTO", file.getAbsolutePath());
            try{
                f = FileProvider.getUriForFile(this, "com.compsci290.edu.duke.kvc.fileprovider", file);
            } catch (Exception e){
                e.printStackTrace();
            }
            Log.d("CHARACTER", "got file");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, f);
            Log.d("CHARACTER", "put file in intent");
            if(intent.resolveActivity(getPackageManager())!=null) startActivityForResult(intent, PICTURE_RESULT);
            Log.d("CHARACTER", "starting picture");
        }
    }
}

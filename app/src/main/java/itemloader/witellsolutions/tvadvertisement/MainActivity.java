package itemloader.witellsolutions.tvadvertisement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    VideoView vv;
    String[] contentTypeArray = null, content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // vv = (VideoView)findViewById(R.id.videoview);
        final TextView txt = (TextView)findViewById(R.id.newsText);
        txt.setSelected(true);

        Intent i = getIntent();
        if(i != null){
            contentTypeArray = i.getStringArrayExtra("contentTypeArray");
            content = i.getStringArrayExtra("content");
        }
    }


    public void change(View v){
        Intent i = new Intent(MainActivity.this, TVScreen.class);
        i.putExtra("contentTypeArray", contentTypeArray);
        i.putExtra("content", content);
        i.putExtra("newsText", "");
        startActivity(i);
    }

    public void logout(View v){

        deleteLog();
        Intent i = new Intent(getApplicationContext() ,Login.class);
        i.putExtra("stat", true);
        startActivity(i);
        finish();
    }
    public void deleteLog(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("usertype", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("usertype", "");
        editor.commit();
    }

}

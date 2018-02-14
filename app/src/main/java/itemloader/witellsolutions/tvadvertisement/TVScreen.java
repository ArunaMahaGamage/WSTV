package itemloader.witellsolutions.tvadvertisement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sumudu on 3/30/2016.
 */
public class TVScreen extends Activity {

    TextView newsText;
    VideoView videoPlayer;
    String newsTextString;
    int videoIndex = 0;
    String[] contentArray = null, contentTypeArray = null;
    File ROOT = new File(Environment.getExternalStorageDirectory() + "/" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
        Intent i = getIntent();
        if(i != null){
            contentArray = i.getStringArrayExtra("content");
            contentTypeArray = i.getStringArrayExtra("contenttypearray");
            videoIndex = i.getIntExtra("index", 0);
            newsTextString = i.getStringExtra("newstext");
        }

      //  videoPlayer = (VideoView)findViewById(R.id.videoview);
        newsText = (TextView)findViewById(R.id.newsText);
        newsText.setText(newsTextString);
        newsText.setSelected(true);


        Uri uri = Uri.parse(ROOT + contentArray[videoIndex]);
        videoPlayer.setVideoURI(uri);
        videoPlayer.setZOrderOnTop(true);
        videoPlayer.requestFocus();
        videoPlayer.start();

        videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer vmp) {

            }
        });


    }
}

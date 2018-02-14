package itemloader.witellsolutions.tvadvertisement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Sumudu on 3/31/2016.
 */
public class VideoFragment extends Fragment {
    String imgURL = "";
    VideoView posterPanel;
    File STORAGE_PATH = Environment.getExternalStorageDirectory();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_player, container, false);
        posterPanel = (VideoView) view.findViewById(R.id.videoPlayer);
        System.out.println(imgURL);

        Uri uri = Uri.parse(STORAGE_PATH + imgURL);
        posterPanel.setVideoURI(uri);
        posterPanel.setZOrderOnTop(true);
        posterPanel.requestFocus();
        posterPanel.start();

        posterPanel.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer vmp) {

            }
        });
        return view;
    }

    public VideoFragment(String imgURL){
        this.imgURL = imgURL;
    }
}

package itemloader.witellsolutions.tvadvertisement;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by Sumudu on 3/31/2016.
 */
public class ImageFragment extends Fragment {
    String fileName = "", fileType = "";
    ImageView posterPanel;
    VideoView videoPanel;
    int position;
    Data d = new Data();
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(fileType.equalsIgnoreCase("img")){
            View view = inflater.inflate(R.layout.image_panel, container, false);
            posterPanel = (ImageView) view.findViewById(R.id.imageScreen);
            if(fileName.equals("end")){
                Picasso
                        .with(context)
                        .load(R.raw.trainsmissionend)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .fit()
                        .into(posterPanel);
            }
            else{
                Picasso
                        .with(context)
                        .load(new File(d.getSTORAGE_PATH(), fileName))
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .fit()
                        .into(posterPanel);
            }
            return view;
        }
        else{
            View view = inflater.inflate(R.layout.video_panel, container, false);
            videoPanel = (VideoView) view.findViewById(R.id.videoScreen);
            Uri uri = Uri.fromFile(new File(d.getSTORAGE_PATH(), fileName));
            videoPanel.setVideoURI(uri);
            videoPanel.setZOrderOnTop(true);
            videoPanel.requestFocus();
            videoPanel.start();
            return view;
        }
    }

    public ImageFragment(String fileName, int position, String fileType, Context context){
        this.fileName = fileName;
        this.fileType = fileType;
        this.position = position;
        this.context = context;
    }
}

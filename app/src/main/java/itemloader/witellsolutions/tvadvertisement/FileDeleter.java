package itemloader.witellsolutions.tvadvertisement;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Sumudu on 3/31/2016.
 */
public class FileDeleter {
    String fileNameString = "";
   Data d = new Data();

    public void deleteFile(String fileName){
        try {
            File f=new File(d.getSTORAGE_PATH(), fileName);
            if(f.exists())
            {
                System.out.println(f.delete());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> listAllFiles(){
        ArrayList<String> imgNames  = new ArrayList<>();
        File[] files = d.getSTORAGE_PATH().listFiles();
        if(files != null){
            for(File f : files){ // loop and print all file
                imgNames.add(f.getName()); // this is file name
            }
        }
        return imgNames;
    }
}

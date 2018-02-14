package itemloader.witellsolutions.tvadvertisement;

/**
 * Created by Sumudu on 3/21/2016.
 */

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
public class CheckTextFile {
    Data data = new Data();
    String returnText="";
    File file;
    public String texting(Context c, String fileName) {

        try {
            file = new File(data.getSTORAGE_PATH(), fileName + ".txt");
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
                br.close();
                returnText = text.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
                returnText = "";
            }
        }

        catch(Exception ex){
            try{
                //FileInputStream fis = c.openFileInput(fileName + ".txt");
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                returnText = sb.toString();
            }
            catch(Exception e){returnText = "";}
        }
        return returnText;
    }
}


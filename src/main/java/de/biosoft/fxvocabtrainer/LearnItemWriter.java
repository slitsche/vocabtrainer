package de.biosoft.fxvocabtrainer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class LearnItemWriter {

    private File appfile;

    public LearnItemWriter(String fileName) {
        initDir(fileName);
    }

    private void initDir(String fileName) {
        File appDataFolder = getAppFolder();
        boolean s = true;
        if (!appDataFolder.exists()) {
            s = appDataFolder.mkdir();
            // TODO: communicate result value
        }
        if (fileName == null) {
            appfile = new File(appDataFolder, "lbh.txt");
        } else {
            appfile = new File(appDataFolder, fileName);
        }
        if (!appfile.exists()) {
            try {
                appfile.createNewFile();
            } catch (IOException e) {
                // TODO communicate error
                e.printStackTrace();
            }
        }
    }
    
    private static File getAppFolder(){
        String home = System.getProperty("user.home");
        return new File(home, ".lbh");
    }
    /**
     * List of names of Files located in appFolder without extension.
     * @return List of Strings
     */
    public static List<String> getBaseFileNames(){
        File f = getAppFolder();
        List<String> l = new ArrayList<String>();
        if (!f.exists()){
            return l;
        }
        String[] s = f.list(new FilenameFilter() {
            
            @Override
            public boolean accept(File d, String f) {
                if (f.endsWith(".txt")){
                    return true;
                }
                return false;
            }
        });
        for (int i = 0; i<s.length; i++){
            String t = s[i].substring(0, s[i].length()-4);
            l.add(t);
        }
        return l;
    }

    public List<LearnItem> getItems() {
        List<LearnItem> result = new ArrayList<LearnItem>();
        FileInputStream is = null;
        try {
            is = new FileInputStream(appfile);
            InputStreamReader ois = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(ois);
            String s = null;
            while ((s = br.readLine()) != null) { // read until EOFException
                String[] l = s.split("\\t");
                LearnItem i = new LearnItem(l[0], l[1], Integer.valueOf(l[2]));
                result.add(i);
            }
            br.close(); // TODO finally
        } catch (Exception e) {
            // TODO Auto-generated catch block FileNotFound, IOExcxpetion
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    public void writeItems(List<LearnItem> items) {
        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(appfile);
            OutputStreamWriter os = new OutputStreamWriter(fs, "UTF-8");
            BufferedWriter bw = new BufferedWriter(os);
            for (LearnItem l : items) {
                bw.write(String.format("%s\t%s\t%d\n", l.getQuestion(),
                        l.getAnswer(), l.getScore()));
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (Exception e2) {
                }
            }
        }
    }

}

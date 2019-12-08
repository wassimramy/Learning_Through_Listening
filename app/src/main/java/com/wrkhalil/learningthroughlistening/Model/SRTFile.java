package com.wrkhalil.learningthroughlistening.Model;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SRTFile {

    private String id;
    private List<String> parsedTranscript;
    private List<String> generatedTranscript;
    private String path;

    SRTFile(String id, List<String> parsedTranscript, List<String> generatedTranscript){
        this.id = id;
        this.parsedTranscript = parsedTranscript;
        this.generatedTranscript = generatedTranscript;
        generateClosedCaptions();
        writeToFile(BaseApplication.getAppContext());
    }

    public String getPath() {
        return path;
    }

    private void generateClosedCaptions(){
        int generatedTranscriptIndex = 0;
        for (int i = 0 ; i < parsedTranscript.size() ; i++){
            if (parsedTranscript.get(i).charAt(0) == generatedTranscript.get(generatedTranscriptIndex).charAt(0)){
                parsedTranscript.set(i, generatedTranscript.get(generatedTranscriptIndex));
                generatedTranscriptIndex ++;
            }
        }
    }

    private void writeToFile(Context context) {
        String fileName = id + ".srt";
        String text;
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            for (int i = 0 ; i < parsedTranscript.size() ; i++){
                text = (parsedTranscript.get(i)+'\n');
                fos.write(text.getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    path = context.getFilesDir() + "/" + fileName;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

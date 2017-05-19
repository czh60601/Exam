package czhy.grey.sun.exam.bin.other_;

import android.content.Context;

import java.util.ArrayList;

import czhy.grey.sun.exam.bin.dialog_.ImportDialog;
import czhy.grey.sun.exam.bin.dialog_.WrongDialog;
import czhy.grey.sun.exam.bin.manager_.ImportManger;
import czhy.grey.sun.exam.bin.manager_.STManger;

public class ImportThread extends Thread {
    private final String FILE_CODE = "#0030";
    private Context context;
    private String message;
    private int nextId;
    private ImportDialog importDialog;
    private ArrayList<String> filePath_doc;
    private ImportManger importManger;

    public ImportThread(Context context, ArrayList<String> filePath_doc, ImportManger importManger){
        this.filePath_doc = filePath_doc;
        importDialog = new ImportDialog(context);
        importDialog.show();
        importDialog.setMaxFile(filePath_doc.size());
        importDialog.setRun(true);
        this.importManger = importManger;
        this.context = context;
        nextId = -1;
        message = "T-new";
    }

    @Override
    public synchronized void start() {
        importManger.start();
        super.start();
    }

    @Override
    public void run() {
        do {
            if (importManger.MoveToNext()) {
                message = importManger.AnalyzeParagraphText();
                importDialog.setProgressNow(importManger.getNextId());
            } else {
                nextId++;

                if (nextId < filePath_doc.size()) {
                    message = importManger.OpenAndRead(filePath_doc.get(nextId));
                    importDialog.setProgressAll(nextId);
                    importDialog.setProgressNow(0);
                    importDialog.setMaxSize(importManger.Size());
                } else {
                    importDialog.setProgressAll(nextId);
                    importDialog.setRun(false);
                    importManger.stop();
                }
            }

            if(!message.equals("success")) {
                new WrongDialog(FILE_CODE,message,context).show();
                importManger.stop();
            }
        } while (importManger.isRun());
    }

    @Override
    public void interrupt() {
        importManger.stop();
        STManger.UpdateSubjectList();
        //STManger.NotifySubjectSetChanged();
        super.interrupt();
    }
}

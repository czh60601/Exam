package czhy.grey.sun.exam.bin.dialog_;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import czhy.grey.sun.exam.R;

public class ImportDialog extends AlertDialog {

    private ProgressBar pBarNow;
    private ProgressBar pBarAll;
    private boolean isRun;

    public ImportDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_import);

        pBarAll = (ProgressBar) findViewById(R.id.pBarAll);
        pBarNow = (ProgressBar) findViewById(R.id.pBarNow);
    }

    public void setMaxFile(int max){
        pBarAll.setMax(max);
    }

    public void setMaxSize(int max){
        pBarNow.setMax(max);
    }

    public void setProgressAll(int progress){
        pBarAll.setProgress(progress);
    }

    public void setProgressNow(int progress){
        pBarNow.setProgress(progress);
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    @Override
    public void cancel() {
        if(!isRun)
            super.cancel();
    }
}

package czhy.grey.sun.exam.bin.dialog_;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import czhy.grey.sun.exam.R;

public class AboutDialog extends Dialog {

    public AboutDialog(Context context) {
        super(context);
        this.setTitle("关于");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_about);

        ((TextView) findViewById(R.id.versionName)).setText(getAppInfo());
        // TODO: 2017/4/23 help tips doc norm
    }

    private String getAppInfo(){
        try {
            return getContext().getPackageManager().getPackageInfo(getContext().getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}

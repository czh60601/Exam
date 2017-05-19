package czhy.grey.sun.exam.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.util.VersionInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import czhy.grey.sun.exam.R;
import czhy.grey.sun.exam.bin.dialog_.AboutDialog;
import czhy.grey.sun.exam.bin.dialog_.WrongDialog;
import czhy.grey.sun.exam.bin.manager_.DBManager;
import czhy.grey.sun.exam.bin.manager_.STManger;

public class MenuActivity extends Activity {
    private final String FILE_CODE = "#0001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void Main_Menu_OnClick(View view)
    {
        switch (view.getId()) {
            case R.id.Browse_all: {
                startActivity(new Intent(this, BrowseActivity.class));
            }
            break;

            case R.id.Clear_all: {
                DBManager.OpenDatabase();
                String message = DBManager.Clear();
                DBManager.CloseDatabase();
                if(message == null){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("提示");
                    dialog.setMessage("题库已清空。");
                    dialog.show();
                    new STManger();
                }else {
                    new WrongDialog(FILE_CODE,message,this).show();
                }
            }
            break;

            case R.id.Import_db: {
                startActivity(new Intent(this, ImportActivity.class));
            }
            break;

            case R.id.Random_exam: {
                startActivity(new Intent(this, TestActivity.class));
            }
            break;

            case R.id.About_:{
                new AboutDialog(this).show();
            }
            break;

            case R.id.Wrong_list:{
                startActivity(new Intent(this, WrongActivity.class));
            }
            break;
        }
    }
}

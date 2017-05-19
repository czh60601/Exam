package czhy.grey.sun.exam.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import czhy.grey.sun.exam.R;
import czhy.grey.sun.exam.activity.MenuActivity;
import czhy.grey.sun.exam.bin.manager_.DBManager;
import czhy.grey.sun.exam.bin.manager_.STManger;

public class Welcome extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        new DBManager();
        new STManger();
        // TODO: 2017/3/30  welcome

        Handler handler = new Handler();     //当计时结束,跳转至主界面
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(Welcome.this, MenuActivity.class));
                Welcome.this.finish();
            }

        }, 3000);
    }
}

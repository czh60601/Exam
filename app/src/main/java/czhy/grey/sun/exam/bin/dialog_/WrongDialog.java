package czhy.grey.sun.exam.bin.dialog_;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import czhy.grey.sun.exam.R;

public class WrongDialog extends Dialog{
    private String fileCode;
    private String message;

    public WrongDialog(String fileCode, String message, Context context) {
        super(context);

        this.message = message;
        this.fileCode = fileCode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_question_info);

        System.out.println(fileCode+" "+message);
    }

    private void SendBug() {
        // TODO: 2017/1/31 BUG反馈
        System.out.print("\n--------意见反馈--------\n" +
                "\n----------" + fileCode + "---------\n");
    }
}

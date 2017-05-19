package czhy.grey.sun.exam.bin.dialog_;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import czhy.grey.sun.exam.R;
import czhy.grey.sun.exam.bin.manager_.DBManager;
import czhy.grey.sun.exam.bin.manager_.STManger;
import czhy.grey.sun.exam.bin.question_.Question;

public class QuestionInfoDialog extends Dialog {
    private Question question;
    private boolean isEdit;

    public QuestionInfoDialog(Question question, Context context) {
        super(context);

        this.question = question;
        isEdit = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_question_info);

        TextView txtKeys = (TextView)findViewById(R.id.txtKeys);
        TextView txtType = (TextView)findViewById(R.id.txtType);
        TextView txtSubject = (TextView)findViewById(R.id.txtSubject);
        EditText txtQuestion = (EditText)findViewById(R.id.txtQuestion);
        GridView gvExtra = (GridView)findViewById(R.id.gvExtra);
        Button btnSubmit = (Button)findViewById(R.id.btnSubmit);
        Button btnCancel = (Button)findViewById(R.id.btnCancel);

        setTitle(question.getCodeNum());
        txtSubject.setText(STManger.GetSubject(question.getCode()));
        txtType.setText(question.getType());
        txtQuestion.setText(question.getQuestion());
        txtKeys.setText(question.getKeys());

        ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
        DBManager.OpenDatabase();
        Cursor cursor = DBManager.GetExtra(question.getNum());

        while (cursor.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("extra_code", cursor.getString(0));
            hashMap.put("extra_text", cursor.getString(1));
            data_list.add(hashMap);
        }

        cursor.close();
        DBManager.CloseDatabase();

        String[] from = {"extra_code", "extra_text"};
        int[] to = {R.id.extra_code, R.id.extra_text};
        gvExtra.setAdapter(new SimpleAdapter(getContext(), data_list, R.layout.grid_style_extra_dialog, from, to));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = false;
                cancel();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/3/30 修改提交
                isEdit = true;
            }
        });
    }

    @Override
    public void cancel() {
        if (!isEdit)
            super.cancel();
    }
}

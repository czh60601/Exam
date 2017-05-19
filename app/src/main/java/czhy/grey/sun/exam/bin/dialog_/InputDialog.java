package czhy.grey.sun.exam.bin.dialog_;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import czhy.grey.sun.exam.R;
import czhy.grey.sun.exam.bin.manager_.STManger;
import czhy.grey.sun.exam.bin.question_.QuestionSubject;

public class InputDialog extends Dialog {
    private boolean isInput;
    private EditText txtName;
    private String code;
    private View.OnClickListener listener;

    public InputDialog(Context context,View.OnClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_input);

        txtName = (EditText)findViewById(R.id.txtName);
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isInput = true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtName.getText().length()==0)
                    isInput = false;
            }
        });
        ArrayList<String> list = new ArrayList<>();
        final char chCode = 'A';
        for(int i=0;i<26;i++){
            list.add((char)((int)chCode+i)+"");
        }

        for(QuestionSubject subject: STManger.GetSubjectList()){
            list.remove(subject.getSubjectCode());
        }

        Spinner spinner = (Spinner)findViewById(R.id.sprCode);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                code = parent.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                code = parent.toString();
            }
        });
        spinner.setAdapter(new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,list));

        findViewById(R.id.btnAdd).setTag(this);
        findViewById(R.id.btnAdd).setOnClickListener(listener);

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInput = false;
                cancel();
            }
        });
    }

    public void setInput(boolean input) {
        isInput = input;
    }

    public String getName() {
        return txtName.getText().toString();
    }

    public String getCode() {
        return code;
    }

    @Override
    public void cancel() {
        if(!isInput)
            super.cancel();
    }
}

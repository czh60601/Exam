package czhy.grey.sun.exam.bin.holder_;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import czhy.grey.sun.exam.R;


public class QuestionHolder {
    private CheckBox data_codex;
    private TextView data_question;
    private TextView data_type;

    public QuestionHolder(View convertView)
    {
        data_codex = (CheckBox) convertView.findViewById(R.id.data_code);
        data_question = (TextView)convertView.findViewById(R.id.data_question);
        data_type = (TextView)convertView.findViewById(R.id.data_type);
    }

    public void setData_type(String type) {
        data_type.setText(type);
    }

    public void setData_question(String question) {
        data_question.setText(question);
    }

    public void setData_codex(int id, String codeName, boolean isChecked, View.OnClickListener listener) {
        // 设置list中TextView的显示
        data_codex.setText(codeName);
        data_codex.setTag(id);
        // 根据isSelected来设置checkbox的选中状况
        data_codex.setChecked(isChecked);
        //监听选中状态
        data_codex.setOnClickListener(listener);
    }
}

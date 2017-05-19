package czhy.grey.sun.exam.bin.adapter_;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import czhy.grey.sun.exam.bin.holder_.QuestionHolder;
import czhy.grey.sun.exam.R;
import czhy.grey.sun.exam.bin.question_.Question;

public class QuestionAdapter extends BaseAdapter{
    // 填充数据的list
    private ArrayList<Question> list;
    // 用来导入布局
    private LayoutInflater inflater = null;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> checkedList;

    public QuestionAdapter(ArrayList<Question> list, Context context) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        checkedList = new HashMap<>();
        // 初始化数据
        initDate();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Question getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionHolder holder;
        Question question = getItem(position);

        if (convertView == null) {
            // 获得ViewHolder对象
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.list_question_style, parent,false);
            holder = new QuestionHolder(convertView);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (QuestionHolder) convertView.getTag();
        }

        holder.setData_codex(position,question.getCodeNum(), getChecked(position),new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)  v.getTag();
                checkedList.put(position, !getChecked(position));
                ((CheckBox) v).setChecked(getChecked(position));
            }
        });
        holder.setData_question(question.getQuestion());
        holder.setData_type(question.getType());

        return convertView;
    }

    public void setChecked(int id,boolean isChecked) {
        checkedList.put(id,isChecked);
    }

    public boolean getChecked(int id) {
        return checkedList.get(id);
    }

    //私有函数
    /** 初始化isSelected的数据
     */
    private void initDate(){
        for(int i=0; i<list.size();i++) {
            checkedList.put(i,false);
        }
    }
}
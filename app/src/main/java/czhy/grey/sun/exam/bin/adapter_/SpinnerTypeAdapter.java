package czhy.grey.sun.exam.bin.adapter_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import czhy.grey.sun.exam.R;
import czhy.grey.sun.exam.bin.holder_.STHolder;
import czhy.grey.sun.exam.bin.question_.QuestionType;

import java.util.ArrayList;


public class SpinnerTypeAdapter extends ArrayAdapter {
    //数据
    private ArrayList<QuestionType> list;
    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public SpinnerTypeAdapter(ArrayList<QuestionType> list, Context context) {
        super(context,R.layout.spinner_style);

        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setInflater(Context context){
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public QuestionType getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        STHolder holder;
        QuestionType type = getItem(position);
        //下拉项
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_dropdown_style, parent, false);

            // 设置list中TextView的显示
            holder = new STHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (STHolder) convertView.getTag();
        }

        holder.setName(type.getTypeName(),position);
        holder.setDelete(false);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        STHolder holder;
        QuestionType type = getItem(position);
        //选中显示
        if (convertView == null) {
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.spinner_style,parent,false);

            // 设置list中TextView的显示
            holder = new STHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (STHolder) convertView.getTag();
        }

        holder.setName(type.getTypeName(),position);

        return convertView;
    }

    public int size(){
        return list.size();
    }
}

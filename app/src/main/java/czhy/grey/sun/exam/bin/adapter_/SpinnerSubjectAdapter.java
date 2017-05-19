package czhy.grey.sun.exam.bin.adapter_;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import czhy.grey.sun.exam.R;
import czhy.grey.sun.exam.bin.holder_.STHolder;
import czhy.grey.sun.exam.bin.question_.QuestionSubject;

import java.util.ArrayList;

public class SpinnerSubjectAdapter extends ArrayAdapter {
    //数据
    private ArrayList<QuestionSubject> list;
    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public SpinnerSubjectAdapter(ArrayList<QuestionSubject> list, Context context) {
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
    public QuestionSubject getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        STHolder holder;
        QuestionSubject subject = getItem(position);
        //下拉项
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_dropdown_style, parent, false);

            // 设置list中TextView的显示
            holder = new STHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (STHolder) convertView.getTag();
        }

        holder.setName(subject.getSubjectName(),position);
        holder.setDelete(subject.isEmpty());

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        STHolder holder;
        QuestionSubject subject = getItem(position);
        //选中显示
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_style,parent,false);

            // 设置list中TextView的显示
            // 设置list中TextView的显示
            holder = new STHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (STHolder) convertView.getTag();
        }

        holder.setName(subject.getSubjectName(),position);
        return convertView;
    }

    public int size() {
        return list.size();
    }
}

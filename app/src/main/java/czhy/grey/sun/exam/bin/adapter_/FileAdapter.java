package czhy.grey.sun.exam.bin.adapter_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import czhy.grey.sun.exam.R;
import czhy.grey.sun.exam.bin.holder_.FileHolder;

public class FileAdapter extends BaseAdapter {
    private ArrayList<File> list;
    private LayoutInflater inflater;
    private boolean isRoot;
    // 用来控制CheckBox的选中状况
    private HashMap<Integer, Boolean> checkedList;
    private int selectNum;

    public FileAdapter(Context context, ArrayList<File> list,boolean isRoot) {
        this.list = list;
        this.isRoot = isRoot;
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
    public File getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileHolder holder;
        File file = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_file_style, parent, false);
            holder = new FileHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (FileHolder) convertView.getTag();
        }

        if(isRoot){
            holder.setId(position);
            holder.setTime(new SimpleDateFormat("yyyy/mm/hh/dd hh:mm:ss").format(file.lastModified()));
            holder.setSize("");
        }else if (position == 0) {
            holder.setName("返回上一层", file.isDirectory(), getChecked(position));
            holder.setId(position, getChecked(position),new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int)  v.getTag();
                    boolean b = !checkedList.get(position);

                    checkedList.put(position, b);
                    ((CheckBox) v).setChecked(b);
                    //全选或全取消操作
                    for(int i=0;i< getCount();i++){
                        setChecked(i,b);
                    }
                    selectNum = b?getCount()-1:0;

                    notifyDataSetChanged();
                }
            });
            holder.setTime("全选");
            holder.setSize("已选择"+selectNum+"项");
        } else {
            holder.setName(file.getName(), file.isDirectory(), getChecked(position));
            holder.setId(position, getChecked(position),new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int)  v.getTag();
                    boolean b = !getChecked(position);
                    checkedList.put(position, b);
                    ((CheckBox) v).setChecked(b);
                    //是否已经全选
                    if(isSelectedAll()) {
                        checkedList.put(0, true);
                        selectNum = getCount();
                    }else {
                        checkedList.put(0, false);
                        selectNum = b?selectNum+1:selectNum-1;
                    }

                    notifyDataSetChanged();
                }
            });
            holder.setTime(new SimpleDateFormat("yyyy/mm/hh/dd hh:mm:ss").format(file.lastModified()));

            if (file.isFile())
                holder.setSize(fileLength(file.length()));
            else {
                holder.setSize("");
            }
        }

        return convertView;
    }

    public boolean getChecked(int id) {
        return checkedList.get(id);
    }

    public int getSelectNum() {
        return selectNum;
    }

    //私有函数
    /** 初始化isSelected的数据
     */
    private void initDate() {
        selectNum = 0;
        for (int i = 0; i < list.size(); i++) {
            checkedList.put(i, false);
        }
    }

    private void setChecked(int id,boolean isChecked) {
        checkedList.put(id,isChecked);
    }

    private String fileLength(long length) {
        String size;

        if (length > 1024 * 1024)
            size = new DecimalFormat("#.00").format(length / (1024.0 * 1024.0)) + "MB";
        else if (length > 1024)
            size = new DecimalFormat("#.00").format(length / 1024.0) + "KB";
        else
            size = length + "B";

        return size;
    }

    private boolean isSelectedAll(){
        for(int i=1;i< getCount();i++){
            if(!getChecked(i))
                return false;
        }

        return true;
    }

}

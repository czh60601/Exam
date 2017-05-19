package czhy.grey.sun.exam.bin.holder_;


import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import czhy.grey.sun.exam.R;

public class FileHolder{

    private CheckBox cbSelect;
    private TextView name;
    private TextView time;
    private TextView size;
    private ImageView img;

    public FileHolder(View convertView) {
        cbSelect = (CheckBox)convertView.findViewById(R.id.cbSelect);
        name = (TextView)convertView.findViewById(R.id.name);
        time = (TextView)convertView.findViewById(R.id.time);
        img = (ImageView)convertView.findViewById(R.id.img);
        size = (TextView)convertView.findViewById(R.id.size);
    }

    public void setTime(String time) {
        this.time.setText(time);
    }

    public void setId(int id) {
        switch (id){
            case 0:{
                img.setImageResource(R.mipmap.inside);
                name.setText("内置储存卡");
            }break;

            case 1:{
                img.setImageResource(R.mipmap.sdcard);
                name.setText("外置存储卡");
            }break;

            default:{
                img.setImageResource(R.mipmap.usbotg);
                name.setText("USBOTG"+(id-2));
            }
        }
        cbSelect.setVisibility(View.INVISIBLE);
    }

    public void setId(int id,boolean isSelected, View.OnClickListener listener) {
        cbSelect.setTag(id);
        cbSelect.setChecked(isSelected);
        cbSelect.setOnClickListener(listener);
    }

    public void setName(String name,boolean isDirectory,boolean isChecked) {
        this.name.setText(name);
        cbSelect.setChecked(isChecked);
        if (isDirectory) {
            if(name.equalsIgnoreCase("返回上一层")){
                img.setImageResource(R.mipmap.back);
            }else
                img.setImageResource(R.mipmap.folder);
        }else {
            if (name.toLowerCase().endsWith(".doc")) {
                img.setImageResource(R.mipmap.doc_text);
            } else {
                img.setImageResource(R.mipmap.other);
            }
        }
    }

    public void setSize(String size) {
        this.size.setText(size);
    }
}

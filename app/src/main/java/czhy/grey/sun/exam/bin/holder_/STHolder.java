package czhy.grey.sun.exam.bin.holder_;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import czhy.grey.sun.exam.R;
import czhy.grey.sun.exam.bin.manager_.STManger;

public class STHolder {
    private TextView txtName;
    private ImageView btnDelete;

    public STHolder(View convertView){
        txtName = (TextView) convertView.findViewById(R.id.txtName);
        btnDelete = (ImageView) convertView.findViewById(R.id.btnDelete);
    }

    public void setName(String name,int id) {
        txtName.setText(name);
        txtName.setTag(id);
    }

    public void setDelete(boolean isEmpty){
        if(isEmpty){
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    STManger.DeleteSubject((int)txtName.getTag());
                    STManger.NotifySubjectSetChanged();
                }
            });
        }else {
            btnDelete.setVisibility(View.INVISIBLE);
        }
    }
}

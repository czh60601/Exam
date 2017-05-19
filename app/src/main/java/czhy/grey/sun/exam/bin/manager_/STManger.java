package czhy.grey.sun.exam.bin.manager_;

import android.content.Context;
import android.database.Cursor;

import czhy.grey.sun.exam.bin.adapter_.SpinnerSubjectAdapter;
import czhy.grey.sun.exam.bin.adapter_.SpinnerTypeAdapter;
import czhy.grey.sun.exam.bin.question_.QuestionSubject;
import czhy.grey.sun.exam.bin.question_.QuestionType;

import java.util.ArrayList;

public class STManger {

    private static ArrayList<QuestionSubject> subjectList;
    private static SpinnerSubjectAdapter subjectAdapter;

    private static ArrayList<QuestionType> typeList;
    private static SpinnerTypeAdapter typeAdapter;

    public STManger() {
        subjectList = new ArrayList<>();
        typeList = new ArrayList<>();

        typeList.add(new QuestionType(0, "选择题型"));
        subjectList.add(new QuestionSubject(null, "选择科目",false));//隐藏删除图标

        DBManager.OpenDatabase();
        Cursor cursor;
        cursor = DBManager.GetTypeOrSubject("Type_");
        while (cursor.moveToNext()) {
            typeList.add(new QuestionType(Integer.parseInt(cursor.getString(0)), cursor.getString(1)));
        }

        cursor.close();
        cursor = DBManager.GetTypeOrSubject("Subject_");
        while (cursor.moveToNext()) {
            subjectList.add(new QuestionSubject(cursor.getString(0), cursor.getString(1),Integer.parseInt(cursor.getString(2)) == 0));
        }

        cursor.close();
        DBManager.CloseDatabase();
    }

    public static ArrayList<QuestionSubject> GetSubjectList() {
        return subjectList;
    }

    public static ArrayList<QuestionType> GetTypeList() {
        return typeList;
    }

    public static String GetSubject(String subjectCode) {
        for (int i = 1; i< subjectList.size(); i++)
            if(subjectList.get(i).getSubjectCode().equals(subjectCode))
                return subjectList.get(i).getSubjectName();

        return null;
    }

    public static void AddNewSubject(String code,String name) {
        subjectList.add(new QuestionSubject(code,name,true));
        DBManager.OpenDatabase();
        DBManager.AddNewSubject(code,name);
        DBManager.CloseDatabase();
    }

    public static int GetTypeCode(String typeName) {
        for (QuestionType type:typeList)
            if(type.getTypeName().equals(typeName))
                return type.getTypeCode();

        return 0;
    }

    public static void SetInflater(Context context){
        if(subjectAdapter != null) {
            subjectAdapter.setInflater(context);
            typeAdapter.setInflater(context);
        }
        else {
            subjectAdapter = new SpinnerSubjectAdapter(subjectList, context);
            typeAdapter = new SpinnerTypeAdapter(typeList,context);
        }
    }

    public static SpinnerSubjectAdapter GetSubjectAdapter(){
        return subjectAdapter;
    }

    public static void NotifySubjectSetChanged() {
        subjectAdapter.notifyDataSetChanged();
    }

    public static void DeleteSubject(int id) {
        DBManager.OpenDatabase();
        DBManager.DeleteSubject(subjectList.get(id).getSubjectCode());
        DBManager.CloseDatabase();
        subjectList.remove(id);
    }

    public static SpinnerTypeAdapter GetTypeAdapter(){
        return typeAdapter;
    }

    public static void UpdateSubjectList() {
        subjectList.clear();
        subjectList.add(new QuestionSubject(null, "选择科目",false));//隐藏删除图标

        DBManager.OpenDatabase();
        Cursor cursor = DBManager.GetTypeOrSubject("Subject_");
        while (cursor.moveToNext()) {
            subjectList.add(new QuestionSubject(cursor.getString(0), cursor.getString(1),cursor.getInt(2) == 0));
        }

        cursor.close();
        DBManager.CloseDatabase();
    }
}

package czhy.grey.sun.exam.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import czhy.grey.sun.exam.R;
import czhy.grey.sun.exam.bin.adapter_.QuestionAdapter;
import czhy.grey.sun.exam.bin.dialog_.QuestionInfoDialog;
import czhy.grey.sun.exam.bin.manager_.DBManager;
import czhy.grey.sun.exam.bin.question_.Question;
import czhy.grey.sun.exam.bin.question_.QuestionSubject;
import czhy.grey.sun.exam.bin.question_.QuestionType;
import czhy.grey.sun.exam.bin.manager_.STManger;

import java.util.ArrayList;

public class BrowseActivity extends Activity {

    private final String FILE_CODE = "#0003";
    private int type_codee;
    private String subject_code;
    private String keys;
    private QuestionAdapter questionList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        subject_code = null;
        type_codee = 0;
        keys = "";

        listView = (ListView) findViewById(R.id.dataList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new QuestionInfoDialog((Question) parent.getAdapter().getItem(position),BrowseActivity.this).show();
            }
        });

        iniData();
        setTypeAndSubject();
        refurbish();
    }

    private void iniData(){
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                keys = newText;
                refurbish();
                return false;
            }
        });
    }

    private void setTypeAndSubject(){
        STManger.SetInflater(this);
        Spinner spinner = (Spinner)findViewById(R.id.sprType);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type_codee = ((QuestionType)parent.getAdapter().getItem(position)).getTypeCode();
                refurbish();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type_codee = 0;
            }
        });

        spinner.setAdapter(STManger.GetTypeAdapter());

        spinner = (Spinner)findViewById(R.id.sprSubject);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject_code = ((QuestionSubject)parent.getAdapter().getItem(position)).getSubjectCode();
                refurbish();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                subject_code = null;
            }
        });

        spinner.setAdapter(STManger.GetSubjectAdapter());
    }

    private void refurbish() {
        ArrayList<Question> dataList = new ArrayList<>();
        DBManager.OpenDatabase();
        Cursor cursor = DBManager.GetInfo(keys,type_codee, subject_code);

        while (cursor.moveToNext()) {
            dataList.add(new Question(cursor.getString(0), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
        }

        cursor.close();
        DBManager.CloseDatabase();
        questionList = new QuestionAdapter(dataList, BrowseActivity.this);
        listView.setAdapter(questionList);
    }

    public void btnSelectAll(View v){
        for(int i = 0; i< questionList.getCount(); i++){
            questionList.setChecked(i,true);
        }

        questionList.notifyDataSetChanged();
    }

    public void btnInverse(View v){
        for(int i = 0; i< questionList.getCount(); i++){
            questionList.setChecked(i, !questionList.getChecked(i));
        }

        questionList.notifyDataSetChanged();
    }

    public void btnCancel(View v){
        for(int i = 0; i< questionList.getCount(); i++){
            questionList.setChecked(i,false);
        }

        questionList.notifyDataSetChanged();
    }

    public void btnDelete(View v) {

        DBManager.OpenDatabase();

        for (int i = 0; i < questionList.getCount(); i++) {
            if (questionList.getChecked(i)) {
                DBManager.DeleteQuestion(questionList.getItem(i));
            }
        }

        DBManager.CloseDatabase();
        STManger.UpdateSubjectList();
        STManger.NotifySubjectSetChanged();
        refurbish();
    }
}

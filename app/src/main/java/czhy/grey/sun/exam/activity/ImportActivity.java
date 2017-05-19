package czhy.grey.sun.exam.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import czhy.grey.sun.exam.R;
import czhy.grey.sun.exam.bin.adapter_.FileAdapter;
import czhy.grey.sun.exam.bin.dialog_.InputDialog;
import czhy.grey.sun.exam.bin.manager_.ImportManger;
import czhy.grey.sun.exam.bin.other_.ImportThread;
import czhy.grey.sun.exam.bin.manager_.STManger;
import czhy.grey.sun.exam.bin.other_.SearchDoc;
import czhy.grey.sun.exam.bin.question_.QuestionSubject;

public class ImportActivity extends Activity {
    private final String FILE_CODE = "#0002";

    private ImportThread thread;
    private ImportManger importManger;
    private ListView folderList;
    private TextView txtPath;
    private FileAdapter fileAdapter;
    private ArrayList<File> rootFileList;
    private int folder_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        txtPath = (TextView) findViewById(R.id.txtPath);
        folderList = (ListView) findViewById(R.id.folderList);
        folderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = fileAdapter.getItem(position);
                if (file.isDirectory()) {
                    if(folder_level == 0)//判断是否在根目录
                        refurbish(file,false);
                    else if (folder_level == 1 && position == 0) //判断当前位置是否在一级目录 并且 是否点击第一项
                        rootFile();
                    else
                        refurbish(file,position == 0);
                }
            }
        });
        getRootFile();
        rootFile();
        setSubject();
        importManger = new ImportManger();
    }

    private void getRootFile() {
        rootFileList = new ArrayList<>();
        StorageManager storageManager = (StorageManager) this.getSystemService(STORAGE_SERVICE);
        try {
            //内部存储器
            File inside = null;
            //可移除存储器集合
            ArrayList<File> outside = new ArrayList<>();

            //获取存储器接口 API-24以下不支持StorageVolume接口
            //API-24开始可直接 List<StorageVolume> svList = storageManager.getStorageVolumes();
            Method getVolumeList = StorageManager.class.getMethod("getVolumeList");
            getVolumeList.setAccessible(true);
            //获取存储器列表
            Object[] invokes = (Object[]) getVolumeList.invoke(storageManager);
            if (invokes != null) {
                for (Object obj : invokes) {
                    //获取存储器地址接口
                    Method getPath = obj.getClass().getMethod("getPath");
                    //获取存储器地址
                    String path = (String) getPath.invoke(obj);
                    File file = new File(path);
                    if (file.canWrite()) {
                        //获取存储器是否可移除接口
                        Method isRemovable = obj.getClass().getMethod("isRemovable");
                        //存储器是否可移除
                        if ((isRemovable.invoke(obj)).equals(true)) {
                            outside.add(file);
                        } else {
                            inside = file;
                        }
                    }
                }
                //按0-内部存储器 >0外部存储器 顺序 添加到根目录列表
                rootFileList.add(inside);
                rootFileList.addAll(outside);
            }
        } catch (NoSuchMethodException |
                IllegalArgumentException |
                IllegalAccessException |
                InvocationTargetException e1) {
            e1.printStackTrace();
        }
    }

    //获取根目录数据
    private void rootFile() {
        txtPath.setText("/");
        fileAdapter = new FileAdapter(this, rootFileList, true);
        folderList.setAdapter(fileAdapter);
        folder_level = 0;
    }

    //获取目录数据
    private void refurbish(File folder,boolean isBack) {
        if (isBack)
            folder_level--;
        else
            folder_level++;

        txtPath.setText(folder.getPath());
        ArrayList<File> files = new ArrayList<>();

        File[] folderFile = folder.listFiles();
        if (null != folderFile && folderFile.length > 0) {
            Collections.addAll(files, folderFile);
        }

        //排序 文件夹在前，然后按文件名排序
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                if (f1.isDirectory()) {
                    if (f2.isDirectory()) {
                        return f1.getName().compareToIgnoreCase(f2.getName());
                    } else {
                        return -1;
                    }
                } else if (f2.isDirectory()) {
                    return 1;
                } else {
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            }
        });

        //新建集合用做打开文件夹
        ArrayList<File> openedFolder = new ArrayList<>();
        //将上层目录保存到第一项
        openedFolder.add(folder.getParentFile());
        //将排序完毕的内容添加到目标集合 目的:解决第一个文件夹不是上一层地址问题
        openedFolder.addAll(files);
        fileAdapter = new FileAdapter(this, openedFolder, false);
        folderList.setAdapter(fileAdapter);
    }

    private void setSubject() {
        STManger.SetInflater(this);
        (findViewById(R.id.btnAddSubject)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog inputDialog = new InputDialog(ImportActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputDialog dialog = (InputDialog) v.getTag();
                        STManger.AddNewSubject(dialog.getCode(),dialog.getName());
                        ((Spinner)findViewById(R.id.sprSubject)).setSelection(STManger.GetSubjectAdapter().size() - 1);
                        dialog.setInput(false);
                        dialog.cancel();
                    }
                });
                inputDialog.setTitle("请输入科目");
                inputDialog.show();
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.sprSubject);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subject_code = ((QuestionSubject) parent.getAdapter().getItem(position)).getSubjectCode();
                importManger.setSubject_Code(subject_code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                importManger.setSubject_Code(null);
            }
        });

        spinner.setAdapter(STManger.GetSubjectAdapter());
    }

    /**
     * 监听物理按键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            if (importManger.isRun()) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("警告");
                dialog.setMessage("正在导入题目，是否放弃剩余导入，继续退出？");

                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        importManger.stop();
                        finish();
                    }
                });

                dialog.setNegativeButton("取消", null);

                dialog.show();
                return true;
            } else {
                if (folder_level != 0) {
                    if (folder_level != 1)
                        refurbish(fileAdapter.getItem(0),true);
                    else
                        rootFile();

                    return true;
                } else {
                    finish();
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public void btnOnClick(View v) {
        if (importManger.getSubject_Code() != null) {
            SearchDoc sDoc = new SearchDoc(fileAdapter);

            if (!sDoc.isEmpty()) {
                //申请新线程
                thread = new ImportThread(this, sDoc.getFiles(), importManger);
                thread.start();
            } else {
                Toast.makeText(this, "请选择导入的文件或文件夹", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请选择导入科目", Toast.LENGTH_SHORT).show();
        }
    }
}

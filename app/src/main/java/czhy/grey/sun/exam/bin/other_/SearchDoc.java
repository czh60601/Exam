package czhy.grey.sun.exam.bin.other_;

import java.io.File;
import java.util.ArrayList;

import czhy.grey.sun.exam.bin.adapter_.FileAdapter;

public class SearchDoc {
    private ArrayList<String> list;

    public SearchDoc(FileAdapter fileAdapter) {
        list = new ArrayList<>();
        int size;
        int nextId;
        File file;

        for (nextId = 0, size = 0;
             size < fileAdapter.getSelectNum() || nextId < fileAdapter.getCount();
             nextId++) {
            if (fileAdapter.getChecked(nextId)) {
                file = fileAdapter.getItem(nextId);
                if (file.isDirectory()) {
                    analyze(file);
                } else {
                    if (file.getName().toLowerCase().endsWith(".doc")) {
                        list.add(file.getAbsolutePath());
                    }
                }
                size++;
            }
        }
    }

    public ArrayList<String> getFiles(){
        return list;
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }

    private void analyze(File folder) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                analyze(file);
            } else {
                if (file.getName().toLowerCase().endsWith(".doc") || file.getName().toLowerCase().endsWith(".docx")) {
                    list.add(file.getAbsolutePath());
                }
            }
        }
    }
}

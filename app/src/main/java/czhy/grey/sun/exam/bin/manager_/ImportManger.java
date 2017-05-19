package czhy.grey.sun.exam.bin.manager_;

import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import czhy.grey.sun.exam.bin.question_.Question;
import czhy.grey.sun.exam.bin.question_.QuestionExtra;

public class ImportManger {
    private Question qForImport;
    private String bugMessage;
    private String subject_code;
    private boolean isNewQuestion;

    private int count;
    private int nextId;
    private boolean run;
    private boolean readAnswer;
    private ArrayList<String> list;

    public ImportManger() {
        list = new ArrayList<>();
        run = false;
        readAnswer = false;
        nextId = -1;
        count = -1;
    }

    public void start() {
        run = true;
        System.out.println("im-start");
    }

    public void stop() {
        run = false;
        System.out.println("im-stop");
    }

    public boolean isRun() {
        return run;
    }

    public void setSubject_Code(String code) {
        subject_code = code;
    }

    public String OpenAndRead(String namePath) {
        bugMessage = "success";

        try {
            Log.i("Read New Document",namePath);
            list.clear();
            bugMessage = readDOC(namePath);
            nextId = -1;
        } catch (IOException e) {
            bugMessage = e.getMessage();
        }

        return bugMessage;
    }

    public String AnalyzeParagraphText() {
        if(nextId < Size()) {
            StringBuilder str = clearSpace(list.get(nextId));
            String copy = str.toString();
            String data = isType(str.toString());

            if (data.equals("success")) {

                bugMessage = data = isExtra(str, "*");

                if (data.equals(copy)) {
                    data = isAnswer(str);

                    if (data.equals("success")) {
                        bugMessage = isQuestion(str);
                    } else {
                        bugMessage = "success";
                    }
                } else {
                    bugMessage = "success";
                }
            } else {
                qForImport = new Question(data, subject_code);//Question(type,subject_code)
                isNewQuestion = true;
                bugMessage = "success";
            }
        }else {
            isToInsert();
        }

        return bugMessage;
    }

    public boolean MoveToNext() {
        return ++nextId <= Size();
    }

    public int Size() {
        return count;
    }

    public String getSubject_Code() {
        return subject_code;
    }

    public int getNextId() {
        return nextId;
    }
    // 私有变量

    private String readDOC(String namePath) throws IOException {
        FileInputStream in;
        POIFSFileSystem pfs;

        try {
            in = new FileInputStream(namePath);
            pfs = new POIFSFileSystem(in);
            HWPFDocument hwpf = new HWPFDocument(pfs);
            Range range = hwpf.getRange();
            count = range.numParagraphs();
            for (int i = 0;i < range.numParagraphs();i++)
                list.add(range.getParagraph(i).text().trim());
            pfs.close();
            in.close();
        } catch (IOException e) {
            return e.getMessage();
        }

        return "success";
    }

    private String isType(String str) {
        switch (str) {
            case "单选题":
            case "单项选择题": {
                return "单选题";
            }

            case "多选题":
            case "多项选择题": {
                return "多选题";
            }

            case "判断题": {
                return "判断题";
            }

            case "辨析题": {
                return "辨析题";
            }

            case "填空题": {
                return "填空题";
            }

            case "计算题": {
                return "计算题";
            }

            case "应用题": {
                return "应用题";
            }

            case "思考题": {
                return "思考题";
            }

            default:
                return "success";
        }
    }

    private String isExtra(StringBuilder str, String no_) {
            String extra = str.toString();
            int n = str.indexOf(".") != -1 ? str.indexOf("．") != -1 ? str.indexOf("．") : str.indexOf(".") : str.indexOf("、");
            char extraCode;
            //System.out.println(no_+"-0.1-"+extra);
            if (n != -1) {
                extraCode = str.charAt(n - 1);

                //System.out.println(no_+"-0.2-"+extra);
                //是否是选项
                if (extraCode >= 'a' && extraCode <= 'z' ||
                        extraCode >= 'A' && extraCode <= 'Z') {
                    String copy = str.delete(0, n + 1).toString();

                    //System.out.println(no_+"-1-"+str);
                    //是 提交递归
                    extra = isExtra(str, no_ + "*");

                    //System.out.println(no_+"-2-"+extra);
                    //返回值是否与原值相同 Y-无后续项 N-有后续项
                    //System.out.println(no_+"-3-"+extra+"-"+copy);
                    if (extra.equals(copy)) {
                        qForImport.addExtra(new QuestionExtra(extraCode, extra));
                        //System.out.println(no_+"-5-"+extraCode+extra);
                    } else {
                        //提取备份内容 删除后续项 PS:递归返回值为后续项
                        str = new StringBuilder(copy);
                        n = str.indexOf(".") != -1 ? str.indexOf("．") != -1 ? str.indexOf("．") : str.indexOf(".") : str.indexOf("、");

                        //System.out.println(no_+"-4.1-"+str);
                        extra = str.delete(n - 1, str.length()).toString();

                        //System.out.println(no_+"-4.2-"+str);
                        //System.out.println(no_+"-4.3-"+extra);
                        qForImport.addExtra(new QuestionExtra(extraCode, extra));
                        //System.out.println(no_+"-6-"+extraCode+extra);
                    }
                }
            }

            return extra;
    }

    private String isAnswer(StringBuilder str) {
        if (str.toString().startsWith("#A#")) {
            readAnswer = true;
            qForImport.setKeys("#A#");

            if(!str.toString().equals("#A#")){
                str.delete(0,3);

                if(str.toString().endsWith("#A#")) {
                    readAnswer = false;
                    str.delete(str.length()-3,str.length());
                    qForImport.addExtra(new QuestionExtra('#', str.toString()));
                }else {
                    qForImport.addExtra(new QuestionExtra('#', str.toString()));
                }
            }

            return "#A#";
        }else if(str.toString().endsWith("#A#")){
            readAnswer = false;

            if(str.delete(str.length()-3,str.length()).length() != 0){
                qForImport.addExtra(new QuestionExtra('#', str.toString()));
            }

            return "#A#";
        }else if(readAnswer) {
            if (str.indexOf("#A#") != -1) {
                readAnswer = false;

                return "#A#";
            }else
            qForImport.addExtra(new QuestionExtra('#', str.toString()));

            return "#A#";
        }

        return "success";
    }

    private String isQuestion(StringBuilder str) {
        isToInsert();

        int n = str.indexOf(".") != -1 ? str.indexOf(".") : str.indexOf("、");
        int m;
        if (n != -1)
            str.delete(0, n + 1);

        n = str.indexOf("（") != -1 ? str.indexOf("（") : str.indexOf("(");
        m = str.indexOf("）") != -1 ? str.indexOf("）") : str.indexOf(")");

        if (n != -1) {
            char[] keys = new char[m - n - 1];
            str.getChars(n + 1, m, keys, 0);

            if (qForImport.getType().equals("判断题")) {
                switch (new String(keys)) {
                    case "Y":
                    case "y":
                    case "T":
                    case "t":
                    case "√":
                    case "正确": {
                        qForImport.setKeys("T");
                    }
                    break;

                    default: {
                        qForImport.setKeys("F");
                    }
                }
            } else {
                qForImport.setKeys(new String(keys));
            }

            str.delete(n, m + 1);
            str.insert(n, "(   )");
        }

        if(str.length() != 0) {
            qForImport.setQuestion(str.toString());
        }

        return "success";
    }

    private StringBuilder clearSpace(String inString) {
        int n;
        StringBuilder outString = new StringBuilder();
        outString.append(inString);

        for (n = outString.indexOf("　"); n != -1; n = outString.indexOf("　")) {
            outString.deleteCharAt(n);
        }

        for (n = outString.indexOf("\t"); n != -1; n = outString.indexOf("\t")) {
            outString.deleteCharAt(n);
        }

        return outString;
    }

    private void isToInsert() {
        if (isNewQuestion) {
            isNewQuestion = false;
            return;
        }

        if (qForImport.getQuestion() != null) {
            System.out.println(qForImport.getCode() + qForImport.getType() + ":" + qForImport.getQuestion());
            for (QuestionExtra extra : qForImport.getExtra()) {
                System.out.println("\t" + extra.getExtra_code() + "." + extra.getExtra_text());
            }
            System.out.println("答案：" + qForImport.getKeys());
            System.out.println();

            DBManager.OpenDatabase();
            DBManager.Insert(qForImport);
            DBManager.CloseDatabase();

            qForImport = new Question(qForImport.getType(), subject_code);
            isNewQuestion = true;
        }
    }
}

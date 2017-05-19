package czhy.grey.sun.exam.bin.question_;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Question {
    private String code;
    private int num;
    private String question;
    private String keys;
    private String type;
    private ArrayList<QuestionExtra> extra;

    public Question(String type, String code) {
        this.type = type;
        this.code = code;
        extra = new ArrayList<>();
    }

    public Question(String code, int num, String question, String keys, String type) {
        this.code = code;
        this.num = num;
        this.question = question;
        this.keys = keys;
        this.type = type;
    }

    public String getCodeNum() {
        NumberFormat nf = NumberFormat.getInstance();
        //设置是否使用分组
        nf.setGroupingUsed(false);
        //设置最大整数位数
        nf.setMaximumIntegerDigits(4);
        //设置最小整数位数
        nf.setMinimumIntegerDigits(4);

        return code + nf.format(num);
    }

    public String getCode() {
        return code;
    }

    public int getNum() {
        return num;
    }

    public String getQuestion() {
        return question;
    }

    public String getKeys() {
        return keys;
    }

    public String getType() {
        return type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getData_() {
        return new String[]{question, keys, type};
    }

    public ArrayList<QuestionExtra> getExtra() {
        return extra;
    }

    public void setExtra(ArrayList<QuestionExtra> extra) {
        this.extra = extra;
    }

    public void addExtra(QuestionExtra extra) {
        this.extra.add(extra);

        //排序
        Collections.sort(this.extra, new Comparator<QuestionExtra>() {
            @Override
            public int compare(QuestionExtra qc1, QuestionExtra qc2) {
                //按选项标号排序
                return qc1.getExtra_code()-qc2.getExtra_code();

            }

        });
    }
}


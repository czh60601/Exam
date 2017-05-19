package czhy.grey.sun.exam.bin.question_;

public class QuestionExtra {
    private char extra_code;
    private String extra_text;

    public QuestionExtra(char extra_code, String extra_text){
        this.extra_code = extra_code;
        this.extra_text = extra_text;
    }

    public char getExtra_code() {
        return extra_code;
    }

    public String getExtra_text() {
        return extra_text;
    }
}

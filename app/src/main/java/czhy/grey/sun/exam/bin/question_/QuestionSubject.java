package czhy.grey.sun.exam.bin.question_;

public class QuestionSubject {
    private String subjectCode;
    private String subjectName;
    private boolean isEmpty;

    public QuestionSubject(String subjectCode, String subjectName,boolean isEmpty){
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.isEmpty = isEmpty;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}

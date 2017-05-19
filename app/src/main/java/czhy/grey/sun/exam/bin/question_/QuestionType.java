package czhy.grey.sun.exam.bin.question_;

public class QuestionType {
    private int typeCode;
    private String typeName;

    public QuestionType(int typeCode,String typeName){
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getTypeCode() {
        return typeCode;
    }
}

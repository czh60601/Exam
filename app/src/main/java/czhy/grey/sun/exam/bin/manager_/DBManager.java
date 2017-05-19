package czhy.grey.sun.exam.bin.manager_;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import czhy.grey.sun.exam.bin.question_.Question;
import czhy.grey.sun.exam.bin.question_.QuestionExtra;

import java.io.File;

public class DBManager{
    private static final String DB_NAME = "exam_db.db"; //保存的数据库文件名
    private static final String PACKAGE_NAME = "czhy.grey.sun.exam";
    private static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME;  //在手机里存放数据库的位置
    private static final String DB_FILE = DB_PATH + "/" + DB_NAME;

    private static SQLiteDatabase database;

    public DBManager() {
        onCreate();
    }

    private static void onCreate() {
        if (!(new File(DB_FILE).exists())) {//判断数据库文件是否存在，若不存在则新建(执行导入)，否则跳过
                /* 导入已有
                InputStream is = context.getAssets().open(DB_NAME); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(DB_FILE);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                    }
                fos.close();
                is.close();
                */
            database = SQLiteDatabase.openOrCreateDatabase(DB_FILE,null);
            database.execSQL("CREATE TABLE [Type_](" +
                    "    [type_code] INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "    [type_name] VARCHAR(50) NOT NULL)");
            database.execSQL("CREATE TABLE [Subject_](" +
                    "    [subject_code] VARCHAR(5) PRIMARY KEY," +
                    "    [subject_name] VARCHAR(50) NOT NULL," +
                    "    [subject_count] INTEGER NOT NULL DEFAULT 0)");
            database.execSQL("CREATE TABLE [Data_](" +
                    "    [data_subject] VARCHAR(5) NOT NULL REFERENCES [Subject_]([subject_code]) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "    [data_code] INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "    [data_question] TEXT NOT NULL," +
                    "    [data_keys] VARCHAR(10) NOT NULL," +
                    "    [data_type] INTEGER NOT NULL REFERENCES [Type_]([type_code]) ON DELETE CASCADE ON UPDATE CASCADE)");
            database.execSQL("CREATE TABLE [Extra_](" +
                    "    [extra_code] VARCHAR NOT NULL," +
                    "    [extra_text] TEXT NOT NULL," +
                    "    [extra_data] INTEGER NOT NULL REFERENCES [Data_]([data_code]) ON DELETE CASCADE ON UPDATE CASCADE)");
            database.execSQL("CREATE TABLE [Wrong_](" +
                    "    [wrong_data] INTEGER NOT NULL REFERENCES [Data_]([data_code]) ON DELETE CASCADE ON UPDATE CASCADE)");
            database.execSQL("INSERT INTO [Type_] ([type_code],[type_name]) VALUES (1000101,'单选题')");
            database.execSQL("INSERT INTO [Type_] ([type_code],[type_name]) VALUES (1000102,'多选题')");
            database.execSQL("INSERT INTO [Type_] ([type_code],[type_name]) VALUES (1000103,'判断题')");
            database.execSQL("INSERT INTO [Type_] ([type_code],[type_name]) VALUES (1000104,'辨析题')");
            database.execSQL("INSERT INTO [Type_] ([type_code],[type_name]) VALUES (1000105,'填空题')");
            database.execSQL("INSERT INTO [Type_] ([type_code],[type_name]) VALUES (1000106,'计算题')");
            database.execSQL("INSERT INTO [Type_] ([type_code],[type_name]) VALUES (1000107,'应用题')");
            database.execSQL("INSERT INTO [Type_] ([type_code],[type_name]) VALUES (1000108,'思考题')");
            database.close();
        }
    }

    public static void OpenDatabase() {
        database = SQLiteDatabase.openOrCreateDatabase(DB_FILE,null);
    }

    public static String Clear(){
        String str;

        try {
            database.execSQL("delete from [Data_]");
            database.execSQL("delete from [Extra_]");
            database.execSQL("delete from [Subject_]");
            database.execSQL("update [sqlite_sequence] SET [seq] = 0 where [name] = 'Data_'");

            return null;
        }catch (Exception e){
            str = e.getMessage();
        }

        return str;
    }

    public static void CloseDatabase() {
        database.close();
    }

    /**
     * 插入
     * @param question 题目信息
     */
    public static void Insert(Question question){
        String sql = "select [subject_count] from [Subject_] where [subject_code] = '"+question.getCode()+"'";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();

        //插入题目基本信息
        sql = "insert into [Data_] ([data_subject],[data_question],[data_type],[data_keys]) values (?,?,?,?)";
        database.execSQL(sql,new String[]{question.getCode(),question.getQuestion(),""+ STManger.GetTypeCode(question.getType()),question.getKeys()});
        //插入额外信息 如：选择题选项，问答题详细答案
        if(question.getExtra()!=null && !question.getExtra().isEmpty()){
            cursor = database.rawQuery("select last_insert_rowid()",null);
            cursor.moveToNext();
            String extra_data = cursor.getString(0);
            cursor.close();
            for(QuestionExtra extra:question.getExtra()) {
                sql = "insert into [Extra_] ([extra_code],[extra_text],[extra_data]) values (?,?,?)";
                database.execSQL(sql, new String[]{""+extra.getExtra_code(),extra.getExtra_text(),extra_data});
            }
        }

        //数量增加
        count++;
        sql = "update [Subject_] set [subject_count] = "+count+" where [subject_code] = '"+question.getCode()+"'";
        database.execSQL(sql);
    }

    /**
     * 获取Data_部分
     * @return  题目信息
     */
    public static Cursor GetInfo(String keys, int type_code, String subject_code){
        String sql = "select [data_subject],[data_code],[data_question],[data_keys],[type_name] from [Type_],[Data_] where [type_code] = [data_type] and [data_question] like '%"+keys+"%'";
        if(type_code != 0){
            sql +=" and [data_type] = " + type_code +" and [type_code] = " + type_code;
        }

        if(subject_code != null){
            sql +=" and [data_subject] = '"+subject_code+"'";
        }

        return database.rawQuery(sql,null);
    }

    /**
     * 获取Extra部分
     * @param code 编号
     * @return  选项信息
     */
    public static Cursor GetExtra(int code){
        String sql = "select [extra_code],[extra_text] from [Extra_] where [extra_data] = " + code;
        return database.rawQuery(sql,null);
    }

    /**
     * 获取题型 或 科目
     * @param table 获取目标
     * @return Cursor
     */
    public static Cursor GetTypeOrSubject(String table){
        if(table.equals("Type_"))
            return database.rawQuery("select [type_code],[type_name] from [Type_]",null);
        else
            return database.rawQuery("select [subject_code],[subject_name],[subject_count] from [Subject_]",null);
    }

    /**
     * 查看错误题目
     * @param keys 关键字
     * @param type_code 题型
     * @param subject_code 科目
     * @return Cursor
     */
    public static Cursor GetWrong(String keys,int type_code,String subject_code){
        String sql = "select [data_subject],[data_code],[data_question],[data_keys],[type_name] from [Type_],[Data_],[Wrong_] " +
                "where [type_code] = [data_type] and [data_code] = [wrong_data] and [data_question] like '%"+keys+"%'";
        if(type_code != 0){
            sql +=" and [data_type] = " + type_code +" and [type_code] = " + type_code;
        }

        if(subject_code != null){
            sql +=" and [data_subject] = '"+subject_code+"'";
        }

        return database.rawQuery(sql,null);
    }

    /**
     * 删除指定题目
     * @param question 题目信息
     */
    public static void DeleteQuestion(Question question) {
        //删除题目
        String sql = "delete from [Data_] where [data_code] = " + question.getNum();
        database.execSQL(sql);

        //数量减少
        sql = "select [subject_count] from [Subject_] where [subject_code] = '"+question.getCode()+"'";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        count--;
        sql = "update [Subject_] set [subject_count] = "+count+" where [subject_code] = '"+question.getCode()+"'";
        database.execSQL(sql);
    }

    public static void DeleteSubject(String code){
        //删除科目
        String sql = "delete from [Subject_] where [subject_code] = '" + code + "'";
        database.execSQL(sql);
    }

    // TODO: 2017/4/7 修改
    public static void Edit() {

    }

    /**
     * 添加科目
     * @param code 科目代码
     * @param name 科目名称
     */
    public static void AddNewSubject(String code, String name) {
        String sql = "insert into [Subject_] ([subject_code],[subject_name]) values ('"+code+"','"+name+"')" ;
        database.execSQL(sql);
    }
}
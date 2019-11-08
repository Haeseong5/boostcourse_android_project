package com.example.a82102.movieprojectfinal.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class Database {
    public static final String TAG = "AppHelper";
    public static SQLiteDatabase database;
    public static final int DATABASE_VERSION1 = 1;

    public static String createTableOutlineSql =
                        "create table if not exists movie " +"(_id integer PRIMARY KEY autoincrement, " +
                        "id Integer, " +
                        "title text, " +
                        "reservation float, "+
                        "grade Integer, " +
                        "image text "+
                        ")";
    public static String insertSQL = "insert into movie(id, title, reservation, grade, image) values(?, ?, ?, ?, ?)";
//public static String insertSQL = "insert into movie(id, title, reservation, grade, image) select id, title, reservation, grade, image from dual where not_exists (select count(*) from movie where id = 1)";

    //1단계. db를 생성하거나 open 한다.
    public static void openDatabase(Context context, String databaseName) {
        println("openDatabase() 호출됨.");
        database = context.openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
        try{
            if(database != null)
            {
                println(databaseName+" db 오픈됨.");
            }
//            DatabaseHelper databaseHelper = new DatabaseHelper(this,databaseName,null,DATABASE_VERSION1);
//            database =  databaseHelper.getWritableDatabase(); //db에 쓸 수 있는 권한+ db를 리턴받는다.
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static void createTable(String tableName){
        println("createTable() 호출됨.");
        try{
            if(database!=null)
            {

                if(tableName.equals("movie")){
                    database.execSQL(createTableOutlineSql);
                    println("테이블 요청됨.");
                }
            }else{
                println("database is null. open database");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void insertData(int id, String title, float reservation, int grade, String image)
    {
        println("insertData() 호출됨.");
        try{
            if(database !=null)
            {
//                String insertSQL = "insert into movie(id, title, reservation, grade, image) values(?, ?, ?, ?, ?)";
                Object params[] = {id, title, reservation, grade, image};
                database.execSQL(insertSQL,params);
                println("데이터 추가됨.");
            }else{
                println("database is null. open database");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void selectData(String tableName)
    {
        println("selectData() 호출됨.");
        try{
            if(database!=null)
            {
                Log.d("tableName",tableName);
                String selectSQL = "select id, title, reservation, grade, image from "+tableName;
                Cursor cursor = database.rawQuery(selectSQL, null);
                println("조회된 데이터의 갯수: "+cursor.getCount());

                for(int i=0; i<cursor.getCount(); i++)
                {
                    cursor.moveToNext(); //다음 레코드로 넘어가기
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    float reservation  = cursor.getFloat(2);
                    int grade = cursor.getInt(3);
                    String image = cursor.getString(4);
                    println("#"+i+"->"+id+", "+title+", "+reservation+", "+grade+", "+image+", ");
                }
                cursor.close();
            }else{
                println("database is null. open database");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void println(String data)
    {
        Log.d(TAG,data);
    }

    private class DatabaseHelper extends SQLiteOpenHelper
    {
        //앱을 배포했는데, 데이터베이스를 수정해야하는 상황
        //기존의 유저가 이미 사용하고 있는 경우 db를 무작정 날리고 새로 만들면 기존 유저들의 데이터가 날라간다.
        //새로 설치하는 사람도 있을거임.
        //원래쓰던 사람 = upgrade , 기존유저 = create
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            //버전관리 가능
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            Log.d("onOpen()", "open db");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //onCreate 메소드는 데이터베이스가 처음 만들어지는 경우에 호출되므로 데이터베이스를 초기화(생성) 하는 SQL을 넣어줍니다.
            //위에서 생성해준 database와 다른객체를 사용. 파라미터로 전달되는 eb사용
            println("onCreate() 호출됨.");
            String tableName = "customer";
            String createTableSQL = "create table if not exists "+tableName +"(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
            db.execSQL(createTableSQL);

            println("테이블 생성됨.");


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            onUpgrade 메소드는 테이블이 변경되어야 하는 등 단말에 저장된 데이터베이스의 구조가 바뀌어야 하는 경우에 사용할 수 있습니다.
            //기존유저의 경우 데이터베이스 변경 코드
            println("onUpgrade() 호출됨."+oldVersion+", "+newVersion);
            try{
                if(newVersion > 1)
                {
                    String tableName = "customer";
                    db.execSQL("drop table if exists "+tableName);
                    //보통은 삭제하지않고 컬럼만 변경해줌

                    String createTableSQL = "create table if not exists "+tableName +"(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
                    db.execSQL(createTableSQL);

                    println("테이블 생성됨.");
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }


        }
    }

}

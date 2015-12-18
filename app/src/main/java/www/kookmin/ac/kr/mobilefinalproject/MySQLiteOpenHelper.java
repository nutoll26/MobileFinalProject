package www.kookmin.ac.kr.mobilefinalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shinwoochul on 2015. 12. 18..
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper{
    public MySQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // SQLiteOpenHelper 가 최초 실행 되었을 때
        String sql = "create table lifelog (" +
                "Id integer primary key autoincrement, " +
                "Location text, " +
                "Action text, " +
                "Accident text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        String sql = "drop table if exists lifelog";
        db.execSQL(sql);

        onCreate(db); // 테이블을 지웠으므로 다시 테이블을 만들어주는 과정
    }
}

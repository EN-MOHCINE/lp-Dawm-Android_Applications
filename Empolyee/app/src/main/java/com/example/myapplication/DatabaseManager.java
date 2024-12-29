import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class DatabaseManager extends SQLiteOpenHelper {

    public DatabaseManager(Context context) {
        super(context, "EmployeeDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Employee (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, department TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Employee");
        onCreate(db);
    }

    // Add a new employee
    public void addEmployee(String name, String department) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("department", department);
        db.insert("Employee", null, values);
        db.close();
    }

    // Get all employees
    public Cursor getAllEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Employee", null);
    }

    // Update an employee
    public void updateEmployee(int id, String name, String department) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("department", department);
        db.update("Employee", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Delete an employee
    public void deleteEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Employee", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}

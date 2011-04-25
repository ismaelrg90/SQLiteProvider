
package novoda.lib.sqliteprovider.sqlite;

import novoda.lib.sqliteprovider.migration.Migrations;
import novoda.lib.sqliteprovider.util.DBUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// TODO caching?
public class ExtendedSQLiteOpenHelper2 extends SQLiteOpenHelper implements IDatabaseMetaInfo {

    private static final String MIGRATIONS_PATH = "migrations";

    private Context context;

    public ExtendedSQLiteOpenHelper2(Context context) throws IOException {
        
        this(context,
                context.getPackageName() + ".db",
                null, 
                Migrations.getVersion(context.getAssets(), MIGRATIONS_PATH));
        
    }

    public ExtendedSQLiteOpenHelper2(Context context, String name, CursorFactory factory,
            int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public Map<String, SQLiteType> getColumns(String table) {
        return DBUtils.getFields(getReadableDatabase(), table);
    }

    @Override
    public List<String> getTables() {
        return DBUtils.getTables(getReadableDatabase());
    }

    @Override
    public List<String> getForeignTables(String table) {
        return DBUtils.getForeignTables(getReadableDatabase(), table);
    }

    @Override
    public int getVersion() {
        return getReadableDatabase().getVersion();
    }

    @Override
    public void setVersion(int version) {
        getWritableDatabase().setVersion(version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Migrations.migrate(db, context.getAssets(), MIGRATIONS_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    public Map<String, String> getProjectionMap(String parent, String... foreignTables) {
        return DBUtils.getProjectionMap(getReadableDatabase(), parent, foreignTables);
    }

	@Override
	public List<String> getUniqueConstrains(String table) {
		// TODO Auto-generated method stub
		return null;
	}
}

package anylife.androiddbmanger.entity.daoManger;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import anylife.androiddbmanger.entity.Messages;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MESSAGES".
*/
public class MessagesDao extends AbstractDao<Messages, Long> {

    public static final String TABLENAME = "MESSAGES";

    /**
     * Properties of entity Messages.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Read = new Property(1, boolean.class, "read", false, "READ");
        public final static Property Created = new Property(2, long.class, "created", false, "CREATED");
        public final static Property Title = new Property(3, String.class, "title", false, "TITLE");
        public final static Property Message = new Property(4, String.class, "message", false, "MESSAGE");
        public final static Property Classify = new Property(5, String.class, "classify", false, "CLASSIFY");
    }


    public MessagesDao(DaoConfig config) {
        super(config);
    }
    
    public MessagesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MESSAGES\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"READ\" INTEGER NOT NULL ," + // 1: read
                "\"CREATED\" INTEGER NOT NULL ," + // 2: created
                "\"TITLE\" TEXT," + // 3: title
                "\"MESSAGE\" TEXT," + // 4: message
                "\"CLASSIFY\" TEXT);"); // 5: classify
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_MESSAGES_MESSAGE ON MESSAGES" +
                " (\"MESSAGE\" ASC);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_MESSAGES_CLASSIFY ON MESSAGES" +
                " (\"CLASSIFY\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MESSAGES\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Messages entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getRead() ? 1L: 0L);
        stmt.bindLong(3, entity.getCreated());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
 
        String message = entity.getMessage();
        if (message != null) {
            stmt.bindString(5, message);
        }
 
        String classify = entity.getClassify();
        if (classify != null) {
            stmt.bindString(6, classify);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Messages entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getRead() ? 1L: 0L);
        stmt.bindLong(3, entity.getCreated());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
 
        String message = entity.getMessage();
        if (message != null) {
            stmt.bindString(5, message);
        }
 
        String classify = entity.getClassify();
        if (classify != null) {
            stmt.bindString(6, classify);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Messages readEntity(Cursor cursor, int offset) {
        Messages entity = new Messages( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getShort(offset + 1) != 0, // read
            cursor.getLong(offset + 2), // created
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // title
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // message
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // classify
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Messages entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRead(cursor.getShort(offset + 1) != 0);
        entity.setCreated(cursor.getLong(offset + 2));
        entity.setTitle(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMessage(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setClassify(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Messages entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Messages entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Messages entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

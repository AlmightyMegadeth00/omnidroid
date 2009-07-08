/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package edu.nyu.cs.omnidroid.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Database helper class for the DataFilters table. Defines basic CRUD methods. 
 * 
 * TODO(ehotou) document about this table.
 */
public class DataFilterDbAdapter extends DbAdapter {

  /* Column names */
  public static final String KEY_DATAFILTERID = "DataFilterID";
  public static final String KEY_DATAFILTERNAME = "DataFilterName";
  public static final String KEY_DATATYPEID = "FK_DataTypeID";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_DATAFILTERID, KEY_DATAFILTERNAME, KEY_DATATYPEID };

  /* Table name */
  private static final String DATABASE_TABLE = "DataFilters";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_DATAFILTERID + " integer primary key autoincrement, " 
      + KEY_DATAFILTERNAME + " text not null, " 
      + KEY_DATATYPEID + " integer);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  /**
   * Constructor.
   * 
   * @param database
   *          is the database object to work within.
   */
  public DataFilterDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new DataFilter record.
   * 
   * @param dataFilterName
   *          is the name of the filter.
   * @param dataTypeID
   *          is the id of data type it belongs to.
   * @return dataFilterID or -1 if creation failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insert(String dataFilterName, Long dataTypeID) {
    if (dataFilterName == null || dataTypeID == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_DATAFILTERNAME, dataFilterName);
    initialValues.put(KEY_DATATYPEID, dataTypeID);
    // Set null because don't use 'null column hack'.
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Delete a DataFilter record.
   * 
   * @param dataFilterID
   *          is the id of the record.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if dataFilterID is null
   */
  public boolean delete(Long dataFilterID) {
    if (dataFilterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_DATAFILTERID + "=" + dataFilterID, null) > 0;
  }

  /**
   * Delete all DataFilter records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the dataFilterID.
   * 
   * @param dataFilterID
   *          is the id of the record to be fetched.
   * @return a Cursor pointing to the found record.
   * @throws IllegalArgumentException
   *           if dataFilterID is null
   */
  public Cursor fetch(Long dataFilterID) {
    if (dataFilterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_DATAFILTERID + "="
        + dataFilterID, null, null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all DataFilter records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * Return a Cursor that contains all DataFilter records which matches the parameters.
   * 
   * @param dataFilterName
   *          is the filter name or null to fetch any filter name.
   * @param dataTypeID
   *          is id of the data type it belongs to, or null to fetch any dataTypeID.
   * @return a Cursor that contains all DataFilter records which matches the parameters.
   */
  public Cursor fetchAll(String dataFilterName, Long dataTypeID) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DATABASE_TABLE);
    qb.appendWhere("1=1");
    if (dataFilterName != null) {
      qb.appendWhere(" AND " + KEY_DATAFILTERNAME + " = ");
      qb.appendWhereEscapeString(dataFilterName);
    }
    if (dataTypeID != null) {
      qb.appendWhere(" AND " + KEY_DATATYPEID + " = " + dataTypeID);
    }
    // Not using additional selections, selectionArgs, groupBy, having, orderBy, set them to null.
    return qb.query(database, KEYS, null, null, null, null, null);
  }

  /**
   * Update a DataFilter record with specific parameters.
   * 
   * @param dataFilterID
   *          is the id of the record to be updated.
   * @param dataFilterName
   *          is the application name or null if not updating it.
   * @param dataTypeID
   *          is the package name or null if not updating it.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if dataFilterID is null
   */
  public boolean update(Long dataFilterID, String dataFilterName, Long dataTypeID) {
    if (dataFilterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    ContentValues args = new ContentValues();
    if (dataFilterName != null) {
      args.put(KEY_DATAFILTERNAME, dataFilterName);
    }
    if (dataTypeID != null) {
      args.put(KEY_DATATYPEID, dataTypeID);
    }

    if (args.size() > 0) {
      // Set whereArg to null here
      return database.update(DATABASE_TABLE, args, KEY_DATAFILTERID + "=" + dataFilterID, null) > 0;
    }
    return false;
  }

}
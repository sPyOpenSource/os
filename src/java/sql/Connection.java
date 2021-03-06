/* Connection.java -- Manage a database connection.
   Copyright (C) 1999, 2000 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.
 
GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.sql;

import java.util.Map;

/**
  * This interface provides methods for managing a connection to a database.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface Connection
{

/**
  * This transaction isolation level indicates that transactions are not
  * supported.
  */
public static final int TRANSACTION_NONE = 0;

/**
  * This transaction isolation level indicates that one transaction can
  * read modifications by other transactions before the other transactions
  * have committed their changes.  This could result in invalid reads.
  */
public static final int TRANSACTION_READ_UNCOMMITTED = 1;

/**
  * This transaction isolation leve indicates that only committed data from
  * other transactions will be read.  If a transaction reads a row, then
  * another transaction commits a change to that row, the first transaction
  * would retrieve the changed row on subsequent reads of the same row.
  */
public static final int TRANSACTION_READ_COMMITTED = 2;

/**
  * This transaction isolation level indicates that only committed data from
  * other transactions will be read.  It also ensures that data read from
  * a row will not be different on a subsequent read even if another
  * transaction commits a change.
  */
public static final int TRANSACTION_REPEATABLE_READ = 4;

/**
  * This transaction isolation level indicates that only committed data from
  * other transactions will be read.  It also ensures that data read from
  * a row will not be different on a subsequent read even if another
  * transaction commits a change.  Additionally, rows modified by other
  * transactions will not affect the result set returned during subsequent
  * executions of the same WHERE clause in this transaction.
  */
public static final int TRANSACTION_SERIALIZABLE = 8;

/*************************************************************************/

/**
  * This method creates a new SQL statement.  The default result set type
  * and concurrency will be used.
  *
  * @return A new <code>Statement</code> object. 
  *
  * @exception SQLException If an error occurs.
  *
  * @see Statement
  */
public abstract Statement
createStatement() throws SQLException;

/*************************************************************************/

/**
  * This method creates a new SQL statement with the specified type and
  * concurrency.  Valid values for these parameters are specified in the
  * <code>ResultSet</code> class.
  *
  * @param resultSetType The type of result set to use for this statement.
  * @param resultSetConcurrency.  The type of concurrency to be used in
  * the result set for this statement.
  *
  * @return A new <code>Statement</code> object.
  *
  * @exception SQLException If an error occurs.
  *
  * @see Statement
  * @see ResultSet
  */
public abstract Statement
createStatement(int resultSetType, int resultSetConcurrency) 
                throws SQLException;

/*************************************************************************/

/**
  * This method creates a new <code>PreparedStatement</code> for the specified
  * SQL string.  This method is designed for use with parameterized
  * statements.  The default result set type and concurrency will be used.
  *
  * @param The SQL statement to use in creating this 
  * <code>PreparedStatement</code>.
  *
  * @return A new <code>PreparedStatement</code>.
  *
  * @exception SQLException If an error occurs.
  *
  * @see PreparedStatement
  */
public abstract PreparedStatement
prepareStatement(String sql) throws SQLException;

/*************************************************************************/

/**
  * This method creates a new <code>PreparedStatement</code> for the specified
  * SQL string.  This method is designed for use with parameterized
  * statements.  The specified result set type and concurrency will be used.
  * Valid values for these parameters are specified in the
  * <code>ResultSet</code> class.
  *
  * @param The SQL statement to use in creating this 
  * <code>PreparedStatement</code>.
  * @param resultSetType The type of result set to use for this statement.
  * @param resultSetConcurrency.  The type of concurrency to be used in
  * the result set for this statement.
  *
  * @return A new <code>PreparedStatement</code>.
  *
  * @exception SQLException If an error occurs.
  *
  * @see PreparedStatement
  * @see ResultSet
  */
public abstract PreparedStatement
prepareStatement(String sql, int resultSetType, int resultSetConcurrency) 
                 throws SQLException;

/*************************************************************************/

/**
  * This method creates a new <code>CallableStatement</code> for the 
  * specified SQL string.  Thie method is designed to be used with
  * stored procedures.  The default result set type and concurrency
  * will be used.
  *
  * @param The SQL statement to use in creating this 
  * <code>CallableStatement</code>.
  *
  * @return A new <code>CallableStatement</code>.
  *
  * @exception SQLException If an error occurs.
  *
  * @see CallableStatement
  */
public abstract CallableStatement
prepareCall(String sql) throws SQLException;

/*************************************************************************/

/**
  * This method creates a new <code>CallableStatement</code> for the 
  * specified SQL string.  Thie method is designed to be used with
  * stored procedures.  The specified result set type and concurrency
  * will be used.  Valid values for these parameters are specified in the
  * <code>ResultSet</code> class.
  *
  * @param The SQL statement to use in creating this 
  * <code>PreparedStatement</code>.
  * @param resultSetType The type of result set to use for this statement.
  * @param resultSetConcurrency.  The type of concurrency to be used in
  * the result set for this statement.
  *
  * @return A new <code>CallableStatement</code>.
  *
  * @exception SQLException If an error occurs.
  *
  * @see CallableStatement
  * @see ResultSet
  */
public abstract CallableStatement
prepareCall(String sql, int resultSetType, int resultSetConcurrency) 
            throws SQLException;

/*************************************************************************/

/**
  * This method converts the specified generic SQL statement into the
  * native grammer of the database this object is connected to.
  *
  * @param The JDBC generic SQL statement.
  *
  * @return The native SQL statement.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
nativeSQL(String sql) throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not auto commit mode is currently enabled.
  * In auto commit mode,  every SQL statement is committed its own transaction.
  * Otherwise a transaction must be explicitly committed or rolled back.
  *
  * @return <code>true</code> if auto commit mode is enabled, 
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  *
  * @see commit
  * @see rollback
  */
public abstract boolean
getAutoCommit() throws SQLException;

/*************************************************************************/

/**
  * This method turns auto commit mode on or off.  In auto commit mode,
  * every SQL statement is committed its own transaction.  Otherwise a
  * transaction must be explicitly committed or rolled back.
  *
  * @param autoCommit <code>true</code> to enable auto commit mode,
  * <code>false</code> to disable it.
  *
  * @exception SQLException If an error occurs.
  *
  * @see commit
  * @see rollback
  */
public abstract void
setAutoCommit(boolean autoCommit) throws SQLException;

/*************************************************************************/

/**
  * This method commits any SQL statements executed on this connection since 
  * the last commit or rollback.
  *
  * @exception SQLException If an error occurs.
  */
public abstract void
commit() throws SQLException;

/*************************************************************************/

/**
  * This method rolls back any SQL statements executed on this connection
  * since the last commit or rollback.
  *
  * @exception SQLException If an error occurs.
  */
public abstract void
rollback() throws SQLException;

/*************************************************************************/

/**
  * This method immediately closes this database connection.
  *
  * @exception SQLException If an error occurs.
  */
public abstract void
close() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not this connection has been closed. 
  *
  * @return <code>true</code> if the connection is closed, <code>false</code>
  * otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
isClosed() throws SQLException;

/*************************************************************************/

/**
  * This method returns the meta data for this database connection.
  *
  * @return The meta data for this database.
  *
  * @exception SQLException If an error occurs.
  *
  * @see DatabaseMetaData
  */
public abstract DatabaseMetaData
getMetaData() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not this connection is in read only mode.
  *
  * @return <code>true</code> if the connection is read only <code>false</code>
  * otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
isReadOnly() throws SQLException;

/*************************************************************************/

/**
  * This method turns read only mode on or off.  It may not be called while
  * a transaction is in progress.
  *
  * @param readOnly <code>true</code> if this connection is read only,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract void
setReadOnly(boolean readOnly) throws SQLException;

/*************************************************************************/

/**
  * This method returns the name of the catalog in use by this connection,
  * if any.
  *
  * @return The name of the catalog, or <code>null</code> if one does not
  * exist or catalogs are not supported by this database.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getCatalog() throws SQLException;

/*************************************************************************/

/**
  * This method sets the name of the catalog in use by this connection.
  * Note that this method does nothing if catalogs are not supported by
  * this database.
  *
  * @param catalog The name of the catalog to use for this connection.
  *
  * @exception SQLException If an error occurs.
  */
public abstract void
setCatalog(String catalog) throws SQLException;

/*************************************************************************/

/**
  * This method returns the current transaction isolation mode.  This will
  * be one of the constants defined in this interface.
  *
  * @return The transaction isolation level.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getTransactionIsolation() throws SQLException;

/*************************************************************************/

/**
  * This method sets the current transaction isolation mode.  This must
  * be one of the constants defined in this interface.
  *
  * @param level The transaction isolation level.
  *
  * @exception SQLException If an error occurs.
  */
public abstract void
setTransactionIsolation(int level) throws SQLException;

/*************************************************************************/

/**
  * This method returns the first warning that occurred on this connection,
  * if any.  If there were any subsequence warnings, they will be chained
  * to the first one.
  *
  * @return The first <code>SQLWarning</code> that occurred, or 
  * <code>null</code> if there have been no warnings.
  *
  * @exception SQLException If an error occurs.
  */
public abstract SQLWarning
getWarnings() throws SQLException;

/*************************************************************************/

/**
  * This method clears all warnings that have occurred on this connection.
  *
  * @exception SQLException If an error occurs.
  */
public abstract void
clearWarnings() throws SQLException;

/*************************************************************************/

/**
  * This method returns the mapping of SQL types to Java classes
  * currently in use by this connection.  This mapping will have no
  * entries unless they have been manually added.
  *
  * @return The SQL type to Java class mapping.
  *
  * @exception SQLException If an error occurs.
  */
public abstract Map
getTypeMap() throws SQLException;

/*************************************************************************/

/**
  * This method sets the mapping table for SQL types to Java classes.
  * Any entries in this map override the defaults.
  *
  * @param map The new SQL mapping table.
  *
  * @exception SQLException If an error occurs.
  */
public abstract void
setTypeMap(Map map) throws SQLException;

} // interface Connection


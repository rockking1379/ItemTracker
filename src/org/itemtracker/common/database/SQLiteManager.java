package org.itemtracker.common.database;

import org.itemtracker.common.objects.Loan;
import org.itemtracker.common.objects.Loanable;
import org.itemtracker.common.objects.Loanee;
import org.itemtracker.common.utils.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implements Database Manager
 * <br>
 * Specific for SQLite
 * Created by james on 5/29/15.
 */
public class SQLiteManager implements DatabaseManager
{
    private static final String LOANDROP = "DROP TABLE IF EXISTS Loans";
    private static final String LOANABLEDROP = "DROP TABLE IF EXISTS Loanables";
    private static final String LOANEEDROP = "DROP TABLE IF EXISTS Loanees";
    private static final String LOANEECREATE = "CREATE TABLE Loanees(loanee_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, loanee_first_name VARCHAR(50) NOT NULL, loanee_last_name VARCHAR(50) NOT NULL, loanee_email VARCHAR(75), loanee_barcode VARCHAR(50) NOT NULL, loanee_active BOOLEAN NOT NULL)";
    private static final String LOANABLECREATE = "CREATE TABLE Loanables(loanable_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, loanable_name VARCHAR(50) NOT NULL, loanable_barcode VARCHAR(50) NOT NULL, loanable_active BOOLEAN NOT NULL)";
    private static final String LOANCREATE = "CREATE TABLE Loans(loan_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, loanable_id INTEGER, loanee_id INTEGER, check_out INTEGER NOT NULL, check_in INTEGER, FOREIGN KEY(loanable_id) REFERENCES Loanables(loanable_id), FOREIGN KEY(loanee_id) REFERENCES Loanees(loanee_id))";
    public static final int dbId = 0;

    private String dbLocation;

    public SQLiteManager(String dbLocation)
    {
        this.dbLocation = dbLocation.replace('\\', '/');
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e)
        {
            Logger.logException(e);
        }
    }

    @Override
    public Connection connect()
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

            if (connection != null)
            {
                return connection;
            }
            else
            {
                return null;
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return null;
        }
    }

    @Override
    public void disconnect(Connection connection)
    {
        if (connection != null)
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                Logger.logException(e);
            }
        }
    }

    @Override
    public boolean addLoan(Loanable loanable, Loanee loanee)
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmnt = connection.prepareStatement("INSERT INTO Loans(loanable_id, loanee_id, check_out) VALUES (?,?,?)");

            stmnt.setInt(1, loanable.getLoanableId());
            stmnt.setInt(2, loanee.getLoaneeId());
            stmnt.setLong(3, new Date().getTime());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return false;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public boolean removeLoan(Loanable loanable)
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmnt = connection.prepareStatement("UPDATE Loans SET check_in=? WHERE loanable_id=?");

            stmnt.setLong(1, new Date().getTime());
            stmnt.setInt(2, loanable.getLoanableId());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return false;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public List<Loan> getLoans()
    {
        Connection connection = connect();
        try
        {
            Statement stmnt = connection.createStatement();

            ResultSet loanResultSet = stmnt.executeQuery("SELECT * FROM Loans WHERE check_in IS NULL");

            return processLoanResult(loanResultSet, connection);
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return null;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public List<Loan> getLoans(Loanee loanee)
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Loans WHERE loanee_id=?");

            stmnt.setInt(1, loanee.getLoaneeId());

            ResultSet loanResultSet = stmnt.executeQuery();

            return processLoanResult(loanResultSet, connection);
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return null;
        }
        finally
        {
            disconnect(connection);
        }
    }

    private List<Loan> processLoanResult(ResultSet loanResultSet, Connection connection)
    {
        try
        {
            ArrayList<Loan> result = new ArrayList<>();

            while (loanResultSet.next())
            {
                Loanable loanable = null;
                Loanee loanee = null;

                PreparedStatement pStmnt = connection.prepareStatement("SELECT * FROM Loanables WHERE loanable_id=? AND loanable_active=1");

                pStmnt.setInt(1, loanResultSet.getInt("loanable_id"));
                ResultSet loanableResultSet = pStmnt.executeQuery();

                while (loanableResultSet.next())
                {
                    loanable = new Loanable(loanableResultSet.getInt("loanable_id"), loanableResultSet.getString("loanable_name"), loanableResultSet.getString("loanable_barcode"));
                }

                pStmnt = connection.prepareStatement("SELECT * FROM Loanees WHERE loanee_id=? AND loanee_active=1");

                pStmnt.setInt(1, loanResultSet.getInt("loanee_id"));

                ResultSet loaneeResultSet = pStmnt.executeQuery();

                while (loaneeResultSet.next())
                {
                    loanee = new Loanee(loaneeResultSet.getInt("loanee_id"), loaneeResultSet.getString("loanee_first_name"), loaneeResultSet.getString("loanee_last_name"), loaneeResultSet.getString("loanee_barcode"), loaneeResultSet.getString("loanee_email"));
                }

                result.add(new Loan(loanResultSet.getInt("loan_id"), loanable, loanee, loanResultSet.getLong("check_out"), loanResultSet.getLong("check_in")));
            }

            return result;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return null;
        }
    }

    @Override
    public boolean addLoanable(Loanable loanable)
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmnt = connection.prepareStatement("INSERT INTO Loanables(loanable_name, loanable_barcode, loanable_active) VALUES(?,?,1)");

            stmnt.setString(1, loanable.getLoanableName());
            stmnt.setString(2, loanable.getLoanableBarcode());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return false;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public boolean removeLoanable(Loanable loanable)
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmnt = connection.prepareStatement("UPDATE Loanables SET loanable_active=0 WHERE loanable_id=?");

            stmnt.setInt(1, loanable.getLoanableId());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return false;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public Loanable getLoanable(int loanableId)
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Loanables WHERE loanable_id=?");

            stmnt.setInt(1, loanableId);

            ResultSet rs = stmnt.executeQuery();

            Loanable retVal = null;
            while (rs.next())
            {
                retVal = new Loanable(rs.getInt("loanable_id"), rs.getString("loanable_name"), rs.getString("loanable_barcode"));
            }
            return retVal;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return null;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public Loanable getLoanable(String loanableBarcode)
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Loanables WHERE loanable_barcode=?");

            stmnt.setString(1, loanableBarcode);

            ResultSet rs = stmnt.executeQuery();

            Loanable retVal = null;
            while (rs.next())
            {
                retVal = new Loanable(rs.getInt("loanable_id"), rs.getString("loanable_name"), rs.getString("loanable_barcode"));
            }
            return retVal;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return null;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public List<Loanable> getLoanables()
    {
        Connection connection = connect();
        try
        {
            Statement stmnt = connection.createStatement();

            ResultSet rs = stmnt.executeQuery("SELECT * FROM Loanables WHERE loanable_active=1");

            ArrayList<Loanable> results = new ArrayList<>();

            while (rs.next())
            {
                results.add(new Loanable(rs.getInt("loanable_id"), rs.getString("loanable_name"), rs.getString("loanable_barcode")));
            }

            return results;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return null;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public boolean addLoanee(Loanee loanee)
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmnt = connection.prepareStatement("INSERT INTO Loanees(loanee_first_name, loanee_last_name, loanee_email, loanee_barcode, loanee_active) VALUES (?,?,?,?,1)");

            stmnt.setString(1, loanee.getFirstName());
            stmnt.setString(2, loanee.getLastName());
            stmnt.setString(3, loanee.getEmailAddress());
            stmnt.setString(4, loanee.getBarcodeId());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return false;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public boolean removeLoanee(Loanee loanee)
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmnt = connection.prepareStatement("UPDATE Loanees SET loanee_active=0 WHERE loanee_id=? OR loanee_barcode=?");

            stmnt.setInt(1, loanee.getLoaneeId());
            stmnt.setString(2, loanee.getBarcodeId());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return false;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public boolean updateLoanee(Loanee loanee)
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmnt = connection.prepareStatement("UPDATE Loanees SET loanee_first_name=?, loanee_last_name=?, loanee_barcode=?, loanee_email=? WHERE loanee_id=?");

            stmnt.setString(1, loanee.getFirstName());
            stmnt.setString(2, loanee.getLastName());
            stmnt.setString(3, loanee.getBarcodeId());
            stmnt.setString(4, loanee.getEmailAddress());
            stmnt.setInt(5, loanee.getLoaneeId());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return false;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public Loanee getLoanee(int loaneeId)
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Loanees WHERE loanee_id=?");

            stmnt.setInt(1, loaneeId);

            ResultSet rs = stmnt.executeQuery();

            Loanee retVal = null;

            while (rs.next())
            {
                retVal = new Loanee(rs.getInt("loanee_id"), rs.getString("loanee_first_name"), rs.getString("loanee_last_name"), rs.getString("loanee_email"), rs.getString("loanee_barcode"));
            }

            return retVal;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return null;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public Loanee getLoanee(String loaneeBarcode)
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Loanees WHERE loanee_barcode=?");

            stmnt.setString(1, loaneeBarcode);

            ResultSet rs = stmnt.executeQuery();

            Loanee retVal = null;

            while (rs.next())
            {
                retVal = new Loanee(rs.getInt("loanee_id"), rs.getString("loanee_first_name"), rs.getString("loanee_last_name"), rs.getString("loanee_email"), rs.getString("loanee_barcode"));
            }

            return retVal;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return null;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public List<Loanee> getLoanees()
    {
        Connection connection = connect();
        try
        {
            Statement stmnt = connection.createStatement();

            ResultSet rs = stmnt.executeQuery("SELECT * FROM Loanees WHERE loanee_active=1");

            ArrayList<Loanee> results = new ArrayList<>();

            while (rs.next())
            {
                results.add(new Loanee(rs.getInt("loanee_id"), rs.getString("loanee_first_name"), rs.getString("loanee_last_name"), rs.getString("loanee_email"), rs.getString("loanee_barcode")));
            }

            return results;
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            return null;
        }
        finally
        {
            disconnect(connection);
        }
    }

    @Override
    public boolean createDatabase()
    {
        boolean retVal = false;
        Connection connection = connect();

        try
        {
            Statement stmnt = connection.createStatement(); //create statement

            stmnt.execute(LOANDROP); //drop Loans table
            stmnt.execute(LOANABLEDROP); //drop Loanables table
            stmnt.execute(LOANEEDROP); //drop Loanees table
            stmnt.execute(LOANEECREATE); //create Loanees table
            stmnt.execute(LOANABLECREATE); //create Loanables table
            stmnt.execute(LOANCREATE); //create Loans table

            retVal = true; //we made it to here so return true
        }
        catch (SQLException e)
        {
            Logger.logException(e);

            retVal = false; //shit went south, return false
        }
        finally
        {
            disconnect(connection); //disconnect from database regardless of what happened
        }

        return retVal; //return
    }
}

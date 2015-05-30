package org.itemtracker.common.database;

import org.itemtracker.common.objects.Loan;
import org.itemtracker.common.objects.Loanable;
import org.itemtracker.common.objects.Loanee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * Created by james on 5/29/15.
 */
public class MSSQLManager implements DatabaseManager
{
    private static final String LOANDROP = "DROP TABLE IF EXISTS Loans";
    private static final String LOANABLEDROP = "DROP TABLE IF EXISTS Loanables";
    private static final String LOANEEDROP = "DROP TABLE IF EXISTS Loanees";
    private static final String LOANEECREATE = "CREATE TABLE Loanees(loanee_id INTEGER IDENTITY, loanee_first_name VARCHAR(50) NOT NULL, loanee_last_name VARCHAR(50) NOT NULL, loanee_email VARCHAR(75), loanee_barcode VARCHAR(50) NOT NULL, loanee_active bit NOT NULL)";
    private static final String LOANABLECREATE = "CREATE TABLE Loanables(loanable_id INTEGER IDENTITY, loanable_name VARCHAR(50) NOT NULL, loanable_barcode VARCHAR(50) NOT NULL, loanable_active bit NOT NULL)";
    private static final String LOANCREATE = "CREATE TABLE Loans(loan_id int IDENTITY, loanable_id int, loanee_id int, check_out bigint NOT NULL, check_in bigint, CONSTRAINT fk_loanable_id FOREIGN KEY (loanable_id) REFERENCES Loanables(loanable_id), CONSTRAINT fk_loanee_id FOREIGN KEY (loanee_id) REFERENCES Loanees(loanee_id))";
    public static final int dbId = 1;

    String connectionUrl = "jdbc:sqlserver://";
    Connection connection = null;

    public MSSQLManager(String serverAddress, int port, String databaseName, String dbUser, String dbPassword)
    {
        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            connectionUrl += serverAddress;
            connectionUrl += ":" + String.valueOf(port);
            connectionUrl += ";" + "databaseName=" + databaseName;
            connectionUrl += ";" + "user=" + dbUser;
            connectionUrl += ";" + "password=" + dbPassword;
            //hopefully she is ready to go
            connection = connect();
        }
        catch(ClassNotFoundException e)
        {
            //set up logger
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Connection connect()
    {
        try
        {
            return DriverManager.getConnection(connectionUrl);
        }
        catch (SQLException e)
        {
            //set up logger
            System.err.println(e.getMessage());
            return  null;
        }
    }

    @Override
    public void disconnect(Connection connection)
    {
        if(connection != null)
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                //set up logger
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean addLoan(Loanable loanable, Loanee loanee)
    {
        return false;
    }

    @Override
    public boolean removeLoan(Loanable loanable)
    {
        return false;
    }

    @Override
    public List<Loan> getLoans()
    {
        return null;
    }

    @Override
    public List<Loan> getLoans(Loanee loanee)
    {
        return null;
    }

    @Override
    public boolean addLoanable(Loanable loanable)
    {
        return false;
    }

    @Override
    public boolean removeLoanable(Loanable loanable)
    {
        return false;
    }

    @Override
    public Loanable getLoanable(int loanableId)
    {
        return null;
    }

    @Override
    public Loanable getLoanable(String loanableBarcode)
    {
        return null;
    }

    @Override
    public List<Loanable> getLoanables()
    {
        return null;
    }

    @Override
    public boolean addLoanee(Loanee loanee)
    {
        return false;
    }

    @Override
    public boolean removeLoanee(Loanee loanee)
    {
        return false;
    }

    @Override
    public Loanee getLoanee(int loaneeId)
    {
        return null;
    }

    @Override
    public Loanee getLoanee(String loaneeBarcode)
    {
        return null;
    }

    @Override
    public List<Loanee> getLoanees()
    {
        return null;
    }

    @Override
    public boolean createDatabase()
    {
        boolean retVal;
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
            //set up logger
            System.err.println(e.getMessage());
            retVal = false; //shit went south, return false
        }

        return retVal; //return
    }
}

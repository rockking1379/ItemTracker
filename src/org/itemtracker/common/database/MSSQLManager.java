package org.itemtracker.common.database;

import org.itemtracker.common.objects.Loan;
import org.itemtracker.common.objects.Loanable;
import org.itemtracker.common.objects.Loanee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * Created by james on 5/29/15.
 */
public class MSSQLManager implements DatabaseManager
{
    String connectionUrl = "jdbc:sqlserver://";
    Connection connection = null;

    public MSSQLManager(String serverAddress, int port, String databaseName, boolean integratedSecurity)
    {
        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            connectionUrl += serverAddress;
            connectionUrl += ":" + String.valueOf(port);
            connectionUrl += ";" + "databaseName=" + databaseName;
            connectionUrl += ";" + "integratedSecurity=" + String.valueOf(integratedSecurity);
            //hopefully she is ready to go
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
        return false;
    }
}

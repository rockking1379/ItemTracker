package org.itemtracker.common.database;

import org.itemtracker.common.objects.Loan;
import org.itemtracker.common.objects.Loanable;
import org.itemtracker.common.objects.Loanee;

import java.sql.Connection;
import java.util.List;

/**
 * Interface for accessing any database that implements it
 * <p>
 * Created by james on 5/29/15.
 */
public interface DatabaseManager
{
    /**
     * Makes Connection to Database
     *
     * @return new Connection to Database
     */
    Connection connect();

    /**
     * Closes Connection to Database
     *
     * @param connection Connection to be closed
     */
    void disconnect(Connection connection);

    /**
     * Adds new loan
     *
     * @param loanable item to be loaned
     * @param loanee person loaing item
     * @return SQL Code execution
     */
    boolean addLoan(Loanable loanable, Loanee loanee);

    /**
     * Removes loan
     *
     * @param loanable item that was loaned
     * @return SQL Code execution
     */
    boolean removeLoan(Loanable loanable);

    /**
     * Removes loan and adds notes for the return reason
     *
     * @param loanable item that was loaned
     * @param returnNotes reason for the return
     * @return SQL Code execution
     */
    boolean removeLoan(Loanable loanable, String returnNotes);

    /**
     * Gets all active loans
     * <br>
     * Active loans are ones with no check_in date
     *
     * @return List of Active Loans
     */
    List<Loan> getLoans();

    /**
     * Gets all loans for certain loanee
     *
     * @param loanee loanee search criteria
     * @return list of loans
     */
    List<Loan> getLoans(Loanee loanee);

    /**
     * Adds new loanable
     *
     * @param loanable item that can be loaned
     * @return SQL Code execution
     */
    boolean addLoanable(Loanable loanable);

    /**
     * Removes loanable
     *
     * @param loanable item to be removed
     * @return SQL Code execution
     */
    boolean removeLoanable(Loanable loanable);

    /**
     * Returns specific loanable
     *
     * @param loanableId loanable_id of Loanable to retrieve
     * @return Loanable item
     */
    Loanable getLoanable(int loanableId);

    /**
     * returns specific loanable
     *
     * @param loanableBarcode loanable_barcode of Loanable to retrieve
     * @return Loanable item
     */
    Loanable getLoanable(String loanableBarcode);

    /**
     * Gets list of all actie Loanable items
     *
     * @return List of items that can be loaned out
     */
    List<Loanable> getLoanables();

    /**
     * Adds new Loanee
     *
     * @param loanee Loanee to add
     * @return SQL Code execution
     */
    boolean addLoanee(Loanee loanee);

    /**
     * Removes Loanee
     *
     * @param loanee Loanee to remove
     * @return SQL Code execution
     */
    boolean removeLoanee(Loanee loanee);

    /**
     * Changes information about a loanee based on loanee_id
     *
     * @param loanee Loanee to be updated
     * @return SQL Code exception
     */
    boolean updateLoanee(Loanee loanee);

    /**
     * Gets specific Loanee
     *
     * @param loaneeId loanee_id of Loanee to retrieve
     * @return Loanee
     */
    Loanee getLoanee(int loaneeId);

    /**
     * Gets specific Loanee
     *
     * @param loaneeBarcode loanee_barcode of Loanee to retrieve
     * @return Loanee
     */
    Loanee getLoanee(String loaneeBarcode);

    /**
     * Gets list of active Loanees
     *
     * @return List of all active Loanees
     */
    List<Loanee> getLoanees();

    /**
     * Sets up database for operation
     *
     * @return SQL Code execution
     */
    boolean createDatabase();

    /**
     * Checks today's date against graduation date of Loanee's
     * <br>
     *     if today is after their graduation, their active flag is set to 0
     *
     * @return number of loanees marked inactive
     */
    int checkGraduates();

    /**
     * Counts the number of strikes a loanee has against them for returning items
     * <br>
     *     if to many then we will mark them inactive
     * @return number of loanees marked inactive
     */
    int checkStrikes();

    /**
     * Will get the number of items based on type
     * @param type type of laonable, should be in the name
     * @return number of items matching type
     */
    int getNumberOf(String type);
}

package org.itemtracker.common.objects;

/**
 * Represents a loan
 * Created by james on 5/29/15.
 */
public class Loan
{
    private int loanId;
    private Loanable loanedItem;
    private Loanee loanee;
    private String loanDate;

    public Loan(int loanId, Loanable loanedItem, Loanee loanee, String loanDate)
    {
        this.loanId = loanId;
        this.loanedItem = loanedItem;
        this.loanee = loanee;
        this.loanDate = loanDate;
    }

    public int getLoanId()
    {
        return loanId;
    }

    public Loanable getLoanedItem()
    {
        return loanedItem;
    }

    public Loanee getLoanee()
    {
        return loanee;
    }

    public String getLoanDate()
    {
        return loanDate;
    }
}

package org.itemtracker.common.objects;

/**
 * represents an item that may be loaned out
 * Created by james on 5/29/15.
 */
public class Loanable
{
    private int loanableId;
    private String loanableName;
    private String loanableBarcode;

    public Loanable(int loanableId, String loanableName, String loanableBarcode)
    {
        this.loanableId = loanableId;
        this.loanableName = loanableName;
        this.loanableBarcode = loanableBarcode;
    }

    public int getLoanableId()
    {
        return loanableId;
    }

    public String getLoanableName()
    {
        return loanableName;
    }

    public String getLoanableBarcode()
    {
        return loanableBarcode;
    }

    @Override
    public String toString()
    {
        return loanableName;
    }
}

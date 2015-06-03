package org.itemtracker.common.objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a loan
 * Created by james on 5/29/15.
 */
public class Loan
{
    private int loanId;
    private Loanable loanedItem;
    private Loanee loanee;
    private long checkOut;
    private long checkIn;
    private String returnNotes;

    public Loan(int loanId, Loanable loanedItem, Loanee loanee, long checkOut, long checkIn, String returnNotes)
    {
        this.loanId = loanId;
        this.loanedItem = loanedItem;
        this.loanee = loanee;
        this.checkOut = checkOut;
        this.checkIn = checkIn;
        this.returnNotes = returnNotes;
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

    public String getLoanDuration()
    {
        if (Long.valueOf(checkIn) != null)
        {
            Date duration = new Date(checkIn - checkOut);
            DateFormat format = new SimpleDateFormat("DD:HH:mm");
            String output = format.format(duration);
            String[] breakDown = output.split(":");

            return format.format(duration);
        }
        else
        {
            return "not checked in";
        }
    }

    public String getCheckOut()
    {
        Date d = new Date(checkOut);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(d);
    }

    public String getCheckIn()
    {
        Date d = new Date(checkIn);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(d);
    }

    public String getReturnNotes()
    {
        return returnNotes;
    }
}

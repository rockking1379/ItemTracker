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

    public Loan(int loanId, Loanable loanedItem, Loanee loanee, long checkOut, long checkIn)
    {
        this.loanId = loanId;
        this.loanedItem = loanedItem;
        this.loanee = loanee;
        this.checkOut = checkOut;
        this.checkIn = checkIn;
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
        if(Long.valueOf(checkIn) != null)
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

    public long getCheckOut()
    {
        return checkOut;
    }

    public long getCheckIn()
    {
        return checkIn;
    }
}

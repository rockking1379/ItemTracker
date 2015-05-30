package org.itemtracker.common.objects;

/**
 * Represents a student who may loan out a loanable item
 * Created by james on 5/29/15.
 */
public class Loanee
{
    private int loaneeId;
    private String firstName;
    private String lastName;
    private String barcodeId;
    private String emailAddress;

    public Loanee(int loaneeId, String firstName, String lastName, String barcodeId, String emailAddress)
    {
        this.loaneeId = loaneeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.barcodeId = barcodeId;
        this.emailAddress = emailAddress;
    }

    public int getLoaneeId()
    {
        return loaneeId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getBarcodeId()
    {
        return barcodeId;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    @Override
    public String toString()
    {
        return firstName + " " + lastName;
    }
}

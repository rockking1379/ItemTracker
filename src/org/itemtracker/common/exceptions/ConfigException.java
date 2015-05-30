package org.itemtracker.common.exceptions;

/**
 * Configuration Exception
 * <br>
 *     Thrown if the switch statement used to determine what DatabaseManager to create
 *     hits default case
 * Created by james on 5/29/15.
 */
public class ConfigException extends Throwable
{
    public ConfigException(String message)
    {
        super(message);
    }
}

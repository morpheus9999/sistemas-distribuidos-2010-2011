
package ClientRMI2.StatePattern;

public class ForbiddenActionException 
    extends java.lang.Exception
{
    public ForbiddenActionException( )
    {
        super( "/ forbidden action attempted /" );
    }
}

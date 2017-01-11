package forms;

import foundation.RegistryItem;

/**
 * Class TwoColumnTableDialog
 * @author Peter Cross
 */
public class TwoColumnTableDialog extends TwoColumnDialog
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    private int			numRows;	// Number of rows passed
    private String[]		tabs;		// Tab names
    
    /**
     * Returns form fields in text format
     * @param <T> - Type parameter
     * @return Object containing form fields in text format
     */
    @Override
    public <T> T result()
    {
        return new TableDialog(this, regItem).result();
        
    } // End of method ** result **

    /*  
        Constants                                                                                                     */
    /******************************************************************************************************************/
    private static final long serialVersionUID = 1L;

    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    public TwoColumnTableDialog( RegistryItem doc, String title, int numRows )
    {
        super( doc, title );
        regItem = doc;
        this.numRows = numRows;    
    }
    
    public TwoColumnTableDialog( RegistryItem doc, String title, int numRows, String[] tableTabs )
    {
        super( doc, title );
        regItem = doc;
        this.numRows = numRows;
        this.tabs = tableTabs;
    }
    
} // End of class ** TwoColumnTableDialog **
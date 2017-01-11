package forms;

import foundation.RegistryItem;

/**
 * Class OneColumnTableDialog - Dialog Form to work with simple tables
 * @author Peter Cross
 *
 */
public class OneColumnTableDialog extends OneColumnDialog
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

    /*          Constants                                                                                             */
    /******************************************************************************************************************/
    private static final long serialVersionUID = 1L;

    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    public OneColumnTableDialog( RegistryItem doc, String title, int numRows )
    {
        super( doc, title );
        regItem = doc;
        this.numRows = numRows;
    }
    
    /*
    public OneColumnTableDialog( Stage form, String title, DialogElement[] dlg, int numRows, String[] tabs, TableElement[]... tbl ) 
    {
        // Call constructor of parent class
        super( form, title, dlg );

        tableElement = tbl;

        // Pass number of rows for the table
        this.numRows = numRows;
        this.tabs = tabs;

    } // End of constructor
	
    public OneColumnTableDialog( Stage form, String title, String[] hdrTabs, DialogElement[][] dlg, int numRows, String[] tblTabs, TableElement[]... tbl ) 
    {
        // Call constructor of parent class
        super( form, title, hdrTabs, dlg );

        tableElement = tbl;

        // Pass number of rows for the table
        this.numRows = numRows;
        this.tabs = tblTabs;

    } // End of constructor
	
    public OneColumnTableDialog( Stage form, String title, DialogElement[] dlg, TableElement[]... tbl ) 
    {
        this( form, title, dlg, 12, null, tbl );
    }

    public OneColumnTableDialog( Stage form, String title, DialogElement[] dlg, int numRows, TableElement[]... tbl ) 
    {
        this( form, title, dlg, numRows, null, tbl );
    }

    public OneColumnTableDialog( Stage form, String title, DialogElement[] dlg, String[] tabs, TableElement[]... tbl ) 
    {
        this( form, title, dlg, 12, tabs, tbl );
    }
    */
} // End of class ** OneColumnTableDialog **
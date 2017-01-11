package data;

import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.util.LinkedHashSet;

import data.accgroups.IncomeStatementTree;
import data.accgroups.BalanceSheetTree;
import data.references.ChartOfAccounts;
import forms.DialogElement;
import forms.OneColumnTableDialog;
import forms.TableElement;
import forms.TableOutput;
import foundation.AssociativeList;
import foundation.RegistryItem;
import interfaces.Constants;
import static interfaces.Constants.dimension;
import static interfaces.Utilities.createDataClass;
import static interfaces.Utilities.getListElementBy;

/**
 * Class GLAccount - Creates G/L Account object
 * @author Peter Cross
 */
public class GLAccount extends RegistryItem implements Constants
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/  
    private static  LinkedHashSet[] list;  // List of G/L accounts for each Chart of Accounts
        
    /**
     * Gets string representation of class object
     * @return G/L Account's number
     */
    public String toString()
    {
        return (String) fields.get( "glNumber" );    
    }
    
    public static GLAccount getByCode( String code )
    {
        return (GLAccount) getListElementBy( list[0], "glNumber", code );
        
    } // End of method ** getByCode **
    
    
    public static GLAccount getByCode( String code, String chart )
    {
        int chartIndex = ChartOfAccounts.getIndexByName( chart );
        
        if ( chartIndex != -1 )
            return (GLAccount) getListElementBy( list[chartIndex], "glNumber", code );
        else
            return null;
        
    } // End of method ** getByCode **
    
    /**
     * Creates header structure of the form
     * @return Array of form header elements
     */
    protected DialogElement[][] createHeader()
    {
        // Array for header elements
        DialogElement[][] header = new DialogElement[1][7];
        // Helper object for creating new dialog elements
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        // 1st header element on 1st tab
        hdr = new DialogElement( "G/L Number" );
        hdr.attributeName = "glNumber";
        hdr.width = 60;
        hdr.shortName = "G/L #";
        hdr.textValue = (String) fields.get( "glNumber" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        // 2nd header element on 1st tab
        hdr = new DialogElement( "Account Name" );
        hdr.attributeName = "name";
        // If field text value is specified - pass it to the form
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        // 3rd header element on 1st tab
        hdr = new DialogElement( "Type of Account" );
        hdr.attributeName = "type";
        hdr.shortName = "Acct Type";
        // Possible text choices for the field
        hdr.textChoices = new String[]{ "Balance Sheet", "Income Statement" };
        hdr.editable = false;
        // If field text value is specified - pass it to the form
        hdr.textValue = (String) fields.get( "type" );
        hdr.validation = validationCode( hdr.labelName );
        // Lambda expression that gets invoked on element change event
        hdr.onChange = ( accountType, elementsList ) -> 
        {
            // Field name that is effected by element change event
            String fieldName = "Account Group";
            // Get account type without spaces
            String accType = ((String) accountType).trim();

            // Get reference to element field on the form
            TextField field  = (TextField) elementsList.get( fieldName );
            // If field is created - clear the field
            if ( field != null )
                field.setText( "" );

            // Get reference to stage object of the form
            Stage st = (Stage) elementsList.get( "stage" );

            // If account type is specified
            if ( !accType.isEmpty() )
                // If account type is Balance Sheet
                if ( accType == "Balance Sheet" )
                    // Create Balance Sheet Tree object and pass it to the form
                    elementsList.set( fieldName + "Object", new BalanceSheetTree( st ) );
                // If account type is Income Statement
                else if ( accType == "Income Statement" )
                    // Create Income Statement Tree object and pass it to the form
                    elementsList.set( fieldName + "Object", new IncomeStatementTree( st ) );
            
        }; // End of lambda expression
        header[0][2] = hdr;
        
        // 4th header element on 1st tab
        hdr = new DialogElement( "Account Group" );
        hdr.attributeName = "accountGroup";
        hdr.valueType = "Tree";
        // If field text value is specified - pass it to the form
        hdr.textValue = (String) fields.get( "accountGroup" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][3] = hdr;
        
        // 5th header element on 1st tab
        hdr = new DialogElement( "Quantity" );
        hdr.attributeName = "quantity";
        hdr.shortName = "Q-ty";
        hdr.checkBoxlabel = "Quantity Tracking";
	// If field text value is specified - pass it to the form
        hdr.textValue =  ( fields.get( "quantity" )!= null ? "" + fields.get( "quantity" ) : "" );
        header[0][4] = hdr;
        	
        // 6th header element on 1st tab
        hdr = new DialogElement( "Foreign Currency" );
        hdr.attributeName = "foreignCurrency";
        hdr.shortName = "Curr";
        hdr.checkBoxlabel = "Foreign Currency Operations";
        // If field text value is specified - pass it to the form
        hdr.textValue = ( fields.get( "foreignCurrency" )!= null ? "" + fields.get( "foreignCurrency" ) : "" );
        header[0][5] = hdr;
        
        // 7th header element on 1st tab
        hdr = new DialogElement( "Contra Account" );
        hdr.attributeName = "contraAccount";
        hdr.shortName = "Cntr";
        hdr.checkBoxlabel = "Contra Account";
        // If field text value is specified - pass it to the form
        hdr.textValue = ( fields.get( "contraAccount" )!= null ? "" + fields.get( "contraAccount" ) : "" );
        header[0][6] = hdr;
        
        return header;
    
    } // End of method ** createHeader **
	
    /**
     * Creates table structure of the form
     * @return Array of table elements
     */
    protected TableElement[][] createTable()
    {
        // Array of table elements
        TableElement[][] table = new TableElement[1][3];
        // Helper object for creating new table elements
        TableElement tbl;

        // 1st column of the table on the 1st tab
        tbl = new TableElement( "Analytical Dimensions" );
        tbl.width = 200;
        tbl.editable = false;
        // If there are specified values of the column - pass them to the form
        tbl.textValue = (String[]) fields.get( "analyticsControl" );
        tbl.validation = validationCode( tbl.columnName );
        // Possible text choices of the column
        tbl.textChoices = new String[dimension.size()];
        for ( int i = 0; i < dimension.size(); i++ )
            tbl.textChoices[i] = dimension.getKey(i);
        table[0][0] = tbl;
        
        // 2nd column of the table on the 1st tab
        tbl = new TableElement( "Type" );
        tbl.width = 50;
        tbl.editable = false;
        // If there are specified values of the column - pass them to the form
        tbl.textValue = (String[]) fields.get( "analyticsType" );
        tbl.validation = validationCode( tbl.columnName );
        // Possible text choices of the column
        tbl.textChoices = new String[]
                            { "Balance",
                              "Period" };
        table[0][1] = tbl;
        
        return table;
    
    } // End of method ** createTable **
	
    /**
     * Saves input information into object attributes
     * @param header Array of input from header
     * @param table Array of input from table
     */
    protected void init( String[][] header, String[][][] table )
    {
        // If header is specified
        if ( header.length > 0 )
        {
            try
            {
                fields.set( "glNumber",  header[0][0] );
                fields.set( "name",  header[0][1] );
                fields.set( "type",  header[0][2] );
                fields.set( "accountGroup",  header[0][3] );
                
                fields.set( "quantity",  Byte.parseByte( header[0][4] ) );
                fields.set( "foreignCurrency",  Byte.parseByte( header[0][5] ) );
                fields.set( "contraAccount",  Byte.parseByte( header[0][6] ) );
            }
            catch ( Exception e )
            {}
        }
        
        // If table is specified
        if ( table != null && table.length > 0 )
        {
            // Get the number of account analytical dimensions
            int numAnalytics = table[0].length;

            // Create arrays for analytical dimensions and analytics tracking type
            String[] analyticsControl = new String[numAnalytics];
            String[] analyticsType = new String[numAnalytics];

            // Loop for each Analytical Dimension
            for ( int i = 0; i < numAnalytics; i++ )
            {
                analyticsControl[i] = table[0][i][0];
                analyticsType[i] = table[0][i][1];
            }
            
            fields.set( "analyticsControl", analyticsControl );
            fields.set( "analyticsType", analyticsType );
        }
        else
        {
            fields.set( "analyticsControl", new String[]{} );
            fields.set( "analyticsType", new String[]{} );
        }
    } // End of method ** init **
	
    /**
     * Displays dialog form
     */
    public void display()
    {
        // Number of analytical dimensions
        byte numDmns = 5;
        
        String[] analyticsControl = (String[]) fields.get( "analyticsControl" );
        
        // If analytical dimensions are specified
        if ( analyticsControl != null )
            // Assign the number of analytical dimensions
            numDmns = (byte) Math.max( 5, analyticsControl.length );
        
        // Create header elements array and add it to attributes list
        attributesList.set( "header", createHeader() );
        // Create table elements array and add it to attributes list
        attributesList.set( "table", createTable() );
        
        // Display one-column table dialog form and get the results of input or editing
        output =  new OneColumnTableDialog( this, "G/L Account", numDmns ).result();
        
        // Assign the returned results to object properties
        init( output.header, output.table );
        
        // Add output results to attributes list
        attributesList.set( "output", output );
    
    } // End of method ** display **
    
    /**
     * Display input dialog form for new object and get output results
     * @return TableOutput object with output results
     */
    @Override
    public TableOutput getOutput( String itemType )
    {
        // Number of analytical dimensions
        byte numDmns = 5;
        
        String[] analyticsControl = (String[]) fields.get( "analyticsControl" );
        
        // If analytical dimensions are specified
        if ( analyticsControl != null )
            // Assign the number of analytical dimensions
            numDmns = (byte) Math.max( 5, analyticsControl.length );
        
        // Display input form and get the results of input
        TableOutput result =  new OneColumnTableDialog( this, itemType, numDmns ).result();
        
        // Assign the input rsults to object properties
        init( result.header, result.table );
        
        // Return the input result
        return result;
        
    } // End of method ** getOutput **
    
    /**
     * Get List of G/L Accounts in ArrayList
     * @return ArrayList with list of G/L Accounts
     */
    public static LinkedHashSet[] getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of G/L Accounts
     * @return Array with List of G/L Accounts
     */
    public static  LinkedHashSet[] createList()
    {
        Class c = createDataClass( "ChartOfAccounts" );
        
        try
        {
            // Get list of Charts of Accounts
            LinkedHashSet charts = ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0];
            
            // Create array for G/L Accounts list with size equal to number of Charts of Accounts
            list = new LinkedHashSet[ charts.size() ];
        }
        catch ( Exception e )
        {
            // Create array for G/L Accounts for one Chart Of Accounts
            list = new LinkedHashSet[1];
        }
        
        // Create ArrayList for list of G/L Accounts for each Chart of Accounts
        for ( int i = 0; i < list.length; i++ )
            list[i] = new LinkedHashSet<>();
        
        AssociativeList fields;
        GLAccount acnt;
        
        // For 1st Chart of Accounts
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "1000" );
        fields.set( "name", "Cash on Hand" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Cash and Cash Equivalents" );
        fields.set( "foreignCurrency", 1 );
        fields.set( "analyticsControl", new String[]{ "Bank Accounts", "Business Partners" } );
        fields.set( "analyticsType", new String[]{ "Balance", "Period" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "1050" );
        fields.set( "name", "Marketable Securities" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Short-Term Investments" );
        fields.set( "quantity", 1 );
        fields.set( "foreignCurrency", 1 );
        fields.set( "analyticsControl", new String[]{ "Marketable Securities" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "1100" );
        fields.set( "name", "Accounts Receiable" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Customers Receivable" );
        fields.set( "analyticsControl", new String[]{ "Business Partners" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "1200" );
        fields.set( "name", "Inventory for Sale" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Merchandize" );
        fields.set( "quantity", 1 );
        fields.set( "analyticsControl", new String[]{ "Inventory" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "1300" );
        fields.set( "name", "Prepaid Expenses" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Prepaid Expenses" );
        fields.set( "analyticsControl", new String[]{ "Expenses" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "2000" );
        fields.set( "name", "Fixed Assets" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Fixed Assets" );
        fields.set( "quantity", 1 );
        fields.set( "analyticsControl", new String[]{ "Long Term Assets" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "2100" );
        fields.set( "name", "Intangible Assets" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Non-Tangible Assets" );
        fields.set( "quantity", 1 );
        fields.set( "analyticsControl", new String[]{ "Long Term Assets" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "2800" );
        fields.set( "name", "Amortization of Fixed Assets" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Fixed Assets" );
        fields.set( "contraAccount", 1 );
        fields.set( "analyticsControl", new String[]{ "Long Term Assets" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "2900" );
        fields.set( "name", "Amortization of Intangble Assets" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Non-Tangible Assets" );
        fields.set( "contraAccount", 1 );
        fields.set( "analyticsControl", new String[]{ "Long Term Assets" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "3000" );
        fields.set( "name", "Accounts Payable" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Suppliers and Vendors Payable" );
        fields.set( "analyticsControl", new String[]{ "Business Partners" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "3100" );
        fields.set( "name", "Salaries Payable" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Salaries Payable" );
        fields.set( "analyticsControl", new String[]{ "Employees" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "3200" );
        fields.set( "name", "Income Tax Payable" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Income Taxes" );
        fields.set( "analyticsControl", new String[]{  } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "3300" );
        fields.set( "name", "Sales Tax Payable" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Sales Taxes" );
        fields.set( "analyticsControl", new String[]{  } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "3400" );
        fields.set( "name", "Payroll Taxes Payable" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Payroll Taxes" );
        fields.set( "analyticsControl", new String[]{  } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "4000" );
        fields.set( "name", "Credit Loans" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Credit Loans" );
        fields.set( "analyticsControl", new String[]{ "Bank Accounts" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "5000" );
        fields.set( "name", "Common Stock" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Common Stock" );
        fields.set( "analyticsControl", new String[]{ "Shareholders" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(0);
        fields = acnt.getFields();
        fields.set( "glNumber", "5100" );
        fields.set( "name", "Retained Earnings" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Retained Earnings" );
        
        // For 2nd Chart of Accounts
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "100" );
        fields.set( "name", "Cash on Hand" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Cash and Cash Equivalents" );
        fields.set( "foreignCurrency", 1 );
        fields.set( "analyticsControl", new String[]{ "Bank Accounts", "Business Partners" } );
        fields.set( "analyticsType", new String[]{ "Balance", "Period" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "105" );
        fields.set( "name", "Marketable Securities" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Short-Term Investments" );
        fields.set( "quantity", 1 );
        fields.set( "foreignCurrency", 1 );
        fields.set( "analyticsControl", new String[]{ "Marketable Securities" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "110" );
        fields.set( "name", "Accounts Receivable" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Customers Receivable" );
        fields.set( "analyticsControl", new String[]{ "Business Partners" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "120" );
        fields.set( "name", "Inventory for Sale" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Merchandize" );
        fields.set( "quantity", 1 );
        fields.set( "analyticsControl", new String[]{ "Inventory" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "130" );
        fields.set( "name", "Prepaid Expenses" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Prepaid Expenses" );
        fields.set( "analyticsControl", new String[]{ "Expenses" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "200" );
        fields.set( "name", "Fixed Assets" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Fixed Assets" );
        fields.set( "quantity", 1 );
        fields.set( "analyticsControl", new String[]{ "Long Term Assets" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "210" );
        fields.set( "name", "Intangible Assets" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Non-Tangible Assets" );
        fields.set( "quantity", 1 );
        fields.set( "analyticsControl", new String[]{ "Long Term Assets" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "280" );
        fields.set( "name", "Amortization of Fixed Assets" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Fixed Assets" );
        fields.set( "contraAccount", 1 );
        fields.set( "analyticsControl", new String[]{ "Long Term Assets" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "290" );
        fields.set( "name", "Amortization of Intangble Assets" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Non-Tangible Assets" );
        fields.set( "contraAccount", 1 );
        fields.set( "analyticsControl", new String[]{ "Long Term Assets" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "300" );
        fields.set( "name", "Accounts Payable" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Suppliers and Vendors Payable" );
        fields.set( "analyticsControl", new String[]{ "Business Partners" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "310" );
        fields.set( "name", "Salaries Payable" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Salaries Payable" );
        fields.set( "analyticsControl", new String[]{ "Employees" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "320" );
        fields.set( "name", "Income Tax Payable" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Income Taxes" );
        fields.set( "analyticsControl", new String[]{  } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "330" );
        fields.set( "name", "Sales Tax Payable" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Sales Taxes" );
        fields.set( "analyticsControl", new String[]{  } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "340" );
        fields.set( "name", "Payroll Taxes Payable" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Payroll Taxes" );
        fields.set( "analyticsControl", new String[]{  } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "400" );
        fields.set( "name", "Credit Loans" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Credit Loans" );
        fields.set( "analyticsControl", new String[]{ "Bank Accounts" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "500" );
        fields.set( "name", "Common Stock" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Common Stock" );
        fields.set( "analyticsControl", new String[]{ "Shareholders" } );
        fields.set( "analyticsType", new String[]{ "Balance" } );
        
        acnt = new GLAccount(1);
        fields = acnt.getFields();
        fields.set( "glNumber", "510" );
        fields.set( "name", "Retained Earnings" );
        fields.set( "type", "Balance Sheet" );
        fields.set( "accountGroup", "Retained Earnings" );
        
        return list;
    }
    
    /*                        Constructors                                                                                  */
    /************************************************************************************************************************/
    public GLAccount()
    {
        super( "G/L Account" );
    }
    
    public GLAccount( int chartOfAccountsNumber )
    {
        super( "G/L Account" );
        list[chartOfAccountsNumber].add( this );
    }
    
    public GLAccount( Stage stage )
    {
        super( stage, "G/L Account" );
        list[0].add( this );
    }	
    
    public GLAccount( Stage stage, int chartOfAccountsNumber )
    {
        super( stage, "G/L Account" );
        list[chartOfAccountsNumber].add( this );
    }	

} // End of class GLAccount
package data;

import javafx.stage.Stage;
import java.util.LinkedHashSet;
import java.util.Iterator;

import static interfaces.Utilities.createDataClass;

import data.dimensions.BusinessPartner;
import data.references.Unit;
import data.references.Currency;
import forms.DialogElement;
import forms.TableElement;
import forms.TableOutput;
import forms.TwoColumnTableDialog;
import foundation.AssociativeList;
import foundation.RegistryItem;
import java.text.DecimalFormat;

/**
 * Class JournalEntry - creates Journal Entry Object and displays it
 * @author Peter Cross
 */
public class JournalEntry extends RegistryItem
{
    private static LinkedHashSet[] list;       // List of Journal Entries for each Legal Entity
    private static String[]        tableTabs;  // Tabs for table part of the form
    private static String[]        numerator;  // Array of numerators
    
    private int legalEntity;
    
    public String toString()
    {
        String date = (String)fields.get( "date" );
        String jeNumber = (String)fields.get( "jeNumber" );
        double amount = (double)fields.get( "amount" );
        String currency = (String) fields.get( "currency" );
        
        DecimalFormat formatter = new DecimalFormat( "#,###.00" );
        
        return date + " " + jeNumber + " : " + formatter.format( amount ) + " " + currency;
    }
    
    public String getNumerator()
    {
        try
        {
            return numerator[legalEntity];
        }
        catch ( Exception e )
        {
            return null;
        }     
    }
    
    /**
     * Creates header structure of the form
     * @return Array of form header elements
     */
    protected DialogElement[][] createHeader()
    {
        // Array for header elements
        DialogElement[][] header = new DialogElement[1][8];
        // Helper object for creating new dialog elements
        DialogElement hdr;
        
        // If fields is not created yet - create
        if ( fields == null )
            fields = new AssociativeList();
        
        // --  1st Column --
        // 1st header element
        hdr = new DialogElement("Date  ");
        hdr.valueType = "Date";
        // If field text value is specified - pass it to the form
        hdr.textValue = (String) fields.get( "date" );
        header[0][0] = hdr;
        
        // 2nd header element
        hdr = new DialogElement("JE #");
        hdr.attributeName = "jeNumber";
        hdr.valueType = "Numerator"; // "Text";
        hdr.width = 105;
        // If field text value is specified - pass it to the form
        hdr.textValue =  (String) fields.get( "jeNumber" ); 
        header[0][1] = hdr;
        
        // 3rd header element
        hdr = new DialogElement("Amount");
        hdr.valueType = "Number";
        hdr.width = 105;
        // If field text value is specified - pass it to the form
        DecimalFormat formatter = new DecimalFormat( "#,###.00" );
        try
        {
            hdr.textValue = formatter.format( Double.parseDouble( getFieldNumber( "amount" )  ) );
        }
        catch ( Exception e )
        {
            hdr.textValue = getFieldNumber( "amount" );
        }
        header[0][2] = hdr;
        
        // 4th header element
        hdr = new DialogElement("Currency");
        hdr.shortName = "Cur";
        hdr.valueType = "List";
        hdr.list = Currency.getItemsList();
        hdr.defaultChoice = 0;
        hdr.editable = false;
        hdr.width = 105;
        // If field text value is specified - pass it to the form
        hdr.textValue = (String) fields.get( "currency" );
        header[0][3] = hdr;
        
        // 5th header element
        hdr = new DialogElement("Counterpart    ");
        hdr.valueType = "List";
        hdr.list = BusinessPartner.getItemsList();
        hdr.editable = false;
        hdr.width = 250;
        // If field text value is specified - pass it to the form
        hdr.textValue = (String) fields.get( "counterpart" );
        header[0][4] = hdr;
        
        // 6th header element
        hdr = new DialogElement("Operation");
        hdr.valueType = "Tree";
        hdr.editable = true;
        hdr.width = 250;
        // If field text value is specified - pass it to the form
        hdr.textValue = (String) fields.get( "operation" );
        header[0][5] = hdr;
        
        // 7th header element
        hdr = new DialogElement("Source Doc");
        hdr.shortName = "Source";
        hdr.valueType = "Tree";
        hdr.editable = true;
        hdr.width = 250;
        // If field text value is specified - pass it to the form
        hdr.textValue = (String) fields.get( "sourceDoc" );
        header[0][6] = hdr;
        
        // 7th header element
        hdr = new DialogElement("Note        ");
        hdr.valueType = "Text";
        hdr.editable = true;
        hdr.width = 450;
        // If field text value is specified - pass it to the form
        hdr.textValue = (String) fields.get( "note" );
        header[0][7] = hdr;
        
        /*
        // -- 2nd Column --
        
        // 9th header element
        hdr = new DialogElement("Rate Type");
        hdr.shortName = "Rt.type";
        hdr.valueType = "List";
        hdr.list = ExchangeRateType.getItemsList();
        hdr.defaultChoice = 0;
        hdr.editable = false;
        hdr.width = 255;
        // If field text value is specified - pass it to the form
        hdr.textValue = (String) fields.get( "rateType" );
        header[0][8] = hdr;
        
        // 10th header element
        hdr = new DialogElement("Exchange Rate");
        hdr.shortName = "Ex.rate";
        hdr.valueType = "Number";
        hdr.width = 110;
        // If field text value is specified - pass it to the form
        hdr.textValue = getFieldNumber( "exchangeRate" ); 
        header[0][9] = hdr;
        
        // 11th header element
        hdr = new DialogElement("Conv Amount");
        hdr.shortName = "Conv.Amnt";
        hdr.valueType = "Number";
        hdr.width = 110;
        // If field text value is specified - pass it to the form
        hdr.textValue =  getFieldNumber( "convAmount" );
        header[0][10] = hdr;
        */
        
        return header;
    
    } // End of method ** createHeader **
    
    /**
     * Fills array with table element settings
     * @param table Array to fill
     */
    private void fillTable( TableElement[][] table )
    {
        // Helper object for creating new table elements
        TableElement tbl;

        // Loop for each table tab ( each Chart of Accounts )
        for ( int i = 0; i < table.length; i++ )
        {
            // 1st column 
            tbl = new TableElement("G/L Acct");
            tbl.valueType = "List";
            tbl.list = GLAccount.getItemsList()[i];
            tbl.width = 60;
            tbl.editable = false;
            // If there are specified values of the column - pass them to the form
            tbl.textValue = ((String[][]) fields.get( "account" ))[i];
            table[i][0] = tbl;

            // 2nd column 
            tbl = new TableElement("Analytical Dimensions");
            tbl.valueType = "Dimension";
            tbl.width = 150;
            // If there are specified values of the column - pass them to the form
            tbl.textValue = ((String[][]) fields.get( "analytics" ))[i];
            table[i][1] = tbl;

            // 3rd column 
            tbl = new TableElement("Quantity  ");
            tbl.valueType = "Number";
            tbl.width = 60;
            // If there are specified values of the column - pass them to the form
            tbl.textValue = convertToStringArray( ((double[][]) fields.get( "quantity" ))[i] );
            table[i][2] = tbl;

            // 4th column 
            tbl = new TableElement("Unit");
            tbl.valueType = "List";
            tbl.list = Unit.getItemsList();
            tbl.width = 40;
            tbl.editable = false;
            // If there are specified values of the column - pass them to the form
            tbl.textValue = ((String[][]) fields.get( "unit" ))[i];
            table[i][3] = tbl;

            // 5th column 
            tbl = new TableElement("Org.Amnt");
            tbl.valueType = "Number";
            tbl.width = 60;
            double[][] orgAmnt = (double[][]) fields.get( "originalAmount" );
            // If there are specified values of the column - pass them to the form
            tbl.textValue = convertToStringArray( (orgAmnt!= null ? orgAmnt[i] : null) );
            table[i][4] = tbl;

            // 6th column 
            tbl = new TableElement("Org.Curr");
            tbl.valueType = "List";
            tbl.list = Currency.getItemsList();
            tbl.width = 60;
            tbl.editable = false;
            // If there are specified values of the column - pass them to the form
            tbl.textValue = ((String[][]) fields.get( "originalCurrency" ))[i];
            table[i][5] = tbl;

            // 7th column 
            tbl = new TableElement("Debit");
            tbl.valueType = "Number";
            tbl.width = 80;
            // If there are specified values of the column - pass them to the form
            tbl.textValue = convertToStringArray( ((double[][]) fields.get( "debitAmount" ))[i] );
            table[i][6] = tbl;

            // 8th column 
            tbl = new TableElement("Credit");
            tbl.valueType = "Number";
            tbl.width = 80;
            // If there are specified values of the column - pass them to the form
            tbl.textValue = convertToStringArray( ((double[][]) fields.get( "creditAmount" ))[i] );
            table[i][7] = tbl;
        }
    } // End of method ** fillTable **
    
    /**
     * Crates Arrays for table columns
     */
    private void createTableArrays()
    {
        // Number of Tabs
        int numTabs = 1;
        
        // If there are Table tabs created
        if ( tableTabs != null && tableTabs.length > 0 )
            numTabs = tableTabs.length;
        
        // Create arrays for each table column
        fields.set( "account",          new String[numTabs][] );
        fields.set( "analytics",        new String[numTabs][] );
        fields.set( "quantity",         new double[numTabs][] );
        fields.set( "unit",             new String[numTabs][] );
        fields.set( "originalCurrency", new String[numTabs][] );
        fields.set( "originalAmount",   new double[numTabs][] );
        fields.set( "debitAmount",      new double[numTabs][] );
        fields.set( "creditAmount",     new double[numTabs][] );
        
    } // End of method ** createTableArrays **
    
    /**
     * Creates Table tabs for each Chart Of Accounts
     * @return Created Table Tabs
     */
    private static String[] tableTabs()
    {
        Class c = createDataClass( "ChartOfAccounts" );
        
        try
        {
            // Get Charts of Accounts List
            LinkedHashSet charts = ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0];
            
            // Create array for Tab names
            String[] tabs = new String[ charts.size() ]; 
            
            Iterator it = charts.iterator();
            
            int i = 0;
            
            // Fill array for tab names with names of Charts of Accounts
            while ( it.hasNext() )
                tabs[i++] = it.next().toString();
            
            /*
            // Fill array for tab names with names of Charts of Accounts
            for ( int i = 0; i < charts.size(); i++ )
                tabs[i] = charts.get(i).toString();
            */
            
            return tabs;
        }
        catch ( Exception e )
        {
            return null;
        }
        
    } // End of ** tableTabs **
    
    /**
     * Creates table structure of the form
     * @return Array of table elements
     */
    protected TableElement[][] createTable()
    {
        // Array for table columns
        TableElement[][] table;
        
        // Create table tabs for each Chart of Accounts
        tableTabs = tableTabs();
        
        // If no Tabs were created
        if ( tableTabs == null )
            // Create table for one Chart Of Accounts
            table = new TableElement[1][9];
        
        // Otherwise
        else
            // Create table for each Chart Of Accounts
            table = new TableElement[tableTabs.length][9];
        
        // Create arrays for each table column
        createTableArrays();
        
        // Fill table with table element settings
        fillTable( table );
        
        return table;
        
    } // End of method ** createTable **
    
    /**
     * Converts array of double values to string array
     * @param obj Array of double values
     * @return String array
     */
    private String[] convertToStringArray( double[] obj )
    {
        // If array is not sspecified
        if ( obj == null )
            return null;
        
        // If array is empty
        if ( obj.length == 0 )
            return new String[]{};
        
        // Create String array for output
        String[] result = new String[obj.length];
        
        try 
        {
            // Try to convert 1st array element to Double object
            ((Double) obj[0]).toString();
            
            // Loop for every array object
            for ( int i = 0; i < obj.length; i++ )
            {
                // Convert value to int
                int val = (int) obj[i];
                
                // If original value is greater than int value
                if ( obj[i] > val )
                    // Convert original value to string and store in output array
                    result[i] = new String( ((Double) obj[i]).toString() );
                
                // If converted value is zero
                else if ( val == 0 )
                    // store in output array just empty string
                    result[i] = "";
                
                // Otherwise
                else
                    // Convert int value to string and store in output array
                    result[i] = new String( ((Integer) val).toString() );
                
            } // End of loop for every array object
            
        } // End of try clause
        catch ( Exception e1 )
        {
            // Return empty string array
            return new String[]{};
        }
        
        return result;
        
    } // End of method ** convertToStringArray **
    
    /**
     * Converts array of integer values to string array
     * @param obj Array of integer values
     * @return String array
     */
    private String[] convertToStringArray( int[] obj )
    {
        // If array is not specified
        if ( obj == null )
            return null;
        
        // If array is empty
        if ( obj.length == 0 )
            return new String[]{};
        
        // Create String array for output results
        String[] result = new String[obj.length];
        
        try 
        {
            // Try to convert 1st array element to Integer object
            ((Integer) obj[0]).toString();
            
            // Loop for each array element
            for ( int i = 0; i < obj.length; i++ )
            {
                // If array value is zero
                if ( obj[i] == 0 )
                    // Store in output array just empty string
                    result[i] = "";
                
                // Otherwise
                else
                    // Convert integer to string and store in output array
                    result[i] = new String( ((Integer) obj[i]).toString() );
            }
        }
        catch ( Exception e )
        {
            // Return amopty string array
            return new String[]{};
        }
        
        return result;
        
    } // End of method ** convertToStringArray **
    
    /**
     * Saves input information into object attributes
     * @param header Array of input from header
     * @param table Array of input from table
     */
    protected void init( String[][] header, String[][][] table )
    {
        list[legalEntity].add( this );
        
        // If header elements are specified
        if ( header.length > 0 )
        {
            try
            {
                fields.set( "date",      header[0][0] );
                fields.set( "jeNumber",  header[0][1] );
                
                if ( list[legalEntity].contains( this ) )
                    numerator[legalEntity] = header[0][1];
                
                try
                {
                    fields.set( "amount", Double.parseDouble( header[0][2].replaceAll( ",", "" ) ) );
                }
                catch ( Exception e1 )
                {
                    fields.set( "amount", 0 );
                }
                fields.set( "currency",    header[0][3] );
                fields.set( "counterpart", header[0][4] );
                fields.set( "operation",   header[0][5] );
                fields.set( "sourceDoc",   header[0][6] );
                fields.set( "note",        header[0][7] );
                
                /*
                fields.set( "rateType", header[0][8] );
                try
                {
                    fields.set( "exchangeRate", Double.parseDouble( header[0][9] ) );
                }
                catch ( Exception e1 )
                {
                    fields.set( "exchangeRate", 0 );
                }
                try
                {
                    fields.set( "convAmount", Double.parseDouble( header[0][10] ) );
                }
                catch ( Exception e1 )
                {
                    fields.set( "convAmount", 0 );
                }
                */
            }
            catch ( Exception e )
            {}
            
        } // End of If header elements are specified
        
        // If table elements are specified
        if ( table != null && table.length > 0 )
        {
            String[][] account = (String[][]) fields.get( "account" );
            String[][] analytics = (String[][]) fields.get( "analytics" );
            double[][] quantity = (double[][]) fields.get( "quantity" );
            String[][] unit = (String[][]) fields.get( "unit" );
            String[][] originalCurrency = (String[][]) fields.get( "originalCurrency" );
            double[][] originalAmount = (double[][]) fields.get( "originalAmount" );
            double[][] debitAmount = (double[][]) fields.get( "debitAmount" );
            double[][] creditAmount = (double[][]) fields.get( "creditAmount" );
            
            // Loop for each table tab
            for ( int t = 0; t < table.length; t++ )
            {
                // Get table number of lines
                int numLines = table[t].length;

                account[t] = new String[numLines];
                analytics[t] = new String[numLines];
                quantity[t] = new double[numLines];
                unit[t] = new String[numLines];
                originalCurrency[t] = new String[numLines];
                originalAmount[t] = new double[numLines];
                debitAmount[t] = new double[numLines];
                creditAmount[t] = new double[numLines];
                
                // Loop for each Journal Entry line
                for ( int i = 0; i < numLines; i++ )
                {
                    account[t][i] = table[t][i][0];
                    analytics[t][i] = table[t][i][1];
                    try
                    {
                        quantity[t][i] = Double.parseDouble( table[t][i][2] );
                    }
                    catch ( Exception e1 )
                    {
                        quantity[t][i] = 0;
                    }
                    unit[t][i] = table[t][i][3];
                    
                    try
                    {
                        originalAmount[t][i] = Double.parseDouble( table[t][i][4] );
                    }
                    catch ( Exception e1 )
                    {
                        originalAmount[t][i] = 0;
                    }
                    originalCurrency[t][i] = table[t][i][5];
                    
                    try
                    {
                        debitAmount[t][i] = Double.parseDouble( table[t][i][6] );
                    }
                    catch ( Exception e1 )
                    {
                        debitAmount[t][i] = 0;
                    }
                    try
                    {
                        creditAmount[t][i] = Double.parseDouble( table[t][i][7] );
                    }
                    catch ( Exception e1 )
                    {
                        creditAmount[t][i] = 0;
                    }
                } // End of loop for each Journal Entry line
                
            } // End for loop ** for each table tab **
            
        } // End of If table elements are specified
        
    } // End of method ** init **
    
    /**
     * Displays input form for Journal Entry
     */
    public void display( )
    {
        byte numLines = 5;
        
        String[][] account = (String[][]) fields.get( "account" );
        
        // If array of G/L accounts is specified
        if ( account != null )
            for ( String[] acct : account) 
                if ( acct != null )
                    // get the number of lines for Journal Entry
                    numLines = (byte) Math.max( numLines, acct.length );
            
        // Create array of header form elements and strore it in attributes list
        attributesList.set( "header", createHeader() );
        
        TableElement[][] table;
        
        // If Table tabs weren't created
        if ( tableTabs == null )
            // Table with for one Chart of Accounts
            table = new TableElement[1][9];
        else
            // Table with array for each Chart Of Accounts
            table = new TableElement[tableTabs.length][9];
        
        // Fill table with table element settings
        fillTable( table );
        
        // Create array of table form elements and store it in attributes list
        attributesList.set( "table", table );
        
        // Display 2-column table dialog form and get the results of input
        output =  new TwoColumnTableDialog( this, "Journal Entry", numLines, tableTabs ).result();
        
        // Assign the returned results to object properties
        init( output.header, output.table );
        
        // Store output results in attributes list
        attributesList.set( "output", output );
        
    } // End of method ** display **
    
    /**
     * Display input dialog form for new object and get output results
     * @return TableOutput object with output results
     */
    @Override
    public TableOutput getOutput( String itemType )
    {
        byte numLines = 5;
        
        String[][] account = (String[][]) fields.get( "account" );
        
        // If array of G/L accounts is specified
        if ( account != null )
            for ( String[] acct : account) 
                if ( acct != null )
                    // get the number of lines for Journal Entry
                    numLines = (byte) Math.max( numLines, acct.length );
        
        // Display input form and get the results of input
        TableOutput result =  new TwoColumnTableDialog( this, itemType, numLines, tableTabs ).result();
        
        // Assign the returned results to object properties
        init( result.header, result.table );
        
        // Return the input results
        return result;
        
    } // End of method ** getOutput **
    
    /**
     * Get List of Journal Entries in ArrayList
     * @return ArrayList with list of Journal Entries
     */
    public static LinkedHashSet[] getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of Journal Entries
     * @return Array with List of Journal Entries
     */
    public static  LinkedHashSet[] createList()
    {
        Class c = createDataClass( "LegalEntity" );
        
        try
        {
            // Get list of Legal Entities
            LinkedHashSet entities = ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0];
            
            int numEntities = entities.size();
            
            // Create arrays for Legal Entities list with size equal to number of Legal Entities
            list = new LinkedHashSet[ numEntities ];
            numerator = new String[ numEntities ];
        }
        catch ( Exception e )
        {
            // Create arrays for one Legal Entity
            list = new LinkedHashSet[1];
            numerator = new String[1];
        }
        
        // Create ArrayList for list of Legal Entities for each Legal Entity
        for ( int i = 0; i < list.length; i++ )
            list[i] = new LinkedHashSet<>();
        
        return list;
    }
    
    /*                        Constructors                                                                                  */
    /************************************************************************************************************************/
    public JournalEntry()
    {
        super( "Journal Entry" );  
    }
    
    public JournalEntry( int legalEntity )
    {
        super( "Journal Entry" );  
        
        this.legalEntity = legalEntity;
    }
    
    public JournalEntry( Stage stage )
    {
        super( stage, null, tableTabs(), "Journal Entry" );  
        
        this.legalEntity = 0;
    }
    
    public JournalEntry( Stage stage, int legalEntity )
    {
        super( stage, null, tableTabs(), "Journal Entry" );  
        
        this.legalEntity = legalEntity;
    }
    
} // End of class ** JournalEntry **
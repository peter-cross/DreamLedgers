package data.references;

import javafx.stage.Stage;
import java.util.LinkedHashSet;
import java.util.Iterator;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;
import static interfaces.Utilities.getListElementBy;

/**
 * Class ChartOfAccounts - To create and store Charts of Accounts
 * @author Peter Cross
 */
public class ChartOfAccounts extends RegistryItem
{
    protected static  LinkedHashSet list;       // List of Items
    
    /**
     * Gets Chart of Accounts by its name
     * @param name Name of a Chart of Accounts
     * @return Chart of Accounts object
     */
    public static ChartOfAccounts getByName( String name )
    {
        return (ChartOfAccounts) getListElementBy( list, "name", name );
    
    } // End of method ** getByCode **
    
    /**
     * Gets Chart of Accounts by its code
     * @param code Code of a Chart of Accounts
     * @return Chart of Accounts object
     */
    public static ChartOfAccounts getByCode( String code )
    {
        return (ChartOfAccounts) getListElementBy( list, "code", code );
    
    } // End of method ** getByCode **
    
    public static int getIndexByCode( String code )
    {
        AssociativeList fields;
        
        try
        {
            Iterator it = list.iterator();
            
            int i = 0;
            while ( it.hasNext() )
            {
                fields = ((ChartOfAccounts) it.next()).getFields();
                
                if ( fields !=  null && (String) fields.get( "code" ) == code )
                    return i;
                else
                    i++;
            }
            
            /*
            for ( int i = 0; i < list.size(); i++ )
            {
                fields = ((ChartOfAccounts) list.get(i)).getFields();

                if ( fields !=  null && (String) fields.get( "code" ) == code )
                    return i;
            }
            */
        }
        catch ( Exception e )
        {
            return -1;
        }
        
        return -1;
    }
    
     public static int getIndexByName( String name )
    {
        AssociativeList fields;
        
        try
        {
            Iterator it = list.iterator();
            
            int i = 0;
            while ( it.hasNext() )
            {
                fields = ((ChartOfAccounts) it.next()).getFields();
                
                if ( fields !=  null && (String) fields.get( "name" ) == name )
                    return i;
                else
                    i++;
            }
            /*
            for ( int i = 0; i < list.size(); i++ )
            {
                fields = ((ChartOfAccounts) list.get(i)).getFields();

                if ( fields !=  null && (String) fields.get( "name" ) == name )
                    return i;
            }
            */
        }
        catch ( Exception e )
        {
            return -1;
        }
        
        return -1;
    }
    
    /**
     * Creates dialog elements for dialog header
     * @return Array of Header dialog elements
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        // Create array for header dialog elements
        DialogElement[][] header = new DialogElement[1][3];
        // Helper object for creating new dialog elements
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "Code" );
        hdr.valueType = "Text";
        hdr.width = 70;
        hdr.textValue = (String) fields.get( "code" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name         " );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Currency" );
        hdr.valueType = "List";
        hdr.list = Currency.createList()[0];
        hdr.width = 70;
        hdr.editable = false;
        hdr.textValue = (String) fields.get( "currency" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        return header;
    }

    /**
     * Saves input information into object attributes
     * @param header Array of input from header
     * @param table Array of input from table
     */
    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length == 0 )
            return;
        
        fields.set( "code", header[0][0] );
        fields.set( "name", header[0][1] );
        fields.set( "currency", header[0][2] );
    }

    /**
     * Get List of Chart of Accounts in ArrayList
     * @return ArrayList with list of Chart of Accounts
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of Chart of Accounts
     * @return Array with List of Chart of Accounts
     */
    public static  LinkedHashSet[] createList()
    {
        // Create ArrayList object for List of Chart of Accounts
        list = new LinkedHashSet<>();
        
        // Helper object for creating new list elements
        ChartOfAccounts chart;
        AssociativeList fields;
        
        chart = new ChartOfAccounts();
        fields = chart.getFields();
        fields.set( "code", "IAS" );
        fields.set( "name", "Financial (IAS)" );
        fields.set( "currency", "CAD" );
        
        chart = new ChartOfAccounts();
        fields = chart.getFields();
        fields.set( "code", "ASPE" );
        fields.set( "name", "Financial (ASPE)" );
        fields.set( "currency", "CAD" );
        
        chart = new ChartOfAccounts();
        fields = chart.getFields();
        fields.set( "code", "MGM" );
        fields.set( "name", "Management" );
        fields.set( "currency", "USD" );
        
        chart = new ChartOfAccounts();
        fields = chart.getFields();
        fields.set( "code", "BDG" );
        fields.set( "name", "Budget" );
        fields.set( "currency", "USD" );
        
        return new LinkedHashSet[] { list };
    }
    
    public ChartOfAccounts()
    {
        super( "Chart of Accounts" );
        list.add( this );
    }
    
    public ChartOfAccounts( Stage stage )
    {
        super( stage, "Chart of Accounts" );
        list.add( this );
    }
    
} // End of class ** ChartOfAccounts **
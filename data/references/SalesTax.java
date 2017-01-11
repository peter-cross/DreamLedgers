package data.references;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;

/**
 * Class SalesTax - To create and store Sales Taxes
 * @author Peter Cross
 */
public class SalesTax extends RegistryItem
{
    private static  LinkedHashSet  list; // List of Items
    
    /**
     * Gets string representation of class object
     * @return Sales Tax code
     */
    public String toString()
    {
        return (String) fields.get( "code" );
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
        hdr.width = 50;
        hdr.textValue = (String) fields.get( "code" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name        " );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Rate,%" );
        hdr.attributeName = "rate";
        hdr.valueType = "Number";
        hdr.width = 50;
        hdr.textValue = getFieldNumber( "rate" );
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
        try
        {
            fields.set( "rate", Double.parseDouble( header[0][2] ) );
        }
        catch ( Exception e )
        {
            fields.set( "rate", 0 );
        }
    }

    /**
     * Get List of Sales Taxes in ArrayList
     * @return ArrayList with list of Sales Taxes
     */
    public static LinkedHashSet  getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of Sales Taxes
     * @return Array with List of Sales Taxes
     */
    public static  LinkedHashSet[] createList()
    {
        // Create ArrayList object for List of Sales Taxes
        list = new LinkedHashSet<>();
        
        // Helper object for creating new list elements
        SalesTax tax;
        AssociativeList fields;
        
        tax = new SalesTax();
        fields = tax.getFields();
        fields.set( "code", "GST" );
        fields.set( "name", "Goods Sold Tax" );
        fields.set( "rate", 7 );
        
        tax = new SalesTax();
        fields = tax.getFields();
        fields.set( "code", "PST" );
        fields.set( "name", "Provincial Sales Tax" );
        fields.set( "rate", 5 );
        
        return new LinkedHashSet[] { list };
    }
    
    public SalesTax()
    {
        super( "Sales Tax" );
        list.add( this );
    }
    
    public SalesTax( Stage stage )
    {
        super( stage, "Sales Tax" );
        list.add( this );
    }
    
} // End of class ** SalesTax **
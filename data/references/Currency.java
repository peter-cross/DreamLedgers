package data.references;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;
import static interfaces.Utilities.getListElementBy;

/**
 * Class Currency - to store a list of foreign currencies
 * @author Peter Cross
 */
public class Currency extends RegistryItem
{
    private static  LinkedHashSet list;       // List of Items
    
    /**
     * Gets string representation of class object
     * @return Currency Code
     */
    public String toString()
    {
        return (String) fields.get( "code" );
    }
    
    /**
     * Get Currency object by Currency code
     * @param code Currency Code
     * @return Currency object
     */
    public static Currency getByCode( String code )
    {
        return (Currency) getListElementBy( list, "code", code );
        
    } // End of method ** getByCode **
    
    /**
     * Creates dialog elements for dialog header
     * @return Array of Header dialog elements
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        // Create array for header dialog elements
        DialogElement[][] header = new DialogElement[1][2];
        // Helper object for creating new dialog elements
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "Code" );
        hdr.valueType = "Text";
        hdr.width = 60;
        hdr.textValue = (String) fields.get( "code" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name" );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
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
    }

    /**
     * Get List of Currencies in ArrayList
     * @return ArrayList with list of Currencies
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of Currencies
     * @return Array with List of Currencies
     */
    public static  LinkedHashSet[] createList()
    {
        // Create ArrayList object for List of Currencies
        list = new LinkedHashSet<>();
        
        // Helper object for creating new list elements
        Currency currency;
        AssociativeList fields;
        
        currency = new Currency();
        fields = currency.getFields();
        fields.set( "code", "CAD" );
        fields.set( "name", "Canadian dollar" );
        
        currency = new Currency();
        fields = currency.getFields();
        fields.set( "code", "USD" );
        fields.set( "name", "U.S. dollar" );
        
        currency = new Currency();
        fields = currency.getFields();
        fields.set( "code", "EUR" );
        fields.set( "name", "Euro" );
        
        currency = new Currency();
        fields = currency.getFields();
        fields.set( "code", "JPY" );
        fields.set( "name", "Japanese Yen" );
        
        return new LinkedHashSet[] { list };
    }
    
    public Currency()
    {
        super( "Currency" );
        list.add( this );
    }
    
    public Currency( Stage stage )
    {
        super( stage, "Currency" );
        list.add( this );
    }    
} // Endf class ** Currency **
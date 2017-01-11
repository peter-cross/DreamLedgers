package data.references;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;

/**
 * Class ExchangeRateType - stores types of Currency exchange rates
 * @author Peter Cross
 */
public class ExchangeRateType extends RegistryItem
{
    private static  LinkedHashSet  list;       // List of Items
    
    /**
     * Creates dialog elements for dialog header
     * @return Array of Header dialog elements
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        // Create array for header dialog elements
        DialogElement[][] header = new DialogElement[1][1];
        // Helper object for creating new dialog elements
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "Name" );
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
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
        
        fields.set( "name", header[0][0] );
    }

    /**
     * Get List of Exchange Rate Types in ArrayList
     * @return ArrayList with list of Exchange Rate Types
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of Exchange Rate Types
     * @return Array with List of Exchange Rate Types
     */
    public static  LinkedHashSet[] createList()
    {
        // Create ArrayList object for List of Exchange Rate Types
        list = new LinkedHashSet<>();
        
        // Helper object for creating new list elements
        ExchangeRateType exchRate;
        AssociativeList fields;
        
        exchRate = new ExchangeRateType();
        fields = exchRate.getFields();
        fields.set( "name", "Bank of Canada" );
        
        exchRate = new ExchangeRateType();
        fields = exchRate.getFields();
        fields.set( "name", "Foreign Exchange" );
        
        exchRate = new ExchangeRateType();
        fields = exchRate.getFields();
        fields.set( "name", "CIBC" );
        
        exchRate = new ExchangeRateType();
        fields = exchRate.getFields();
        fields.set( "name", "TD Canada Trust" );
        
        exchRate = new ExchangeRateType();
        fields = exchRate.getFields();
        fields.set( "name", "BMO" );
        
        exchRate = new ExchangeRateType();
        fields = exchRate.getFields();
        fields.set( "name", "Scotia Bank" );
        
        return new LinkedHashSet[] { list };
    }
    
    public ExchangeRateType()
    {
        super( "Exchange Rate Type" );
        list.add( this );
    }
    
    public ExchangeRateType( Stage stage )
    {
        super( stage, "Exchange Rate Type" );
        list.add( this );
    }
} // End of class ** ExchangeRateType **
package data.dimensions;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;

/**
 * Class Location
 * @author Peter Cross
 */
public class Location extends RegistryItem
{
    protected static  LinkedHashSet  list;       // List of Items
    
    /**
     * Creates dialog elements for the form header
     * @return Array of dialog elements for form header
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][5];
        
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "ID  " );
        hdr.valueType = "Number";
        hdr.width = 60;
        hdr.textValue = (String) fields.get( "ID" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name     " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Address         " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "address" );
        header[0][2] = hdr;
    
        hdr = new DialogElement( "Phone" );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "phone" );
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Contact" );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "contact" );
        header[0][4] = hdr;
        
        return header;
    }

    /**
     * Initializes form fields with header and table values
     * @param header Header text values
     * @param table Table text values
     */
    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "ID", header[0][0] );
            fields.set( "name", header[0][1]  );
            fields.set( "address", header[0][2] );
            fields.set( "phone",  header[0][3] );
            fields.set( "contact", header[0][4] );
        }
    }

    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    public static LinkedHashSet[] createList()
    {
        list = new LinkedHashSet<>();
        
        return new LinkedHashSet[] { list };
    }
    
    public Location()
    {
        super( "Location" );
    }
    
    public Location( Stage stage )
    {
        super( stage, "Location" );
        list.add( this );
    }
    
} // End of class ** Location **
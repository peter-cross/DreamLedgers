package data.dimensions;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;

/**
 * Class Employee 
 * @author Peter Cross
 */
public class Employee extends RegistryItem 
{
    protected static  LinkedHashSet list;       // List of Items
    
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][4];
        
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "ID  " );
        hdr.valueType = "Number";
        hdr.width = 60;
        hdr.textValue = (String) fields.get( "iD" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name     " );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Phone" );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "phone" );
        header[0][2] = hdr;
        
        hdr = new DialogElement( "Address         " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "address" );
        header[0][3] = hdr;
    
        return header;
    }

    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "iD", header[0][0] );
            fields.set( "name", header[0][1]  );
            fields.set( "phone",  header[0][2] );
            fields.set( "address", header[0][3] );
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
    
    public Employee()
    {
        super( "Employee" );
    }
    
    public Employee( Stage stage )
    {
        super( stage, "Employee" );
        list.add( this );
    }
    
} // class ** Employee **
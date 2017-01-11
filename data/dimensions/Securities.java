package data.dimensions;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;
import static interfaces.Utilities.getListElementBy;

/**
 *
 * @author Peter Cross
 */
public class Securities extends RegistryItem
{
    private static  LinkedHashSet  list;       // List of Items
    
    public static Securities getByCode( String code )
    {
        return (Securities) getListElementBy( list, "code", code );
    }
    
    public static Securities getByName( String name )
    {
        return (Securities) getListElementBy( list, "name", name );
    }
    
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][6];
        DialogElement hdr = new DialogElement();
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "Code" );
        hdr.valueType = "Number";
        hdr.width = 70;
        hdr.textValue = (String) fields.get( "code" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Security Type" );
        hdr.shortName = "Sec.type";
        hdr.valueType = "List";
        hdr.textChoices = new String[] { "Stock", "Bond", "Mutual Fund" };
        hdr.width = 200;
        hdr.editable = false;
        hdr.textValue = (String) fields.get( "type" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Name             " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        BusinessPartner company = fields.get( "company" );

        hdr = new DialogElement( "Company      " );
        hdr.valueType = "List";
        hdr.list = BusinessPartner.getItemsList();
        hdr.width = 300;
        hdr.editable = false;
        hdr.textValue = ( company != null ? (String) company.getFields().get( "name" )  : "" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Price, $" );
        hdr.valueType = "Number";
        hdr.width = 70;
        hdr.textValue = getFieldNumber( "price" );
        header[0][4] = hdr;
        
        hdr = new DialogElement( "Purch Date" );
        hdr.valueType = "Date";
        hdr.width = 100;
        hdr.textValue = (String) fields.get( "purchDate" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][5] = hdr;
        
        return header;
    }

    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "code", header[0][0] );
            fields.set( "type", header[0][1] );
            fields.set( "name", header[0][2] );
            fields.set( "company", BusinessPartner.getByName( header[0][3] ) );
            try
            {
                fields.set( "price", Double.parseDouble( header[0][4] ) );
            }
            catch ( Exception e ) 
            {}
            fields.set( "purchDate", header[0][5] );
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
    
    public Securities()
    {
        super( "Marketable Security" );
    }
    
    public Securities( Stage stage )
    {
        super( stage, "Marketable Security" );
        list.add( this );
    }
    
} // End of class ** Securities **
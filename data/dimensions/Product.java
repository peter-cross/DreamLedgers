package data.dimensions;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;
import static interfaces.Utilities.getListElementBy;

/**
 * Class Product - what company sells to clients
 * @author Peter Cross
 */
public class Product extends RegistryItem
{
    private static  LinkedHashSet  list;       // List of Items
    
    /**
     * Get Product object by Product code
     * @param code Product Code
     * @return Product object
     */
    public static Product getByCode( String code )
    {
        return (Product) getListElementBy( list, "code", code );
        
    } // End of method ** getByCode **
    
    /**
     * Get Product object by Product name
     * @param name Product name
     * @return Product object
     */
    public static Product getByName( String name )
    {
        return (Product) getListElementBy( list, "name", name );
        
    } // End of method ** getByName **
    
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][4];
        
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "Code" );
        hdr.valueType = "Number";
        hdr.width = 70;
        hdr.textValue = (String) fields.get( "code" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Product Type" );
        hdr.shortName = "Pr.type";
        hdr.valueType = "List";
        hdr.textChoices = new String[] { "Merchandize", "Service" };
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

        hdr = new DialogElement( "Price, $" );
        hdr.valueType = "Number";
        hdr.width = 70;
        hdr.textValue = getFieldNumber( "price" );
        header[0][3] = hdr;
         
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
            try
            {
                fields.set( "price", Double.parseDouble( header[0][3] ) );
            }
            catch ( Exception e ) 
            {}
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
    
    public Product()
    {
        super( "Product" );
    }
    
    public Product( Stage stage )
    {
        super( stage, "Product" );
        list.add( this );
    }
    
} // End of class ** Product **
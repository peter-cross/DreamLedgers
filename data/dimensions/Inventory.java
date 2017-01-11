package data.dimensions;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import data.references.InventoryGroup;
import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;

/**
 * Class Inventory - what company keeps in stock
 * @author Peter Cross
 */
public class Inventory extends RegistryItem
{
    private static  LinkedHashSet  list;       // List of Items
    
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][3];
        
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "Inv.#" );
        hdr.valueType = "Number";
        hdr.width = 70;
        hdr.textValue = (String) fields.get( "invNumber" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name   " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        InventoryGroup grp = (InventoryGroup) fields.get( "inventoryGroup" );
        
        hdr = new DialogElement( "Inv. Group" );
        hdr.valueType = "List";
        hdr.list = InventoryGroup.getItemsList();
        hdr.width = 200;
        hdr.editable = false;
        hdr.textValue = ( grp != null ? (String) grp.getFields().get( "name" ) : "" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        return header;
    }

    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "invNumber", header[0][0] );
            fields.set( "name", header[0][1]  );
            fields.set( "inventoryGroup", InventoryGroup.getByName( header[0][2] ) );
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
    
    public Inventory()
    {
        super( "Inventory Item" );
    }
    
    public Inventory( Stage stage )
    {
        super( stage, "Inventory Item" );
        list.add( this );
    }
    
} // End of class ** Inventory **
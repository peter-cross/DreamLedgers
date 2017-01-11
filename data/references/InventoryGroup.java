package data.references;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;
import static interfaces.Utilities.getListElementBy;

/**
 * Class InventoryGroup
 * @author Peter Cross
 */
public class InventoryGroup extends RegistryItem
{
    private static  LinkedHashSet  list;       // List of Items
    
    /**
     * Get InventoryGroup object by InventoryGroup name
     * @param name InventoryGroup name
     * @return InventoryGroup object
     */
    public static InventoryGroup getByName( String name )
    {
        return (InventoryGroup) getListElementBy( list, "name", name );
    
    } // End of method ** getByCode **
    
    /**
     * Creates dialog elements for dialog header
     * @return Array of Header dialog elements
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        // Create array for header dialog elements
        DialogElement[][] header = new DialogElement[1][5];
        // Helper object for creating new dialog elements
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "ID  " );
        hdr.valueType = "Text";
        hdr.width = 70;
        hdr.textValue = (String) fields.get( "id" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name                 " );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        Unit unit = (Unit) fields.get( "stockUnit" );
        
        hdr = new DialogElement( "Stock Unit" );
        hdr.valueType = "List";
        hdr.list = Unit.getItemsList();
        hdr.width = 70;
        hdr.editable = false;
        hdr.textValue = ( unit != null ? (String) unit.getFields().get( "code" ) : "" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        unit = (Unit) fields.get( "salesUnit" );
        
        hdr = new DialogElement( "Sales Unit" );
        hdr.valueType = "List";
        hdr.list = Unit.getItemsList();
        hdr.width = 70;
        hdr.editable = false;
        hdr.textValue = ( unit != null ? (String) unit.getFields().get( "code" ) : "" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Costing Method" );
        hdr.valueType = "List";
        hdr.textChoices = new String[] { "Specific Identification", "FIFO", "Weighted Average" };
        hdr.width = 200;
        hdr.editable = false;
        hdr.textValue = (String) fields.get( "costingMethod" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][4] = hdr;
        
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
        
        fields.set( "id", header[0][0] );
        fields.set( "name", header[0][1] );
        fields.set( "stockUnit", Unit.getByCode( header[0][2] ) );
        fields.set( "salesUnit", Unit.getByCode( header[0][3] ) );
        fields.set( "costingMethod", header[0][4] );
    }

    /**
     * Get List of Inventory Groups in ArrayList
     * @return ArrayList with list of Currencies
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of Inventory Groups
     * @return Array with List of Inventory Groups
     */
    public static  LinkedHashSet[] createList()
    {
        // Create ArrayList object for List of Inventory Groups
        list = new LinkedHashSet<>();
        
        return new LinkedHashSet[] { list };
    }
    
    public InventoryGroup()
    {
        super( "Inventory Group" );
    }
    
    public InventoryGroup( Stage stage )
    {
        super( stage, "Inventory Group" );
        list.add( this );
    }
    
} // End of class ** InventoryGroup **
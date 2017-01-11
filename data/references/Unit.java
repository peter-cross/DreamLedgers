package data.references;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;
import static interfaces.Utilities.getListElementBy;

/**
 * Class Unit - creates and stores Units of measurement
 * @author Peter Cross
 */
public class Unit extends RegistryItem
{
    private static  LinkedHashSet  list;  // List of Items
    
    /**
     * Gets string representation of class object
     * @return Unit code
     */
    public String toString()
    {
        return (String) fields.get( "code" );
    }
    
    /**
     * Get Unit object by Unit code
     * @param code Unit Code
     * @return Unit object
     */
    public static Unit getByCode( String code )
    {
        return (Unit) getListElementBy( list, "code", code );
        
    } // End of method ** getByCode **
    
    /**
     * Get Unit object by Unit name
     * @param name Unit name
     * @return Unit object
     */
    public static Unit getByName( String name )
    {
        return (Unit) getListElementBy( list, "name", name );
        
    } // End of method ** getByName **
    
    /**
     * Creates dialog elements for dialog header
     * @return Array of Header dialog elements
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][6];
        
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "Code" );
        hdr.valueType = "Text";
        hdr.width = 60;
        hdr.textValue = (String) fields.get( "code" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name      " );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Unit Type" );
        hdr.textChoices = new String[]{ "Weight", "Volume", "Length", "Items" };
        hdr.width = 100;
        hdr.editable = false;
        hdr.textValue = (String) fields.get( "unitType" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        Unit baseUnit = (Unit ) fields.get( "baseUnit" );
        
        hdr = new DialogElement( "Base Unit" );
        hdr.valueType = "List";
        hdr.list = Unit.getItemsList();
        hdr.textValue = ( baseUnit != null ? (String) baseUnit.getFields().get( "code" ) : "" );
        hdr.editable = false;
        hdr.width = 100;
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Rate" );
        hdr.valueType = "Number";
        hdr.width = 60;
        hdr.textValue = getFieldNumber( "rate" );
        header[0][4] = hdr;
        
        hdr = new DialogElement( "Precision" );
        hdr.shortName = "Prec.";
        hdr.valueType = "Number";
        hdr.width = 60;
        hdr.textValue = getFieldNumber( "precision" );
        header[0][5] = hdr;
        
        return header;
        
    } // End of method ** createHeader **

    /**
     * Saves input information into object attributes
     * @param header Array of input from header
     * @param table Array of input from table
     */
    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "code", header[0][0] );
            fields.set( "name", header[0][1] );
            fields.set( "unitType", header[0][2] );
            fields.set( "baseUnit", getByCode( header[0][3] ) );
            try
            {
                fields.set( "rate", Double.parseDouble( header[0][4] ) );
            }
            catch ( Exception e1 ) 
            {}
            
            try
            {
                fields.set( "precision", Integer.parseInt( header[0][5] ) );	
            }
            catch ( Exception e2 ) 
            {}
        }
    } // End of method ** init **

    /**
     * Get List of Units in ArrayList
     * @return ArrayList with list of Units
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of Units
     * @return Array with List of Units
     */
    public static  LinkedHashSet[] createList()
    {
        // Create ArrayList object for List of Units
        list = new LinkedHashSet<>();
        
        // Helper object for creating new list elements
        Unit unit;
        AssociativeList fields;
        
        unit = new Unit();
        fields = unit.getFields();
        fields.set( "code", "g" );
        fields.set( "name", "gram" );
        fields.set( "unitType", "Weight" );
        
        unit = new Unit();
        fields = unit.getFields();
        fields.set( "code", "mg" );
        fields.set( "name", "milligram" );
        fields.set( "unitType", "Weight" );
        fields.set( "baseUnit", getByCode( "g" ) );
        fields.set( "rate", 0.001 );
        
        unit = new Unit();
        fields = unit.getFields();
        fields.set( "code", "oz" );
        fields.set( "name", "ounce" );
        fields.set( "baseUnit", getByCode( "g" ) );
        fields.set( "rate", 28.3495 );
        fields.set( "unitType", "Weight" );
        
        unit = new Unit();
        fields = unit.getFields();
        fields.set( "code", "lb" );
        fields.set( "name", "pound" );
        fields.set( "baseUnit", getByCode( "g" ) );
        fields.set( "rate", 453.592 );
        fields.set( "unitType", "Weight" );
        
        unit = new Unit();
        fields = unit.getFields();
        fields.set( "code", "kg" );
        fields.set( "name", "kilogram" );
        fields.set( "baseUnit", getByCode( "g" ) );
        fields.set( "rate", 1000 );
        fields.set( "unitType", "Weight" );
        
        unit = new Unit();
        fields = unit.getFields();
        fields.set( "code", "L" );
        fields.set( "name", "Littres" );
        fields.set( "unitType", "Volume" );
        
        unit = new Unit();
        fields = unit.getFields();
        fields.set( "code", "mL" );
        fields.set( "name", "miliLittres" );
        fields.set( "baseUnit", getByCode( "L" ) );
        fields.set( "rate", 0.001 );
        fields.set( "unitType", "Volume" );
        
        unit = new Unit();
        fields = unit.getFields();
        fields.set( "code", "item" );
        fields.set( "name", "item" );
        fields.set( "unitType", "Items" );
        
        return new LinkedHashSet[] { list };
    }
    
    public Unit()
    {
        super( "Unit" );
        list.add( this );
    }
    
    public Unit( Stage stage )
    {
        super( stage, "Unit" );
        list.add( this );
    }
    
} // End of class ** Unit **
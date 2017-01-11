package foundation;

import javafx.stage.Stage;

import forms.DialogElement;
import forms.OneColumnDialog;
import forms.TableElement;
import forms.TableOutput;
import interfaces.Utilities;

/**
 * Class RegistryItem - To create Items for Registry
 * @author Peter Cross
 */
abstract public class RegistryItem extends Item implements Utilities
{
    protected Stage             stage;          // Current Stage object
    protected DialogElement[][] header;         // Header element parameters
    protected TableElement[][]  table;          // Table column parameters 
    protected String[]          headerTabs;     // Header tabs
    protected String[]          tableTabs;      // Table tabs
    protected AssociativeList	attributesList; // Attributes list   
    protected TableOutput       output;         // Output object
    protected AssociativeList   fields;         // Items' fields
    
    public String ITEM_TYPE = "";
    
    private static String inheritedClass;
    
    // Creates array of header element parameters
    abstract protected DialogElement[][] createHeader();
    
    // Creates array ob table element parameters
    protected TableElement[][] createTable() 
    {
        return null;
    }
    
    // Assigns header and table values to corresponding object properties
    abstract protected void init( String[][] header, String[][][] table );
    
    // Displays dialog form
    public void display( ) 
    {
        attributesList.set( "header", createHeader() );
        
        output = new TableOutput();
        output.header = new OneColumnDialog( this, ITEM_TYPE ).result();
        
        init( output.header, null );
        
        attributesList.set( "output", output );
    }
    
    // Returns output object
    public TableOutput getOutput( String itemType ) 
    {
        TableOutput result = new TableOutput();
        result.header = new OneColumnDialog( this, itemType ).result();
        
        init( result.header, null );
        
        return result;
    }
    
    /**
     * Returns attributes list for current object
     * @return Attributes list
     */
    public AssociativeList getAttributesList()
    {
        return attributesList;
    }
    
    /**
     * Gets Registry Item's fields list
     * @return Fields list
     */
    public AssociativeList getFields()
    {
        return fields;
    }
    
    /**
     * Gets string representation of class object
     * @return Registry Item's name
     */
    public String toString()
    {
        return (String) fields.get( "name" );
    }
    
    /*                        Constructors                                                                                  */
    /************************************************************************************************************************/
    public RegistryItem()
    {
        this.header = createHeader();
        this.table = createTable();
        
        attributesList = new AssociativeList();
        attributesList.set( "header", header );
        attributesList.set( "table", table );    
        
        inheritedClass = this.getClass().getName();
    }
    
    public RegistryItem( Stage stage )
    {
        this();
        
        this.stage = stage;
        attributesList.set( "stage", stage );
        
        this.output = getOutput( "" );
        attributesList.set( "output", output );
    }
    
    public RegistryItem( String itemType )
    {
        ITEM_TYPE = itemType;
        
        this.header = createHeader();
        this.table = createTable();
        
        attributesList = new AssociativeList();
        attributesList.set( "header", header );
        attributesList.set( "table", table );    
        
        inheritedClass = this.getClass().getName();
    }
    
    public RegistryItem( Stage stage, String itemType )
    {
        this( itemType );
        
        this.stage = stage;
        attributesList.set( "stage", stage );
        
        this.output = getOutput( itemType );
        attributesList.set( "output", output );
    }
    
    public RegistryItem( Stage stage, String[] headerTabs, String itemType )
    {
        this( stage, itemType );
        
        this.headerTabs = headerTabs;
        attributesList.set( "headerTabs", headerTabs );
    }
    
    public RegistryItem( Stage stage, String[] headerTabs, String[] tableTabs, String itemType )
    {
        this( stage, headerTabs, itemType );
        
        this.tableTabs = tableTabs;
        attributesList.set( "tableTabs", tableTabs );
    }
    
    public RegistryItem( String[][] header, String[][][] table )
    {
        init( header, table );
    }
    
} // End of interface ** RegistryItem ** 
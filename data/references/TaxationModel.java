package data.references;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import forms.OneColumnTableDialog;
import forms.TableElement;
import forms.TableOutput;
import foundation.AssociativeList;
import foundation.RegistryItem;

/**
 * Class TaxationModel - For Sales and Purchase complicated taxation
 * @author Peter Cross
 */
public class TaxationModel extends RegistryItem
{
    protected static  LinkedHashSet list;       // List of Items
    
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][1];
        
        if ( fields == null )
            fields = new AssociativeList();
        
        DialogElement hdrEl = new DialogElement( "Name" );
        hdrEl.valueType = "Text";
        hdrEl.width = 200;
        hdrEl.textValue = (String) fields.get( "name" );
        hdrEl.validation = validationCode( hdrEl.labelName );
        header[0][0] = hdrEl;
        
        return header;
    }

    @Override
    protected TableElement[][] createTable()
    {
        TableElement[][] table = new TableElement[1][2];
        TableElement tblEl;
        
        tblEl = new TableElement( "Tax         " );
        tblEl.width = 200;
        tblEl.editable = false;
        tblEl.textValue = (String[]) fields.get( "tax" );
        tblEl.valueType = "List";
        tblEl.list = SalesTax.getItemsList();
        table[0][0] = tblEl;
        
        tblEl = new TableElement( "Calc.Type" );
        tblEl.width = 140;
        tblEl.editable = false;
        tblEl.textValue = (String[]) fields.get( "calcType" );
        tblEl.textChoices = new String[] { "Include", "Add" };
        table[0][1] = tblEl;
        
        return table;
    }
    
    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        fields.set( "name", header[0][0] );  
        
        if ( table != null && table.length > 0 )
        {
            int numTaxes = table[0].length;
            
            String[]    taxes = new String[numTaxes],
                        calcTypes = new String[numTaxes];
            
            for ( int i = 0; i < numTaxes; i++ )
            {
                taxes[i] = table[0][i][0];
                calcTypes[i] = table[0][i][1];
            }
            
            fields.set( "tax", taxes );
            fields.set( "calcType", calcTypes );
        }
    }
    
    @Override
    public void display()
    {
        // Create header elements array and add it to attributes list
        attributesList.set( "header", createHeader() );
        // Create table elements array and add it to attributes list
        attributesList.set( "table", createTable() );
        
        // Display one-column table dialog form and get the results of input or editing
        output =  new OneColumnTableDialog( this, "Taxation Model", 5 ).result();
        
        // Assign the returned results to object properties
        init( output.header, output.table );
        
        // Add output results to attributes list
        attributesList.set( "output", output );
    }
    
    @Override
    public TableOutput getOutput( String itemType )
    {
        // Display input form and get the results of input
        TableOutput result =  new OneColumnTableDialog( this, itemType, 5 ).result();
        
        // Assign the input rsults to object properties
        init( result.header, result.table );
        
        // Return the input result
        return result;
        
    } // End of method ** getOutput **
    
    /**
     * Get List of Taxation Models in ArrayList
     * @return ArrayList with list of Taxation Models
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    public static  LinkedHashSet[] createList()
    {
        // Create ArrayList object for List of Taxation Models
        list = new LinkedHashSet<>();
        return new LinkedHashSet[] { list };
    }
     
    public TaxationModel()
    {
        super( "Taxation Model" );
    }
    
    public TaxationModel( Stage stage )
    {
        super( stage, "Taxation Model" );
        list.add( this );
    }
     
} // End of class ** TaxationModel **
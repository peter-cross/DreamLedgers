package data.dimensions;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;

/**
 * Class Expense - To create and store expenses
 * @author Peter Cross
 */
public class Expense extends RegistryItem
{
    private static  LinkedHashSet  list;       // List of Items
    
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][3];
        
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "Code" );
        hdr.valueType = "Number";
        hdr.width = 70;
        hdr.textValue = (String) fields.get( "code" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name   " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Category" );
        hdr.valueType = "List";
        hdr.textChoices = new String[] { "R & D",
                                         "Design",
                                         "Purchasing",
                                         "Production",
                                         "Marketing",
                                         "Distribution",
                                         "Customer Service",
                                         "Administration",
                                         "Depreciation" };
        hdr.width = 200;
        hdr.editable = false;
        hdr.textValue = (String) fields.get( "category" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        return header;
    }

    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "code", header[0][0] );
            fields.set( "name", header[0][1]  );
            fields.set( "category", header[0][2] );
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
    
    public Expense()
    {
        super( "Expense" );
    }
    
    public Expense( Stage stage )
    {
        super( stage, "Expense" );
        list.add( this );
    }
    
} // End of class ** Expense **
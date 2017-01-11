package data;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import static interfaces.Utilities.createDataClass;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;

/**
 * Class ReverseJournalEntry - To create reversing transactions journal entries
 * @author Peter Cross
 */
public class ReverseJournalEntry extends RegistryItem
{
    private static LinkedHashSet[] list;       // Array of reversing journal entries with dimension for Legal Entities
    private static String[]        tableTabs;  // Tabs for Legal Entities
    
        
    @Override
    protected DialogElement[][] createHeader() 
    {
        // Header Elements for each tab
        DialogElement[][] header = new DialogElement[1][3];
        // Helper object for creating new dialog elements
        DialogElement hdr;
        
        // If it's creating a new RegistryItem or 1st access to RegistryItem
        if ( fields == null )
            fields = new AssociativeList();
        
        // Figure out where to get tab num from
        int curTab = 0;

        // 1st header element
        hdr = new DialogElement( "Date  " );
        hdr.valueType = "Date";
        hdr.textValue = (String) fields.get( "date" );
        header[0][0] = hdr;
        
        // 2nd header element
        hdr = new DialogElement( "Journal Entry" );
        hdr.valueType = "List";
        hdr.list = JournalEntry.getItemsList()[curTab];
        hdr.editable = false;
        hdr.width = 350;
        hdr.textValue = (String) fields.get( "journalEntry" );
        header[0][1] = hdr;
        
        // 3rd header element
        hdr = new DialogElement( "Note           " );
        hdr.valueType = "Text";
        hdr.editable = true;
        hdr.width = 350;
        hdr.textValue = (String) fields.get( "note" );
        header[0][2] = hdr;
        
        return header;
    }

    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "date", header[0][0] );
            fields.set( "journalEntry", header[0][1] );
            fields.set( "note", header[0][2] );
        }
    }
    
    public static LinkedHashSet[] getItemsList()
    {
        return list;
    }
    
    public static LinkedHashSet[] createList()
    {
        Class c = createDataClass( "LegalEntity" );
        
        try
        {
            LinkedHashSet entities = ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0];
            
            // Create array for Legal Entities list with size equal to number of Legal Entities
            list = new LinkedHashSet[ entities.size() ];
        }
        catch ( Exception e )
        {
            list = new LinkedHashSet[1];
        }
        
        for ( int i = 0; i < list.length; i++ )
            list[i] = new LinkedHashSet<>();
        
        return list;
    }
    
    
    public ReverseJournalEntry()
    {
        super( "Reverse Journal Entry" );
    }
    
    public ReverseJournalEntry( int legalEntity )
    {
        this();
        list[legalEntity].add( this );
    }
    
    public ReverseJournalEntry( Stage stage )
    {
        super( stage, null, null, "Reverse Journal Entry" );
        list[0].add( this );
    }
    
     public ReverseJournalEntry( Stage stage, int legalEntity )
    {
        super( stage, null, null, "Reverse Journal Entry" );
        list[legalEntity].add( this );
    }
    
} // End of class ** ReverseJournalEntry **
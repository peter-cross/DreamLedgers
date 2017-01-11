package data.dimensions;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;

/**
 * Class Warranty
 * @author Peter Cross
 */
public class Warranty extends RegistryItem
{
    private static  LinkedHashSet  list;  // List of Items
    
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][9];
        
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "Warranty #" );
        hdr.valueType = "Number";
        hdr.width = 80;
        hdr.textValue = (String) fields.get( "warrantyNumber" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        BusinessPartner customer = (BusinessPartner) fields.get( "customer" );
        
        hdr = new DialogElement( "Customer           " );
        hdr.valueType = "List";
        hdr.list = BusinessPartner.getItemsList();
        hdr.textValue = ( customer != null ? (String) customer.getFields().get( "name" ) : "" );
        hdr.editable = false;
        hdr.width = 200;
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        LegalEntity legalEntity = (LegalEntity) fields.get( "legalEntity" );
        
        hdr = new DialogElement( "Legal Entity" );
        hdr.valueType = "List";
        hdr.list = LegalEntity.getItemsList();
        hdr.textValue = ( legalEntity != null ? (String) legalEntity.getFields().get( "name" ) : "" );
        hdr.editable = false;
        hdr.width = 200;
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        Product product = (Product) fields.get( "product" );
        
        hdr = new DialogElement( "Product       " );
        hdr.valueType = "List";
        hdr.list = Product.getItemsList();
        hdr.textValue = ( product != null ? (String) product.getFields().get( "name" ) : "" );
        hdr.editable = false;
        hdr.width = 200;
        hdr.validation = validationCode( hdr.labelName );
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Serial Number" );
        hdr.shortName = "Serial #";
        hdr.valueType = "Number";
        hdr.width = 105;
        hdr.textValue = (String) fields.get( "serialNumber" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][4] = hdr;
        
        hdr = new DialogElement( "Purchase Date" );
        hdr.shortName = "Prch.date";
        hdr.valueType = "Date";
        hdr.width = 105;
        hdr.textValue = (String) fields.get( "purchaseDate" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][5] = hdr;
        
        hdr = new DialogElement( "Term, months" );
        hdr.valueType = "Number";
        hdr.textValue = (String) fields.get( "term" );
        hdr.editable = false;
        hdr.width = 80;
        header[0][6] = hdr;
        
        hdr = new DialogElement( "Amount, $   " );
        hdr.shortName = "Amount";
        hdr.valueType = "Number";
        hdr.width = 80;
        hdr.textValue = getFieldNumber( "amount" );
        header[0][7] = hdr;
        
        hdr = new DialogElement( "Notes      " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "notes" );
        header[0][8] = hdr;
        
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
            fields.set( "warrantyNumber", header[0][0] );
            fields.set( "customer", BusinessPartner.getByName( header[0][1] ) );
            fields.set( "legalEntity", LegalEntity.getByName( header[0][2] ) );
            fields.set( "product", Product.getByName( header[0][3] ) );
            fields.set( "serialNumber", header[0][4] );
            fields.set( "purchaseDate", header[0][5] );
            fields.set( "term", header[0][6] );
            try
            {
                fields.set( "amount", Double.parseDouble( header[0][7] ) );
            }
            catch ( Exception e ) 
            {}
            fields.set( "notes", header[0][8] );  
        }
    } // End of method ** init **

    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    public static LinkedHashSet[] createList()
    {
        list = new LinkedHashSet<>();
        
        return new LinkedHashSet[] { list };
    }
    
    public Warranty()
    {
        super( "Warranty" );
    }
    
    public Warranty( Stage stage )
    {
        super( stage, "Warranty" );
        list.add( this );
    }
} // End of class ** Warranty **
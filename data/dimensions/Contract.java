package data.dimensions;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import data.references.Currency;
import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;

/**
 * Class Contract
 * @author Peter Cross
 */
public class Contract extends RegistryItem
{
    private static  LinkedHashSet  list;  // List of Items
    
    /**
     * Creates dialog elements for dialog header
     * @return Array of Header dialog elements
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][7];
        
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "Contract #" );
        hdr.valueType = "Number";
        hdr.width = 80;
        hdr.textValue = (String) fields.get( "contractNumber" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        BusinessPartner businessPartner = (BusinessPartner) fields.get( "businessPartner" );
        
        hdr = new DialogElement( "Business Partner" );
        hdr.valueType = "List";
        hdr.list = BusinessPartner.getItemsList();
        hdr.textValue = ( businessPartner != null ? (String) businessPartner.getFields().get( "name" ) : "" );
        hdr.editable = false;
        hdr.width = 200;
        header[0][1] = hdr;
        
        LegalEntity legalEntity = (LegalEntity) fields.get( "legalEntity" );
        
        hdr = new DialogElement( "Legal Entity" );
        hdr.valueType = "List";
        hdr.list = LegalEntity.getItemsList();
        hdr.textValue = ( legalEntity != null ? (String) legalEntity.getFields().get( "name" ) : "" );
        hdr.editable = false;
        hdr.width = 200;
        header[0][2] = hdr;
        
        hdr = new DialogElement( "Expiry Date" );
        hdr.shortName = "Exp.date";
        hdr.valueType = "Date";
        hdr.width = 105;
        hdr.textValue = (String) fields.get( "expiryDate" );
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Amount, $   " );
        hdr.shortName = "Amount";
        hdr.valueType = "Number";
        hdr.width = 80;
        hdr.textValue = getFieldNumber( "amount" );
        header[0][4] = hdr;
        
        Currency currency = (Currency) fields.get( "currency" );
        
        hdr = new DialogElement( "Currency" );
        hdr.valueType = "List";
        hdr.list = Currency.getItemsList();
        hdr.textValue = ( currency != null ? (String) currency.getFields().get( "code" ) : "" );
        hdr.editable = false;
        hdr.width = 80;
        header[0][5] = hdr;
        
        hdr = new DialogElement( "Notes                " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "notes" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][6] = hdr;
        
        return header;
        
    } // End of method ** createHeader **

    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "contractNumber", header[0][0] );
            fields.set( "businessPartner", BusinessPartner.getByName( header[0][1] ) );
            fields.set( "legalEntity", LegalEntity.getByName( header[0][2] ) );
            fields.set( "expiryDate", header[0][3] );
            try
            {
                fields.set( "amount", Double.parseDouble( header[0][4] ) );
            }
            catch ( Exception e ) 
            {}
            fields.set( "currency", Currency.getByCode( header[0][5] ) );
            fields.set( "notes", header[0][6] );
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
    
    public Contract()
    {
        super( "Contract" );
    }
    
    public Contract( Stage stage )
    {
        super( stage, "Contract" );
        list.add( this );
    }
    
} // End of class ** Contract **
package data.dimensions;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;
import static interfaces.Utilities.getListElementBy;

/**
 * Class BusinessPartner
 * @author Peter Cross
 */
public class BusinessPartner extends RegistryItem
{
    protected static  LinkedHashSet list;       // List of Items
    
    /**
     * Get BusinessPartner object by  name
     * @param name Business Partner name
     * @return BusinessPartner object
     */
    public static BusinessPartner getByName( String name )
    {
        return (BusinessPartner) getListElementBy( list, "name", name );
        
    } // End of method ** getByName **
    
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][5];
        
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "ID  " );
        hdr.valueType = "Number";
        hdr.width = 60;
        hdr.textValue = (String) fields.get( "iD" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name     " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Phone" );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "phone" );
        header[0][2] = hdr;
        
        hdr = new DialogElement( "Contact" );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "contact" );
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Address         " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "address" );
        header[0][4] = hdr;
    
        return header;
    }

    @Override
    protected void init(String[][] header, String[][][] table) 
    {
        if ( header.length > 0 )
        {
            fields.set( "iD", header[0][0] );
            fields.set( "name", header[0][1]  );
            fields.set( "phone",  header[0][2] );
            fields.set( "contact", header[0][3] );
            fields.set( "address", header[0][4] );
        }
    }

    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    public static LinkedHashSet[] createList()
    {
        list = new LinkedHashSet<>();
        BusinessPartner businessPartner;
        AssociativeList fields;
        
        businessPartner = new BusinessPartner();
        list.add( businessPartner );
        fields = businessPartner.getFields();
        fields.set( "iD" , "001" );
        fields.set( "name" , "Sparta Corporation" );
        fields.set( "phone" , "1-416-593-9597" );
        fields.set( "contact" , "Ian Scherb" );
        fields.set( "address" , "15 Bloor Street, Toronto" );
        
        businessPartner = new BusinessPartner();
        list.add( businessPartner );
        fields = businessPartner.getFields();
        fields.set( "iD" , "002" );
        fields.set( "name" , "Note Leasing" );
        fields.set( "phone" , "1-514-936-9353" );
        fields.set( "contact" , "Craig Miles" );
        fields.set( "address" , "184 St.Catherins Street, Montreal" );
        
        return new LinkedHashSet[] { list };
    }
    
    
    public BusinessPartner()
    {
        super( "Business Partner" );
    }
    
    public BusinessPartner( Stage stage )
    {
        super( stage, "Business Partner" );
        list.add( this );
    }
    
} // End of class ** BusinessPartner **
package data.dimensions;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import data.references.ChartOfAccounts;
import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;
import static interfaces.Utilities.getListElementBy;

/**
 * Class LegalEntity 
 * @author Peter Cross
 *
 */
public class LegalEntity extends RegistryItem
{
    protected static  LinkedHashSet  list;       // List of Items
    
    /**
     * Get LegalEntity object by Legal Entity name
     * @param name Legal Entity name
     * @return LegalEntity object
     */
    public static LegalEntity getByName( String name )
    {
        return (LegalEntity) getListElementBy( list, "name", name );
        
    } // End of method ** getByName **
    
    /**
     * Creates header element settings of the form
     * @return array of header elements settings
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][10];
        
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "ID  " );
        hdr.attributeName = "iD";
        hdr.valueType = "Text";
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
        
        hdr = new DialogElement( "Legal Name   " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "legalName" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        hdr = new DialogElement( "Phone      " );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "phone" );
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Contact" );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "contact" );
        header[0][4] = hdr;
        
        hdr = new DialogElement( "Address           " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "address" );
        header[0][5] = hdr;
        
        hdr = new DialogElement( "Foundation Date" );
        hdr.valueType = "Date";
        hdr.width = 105;
        hdr.textValue = (String) fields.get( "foundationDate" );
        header[0][6] = hdr;
        
        ChartOfAccounts chart = (ChartOfAccounts) fields.get( "finChart" );
        
        hdr = new DialogElement( "Fin Chart of Accounts" );
        hdr.attributeName = "finChart";
        hdr.shortName = "Fin Chart";
        hdr.valueType = "List";
        hdr.list = ChartOfAccounts.getItemsList();
        hdr.width = 200;
        hdr.editable = false;
        hdr.textValue = ( chart != null ? (String) chart.getFields().get( "name" )  : "" );
        header[0][7] = hdr;
    
        chart = (ChartOfAccounts) fields.get( "mngmChart" );
        
        hdr = new DialogElement( "Mngm Chart of Accounts" );
        hdr.attributeName = "mngmChart";
        hdr.shortName = "Mngm Chart";
        hdr.valueType = "List";
        hdr.list = ChartOfAccounts.getItemsList();
        hdr.width = 200;
        hdr.editable = false;
        hdr.textValue = ( chart != null ? (String) chart.getFields().get( "name" )  : "" );
        header[0][8] = hdr;
    
        chart = (ChartOfAccounts) fields.get( "bdgtChart" );
        
        hdr = new DialogElement( "Bdgt Chart of Accounts" );
        hdr.attributeName = "bdgtChart";
        hdr.shortName = "Bdgt Chart";
        hdr.valueType = "List";
        hdr.list = ChartOfAccounts.getItemsList();
        hdr.width = 200;
        hdr.editable = false;
        hdr.textValue = ( chart != null ? (String) chart.getFields().get( "name" )  : "" );
        header[0][9] = hdr;
    
        return header;
    }

    /**
     * Initializes fields of the form with given values of header and table
     * @param header Text values of the header
     * @param table Text values of table part
     */
    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "iD",   header[0][0] );
            fields.set( "name", header[0][1]  );
            fields.set( "legalName", header[0][2]  );
            fields.set( "phone",   header[0][3] );
            fields.set( "contact", header[0][4] );
            fields.set( "address", header[0][5] );
            fields.set( "foundationDate", header[0][6] );
            fields.set( "finChart",  ChartOfAccounts.getByName( header[0][7] ) );
            fields.set( "mngmChart", ChartOfAccounts.getByName( header[0][8] ) );
            fields.set( "bdgtChart", ChartOfAccounts.getByName( header[0][9] ) );
        }
    }

    /**
     * Returns the list of stored items
     * @return List of stored items
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    /**
     * Creates list object to store items and adds hard-coded items
     * @return Created list
     */
    public static LinkedHashSet[] createList()
    {
        list = new LinkedHashSet<>();
        
        LegalEntity entity;
        AssociativeList fields;
        ChartOfAccounts chart;
        
        entity = new LegalEntity();
        list.add( entity );
        fields = entity.getFields();
        fields.set( "iD", "001" );
        fields.set( "name", "Dandelion" );
        fields.set( "legalName", "Dandeloin Enterprise Inc."  );
        fields.set( "phone", "1-604-888-5665" );
        fields.set( "contact", "Peter Cross" );
        fields.set( "address", "1155 Whyte Ave, Vancouver, BC" );
        chart = ChartOfAccounts.getByCode( "IAS" );
        fields.set( "finChart", chart );
        fields.set( "mngmChart", chart );
        chart = ChartOfAccounts.getByCode( "BDG" );
        fields.set( "bdgtChart", chart );
        
        entity = new LegalEntity();
        list.add( entity );
        fields = entity.getFields();
        fields.set( "iD", "002" );
        fields.set( "name", "Spartan" );
        fields.set( "legalName", "Spartan Group Inc."  );
        fields.set( "phone", "1-604-777-0100" );
        fields.set( "contact", "Ray Ean" );
        fields.set( "address", "1181 Woodmills Trail, Vancouver, BC" );
        chart = ChartOfAccounts.getByCode( "IAS" );
        fields.set( "finChart", chart );
        chart = ChartOfAccounts.getByCode( "MGM" );
        fields.set( "mngmChart", chart );
        chart = ChartOfAccounts.getByCode( "BDG" );
        fields.set( "bdgtChart", chart );
        
        entity = new LegalEntity();
        list.add( entity );
        fields = entity.getFields();
        fields.set( "iD", "003" );
        fields.set( "name", "Depeche" );
        fields.set( "legalName", "Depeche Corp. Ltd."  );
        fields.set( "phone", "1-604-416-8201" );
        fields.set( "contact", "Leo Force" );
        fields.set( "address", "2293 South Hwy, Vancouver, BC" );
        chart = ChartOfAccounts.getByCode( "ASPE" );
        fields.set( "finChart", chart );
        chart = ChartOfAccounts.getByCode( "MGM" );
        fields.set( "mngmChart", chart );
        chart = ChartOfAccounts.getByCode( "BDG" );
        fields.set( "bdgtChart", chart );
        
        entity = new LegalEntity();
        list.add( entity );
        fields = entity.getFields();
        fields.set( "iD", "004" );
        fields.set( "name", "Centurion" );
        fields.set( "legalName", "Centurion Inc."  );
        fields.set( "phone", "1-778-780-1101" );
        fields.set( "contact", "David Reilly" );
        fields.set( "address", "1116 172 St., Vancouver, BC" );
        chart = ChartOfAccounts.getByCode( "MGM" );
        fields.set( "finChart", chart );
        fields.set( "mngmChart", chart );
        chart = ChartOfAccounts.getByCode( "BDG" );
        fields.set( "bdgtChart", chart );
        
        return new LinkedHashSet[] { list };
    }
    
    public LegalEntity()
    {
        super( "Legal Entity" );
    }
    
    public LegalEntity( Stage stage )
    {
        super( stage, "Legal Entity" );
        list.add( this );
    }

} // End of class ** LegalEntity **
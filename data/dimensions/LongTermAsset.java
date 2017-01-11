package data.dimensions;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;

/**
 * Class LongTermAsset - Fixed Assets and Intangible Assets
 * @author Peter Cross
 */
public class LongTermAsset extends RegistryItem
{
    private static  LinkedHashSet  list;       // List of Items
    
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][9];
        
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "Asset #" );
        hdr.valueType = "Number";
        hdr.width = 70;
        hdr.textValue = (String) fields.get( "assetNumber" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name               " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = (String) fields.get( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Serial #" );
        hdr.valueType = "Text";
        hdr.width = 150;
        hdr.textValue = (String) fields.get( "serialNumber" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        hdr = new DialogElement( "Asset Type" );
        hdr.valueType = "List";
        hdr.textChoices = new String[] { "Fixed Asset", "Intangible Asset" };
        hdr.width = 150;
        hdr.editable = false;
        hdr.textValue = (String) fields.get( "assetType" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Acquisition Date" );
        hdr.valueType = "Date";
        hdr.width = 105;
        hdr.textValue = (String) fields.get( "acquisitionDate" );
        header[0][4] = hdr;
        
        hdr = new DialogElement( "Useful life, years" );
        hdr.shortName = "Usfl.Life";
        hdr.valueType = "Number";
        hdr.width = 40;
        hdr.textValue = getFieldNumber( "usefulLife" );
        header[0][5] = hdr;
         
        hdr = new DialogElement( "Depreciation Method" );
        hdr.shortName = "Depr.Mthd";
        hdr.valueType = "List";
        hdr.textChoices = new String[] { "Straight Line", "Declining Balance", "Units of Production" };
        hdr.width = 150;
        hdr.editable = false;
        hdr.textValue = (String) fields.get( "depreciationMetod" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][6] = hdr;
        
        hdr = new DialogElement( "Salvage Value, $" );
        hdr.shortName = "Slvg.Value";
        hdr.valueType = "Number";
        hdr.width = 70;
        hdr.textValue = getFieldNumber( "salvageValue" );
        header[0][7] = hdr;
        
        /*
        hdr = new DialogElement( "Service Date" );
        hdr.valueType = "Date";
        hdr.width = 105;
        hdr.textValue = (String) fields.get( "serviceDate" );
        header[0][8] = hdr;
        */
        
        return header;
    }

    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "assetNumber", header[0][0] );
            fields.set( "name", header[0][1] );
            fields.set( "serialNumber", header[0][2] );
            fields.set( "assetType", header[0][3] );
            fields.set( "acquisitionDate", header[0][4] );
            try
            {
                fields.set( "usefulLife", Double.parseDouble( header[0][5] ) );
            }
            catch ( Exception e ) 
            {}
            fields.set( "depreciationMetod", header[0][6] );
            /*
            fields.set( "serviceDate", header[0][7] );
            try
            {
                fields.set( "salvageValue", Double.parseDouble( header[0][8] ) );
            }
            catch ( Exception e ) 
            {}
            */
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
    
    public LongTermAsset()
    {
        super( "Long Term Asset" );
    }
    
    public LongTermAsset( Stage stage )
    {
        super( stage, "Long Term Asset" );
        list.add( this );
    }
    
} // End of class ** LongTermAsset **
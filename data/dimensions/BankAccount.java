package data.dimensions;

import javafx.stage.Stage;
import java.util.LinkedHashSet;

import data.references.Currency;
import forms.DialogElement;
import foundation.AssociativeList;
import foundation.RegistryItem;
import static interfaces.Utilities.getListElementBy;

/**
 * Class BankAccount
 * @author Peter Cross
 */
public class BankAccount extends RegistryItem
{
    private static  LinkedHashSet  list;  // List of Items
    
    /**
     * Gets string representation of class object
     * @return BankAccount name
     */
    public String toString()
    {
        return (String) fields.get( "acctName" );
    }
    
    /**
     * Get BankAccount object by Unit name
     * @param name Cash Account name
     * @return BankAccount object
     */
    public static BankAccount getByname( String name )
    {
        return (BankAccount) getListElementBy( list, "acctName", name );
        
    } // End of method ** getByName **
         
    /**
     * Creates dialog elements for dialog header
     * @return Array of Header dialog elements
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][8];
        
        DialogElement hdr;
        
        if ( fields == null )
            fields = new AssociativeList();
        
        hdr = new DialogElement( "Acct Name     " );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "acctName" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Account #" );
        hdr.attributeName = "acctNumber";
        hdr.valueType = "Text";
        hdr.width = 110;
        hdr.textValue = (String) fields.get( "acctNumber" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Bank Code" );
        hdr.valueType = "Text";
        hdr.width = 70;
        hdr.textValue = (String) fields.get( "bankCode" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        hdr = new DialogElement( "Transit #" );
        hdr.attributeName = "transitNumber";
        hdr.valueType = "Text";
        hdr.width = 70;
        hdr.textValue = (String) fields.get( "transitNumber" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Bank Name          " );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = (String) fields.get( "bankName" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][4] = hdr;
        
        Currency currency = (Currency) fields.get( "currency" );
        
        hdr = new DialogElement( "Currency" );
        hdr.valueType = "List";
        hdr.list = Currency.getItemsList();
        hdr.textValue = ( currency != null ? (String) currency.getFields().get( "code" ) : "" );
        hdr.editable = false;
        hdr.width = 70;
        header[0][5] = hdr;
        
        hdr = new DialogElement( "Account Type" );
        hdr.valueType = "List";
        hdr.textChoices = new String[] { "Cash", "Investment", "Credit", "Loan" };
        hdr.textValue = (String) fields.get( "accountType" );
        hdr.editable = false;
        hdr.width = 110;
        header[0][6] = hdr;
        
        LegalEntity legalEntity = (LegalEntity) fields.get( "legalEntity" );
        
        hdr = new DialogElement( "Legal Entity" );
        hdr.valueType = "List";
        hdr.list = LegalEntity.getItemsList();
        hdr.textValue = ( legalEntity != null ? (String) legalEntity.getFields().get( "name" ) : "" );
        hdr.editable = false;
        hdr.width = 200;
        header[0][7] = hdr;
        
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
            fields.set( "acctName", header[0][0] );
            fields.set( "acctNumber", header[0][1] );
            fields.set( "bankCode", header[0][2] );
            fields.set( "transitNumber", header[0][3] );
            fields.set( "bankName", header[0][4] );
            fields.set( "currency", Currency.getByCode( header[0][5] ) );
            fields.set( "accountType", header[0][6] );
            fields.set( "legalEntity", LegalEntity.getByName( header[0][7] ) );
        }
    } // End of method ** init **

    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    public static LinkedHashSet[] createList()
    {
        list = new LinkedHashSet<>();
        BankAccount bankAccont;
        AssociativeList fields;
        
        bankAccont = new BankAccount();
        list.add( bankAccont );
        fields = bankAccont.getFields();
        fields.set( "acctName", "Chequing" );
        fields.set( "acctNumber", "1234567" );
        fields.set( "bankCode", "123" );
        fields.set( "transitNumber", "12" );
        fields.set( "bankName", "TD Canada Trust" );
        fields.set( "currency", Currency.getByCode( "CAD" ) );
        fields.set( "accountType", "Cash" );
        fields.set( "legalEntity", LegalEntity.getByName( "Spartan" ) );
        
        bankAccont = new BankAccount();
        list.add( bankAccont );
        fields = bankAccont.getFields();
        fields.set( "acctName", "Line of Credit" );
        fields.set( "acctNumber", "2345678" );
        fields.set( "bankCode", "234" );
        fields.set( "transitNumber", "23" );
        fields.set( "bankName", "CIBC" );
        fields.set( "currency", Currency.getByCode( "CAD" ) );
        fields.set( "accountType", "Cash" );
        fields.set( "legalEntity", LegalEntity.getByName( "Centurion" ) );
        
        return new LinkedHashSet[] { list };
    }
    
    public BankAccount()
    {
        super( "Bank Account" );
    }
    
    public BankAccount( Stage stage )
    {
        super( stage, "Bank Account" );
        list.add( this );
    }
    
} // End of class ** BankAccount **
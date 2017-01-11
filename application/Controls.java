package application;

import javafx.stage.Stage;
import java.util.Map;
import java.util.HashMap;

import forms.Registry;
import foundation.AssociativeList;
import interfaces.Constants;
import interfaces.Encapsulation;
import interfaces.Utilities;

/**
 * Class Controls - controls Menu of the main window
 * @author Peter Cross
 *
 */
public class Controls implements Encapsulation, Utilities, Constants
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/  
    
    // Object to control menus
    private static Controls	controls;
    
    // List of registries to display menu items
    private static AssociativeList registries;
    
    /*          Methods                                                                                               */
    /******************************************************************************************************************/
    
    /**
     * Displays a registry in a window
     * @param registryName Name of a registry to display
     */
    public void displayRegistry( String registryName )
    {
        try
        {
            // Get Registry object for specified registry name and display it
            ((Registry) registries.get( registryName )).display();
        }
        catch ( Exception e )
        {}
    }
    
    /**
     * Displays a registry in a window
     * @param registryName Name of a registry to display
     * @param width Window width
     */
    public void displayRegistry( String registryName, int width )
    {
        try
        {
            // Get Registry object for specified registry name and display it
            ((Registry) registries.get( registryName )).display( width );
        }
        catch ( Exception e )
        {}
    }
    
    /**
     * Gets called on exit from the program
     */
    public void onExit()
    {
        displayMessage( "Hope you had a good experience. \n\n Have a nice day!" );
        System.exit(0);
        
    } // End of method ** onExit **

    /**
     * Creates Registry objects for menu items
     * @param stage Stage object where to display content 
     */
    public void createMenuControls( Stage stage )
    {
        Map<String, Registry> m = new HashMap<>(); 
        
        m.put( "Chart of Accounts",               new Registry( stage, "Chart of Accounts", "GLAccount", "ChartOfAccounts" ) );
        m.put( "Journal of Transactions",         new Registry( stage, "Journal of Transactions", "JournalEntry", "LegalEntity" ) );
        m.put( "Journal of Reverse Transactions", new Registry( stage, "Journal of Reverse Transactions", "ReverseJournalEntry", "LegalEntity" ) );
        
        m.put( "Units",                     new Registry( stage, "List of Units", "Unit" ) );
        m.put( "Currencies",                new Registry( stage, "List of Currencies", "Currency" ) );
        m.put( "Inventory Groups",          new Registry( stage, "List of Inventory Groups", "InventoryGroup" ) );
        m.put( "Exchange Rate Types",       new Registry( stage, "Exchange Rate Types", "ExchangeRateType" ) );
        m.put( "Sales Taxes",               new Registry( stage, "List of Sales Taxes", "SalesTax" ) );
        m.put( "Charts of Accounts",        new Registry( stage, "List of Charts of Accounts", "ChartOfAccounts") );
        
        m.put( "Bank Accounts",             new Registry( stage, "List of Bank Accounts", "BankAccount" ) );
        m.put( "Inventory",                 new Registry( stage, "List of Inventory", "Inventory" ) );
        m.put( "Products",                  new Registry( stage, "List of Products", "Product" ) );
        m.put( "Expenses",                  new Registry( stage, "List of Expenses", "Expense" ) );
        m.put( "Long-Term Assets",          new Registry( stage, "List of Long Term Assets", "LongTermAsset" ) );
        m.put( "Securities",                new Registry( stage, "List of Securities", "Securities" ) );
        m.put( "Business Partners",         new Registry( stage, "List of Business Partners", "BusinessPartner" ) );
        m.put( "Employees",                 new Registry( stage, "List of Employees", "Employee" ) );
        m.put( "Shareholders",              new Registry( stage, "List of Shareholders", "Shareholder" ) );
        m.put( "Contracts",                 new Registry( stage, "List of Contracts", "Contract" ) );
        m.put( "Warranties",                new Registry( stage, "List of Warranties", "Warranty" ) );
        m.put( "Locations",                 new Registry( stage, "List of Locations", "Location" ) );
        m.put( "Legal Entities",            new Registry( stage, "List of Legal Entities", "LegalEntity" ) );
        m.put( "Taxation Models",           new Registry( stage, "List of Taxation Models", "TaxationModel" ) );
        
        for ( Map.Entry<String, Registry> e : m.entrySet() )
            registries.set( e.getKey(), e.getValue() );
    }
    
    /**
     * Creates Controls object, if it's not created yet
     * Or returns existing Controls object, if it's been created already
     * @param args Command line arguments
     * @return Controls object
     */
    public static Controls create( String... args )
    {
        return ( controls != null ? controls : new Controls( args ) );

    } // End of method ** create **	
    
    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    private Controls( String... args )
    {
        controls = this;  
        registries = new AssociativeList();
    }
	
} // End of class ** Controls **
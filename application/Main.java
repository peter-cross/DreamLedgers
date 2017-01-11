package application;
	
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.collections.ObservableList;
import java.net.URL;

import interfaces.Constants;
import interfaces.Lambda.MenuItemAction;

/** 
 * Class Main - starts the program
 * @author Peter Cross
 *
 */
public class Main extends Application implements Constants
{
    // Object to cotrol program's GUI
    private static Controls controls;

    /**
     * Starts the program
     * @param args Command line parameters
     */
    public static void main( String[] args ) 
    {
        // Create control object
        controls = Controls.create( args );

        // Launch JavaFX GUI
        launch( args );
    }
	
    /**
     * Creates Graphic User Interface to work with the program
     * @param stage Stage object for main program window
     */
    @Override
    public void start( Stage stage ) 
    {
        try
        {
            // Create menu controls
            controls.createMenuControls( stage );
            
            // Set scene for Stage object
            stage.setScene( createScene() );

            // Set title for Stage window
            stage.setTitle( "DreamLedgers SBP FX" );

            // Display stage
            stage.show();

            // Set up event handler for stage window close event
            stage.setOnCloseRequest( e -> controls.onExit() );

        }
        catch ( Exception e )
        {}
		
    } // End of method ** start **
	
    /**
     * Creates Scene window
     * @return Scene with created menu bar
     */
    private Scene createScene()
    {
    	// Create Scene object for Main Window
        Scene scene = new Scene( new VBox(), WIDTH, HEIGHT );

        // Get URL object for Application stylesheet
        URL styleSheet = getClass().getResource( "application.css" );

        // If stylesheet is found
        if ( styleSheet != null )
        {
            // Get Scene style property
            ObservableList<String> style = scene.getStylesheets();

            // Add stylesheet to style property
            style.add( styleSheet.toExternalForm() );
        }

        // Create Pane object for the scene
        Pane pane = (Pane) scene.getRoot();

        // Add Menu Bar to Pane object
        pane.getChildren().add( createMenuBar() );
     	
    	return scene;
    	
    } // End of method ** createScene **
	
    /**
     * Creates Menu Bar with menus
     * @return Created menu Bar
     */
    private MenuBar createMenuBar()
    {
    	// Create Menu Bar
        MenuBar menuBar = new MenuBar();
        
        // Add Main Menu to Menu Bar
        menuBar.getMenus().addAll( createMenu() );
		
    	return menuBar;
    	
    } // End of method ** createMenuBar **
    
    /**
     * Creates Main Menu 
     * @return Array of menus
     */
    private Menu[] createMenu()
    {
    	// Create array of Menu objects
        Menu[] menu = new Menu[4];
        Menu subMenu;
		
    	// Create General Ledger Menu
        menu[0] = new Menu( "General Ledger" );
        // Add menu items for General Ledger menu
        addMenuItem( menu[0], "Journal of Transactions" );
        addMenuItem( menu[0], "Journal of Reverse Transactions" );
        menu[0].getItems().add( new SeparatorMenuItem() );
        addMenuItem( menu[0], "Chart of Accounts" );
        menu[0].getItems().add( new SeparatorMenuItem() );
        menu[0].getItems().add( new SeparatorMenuItem() );
        
        subMenu = new Menu( "Reference Lists" );
	// Add menu items for Reference Lists sub menu
        addMenuItem( subMenu, "Units" , 600 );
        addMenuItem( subMenu, "Currencies" , 400 );
        addMenuItem( subMenu, "Inventory Groups" , 600 );
        addMenuItem( subMenu, "Exchange Rate Types" , 400 );
        addMenuItem( subMenu, "Charts of Accounts" , 400 );
        addMenuItem( subMenu, "Sales Taxes" , 400 );
        addMenuItem( subMenu, "Taxation Models" , 500 );
        menu[0].getItems().add( subMenu );
        
        menu[0].getItems().add( new SeparatorMenuItem() );
        menu[0].getItems().add( new SeparatorMenuItem() );
        addMenuItem( menu[0], "Exit", () -> controls.onExit() );
        
        // Create Documents menu
        menu[1] = new Menu( "Documents" );
	addMenuItem( menu[1], "Purchase Orders" );
        addMenuItem( menu[1], "Purchase Invoices" );
        addMenuItem( menu[1], "Purchase Discrepancies" );
        menu[1].getItems().add( new SeparatorMenuItem() );
        addMenuItem( menu[1], "Sales Orders" );
        addMenuItem( menu[1], "Sales Invoices" );
        menu[1].getItems().add( new SeparatorMenuItem() );
        addMenuItem( menu[1], "Cheques" );
        addMenuItem( menu[1], "Bank Statements" );
        addMenuItem( menu[1], "Bank Reconciliations" );
        
        // Create Analytical Dimensions Menu
        menu[2] = new Menu( "Analytical Dimensions" );
        
        addMenuItem( menu[2], "Bank Accounts" , 800 );
        
        menu[2].getItems().add( new SeparatorMenuItem() );
        
        subMenu = new Menu( "Operating Assets" );
	// Add menu items for Operating Assets sub menu
        addMenuItem( subMenu, "Inventory" , 600 );
        addMenuItem( subMenu, "Products" , 800 );
        addMenuItem( subMenu, "Expenses" , 600 );
        menu[2].getItems().add( subMenu );
        
        menu[2].getItems().add( new SeparatorMenuItem() );
        
        subMenu = new Menu( "Investments" );
	// Add menu items for Investments sub menu
        addMenuItem( subMenu, "Long-Term Assets" , 960 );
        subMenu.getItems().add( new SeparatorMenuItem() );
        addMenuItem( subMenu, "Securities" , 800 );
        menu[2].getItems().add( subMenu );  
        
        menu[2].getItems().add( new SeparatorMenuItem() );
        menu[2].getItems().add( new SeparatorMenuItem() );
        
        subMenu = new Menu( "Counterparts" );
	// Add menu items for Counterparts sub menu
        addMenuItem( subMenu, "Business Partners" , 800 );
        addMenuItem( subMenu, "Employees" , 800 );
        addMenuItem( subMenu, "Shareholders" , 600 );
        
        subMenu.getItems().add( new SeparatorMenuItem() );
        
        addMenuItem( subMenu, "Contracts" , 800 );
        addMenuItem( subMenu, "Warranties" , 960 );
        menu[2].getItems().add( subMenu );
        
        menu[2].getItems().add( new SeparatorMenuItem() );
        
        subMenu = new Menu( "Divisions" );
	// Add menu items for Operating Assets sub menu
        addMenuItem( subMenu, "Locations" , 800 );
        addMenuItem( subMenu, "Legal Entities" , 960 );
        menu[2].getItems().add( subMenu );
        
        // Create Administration Menu
        menu[3] = new Menu( "Administration" );
	// Add menu items for Administration menu
        addMenuItem( menu[3], "General Settings" );
        addMenuItem( menu[3], "G/L Settings" );
        
        menu[3].getItems().add( new SeparatorMenuItem() );
        
        addMenuItem( menu[3], "Operation Templates" );
                
    	return menu;
    	
    } // End of method ** createMenu **
    
    private void addMenuItem( Menu menu, String itemName, int width  )
    {
    	addMenuItem( menu, itemName, () -> controls.displayRegistry( itemName, width ) );
        
    } // End of method ** addMenuItem **
      
    private void addMenuItem( Menu menu, String itemName )
    {
    	addMenuItem( menu, itemName, () -> controls.displayRegistry( itemName ) );
        
    } // End of method ** addMenuItem **
    
    /**
     * Adds menu item to the menu
     * @param menu Menu to add the item to
     * @param itemName Name of menu item
     * @param code Lambda expression with action to perform for menu item
     */
    private void addMenuItem( Menu menu, String itemName,  MenuItemAction code )
    {
    	// Create menu item object
        MenuItem menuItem = new MenuItem( itemName );

        // Set up event handler on selection of Menu Item
        menuItem.setOnAction( event -> code.run() );

        // Add created menu item to the menu
        menu.getItems().add( menuItem );
		   
    } // End of method ** addMenuItem **
    
} // End of class ** Main **
package forms;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.beans.value.ObservableValue;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.StringConverter;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.text.DecimalFormat;

import foundation.AssociativeList;
import foundation.Data;
import foundation.RegistryItem;
import interfaces.Constants;
import interfaces.Encapsulation;
import interfaces.Utilities;

import static interfaces.Utilities.attrName;
import static interfaces.Utilities.createDataClass;
import static interfaces.Utilities.getByIndex;

/**
 * Class Registry
 * @author Peter Cross
 */
public class Registry extends Stage implements Encapsulation, Constants, Utilities
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    /*                                                                                                                */
    private Stage                               	stage;         // Stage object of the window
    private Scene                               	scene;         // Scene object to display content
    private String                              	title;         // Window title
    private String                              	valueType;     // Value type of Registry Items
    private String[]                            	tabs;          // Tabs
    private String                              	tabsType;      // Value type of Tab Items
    private int                                 	numRows;       // Number of rows to display
    private int                                         numCols = 0;   // Number of columns to display
    
    private LinkedHashSet<RegistryItem>[]               list;          // List of Registry Items to display
    
    private ArrayList<TableView<ArrayList<Data>>>       table;         // Table to display content
    private ArrayList<ObservableList<ArrayList<Data>>>  data;          // data model of TableView objct
    
    /*          Methods                                                                                               */
    /******************************************************************************************************************/
    /*                                                                                                                */
    /**
     * Updates table content after changes
     * @param tabNum Tab number on which table located
     */
    public void updateTable( int tabNum )
    {
        TableView<ArrayList<Data>> tbl = table.get(tabNum);
        
        // Update Table View 
        tbl.setItems( null );
        tbl.layout();
        tbl.setItems( data.get(tabNum) );        
    }
    
    /**
     * Adds new Registry Item to the display list and data model
     * @param tabNum Tab number
     * @param newItem RegistryItem object to add
     */    
    protected void add( int tabNum, RegistryItem newItem )
    {
        // Create TableView object for selected tab
        TableView<ArrayList<Data>> tbl = table.get(tabNum);
        
        // If new RegistryItem is specified
        if ( newItem != null )
        {
            // Get items for current table view
            ObservableList items = (ObservableList) tbl.getItems();
            
            // Get current row of items list
            ArrayList<Data> row = (ArrayList) items.get( items.size() - 1 );
            
            // Get attributes list for new RegistryItem
            AssociativeList attributesList = newItem.getAttributesList();
            
            // Get header elements of the new RegistryItem
            DialogElement[][] header = (DialogElement[][]) attributesList.get( "header" );
            
            // Get output results of the new RegistryItem
            
            AssociativeList fields = newItem.getFields();
            
            int colCounter = 0; // Columns counter
            
            // Go through all header elements and create array elements for data model
            for ( int t = 0; t < header.length; t++ )
                for ( int e = 0; e < header[t].length; e++ )
                    if ( colCounter < numCols )
                        row.set( colCounter++, Data.create( fields.get( attrName(header[t][e]) ) ) );
            
            // Add another Data array object for new display list item
            ArrayList<Data> newRow = new ArrayList<>();
            
            // For each column to display
            for ( int j = 0; j < numCols; j++ )
                newRow.add( null );
            
            // Add new empty row
            items.add( newRow );
            
            updateTable( tabNum );
            
            // Highlight current row number 
            tbl.getSelectionModel().select( items.size()-2 );
            
        } // End If ** new RegistryItem is specified **
            
    } // End of method ** add **
    
    /**
     * Removes Registry Item from display list
     * @param tabNum Tab number
     * @param itemToRemove Registry Item to remove
     */
    protected void remove( int tabNum, RegistryItem  itemToRemove )
    {
        list[tabNum].remove( itemToRemove );
    
    } // End of method ** remove **
    
    /**
     * Creates new Registry Item
     * @param tabNum Tab number
     * @param st Stage to display created item on
     * @return Created new Registry Item
     */
    protected RegistryItem newRegistryItem( int tabNum, Stage st )
    {
        // If value type for new Registry Item is not specified
        if ( valueType == null || valueType.isEmpty() )
            return null;
        
        RegistryItem obj = null;    // To store created new RegistryItem object
        
        Class cls = createDataClass( valueType );  // To store class for new RegistryItem object
        
        try 
        {
            // If Stage object is specified
            if ( st != null )
            {
                // If there are no tabs in current registry
                if ( tabs == null || tabs.length == 0 )
                {
                    // Get class constructor with parameter stage
                    Constructor cnstr = cls.getConstructor( Stage.class );

                    // Create RegistryItem object with class constructor
                    obj = (RegistryItem) cnstr.newInstance( st );
                }
                else
                {
                    // Get class constructor with parameter stage
                    Constructor cnstr = cls.getConstructor( Stage.class, int.class );

                    // Create RegistryItem object with class constructor
                    obj = (RegistryItem) cnstr.newInstance( st, tabNum );
                }
                
                // Add created Registry Item to Registry List
                add( tabNum, obj );
            }
            else
            {
                // Get class constructor without parameters
                Constructor cnstr = cls.getConstructor();

                // Create RegistryItem object with class constructor
                obj = (RegistryItem) cnstr.newInstance();
            }
        } 
        catch ( Exception ex ) 
        {}
        
        return obj;
        
    } // End of method ** newRegistryItem **
    
    /**
     * Opens dialog to edit RegistryItem object
     * @param tabNum Tab number
     */
    protected void editRegistryItem( int tabNum )
    {
        // Get TableView object for current tab
        TableView<ArrayList<Data>> tbl = table.get(tabNum);
        
        RegistryItem regItem; // To store RegistryItem to edit
        
        // Get current Table View row number
        int rowNum = tbl.getSelectionModel().getSelectedIndex();
        
        // If row number is specified and is valid
        if ( rowNum >= 0 && rowNum < list[tabNum].size() )
            // Get RegistryItem to edit from display list
            regItem = getByIndex( list[tabNum], rowNum  );
            //regItem = list[tabNum].get( rowNum );
        else
            return;
        
        try 
        {
            // Open dialog window to edit RegistryItem
            regItem.display( ); 
            
            // Get items for for current table view
            ObservableList items = (ObservableList) tbl.getItems();
	    
            // Get current row data array
            ArrayList<Data> row = (ArrayList) items.get( rowNum );
            
            // Get attributes list for edited RegistryItem
            AssociativeList attributesList = regItem.getAttributesList();
            
            // Get header elements for the RegistryItem
            DialogElement[][] header = (DialogElement[][]) attributesList.get( "header" );
            
            // Get output results for edited RegistryItem
            //TableOutput output = (TableOutput) attributesList.get( "output" );
            AssociativeList fields = regItem.getFields();
            
            int colCounter = 0; // Column counter
            
            // Go through each header element an update information in data model array
            for ( int t = 0; t < header.length; t++ )
                for ( int e = 0; e < header[t].length; e++ )
                    if ( colCounter < numCols )
                        //row.set( colCounter++, Data.create( output.header[t][e] ) );
                        row.set( colCounter++, Data.create( fields.get( attrName(header[t][e]) ) ) );
            
            updateTable( tabNum );
            
            // Highlight current row number 
            tbl.getSelectionModel().select( rowNum );
        } 
        catch ( Exception e ) 
        { } 
        
    } // End of method ** editRegistryItem **
    
    /**
     * Deletes current Registry Item
     * @param tabNum Tab number
     */
    protected void deleteRegistryItem( int tabNum )
    {
        // Get TableView object for selected tab
        TableView<ArrayList<Data>> tbl = table.get(tabNum);
        
        // Get current Table View row number
        int rowNum = tbl.getSelectionModel().getSelectedIndex();
        
        // If either row number is not spcified or it's just a free row for new RegistryItem
        if ( rowNum < 0 ^ rowNum == list[tabNum].size() )
            return;
        
        // Ask user to confirm deleting current Registry Item
        if ( getYesNo( "Do you want to delete current item?" ) != YES )
            return;
            
        // Get items for for current table view
        ObservableList items = (ObservableList)tbl.getItems();
	
        // Remove current row from Table View
        items.remove( rowNum );
        
        // Remove current row from display list
        list[tabNum].remove( rowNum );
        
        updateTable( tabNum );
            
        // If there is RegistryItem before deleted row
        if ( rowNum - 1 >= 0 )
            // Highlight RegistryItem before deleted row
            tbl.getSelectionModel().select( rowNum - 1 );
        
        // otherwise
        else
            // Highlight RegistryItem which goes after deleted row
            tbl.getSelectionModel().select( rowNum );
        
    } // End of method ** deleteRegistryItem **
    
    /**
     * Creates Scene object and prepares scene for displaying something
     * @param width Width of window
     * @return Pane object for created Scene
     */
    protected Pane setScene( double width )
    {
        // Preferred height for display window by default
        final int PREF_HEIGHT = HEIGHT - 3 * taskBarHeight; 
        
        Pane pane; // To store Pane object for created scene
        
        // If Scene object is not created yet
        if ( scene == null )
        {
            // Create vertical box for scene content
            VBox sceneBox = new VBox();
            
            // Set scene preferred height
            sceneBox.setPrefWidth( width < 800 ? width : Math.max(width, 800) );
            sceneBox.setMaxHeight( PREF_HEIGHT );
            
            // Create Scene object with vertical box as content and store it in property variable
            scene = new Scene( sceneBox ); 
            
            // Create label element for the title
            Label ttl = new Label( title );

            // Set font for the label
            ttl.setFont( new Font( "Arial", 15 ) );
            ttl.setMaxWidth( Long.MAX_VALUE );
            
            // Position title in the middle
            ttl.setAlignment( Pos.CENTER );

            // Create box element for the title
            VBox box = new VBox( ttl );
            // Specify style for box rendering
            box.setStyle("-fx-border-style: solid inside;"
                        +"-fx-border-color: #DDD;"
                        +"-fx-background-color: #F7F7F7;"
                        +"-fx-border-width: 0 0 1 0;"
                        +"-fx-padding: 15;");
            
            // Get pane element from Scene obkect
            pane = (Pane) scene.getRoot();
            // Add box element to the pane
            pane.getChildren().add( box );
        }
        
        // Otherwise, if Scene object exists
        else
            // Get pane element for existing Scene object
            pane = (Pane) scene.getRoot();
        
        // Set default date settings
        Locale.setDefault( Locale.CANADA );

        return pane;
        
    } // End of method ** setScene **
    
    /**
     * Creates TextField object in specified column
     * @param column Column in which to create TextField object
     * @param tblEl Table element with TextField parameters
     */
    private void createTextField( TableColumn column, DialogElement dlgEl )
    {
        final String elType; // To store column element type
        
        // If CheckBox label is specified for current clumn
        if ( !dlgEl.checkBoxlabel.isEmpty() )
        {
            // Set column alignment to the center
            column.setStyle("-fx-alignment: center;");
            // Store CheckBox element type
            elType = "CheckBox";
        }
        // If Number type value is specified
        else if ( dlgEl.valueType.contains( "Number" ) )
        {
            // Set column alignment to the right
            column.setStyle("-fx-alignment: center-right;");
            // Store Number element type
            elType = "Number";
        }
        // Otherwise
        else 
            // Element type to display - Text
            elType = "Text";
        
        DecimalFormat formatter = new DecimalFormat( "#,###.00" );
        
        // Set Cell Factory for the column
        column.setCellFactory( event -> 
        { 
            // Create text field object
            // and define anonymous class overriding TextFieldTableCell
            TextFieldTableCell<ArrayList<Data>, String> textField = new TextFieldTableCell<ArrayList<Data>, String>()
            {
                @Override
                // Gets called when cell item is updated
                public void updateItem( String item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );

                    // If table cell is not empty
                    if ( !empty )
                    {
                        // Get Table View current row Data Model array
                        ArrayList<Data> row = (ArrayList) this.getTableRow().getItem();
                        // Get column number from table view
                        int colNum = getTableView().getColumns().indexOf( getTableColumn() );

                        // If column number is specified and data model cell is not empty
                        if ( row != null && colNum != -1 && row.size() > 0 && row.get(0) != null  )
                        {
                            Data cellData = row.get( colNum );
                            Object cellVal = cellData.get();
                            String strVal = "";
                            
                            // If there is something in current cell
                            if ( cellData != null )
                            {
                                // If the value in current cell is a string
                                if ( cellVal instanceof String )
                                    // Get cell value from Data model array cell
                                    strVal = (String) cellVal;
                                
                                // If the value in current cell is a text field
                                else if ( cellVal instanceof TextField )
                                    // Get text value of the text field in current cell
                                    strVal = ((TextField) cellVal).getText();
                                
                                // If the value in current cell is a number
                                else if ( cellVal instanceof Number )
                                {
                                    try
                                    {
                                        // If the value in current cell is zero
                                        if ( (double) cellVal == 0 )
                                            strVal = "";
                                        
                                        // If it's a number without decimals
                                        else if ( (double) cellVal == Math.ceil( (double)cellVal ) )
                                            strVal = formatter.format( (int) Math.floor( (double)cellVal ) );
                                        
                                        // Otherwise, if it's a double value with decimals
                                        else
                                            strVal = formatter.format( cellVal );
                                    }
                                    catch ( Exception e )
                                    {
                                        // If the value in current cell is an integer
                                        try
                                        {
                                            // If the value is zero
                                            if ( (int) cellVal == 0 )
                                                strVal = "";
                                            
                                            // Otherwise, if non-zero value
                                            else
                                                //strVal = cellVal.toString();
                                                strVal = formatter.format( cellVal );
                                        }
                                        catch ( Exception e1 )
                                        {
                                            strVal = cellVal.toString();
                                        }
                                    }
                                }
                                else
                                    try
                                    {
                                        // Try to convert current value to string
                                        strVal = cellVal.toString();
                                    }
                                    catch ( Exception e )
                                    {}
                            }
                            else
                                strVal = "";
                            
                            // If element to display is CheckBox
                            if ( elType == "CheckBox" && !strVal.isEmpty() )
                                // If CheckBox is checked
                                if ( Double.parseDouble( strVal ) > 0 )
                                    // Assign + symbol to display in column
                                    strVal = "+";
                                // Otherwise
                                else
                                    // Display just empty cell
                                    strVal = "";
                            
                            // Set text to display for current Table View cell
                            setText( strVal );
                        }
                        // Otherwise
                        else
                            // Display in current cell nothing
                            setText( null );
                        
                    } // End If ** table cell is not empty **
                    
                } // End of method ** updateItem **
                
            }; // End of ** Anonymous class overriding TextFieldTableCell **
	
            // Set String Converter for current text field
            textField.setConverter( new StringConverter()
            {
                @Override
                public Object fromString( String string ) 
                {
                    return string;
                }

                @Override
                public String toString( Object object ) 
                {
                    return ( object != null ? object.toString() : "" );
                }
            } ); // End of ** String Converter **

            // Return created text field
            return textField;	
            
        } ); // End of ** Cell Factory for the column **
    
    } // End of method ** createTextField **
    
    /**
     * Creates Table View for the scene
     * @param tabNum Tab number
     * @return Table View object
     */
    private TableView createTableView( int tabNum )
    {
        RegistryItem        item;           // To store a sample of Registry Item
        AssociativeList     attributesList; // To store Registry Item's attributes list
        DialogElement[][]   header;         // To store header elements of Registry Items
        String[]            column;         // to store column names to display
    
        double  columnsWidth = 0; // To store calculated columns width
        
        String[] attributeName;
        
        // If there is a filled list
        if ( list != null && list[tabNum] != null && list[tabNum].size() > 0 )
        {
            // Get 1st registry item from the list
            item = getByIndex( list[tabNum], 0 );
            //item = list[tabNum].get(0);
            // Assign number of rows as list size plus one ( for new item )
            numRows = list[tabNum].size() + 1;
        }
        // Otherwise - just create a new item
        else
        {
            // Create new Registry Item without displaying it
            item = newRegistryItem( tabNum, null );
            // Assign number of rows one ( for new item )
            numRows = 1;
        }
        
        // If Registry Item sample is specified
        if ( item != null )
        {
            // Get attributes list for Registry Item sample
            attributesList = item.getAttributesList();
            // Get header elements for Registry Item sample
            header = (DialogElement[][]) attributesList.get( "header" );
            
            // Create String array for column names
            column = new String[ header.length * header[0].length ];
            // Create String array for current RegistryItem object attribute names
            attributeName = new String[ header.length * header[0].length ];
            
            int numCol = 0;
            
            // Go through each tab in Registry Item
            for ( DialogElement[] hdr : header ) 
            {
                // Go through each header element in current tab for Registry Item
                for ( DialogElement itm : hdr ) 
                {
                    // If header element is specified
                    if ( itm != null ) 
                    {
                        // If there is attribute name specified
                        if ( itm.attributeName != null && !itm.attributeName.isEmpty() ) 
                            attributeName[numCol] = itm.attributeName;
                        // Otherwise - assume it's the label name
                        else 
                            attributeName[numCol] = itm.labelName;
                        
                        // If for the header element short name is specified
                        if ( itm.shortName != null && !itm.shortName.isEmpty() ) 
                            // Store short name to display
                            column[numCol] = itm.shortName;
                        // Otherwise
                        else 
                            // Store Label name
                            column[numCol] = itm.labelName;
                        
                        // Add name's to display length to columns width
                        columnsWidth += column[numCol++].length();
                        
                    } // End if header element is specified
                    
                } // End for ** Go through each header element in current tab **
                
            } // End for ** Go through each tab **
            
            numCols = numCol;
            
            columnsWidth *= 1.007; // Adjustment to remove horizontal scroll bar
            
        } // End If ** Registry Item sample is specified **
        
        // Otherwise, if Registry Item sample is not specified for some reason
        else
            return null;
        
        // Create array for Table View data model
        ArrayList<ArrayList<Data>> matrix = new ArrayList<>();
        
        // Loop for each item in list to display 
        for ( int i = 0; i < list[tabNum].size(); i++ )
        {
            // Get next item from list to display
            item = getByIndex( list[tabNum], i );
            //item = list[tabNum].get(i);
            
            // If item is not specified
            if ( item ==  null )
                continue;
            
            // Create ArrayList for current row
            ArrayList<Data> curRow = new ArrayList<>();
            
            String colName;
            
            // Get fields attribute for current Registry Item
            AssociativeList fields = item.getFields();
            
            // For each column to display
            for ( int j = 0; j < numCols; j++ )
            {
                // Get column name, trim it and remove spaces
                colName = attributeName[j].trim().replace( " ", "" );
                
                // Convert 1st character to lower case
                colName = colName.substring(0, 1).toLowerCase() + colName.substring(1);
                
                // Create data model array element obtained from attributes list
                curRow.add( Data.create( fields.get( colName ) ) );
            }
            
            // Add current row to the matrix array
            matrix.add( curRow );
            
        } // End of Loop ** for each list to display item **
        
        // Create ArrayList object for current row to fill empty cells in new row
        ArrayList<Data> curRow = new ArrayList<>();
        
        // For each column to display
        for ( int j = 0; j < numCols; j++ )
            curRow.add( null );
        
        // Add empty row to the matrix
        matrix.add( curRow );
        
        // Create data model array from created Data array
        data.add( tabNum, FXCollections.observableList( (List) matrix ) );
        
        // Create TableView object with data model passed to constructor
        TableView<ArrayList<Data>> tbl = new TableView<>( data.get( tabNum ) );
        
        // Make table non-editable
        tbl.setEditable( false );
        // Set placeholder for empty rows
        tbl.setPlaceholder( new Label("") );
        
        tbl.setFixedCellSize(25);
        // Get table  view bindable to the number of items
        ObservableValue bindSize = Bindings.size( tbl.getItems() ).multiply( tbl.getFixedCellSize() ).add( tbl.getFixedCellSize() );
        
        tbl.prefHeightProperty().bind( bindSize );
        tbl.setMinHeight( HEIGHT * 0.82 );
        
        // Add created TableView object to table ArrayList
        table.add( tabNum, tbl );
        
        int colCntr = 0;    // Columns counter
        
        // Go through each tab in header part
        for ( DialogElement[] hdr : header ) 
        {
            // Go through each header element in current tab
            for ( DialogElement itm : hdr ) 
            {
                // If header element is specified
                if ( itm != null) 
                {
                    // Create TableColumn object for current header element with name from column array
                    TableColumn clmn = new TableColumn( column[colCntr] );
                    
                    // Create text field for current column based on header element's parameters
                    createTextField( clmn, itm );
                    // Bind column width property reative to column's name length
                    clmn.prefWidthProperty().bind( tbl.widthProperty().multiply( column[colCntr].length()/columnsWidth) );
                    // Add current column to the table
                    tbl.getColumns().add( clmn );
                    
                    // Increment columns counter
                    colCntr++;
                }
            }
        }
        
        // Highlight 1st table row
        tbl.getSelectionModel().select(0);
        
        // Return table for specified tab
        return tbl;
        
    } // End of method ** createTableView **
    
    /**
     * Adds buttons to the form
     * @param tabPane Tab pane to which add buttons
     */
    private void addTableButtons( TabPane tabPane )
    {
        // Create array for table button objects
        Button[] btn = new Button[5];
        
        // Create New button element
        btn[0] = new Button( "New" );
        btn[0].setPadding( new Insets( 5, 20, 5, 20 ) );
        
        // Set event handler for press button event
        btn[0].setOnAction( e -> 
        {
            int tabNum;
            
            // If there are tabs
            if ( tabPane != null )
                // Get tab number from selected tab
                tabNum = tabPane.getSelectionModel().getSelectedIndex();
            else
                tabNum = 0;
        
            // Create new Registr Item for specified tab
            newRegistryItem( tabNum, stage );
        } );
	
        // Create Edit button element
        btn[1] = new Button( "Edit" );
        btn[1].setPadding( new Insets( 5, 20, 5, 20 ) );
        
        // Set event handler for press button event
        btn[1].setOnAction( e -> 
        {
            int tabNum;
            
            // If there are tabs
            if ( tabPane != null )
                // Get tab number from selected tab
                tabNum = tabPane.getSelectionModel().getSelectedIndex();
            else
                tabNum = 0;
        
            // Edit current Item on specified Tab
            editRegistryItem( tabNum );
        } );
	
        // Create Delete button element
        btn[2] = new Button( "Delete" );
        btn[2].setPadding( new Insets( 5, 20, 5, 20 ) );
        
        // Set event handler for press button event
        btn[2].setOnAction( e -> 
        {
            int tabNum;
            
            // If there are tabs
            if ( tabPane != null )
                // Get tab number from selected tab
                tabNum = tabPane.getSelectionModel().getSelectedIndex();
            else
                tabNum = 0;
            
            // Delete current Registry Item on specified tab
            deleteRegistryItem( tabNum );
        } );
	
        // Create Print button object
        btn[3] = new Button( "Print " );
        btn[3].setPadding( new Insets( 5, 20, 5, 20 ) );
        
        // Set event handler for press button event
        btn[3].setOnAction( e -> 
        {
            close();
        } );

        // Create Close button object
        btn[4] = new Button( "Close " );
        btn[4].setPadding( new Insets( 5, 20, 5, 20 ) );
        // Set event handler for press button event
        btn[4].setOnAction( e -> close() );

        // Get pane from Scene object
        Pane pane = (Pane) scene.getRoot();

        // Create horizontal box object
        HBox buttons = new HBox();

        // Set padding settings for the buttons
        buttons.setPadding( new Insets( 12, 12, 12, 12 ) );
        buttons.setSpacing( 5 );

        // Align buttons box to the right
        buttons.setAlignment( Pos.CENTER_RIGHT );

        // Add OK and Cancel buttons to buttons box
        buttons.getChildren().addAll( btn );
		
        // Add buttons box to the pane
        pane.getChildren().addAll( buttons );
        
    } // End of method ** addTableButtons **
    
    /**
     * Displays the form
     */
    public void displayForm()
    {
        // Get URL object for the style sheet
        URL styleSheet = getClass().getResource( "/application/application.css" );

        // If style sheet is specified
        if ( styleSheet != null )
        {
            // Get style sheet object from the scene
            ObservableList<String> style = scene.getStylesheets();

            // If style is not specified yet
            if ( style.isEmpty() )
                // Add style sheet to style attribute of the stage
                style.add( styleSheet.toExternalForm() );	
        }
	
        // Set created Scene object as scene to display
        setScene( scene );
        
        try
        {
            // Show dialog element and wait
            showAndWait();
        }
        catch ( Exception e )
        {
            System.out.println( "Can't display stage : " +e.getMessage() );
        }
        
    } // End of method ** displayForm **

    /**
     * Creates Tab Pane object
     * @return Created Tab Pane
     */
    private TabPane createTabPane()
    {
        TabPane tabPane = new TabPane();
        
        // Make closing tab unavailable
        tabPane.setTabClosingPolicy( TabPane.TabClosingPolicy.UNAVAILABLE );

        return tabPane;
        
    } // End of method ** createTabPane **

    /**
     * Creates Tab object
     * @param tabNum Tab number
     * @return Created Tab
     */
    private Tab createTab( int tabNum )
    {
        // Create Tab object for current tab
        Tab tab = new Tab();

        // If there is array of tab names
        if ( tabs != null )
            // Set name for current tab
            tab.setText( tabs[tabNum] );
        else
            // Set numbered tab name
            tab.setText( tabsType + " " + tabNum );

        // Set selected table as content of current tab
        tab.setContent( table.get(tabNum) );
        
        return tab;
    
    } // End of method ** createTab **
    
    /**
     * Displays the registry form
     */
    public void display()
    {
        display( WIDTH*0.95 );
         
    } // End of method * display **
    
    /**
     * Displays the registry form
     * @param width Form width
     */
    public void display( double width )
    {
        // Create Scene, set up scene setings and get current scene Pane object
        Pane pane = setScene( width );
        
        // If Pane object was not created
        if ( pane == null )
            return;
        
        // If tabs are specified but Table Views are not created yet
        if ( tabs != null && tabs.length > 0 && table.isEmpty() )
        {
            // Create Pane for tabs
            TabPane tabPane = createTabPane();
            
            // Loop for each tab
            for ( int i = 0; i < tabs.length; i++ )
            {
                // Create TableView for current tab
                createTableView(i);
                    
                // Create Tab object for current table
                Tab tab = createTab(i);
                
                // Add current tab to the tab pane
                tabPane.getTabs().add( tab );
            }
            
            // Add tab pane to pane
            pane.getChildren().add( tabPane );
            
            // Add buttons to the form
            addTableButtons( tabPane );
        }
        // If there are no tabs specified
        else
            // If TableView is not created yet
            if ( table.isEmpty() )
            {
                // Create TableView object and add it to the pane
                pane.getChildren().add( createTableView(0) );
                
                // Add buttons to the form
                addTableButtons( null );
            }
        
        // Display form
        displayForm();
    }
    
    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    /*                                                                                                                */
    public Registry( Stage stage, String title, String valueType )
    {
        this.table = new ArrayList<>(1);
        this.data = new ArrayList<>(1);
        this.stage = stage;
        this.title = title;
        this.valueType = valueType;
        
        Class c = createDataClass( valueType );
        
        try
        {
            // Get list of Value Type objects
            list = (LinkedHashSet[]) c.getMethod( "createList" ).invoke( null );
        }
        catch ( Exception e )
        {}
    }
    
    public Registry( Stage stage, String title, String valueType, String tabsType )
    {
        this( stage, title, valueType );
        
        HashSet tabList =  null;
        
        Class c = createDataClass( tabsType );
        
        try
        {
            // Get list of Tabs
            tabList = ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0];
        }
        catch ( Exception e )
        {}
        
        c = createDataClass( valueType );
        
        try
        {
            // Get list of Value Type objects
            list = (LinkedHashSet[]) c.getMethod( "createList" ).invoke( null );
        }
        catch ( Exception e )
        {}
        
        int arrSize = ( tabList != null ? tabList.size() : 1 );
        
        // Create main arays for Table View
        this.table = new ArrayList<>( arrSize );
        this.data = new ArrayList<>( arrSize );
        this.tabs = new String[ arrSize ];
        this.tabsType = tabsType;

        // If there are tabs in the registry
        if ( tabList != null && arrSize > 0 )
        {
            Iterator it = tabList.iterator();
            int i = 0;
            
            // Loop through each tab
            while ( it.hasNext() )
                // Get Tab name from tab list
                this.tabs[i++] = it.next().toString();
        }
    }
    
} // End of class ** Registry **
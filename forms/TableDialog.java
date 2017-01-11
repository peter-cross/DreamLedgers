package forms;

import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.StringConverter;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.lang.reflect.Constructor;
import java.time.LocalDate;

import static interfaces.Utilities.createDataClass;
import static interfaces.Utilities.arrayCount;

import data.GLAccount;
import foundation.AssociativeList;
import foundation.Data;
import foundation.Item;
import foundation.RegistryItem;
import interfaces.Encapsulation;
import interfaces.Lambda.DialogAction;
import java.util.Iterator;

/**
 *
 * @author Peter Cross
 */
public class TableDialog implements Encapsulation
{
    final static int PREF_HEIGHT = 250,
                     PREF_WIDTH = 300;
    
    final private OneColumnDialog tblObj;
    
    private TableElement[][]                            tableElement;       // Array of table elements passed to the form
    private AssociativeList                             dlgElementsList;    // Dialog attributes list
    private int                                         maxCols;
    private int                                         ctrlBtnWidth = 12;  // Control button width
    private int                                         numRows;            // Number of rows passed
    private AssociativeList[][]                         elementsList;       // Associative array of table elements
    private Stage                                       stage;              // Get current Stage object
    private String[]                                    tabs;               // Tab names
   
    // ArrayList of TableView objects for each tab
    private ArrayList<TableView<ArrayList<Data>>>       table;
    // Data model object for current table dialog
    private ArrayList<ObservableList<ArrayList<Data>>>  data; 
            
    /**
     * Returns form fields in text format
     * @param <T> - Type parameter
     * @return Object containing form fields in text format
     */
    public <T> T result()
    {
        // Get dialog elements for current object
        DialogElement[][]   dialogElement = (DialogElement[][]) dlgElementsList.get( "dialogElement" );
        
        int numRows;                            // Number of rows passed
        
        // Add buttons to the form
        addTableButtons();
        
        // Display Table dialog form
        tblObj.displayForm();

        int headElms = arrayCount( dialogElement[0] );  // Number of head elements
        int cols = maxCols;                             // Number of columns

        // Create array for row counters
        int[] rows = new int[tableElement.length];

        int maxRows = 0;
	
        ObservableList items;
        
        // Loop to count the number of rows on each tab and find the max
        // Loop for each table element
        for ( int t = 0; t < tableElement.length; t++ )
        {
            // Set rows counter to zero
            rows[t] = 0;
            // Get items for for current table view
            items = (ObservableList) table.get(t).getItems();
            
            numRows = items.size();
            
            // Loop for each row specified through passed parameter
            for ( int i = 0; i < numRows; i++ )
                // If in the row there is some value entered
                if ( rowEntered(i, t) )
                    // Increment row counter
                    rows[t]++;

            // If for current table element rows counter is greater than maxRows 
            if ( rows[t] > maxRows )
                // Save current rows counter to maxRows 
                maxRows = rows[t];
            
        } // End of loop ** for each table element **
		
        // Create output object
        TableOutput output = new TableOutput( headElms, maxRows, cols, tableElement.length );
        
        // Get element content for current dialog form
        String[][]  elementContent = (String[][]) dlgElementsList.get( "elementContent" );
        
        System.arraycopy( elementContent[0], 0, output.header[0], 0, headElms ); // Get text value of head element

        // Loop for each tab
        for ( int t = 0; t < tableElement.length; t++ )
        {
            // Get items for for current table view
            items = (ObservableList) table.get(t).getItems();
            
            numRows = items.size();
            
            // Loop for each row number 
            for ( int i = 0, r = 0; i < numRows; i++ )
                // If there is some value entered in the row
                if ( rowEntered(i, t) )
                {
                    // Loop for each column 
                    for ( int c = 0; c < tableElement[t].length; c++ )
                        if ( tableElement[t][c] != null )
                            // Get current cell text value and store in output object
                            output.table[t][r][c] = (String) getCellValue( c+1, i, t );

                    // Increment non-empty row counter
                    r++;
                }
        }
        
        // Return output object
        return (T)output;

    } // End of method ** result **
    
    /**
    * Adds buttons to the form
    */
    private void addTableButtons()
    {
        // Get dialog's Scene object
        Scene  scene = (Scene) dlgElementsList.get( "scene" );

        // Create OK button object
        Button btnOK = new Button( "OK" );
        btnOK.setPadding( new Insets( 5, 20, 5, 20 ) );

        // Set event handler for on press button event
        btnOK.setOnAction( e ->
        {
            // If table elements pass validation
            if ( validateTableElements() ) 
                // If dialog elements passed validation
                if ( tblObj.validateDialogElements() ) 
                {
                    // Get dialog action rode lambda expression
                    DialogAction  code = (DialogAction) dlgElementsList.get( "code" );

                    // If there is another lambda expression to execute specified
                    if ( code != null )
                        // Run that lambda expression
                        code.run( null );

                    // Close dialog window
                    tblObj.close();
                }
        } ); // End of ** event handler for on press button event **

        // Create Cancel button object
        Button btnCancel = new Button( "Cancel " );
        btnCancel.setPadding( new Insets( 5, 20, 5, 20 ) );

        // Set event handler for on press button event
        btnCancel.setOnAction( e -> 
        {
            // Get rid of output information
            dlgElementsList.set( "dialogElement", null );
            // Close dialog window
            tblObj.close();
        } );

        // Get Pane from Scene object
        Pane pane = (Pane) scene.getRoot();

        // Create horizontal box for buttons
        HBox buttons = new HBox();
        buttons.setPadding( new Insets( 5, 5, 5, 5 ) );
        buttons.setSpacing( 5 );

        // Set box alignment to the right
        buttons.setAlignment( Pos.CENTER_RIGHT );

        // Add OK and Cancel buttons to buttons box
        buttons.getChildren().addAll( btnOK, btnCancel  );

        // Add buttons box to the pane
        pane.getChildren().addAll( buttons );

    } // End of method ** addButtons **
    
    /**
     * Validates table elements
     * @return False if something is wrong, or true otherwise
     */
    private boolean validateTableElements()
    {
        int  numRows;    // Number of rows passed
        ObservableList items ;
        String val; // To store cell text value
        
        int arrSize;

        // Loop for each tab
        for ( int t = 0; t < tableElement.length; t++ )
        {
            // Get items for for current table view
            items = (ObservableList) table.get(t).getItems();
	
            numRows = items.size();
            
            // Loop for each table row
            for ( int i = 0; i < numRows; i++ )
            {
                arrSize = arrayCount( tableElement[t] );
                
                // Loop for each table column
                for ( int j = 0; j < arrSize; j++ )
                {
                    // Get text value of current cell
                    val = (String) getCellValue( j+1, i, t );

                    // If cell value is not specified
                    if ( val == null )
                        val = "";

                    // If there is lambda expression for validating field value and row is not empty
                    if ( tableElement[t][j].validation != null && rowEntered(i, t) )
                        // If the current element does not pass validation
                        if ( !tableElement[t][j].validation.run( val ) )
                            return false;

                } // End for loop ** each table column **
            }                
        } // End for loop ** t **

        return true;

    } // End of method ** validateTableElements **

    /**
     * Gets table cell value
     * @param colNum Column number
     * @param rowNum Row number
     * @param tabNum Tab number
     * @return Table cell value
     */
    @SuppressWarnings("unchecked")
    private Object getCellValue( int colNum, int rowNum, int tabNum )
    {
        // Create Observable List object for table items list
        ObservableList<ArrayList<Data>> items = (ObservableList<ArrayList<Data>>) table.get(tabNum).getItems();

        // Get current row value
        ArrayList<Data> row = (ArrayList<Data>) items.get( rowNum );

        Data cellVal = (Data) row.get(colNum);
        
        // Current cell is specified
        if ( cellVal != null )
            // Return current cell value
            return cellVal.get();
        else
            return null;

    } // End of method ** getCellValue **
    
    /**
     * Checks if there is any info entered in the row
     * @param rowNum Row number
     * @param tabNum Tab number
     * @return True if at least in one column there is some value entered, or false otherwise
     */
    private boolean rowEntered( int rowNum, int tabNum )
    {
        int arrSize = arrayCount( tableElement[tabNum] );
        
        // Loop for each table column
        for ( int i = 0; i < arrSize; i++  )
            // If value in the cell is specified
            if ( getCellValue( i, rowNum, tabNum ) != null )
                // Return true because there is at least one cell with specified value
                return true;

        // No specified cells in the row
        return false;

    } // End of method ** rowEntered **
    
    /**
     * Checks if there is any info entered in the row
     * @param row Array containing row information
     * @return True if at least in one column there is some value entered, or false otherwise
     */
    private boolean rowEntered( ArrayList<Data> row )
    {
        // Loop for each row array element
        for ( Data rowVal : row )
            // If value is not empty
            if ( rowVal != null )
                return true;
                
        return false;
        
    } // End of method ** rowEntered **
    
    /**
     * Adds to the form Table elements
     */
    @SuppressWarnings( { "unchecked", "unused", "rawtypes" } )
    private void addTableElements()
    {
        // Get current Scene object
        Scene  scene = (Scene) dlgElementsList.get( "scene" );
        
        // If no table elements passed - just exit
        if ( tableElement == null )
            return;

        // If table elements are not specified
        if  ( tableElement.length < 1 ^ tableElement[0].length < 1 )
            return;

        TableCell cell;         // To hold current table cell		
        int barWidth = 15;	// Set scroll bar width
        
        TabPane tabPane = createTabPane(); // Create Tab pane object

        // Create main arrays: table, elementsList, data
        createMainArrays();
	
        TableView<ArrayList<Data>> curTable;
        
        // Loop for each table tab
        for ( int i = 0; i < tableElement.length; i++ )
        {
            // Create TableView for current tab
            createTableView(i);

            // Get TableView object for current tab
            curTable = table.get(i);
        
            // Get width of all columns
            int columnsWidth = calculateColumnsWidth(i);
            final int tabNum = i;

            // Create associative list for current tab
            AssociativeList elList = new AssociativeList();

            if ( tabs != null && tabs.length > 0 )
                elList.set( "Tab", tabs[i] );
            
            // Get the number of columns
            int cols = arrayCount( tableElement[i] );

            // Create Associative list for current Tab
            elementsList[i] = new AssociativeList[cols];
             
            // Add empty column to to current table
            TableColumn firstColumn = new TableColumn( "" );
            firstColumn.prefWidthProperty().bind( curTable.widthProperty().multiply( (double) 8/columnsWidth ) );
            curTable.getColumns().add( firstColumn );

            // Loop for each table column
            for ( int j = 0; j < cols; j++ )
            {
                // Create new column
                TableColumn column = new TableColumn( tableElement[i][j].columnName );

                // Create associative list for current column
                elementsList[i][j] = new AssociativeList();

                // Get current table element column name and store it as label name
                final String labelName = tableElement[i][j].columnName;
                
                // Get current columns attributes
                final TableElement tblEl = tableElement[i][j];

                ArrayList<String> initVal = new ArrayList<>();
                
                if ( tblEl.textValue != null && tblEl.textValue.length > 0 )
                    for ( int v = 0; v < tblEl.textValue.length; v++ )
                        initVal.add( tblEl.textValue[v] );
                
                // Create data element for current column
                elList.set( column.getText(), initVal );
                
                // If List reference  or text choices are passed
                if ( tblEl.textChoices.length > 0 || tblEl.list != null )
                    createComboBox( column, tblEl, elList );

                // If field should contain a date
                else if ( tblEl.valueType.contains( "Date" ) )
                    createDateDialog( column, tblEl, elList );
				
                // If field should contain File type field
                else if ( tblEl.valueType.contains( "File" ) )
                    createFileChooser( column, tblEl, elList );

                // If field should contain Tree List type field
                else if ( tblEl.valueType.contains( "Tree" ) )
                    createTreeList( column, tblEl, elList );
                
                // If field should contain Analytical Dimensions field
                else if ( tblEl.valueType.contains( "Dimension" ) )
                    createDimensions( column, tblEl, elList );
                
                // Otherwise - it's a text field    
                else
                    createTextField( column, tblEl, elList );

                final int colNum = j;

                // Set event handlers for current column
                setEditEventHandlers( column, tblEl, elList );

                // Set preferred width with value passed to the form
                column.prefWidthProperty().bind( curTable.widthProperty().multiply( (double) tableElement[i][j].width/columnsWidth  ) );

                // Add column to to current table
                curTable.getColumns().add( column );

            } // End of Loop  ** for each table column **
			
            // Create Ctrl Button column
            TableColumn column = createCtrlBtnColumn();

            // Set preferred width with value passed to the form proportionate to all columns width
            column.prefWidthProperty().bind( curTable.widthProperty().multiply( (double) ctrlBtnWidth/columnsWidth  ) );

            // Add column to to current table
            curTable.getColumns().add( column );

            // Set height for current table
            curTable.setPrefHeight( Math.min( numRows * 33, PREF_HEIGHT ));

            // Create Tab object for current table
            Tab tab = createTab(i);

            // Add current tab to the tab pane
            tabPane.getTabs().add( tab );

            // Highlight 1st table row
            curTable.getSelectionModel().select(0);
            
        } // End of loop for each table tab
		
        // Get pane from scene object
        Pane pane = (Pane) scene.getRoot();

        // If there is more than one table
        if ( table.size() > 1 )
            // Add tab pane to pane
            pane.getChildren().add( tabPane );
        else
            // Add current table to pane
            pane.getChildren().add( table.get(0) );

    } // End of method ** addTableElements **
    
    /**
     * Creates Tab Pane object
     * @return Created Tab Pane
     */
    private TabPane createTabPane()
    {
        TabPane tabPane = new TabPane();
        tabPane.setPrefHeight( PREF_HEIGHT );

        // Make closing tab unavailable
        tabPane.setTabClosingPolicy( TabPane.TabClosingPolicy.UNAVAILABLE );

        return tabPane;
        
    } // End of method ** createTabPane **

    /**
     * Creates main arrays for table dialog
     */
    private void createMainArrays()
    {
        // Maximum columns in tables
        maxCols = 0;

        // Loop for each table element
        for ( TableElement[] tblEl : tableElement ) 
            // If table length is more than maximum column width
            if ( tblEl.length > maxCols ) 
                // Save table length to maximum column width
                maxCols = tblEl.length;
        
        int numTabs = tableElement.length;
        
        elementsList = new AssociativeList[numTabs][];
        table = new ArrayList<>( numTabs );
        data = new ArrayList<>( numTabs );
        
    } // End of method ** createMainArrays **
    
    /**
     * Creates TableView object
     * @param tabNum Tab number where to create TableView object
     */
    private void createTableView( int tabNum )
    {
        // Create array matrix for data model
        ArrayList<ArrayList<Data>> matrix = new ArrayList<>();
        
        String[] textValue;  // To store current column text value
        
        // Array of data values for each row
        ArrayList<Data>[] curRow = new ArrayList[numRows];         
        
        // Get the number of columns for current table
        int numCol = tableElement[tabNum].length;
        
        // Loop for each table row
        for ( int r = 0; r < numRows; r++ )
        {
            // Create ArrayList for current row
            curRow[r] = new ArrayList<>(numCol+1);
            
            // Fill current row with null values
            for ( int k = 0; k < numCol+1; k++ )
                curRow[r].add( null );
        }
        
        // Loop for each column
        for ( int i = 1; i <= numCol; i++ )
        {
            // If current table element is not specified
            if ( tableElement[tabNum][i-1] == null )
                continue;

            // Get array of text values specified for current table element
            textValue = tableElement[tabNum][i-1].textValue;

            // If the array for text values is not specified
            if ( textValue == null )
                continue;
            
            // If the array for text values is empty
            if ( textValue.length == 0 )
                continue;
            
            // For each row of current column
            for ( int j = 0; j < textValue.length; j++ )
                curRow[j].set( i, Data.create( textValue[j] ) );
            
        } // End of For loop for each table element

        // Add created rows to matrix array
        for ( int k = 0; k < numRows; k++ )
            matrix.add( curRow[k] );
        
        // Create data model array from created Data array
        data.add( tabNum, FXCollections.observableList( (List) matrix ) );
        
        // Create TableView object
        TableView<ArrayList<Data>> newTable = new TableView<>( data.get(tabNum) );
        newTable.setEditable( true );
        
        // Place holder for empty cells
        newTable.setPlaceholder( new Label("") );
        // Add new table for current tab
        table.add( tabNum, newTable );
        
        // Create associative list array for elements list
        elementsList[tabNum] = new AssociativeList[numRows];
    
    } // End of method ** createTableView **
    
    /**
     * Calculates the width of all columns
     * @param tabNum Tab number on which table is located
     * @return  The width of all columns
     */
    private int calculateColumnsWidth( int tabNum )
    {
        int barWidth = 8; // Set scroll bar width
        
        int columnsWidth =  barWidth + ctrlBtnWidth; // For empty column and  control button column
        
        // Get the number of columns for table in specified tab
        int cols = arrayCount( tableElement[tabNum] );
        
        // Calculate width of all table columns for current tab
        for ( int j = 0; j < cols; j++ )
            columnsWidth += tableElement[tabNum][j].width;		

        columnsWidth += tableElement[tabNum].length + 2; // Additional pixels for column separators 

        // Get items for for current table view
        ObservableList items = (ObservableList) table.get(tabNum).getItems();
	
        // If there are more items in table than rows displayed
        if ( items.size() >= numRows )
            // Add additionally scroll bar width
            columnsWidth += barWidth;
                
        return columnsWidth;
        
    } // End of method ** calculateColumnsWidth **
    
    /**
     * Creates ComboBox object in specified column
     * @param column Column in which to create ComboBox object
     * @param tblEl Table element with ComboBox parameters
     * @param elList Associative list with table elements values
     */
    private void createComboBox( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
	String[] textChoices = null; // Array for text choices
		
        // If there are choices specified for the current element
        if ( tblEl.textChoices.length > 0 )
            textChoices = tblEl.textChoices;

        // If List reference is passed
        else if ( tblEl.list != null )
        {
            Item itm;
            
            int listSize = tblEl.list.size();
            
            // Create string array for text choices
            textChoices = new String[ listSize ];
            
            Iterator it = tblEl.list.iterator();
            int p = 0;
            
            // Fill array of text choices with text values
            while ( it.hasNext() )
            {
                itm = (Item) it.next();
                
                // Convert Item to String and add to array of text choices
                textChoices[p++] = itm .toString();
            }
            
            /*
            // Fill array of text choices with text values
            for ( int p = 0; p < listSize; p++ )
            {
                // Get Item element from the list
                itm = (Item) tblEl.list.get( p );
                // Convert Item to String and add to array of text choices
                textChoices[p] = itm .toString();
            }
            */
        }
		
	final String[] choices = textChoices; // Text coices for Cell factory methods
		
        // Set cell factory for current column
        column.setCellFactory( event -> 
        { 
            TableView tbl =  ((TableColumn) event).getTableView();
            // Get items for for current table view
            ObservableList tblItems = (ObservableList) tbl.getItems();
	    
            // Create combo box object for list of choices 
            // and define anonymous class overriding ComboBoxTableCell
            ComboBoxTableCell comboBox = new ComboBoxTableCell( choices )
            {
                // Gets called when cell item is updated
                @Override
                public void updateItem( Object item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );

                    ObservableList items;

                    // If cell is empty
                    if ( empty )
                        try
                        {
                            // Get items stored in associative elements list
                            items = (ObservableList) elList.get( column.getText() );
                        }
                        catch ( Exception ex )
                        {
                            // Clear the cell text if nothing in associative elements list
                            setText( "" );
                        }
                    // If cell is not empty
                    else
                    {
                        // Get column number from table view
                        int colNum = getTableView().getColumns().indexOf( getTableColumn() );
                        // Get current row number
                        int rowNum = this.getTableRow().getIndex();
                            
                        ArrayList<Data> row;
                        
                        // If row number is specified
                        if ( rowNum >= 0 )
                            // Get row from data model
                            row = (ArrayList<Data>) tblItems.get( rowNum );
                        else
                            // Get row from table view
                            row = (ArrayList<Data>) this.getTableRow().getItem();
                        
                        String cellVal = "";
                        
                        // If column number is specified and cell model value is not empty
                        if ( colNum != -1 && row != null && row.size() > 0 && row.get(colNum) != null  )
                            // Get cell model value to set text to that value 
                            cellVal = (String) row.get(colNum).get();
                        
                        setText( cellVal );
                        
                        if ( cellVal != null && !cellVal.isEmpty() )
                        {   
                            ArrayList<String> attr = elList.get( column.getText() );
                            
                            if ( attr == null )
                                attr = new ArrayList<>();
                            
                            if ( rowNum >= attr.size() )
                                attr.add( cellVal );
                            else
                                attr.set( rowNum, cellVal );
                            
                            //elList.set( column.getText(), cellVal );
                        } 
                    
                    } // End if ** cell is not empty **

                    // If item is specified 
                    if ( item != null && !empty )
                        // If there is action to be performed on element change
                        if ( tblEl.onChange != null )
                            // Invoke lambda expression when element changes
                            tblEl.onChange.run( item, elList );	
                    
                } // End of method ** updateItem **
				
                // Gets called when end-user starts editing table cell
                @Override
                public void startEdit()
                {
                    // Create cell text label
                    setText( null );
                    // Invoke parent class method
                    super.startEdit();

                    // Get filtered choices for current cell
                    String[] filteredChoices = (String[]) elList.get( column.getText() );

                    // If filtered choices are specified
                    if ( filteredChoices != null )
                        // Set filtered choices as items for current table cell
                        this.getItems().setAll( filteredChoices );
                    
                } // End of method ** startEdit **
                
            }; // End of ** anonymous class overriding ComboBoxTableCell **

            // Set editable attribute for combo box
            comboBox.setComboBoxEditable( tblEl.editable );
            
            // Create element list item for current column
            //elList.set( column.getText(), comboBox );

            // Return created combo box
            return comboBox;	
            
        } ); // End of ** cell factory for current column **

    } // End of method ** createComboBox **
	
    /**
     * Creates DatePicker object for specified column
     * @param column Column in which to create DatePicker object
     * @param tblEl Table element with DatePicker parameters
     * @param elList Associative list with table elements values
     */
    private void createDatePicker( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        column.setCellFactory( e -> 
        { 
            TextFieldTableCell<ArrayList<Data>, String> datePicker = new TextFieldTableCell<ArrayList<Data>, String>()
            {
                @Override
                public void startEdit()
                {
                    // Create date picker object and set it as cell graphic
                    DatePicker date = new DatePicker();
                    setGraphic( date ); 
                    
                    ArrayList<Data> row = (ArrayList<Data>) this.getTableRow().getItem();
                        
                    // Get column number from table view
                    int colNum = getTableView().getColumns().indexOf( getTableColumn() );
                    
                    Object val = date.getValue();
                    String dateVal;
                    
                    // If value is specified
                    if ( val != null )
                        // Convert value to string and save to element content array
                        dateVal = val.toString();
                    else
                        dateVal = "";

                    //setGraphic( null );
                    setText( dateVal );
                    row.set( colNum, Data.create(dateVal) );
                }
                
                @Override
                // Gets called when cell item is updated
                public void updateItem( String item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );
					
                    // If item is not empty
                    if ( !empty )
                    {
                        // Get row from table view
                        ArrayList<Data> row = (ArrayList<Data>) this.getTableRow().getItem();
                        // Get column number from table view
                        int colNum = getTableView().getColumns().indexOf( getTableColumn() );

                        // If column number is specified and cell model value is not empty
                        if ( colNum != -1 && row != null && row.size() > 0 && row.get(colNum) != null  )
                        {
                            // Create new DatePicker object
                            DatePicker datePicker = new DatePicker();

                            // Set value of created DatePicker to what is stored in current data model cell
                            datePicker.setValue( LocalDate.parse( (String) row.get(colNum).get() ) );
                            
                            // Set cell graphic to created DatePicker
                            //setGraphic( datePicker 
                            setText( (String) row.get(colNum).get() );
                        }
                        else
                            setGraphic( null );
                    }

                    // If item is specified 
                    if ( item != null && !empty )
                        // If there is action to be performed on element change
                        if ( tblEl.onChange != null )
                            // Invoke lambda expression when element changes
                            tblEl.onChange.run( item, elList );	
                    
                } // End of ** method updateItem **
            };
            // Create data element for current column
            elList.set( column.getText(), datePicker );

            // Return created text field
            return datePicker;	
        } );

    } // End of method ** createDatePicker **
	
    /**
     * Creates FileChooser object in specified column
     * @param column Column in which to create FileChooser object
     * @param tblEl Table element with FileChooser parameters
     * @param elList Associative list with table elements values
     */
    private void createFileChooser( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        // Set column cell factory
        column.setCellFactory( e -> 
        { 
            // Create file picker object with anonymous class extending TableCell
            TableCell filePicker = new TableCell()
            {
		@Override  
                // Gets invoked when user starts editing table cell
                public void startEdit() 
                {  
                    // Create text field object
                    TextField textField = createTextField();

                    // Create button object
                    Button btn = createFileChooserBtn( textField );

                    // Create horizontal box for file field
                    HBox fileField = new HBox(); 
                    // Add text field and button to file field box
                    fileField.getChildren().addAll( textField, btn );

                    // Set file field as graphic to display
                    setGraphic( fileField ); 
                    
                } // End of method ** startEdit **
				
                @Override
                // Gets called when cell item is updated
                public void updateItem( Object item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );
					
                    // If item is not empty
                    if ( !empty  )
                    {
                        // Get current table view row
                        ArrayList<Data> row = (ArrayList<Data>) this.getTableRow().getItem();
                        // Get current column number
                        int colNum = getTableView().getColumns().indexOf( getTableColumn() );

                        // If column number is specified and cell model value is not empty
                        if ( colNum != -1 && row != null && row.size() > 0 && row.get(colNum) != null && !((String) row.get(colNum).get()).isEmpty() )
                        {
                            // Create text field object
                            TextField textField = createTextField();

                            // Create button object
                            Button btn = createFileChooserBtn( textField );

                            // Create horizontal box for file field
                            HBox fileField = new HBox(); 
                            textField.setPrefHeight(20);
                            // Add text field and button to file field box
                            fileField.getChildren().addAll( textField, btn );

                            // Set value of created text field to what is stored in current data model cell
                            textField.setText( (String) row.get(colNum).get() );

                            // Set cell graphic to what is in file field
                            setGraphic( fileField );
                        }
                        else
                            setGraphic( null );
                    }
                    else
                        setGraphic( null );

                    // If item is specified 
                    if ( item != null && !empty )
                        // If there is action to be performed on element change
                        if ( tblEl.onChange != null )
                            // Invoke lambda expression when element changes
                            tblEl.onChange.run( item, elList );
                    
                } // End of method ** updateItem **
                
            }; // End of ** file picker object with anonymous class extending TableCell **

            // Set file picker Editable attribute
            filePicker.setEditable( tblEl.editable );

            // Create data element for current column
            elList.set( column.getText(), filePicker );

            // Return created file picker
            return filePicker;
            
        } ); // End of ** column cell factory **
    
    } // End of method ** createFileChooser **
	
    /**
     * Creates TextField object
     * @return Created object
     */
    private TextField createTextField()
    {
        // Create text field object
        TextField textField = new TextField();
        // Set width of text field
        textField.setPrefWidth( PREF_WIDTH );
        // Set height of text field
        textField.setPrefHeight(20);
        // Make text field non-editable
        textField.setEditable( false );
        textField.setStyle("-fx-border-width: 0 0 0 0;"
                          +"-fx-padding: 3;");
        return textField;
    
    } // End of method ** createTextField **
	
    /**
     * Creates FileChooser Button object
     * @param textField Text field where to place selected file
     * @return Created button object
     */
    private Button createFileChooserBtn( TextField textField )
    {
        // Create button object to select file
        Button btn = new Button("...");
        btn.setPrefWidth(15);
        btn.setPrefHeight(20);
        btn.setStyle("-fx-padding: 2;");

        // Create file chooser object
        FileChooser fileChooser = new FileChooser();
        // Set title for file chooser window
        fileChooser.setTitle( "Select file" );

        // Set event handler for press button event
        btn.setOnAction( e -> 
        {
            // Display Dialog window for file chooser
            File file = fileChooser.showOpenDialog( stage );

            // If file is specified
            if ( file != null )
                // Set full file path as text field text
                textField.setText( file.getPath() );
            
        } ); // End of ** event handler for press button event **

        return btn;
        
    } // End of method ** createFileChooserBtn **
	
    /**
     * Creates TreeList object in specified column
     * @param column Column in which to create TreeList object
     * @param tblEl Table element with TreeList parameters
     * @param elList Associative list with table elements values
     */
    private void createTreeList( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        // Set column cell factory
        column.setCellFactory( e -> 
        { 
            // Create Tree List object with anonymous class extending TableCell
            TableCell treeList = new TableCell()
            {
                // Gets invoked when user starts editing table cell
                @Override
                public void startEdit() 
                {  
                    // Create text field object
                    TextField textField = createTextField();

                    // Create button object
                    Button btn = createTreeListBtn( textField, tblEl, elList );

                    // Create horizontal box for tree list field
                    HBox treeListField = new HBox(); 
                    // Add text field and button to tree list box
                    treeListField.getChildren().addAll( textField, btn );

                    // Set tree list field as graphic to display
                    setGraphic( treeListField );  
                    
                } // End of method ** startEdit **
				
                // Gets called when cell item is updated
                @Override
                public void updateItem( Object item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );

                    // If item is not empty
                    if ( !empty )
                    {
                        ArrayList<Data> row = (ArrayList<Data>) this.getTableRow().getItem();
                        int colNum = getTableView().getColumns().indexOf( getTableColumn() );

                        // If column number is specified and cell model value is not empty
                        if ( colNum != -1 && row.get(colNum) != null && !((String) row.get(colNum).get()).isEmpty() )
                        {
                            // Create text field object
                            TextField textField = createTextField();

                            // Create button object
                            Button btn = createTreeListBtn( textField, tblEl, elList );

                            // Create horizontal box for tree list field
                            HBox treeListField = new HBox(); 
                            textField.setPrefHeight(20);
                            // Add text field and button to tree list field box
                            treeListField.getChildren().addAll( textField, btn );

                            // Set text field to what is stored in data model cell
                            textField.setText( (String) row.get(colNum).get() );

                            // Set cell graphic to created tree list field
                            setGraphic( treeListField );
                        }
                        else
                            setGraphic( null );
                    }
                    else
                        setGraphic( null );

                    // If item is specified 
                    if ( item != null && !empty )
                        // If there is action to be performed on element change
                        if ( tblEl.onChange != null )
                            // Invoke lambda expression when element changes
                            tblEl.onChange.run( item, elList );	
                    
                } // End of method ** updateItem **
                
            }; // End of ** Tree List object with anonymous class extending TableCell **
			
            // Set Tree List Editable attribute
            treeList.setEditable( tblEl.editable );

            // Create data element for current column
            elList.set( column.getText(), treeList );

            // Return created Tree List
            return treeList;
            
        } ); // End of ** column cell factory **
    
    } // End of method ** createTreeList **

    /**
     * Creates TreeList select Button object
     * @param textField Text field in which selected tree element will be placed
     * @param tblEl Table element with TreeList parameters
     * @param elList Associative list with table elements values
     * @return Created select Button object
     */
    private Button createTreeListBtn( TextField textField, TableElement tblEl, AssociativeList elList )
    {
        // Create button object to select from tree list
        Button btn = new Button("...");
        btn.setPrefWidth(15);
        btn.setPrefHeight(20);
        btn.setStyle("-fx-padding: 2;");

        // Set event handler for press button event
        btn.setOnAction( e -> 
        {
            TreeDialog obj = null; // To store created TreeDialog object

            // If value type is specified
            if ( !tblEl.valueType.isEmpty() )
            {
                // Create class object by its name
                Class cls = createDataClass( tblEl.valueType );
                    
                try 
                {
                    Constructor cnstr; // To store class constructor object

                    // If tree width is specified
                    if ( tblEl.width > 0 )
                    {
                        // Get class constructor with parameters stage and width
                        cnstr = cls.getConstructor( Stage.class, int.class );
                        // Create TreeDialog object with class constructor
                        obj = (TreeDialog) cnstr.newInstance( stage, tblEl.width );	
                    }
                    else
                    {
                        // Get class constructor with parameter stage
                        cnstr = cls.getConstructor( Stage.class );
                        // Create TreeDialog object with class constructor
                        obj = (TreeDialog) cnstr.newInstance( stage );
                    }
                } 
                catch ( Exception ex ) 
                {}
            }

            // If object was created
            if ( obj != null )
            {
                // Display dialog form and get the result of submit button pressed
                String result = obj.result( btn );

                // If something was selected
                if ( result != null && !result.isEmpty() )
                    // Save full file path to text field
                    textField.setText( result );	
            }
        } ); // End of ** event handler for press button event **

        // Return created button
        return btn;
    
    } // End of method ** createTreeListBtn **
	
    /**
     * Creates Date dialogue form in a specified column
     * @param column Column in which to create TreeList object
     * @param tblEl Table element with TreeList parameters
     * @param elList Associative list with table elements values
     */
    private void createDateDialog( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        column.setCellFactory( e -> 
        { 
            TextFieldTableCell<ArrayList<Data>, String> datePicker = new TextFieldTableCell<ArrayList<Data>, String>()
            {
                @Override
                public void startEdit()
                {
                    // Get data model for current row 
                    ArrayList<Data> row = (ArrayList<Data>) this.getTableRow().getItem();

                    // Get column number from table view
                    int colNum = getTableView().getColumns().indexOf( getTableColumn() );
                    
                    Data cellVal = row.get(colNum);
                    
                    String initVal = ( cellVal != null ? (String) cellVal.get() : "" );
                        
                    String result = new DateDialog( stage, initVal ).result( this );

                    if ( result != null )
                    {
                        setText( result );
                        
                        row.set( colNum, Data.create( result ) );
                    }
                }
                
                @Override
                // Gets called when cell item is updated
                public void updateItem( String item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );

                    // If table cell is not empty
                    if ( !empty )
                    {
                        // Get data model for current row 
                        ArrayList<Data> row = (ArrayList<Data>) this.getTableRow().getItem();
                        
                        // Get column number from table view
                        int colNum = getTableView().getColumns().indexOf( getTableColumn() );

                        // If column number is specified and data model cell is not empty
                        if ( colNum != -1 && row != null && row.size() > 0 && row.get(colNum) != null  )
                            setText( (String) row.get(colNum).get() );
                        else
                            setText( "" );
                    }

                    // If item is specified 
                    if ( item != null && !empty )
                        // If there is action to be performed on element change
                        if ( tblEl.onChange != null )
                            // Invoke lambda expression when element changes
                            tblEl.onChange.run( item, elList );	
                    
                } // End of ** method updateItem **
            };
            // Create data element for current column
            elList.set( column.getText(), datePicker );

            // Return created text field
            return datePicker;	
        } );
    
    }
    
    /**
     * Creates Dimensions dialogue form in a specified column
     * @param column Column in which to create TreeList object
     * @param tblEl Table element with TreeList parameters
     * @param elList Associative list with table elements values
     */
    private void createDimensions( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        column.setCellFactory( e -> 
        { 
            TextFieldTableCell<ArrayList<Data>, String> dimensions;
            dimensions = new TextFieldTableCell<ArrayList<Data>, String>()
            {
                @Override
                public void startEdit()
                {
                    int index = this.getTableRow().getIndex();
                    
                    ArrayList<String> dimsAttr = (ArrayList<String>) elList.get( column.getText() ); 
                    ArrayList<String> acctAttr = (ArrayList<String>) elList.get( "G/L Acct" );
                    
                    String initVal = "", accnt = "";
                    GLAccount account;
                    
                    if ( dimsAttr.size() > 0 && index < dimsAttr.size() )
                        initVal = dimsAttr.get( index );
                   
                    if ( acctAttr.size() > 0 && index < acctAttr.size() )
                        accnt = acctAttr.get( index );
                    
                    String tab = (String) elList.get( "Tab" );
                    
                    if ( tab != null && !tab.isEmpty() )
                        account = GLAccount.getByCode( accnt, tab );
                    else
                        account = GLAccount.getByCode( accnt );

                    String[] result = new DynamicDimensions( account, stage, initVal ).result( this );

                    if ( result != null && result.length > 0 )
                    {
                        String displStr = "";

                        for ( int i = 0; i < result.length; i++ )
                            displStr += result[i] + ( i < result.length-1 ? "/" : "" );

                        setText( displStr );
                        
                        ArrayList<Data> row = (ArrayList<Data>) this.getTableRow().getItem();
                        
                        // Get column number from table view
                        int colNum = getTableView().getColumns().indexOf( getTableColumn() );

                        row.set( colNum, Data.create(displStr) );
                        
                        if ( dimsAttr == null )
                            dimsAttr = new ArrayList<>();

                        if ( index >= dimsAttr.size() )
                            dimsAttr.add( displStr );
                        else
                            dimsAttr.set( index, displStr );
                    }
                }
                
                @Override
                // Gets called when cell item is updated
                public void updateItem( String item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );

                    // If table cell is not empty
                    if ( !empty )
                    {
                        // Get data model for current row 
                        ArrayList<Data> row = (ArrayList<Data>) this.getTableRow().getItem();
                        
                        // Get column number from table view
                        int colNum = getTableView().getColumns().indexOf( getTableColumn() );

                        // If column number is specified and data model cell is not empty
                        if ( colNum != -1 && row != null && row.size() > 0 && row.get(colNum) != null  )
                            setText( (String) row.get(colNum).get() );
                        else
                            setText( "" );
                    }
                    
                    // If item is specified 
                    if ( item != null && !empty )
                        // If there is action to be performed on element change
                        if ( tblEl.onChange != null )
                            // Invoke lambda expression when element changes
                            tblEl.onChange.run( item, elList );	
                    
                } // End of ** method updateItem **
            };
            
            // Return created text field
            return dimensions;	
        } );
    }
    
    /**
     * Creates TextField object in specified column
     * @param column Column in which to create TextField object
     * @param tblEl Table element with TextField parameters
     * @param elList Associative list with table elements values
     */
    private void createTextField( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        // If the field should contain a number
        if ( tblEl.valueType.contains( "Number" ) )
            // Set column alignment to the right
            column.setStyle( "-fx-alignment: center-right;" );
        
        // Set current column Cell Factory
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
                        // Get data model for current row 
                        ArrayList<Data> row = (ArrayList<Data>) this.getTableRow().getItem();
                        
                        // Get column number from table view
                        int colNum = getTableView().getColumns().indexOf( getTableColumn() );

                        // If column number is specified and data model cell is not empty
                        if ( colNum != -1 && row != null && row.size() > 0 && row.get(colNum) != null  )
                            setText( (String) row.get(colNum).get() );
                        else
                            setText( "" );
                    }

                    // If item is specified 
                    if ( item != null && !empty )
                        // If there is action to be performed on element change
                        if ( tblEl.onChange != null )
                            // Invoke lambda expression when element changes
                            tblEl.onChange.run( item, elList );	
                    
                } // End of ** method updateItem **
                
            }; // End of ** anonymous class overriding TextFieldTableCell **
	
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
            } ); // End of ** String Converter for current text field **

            // Create data element for current column
            elList.set( column.getText(), textField );

            // Return created text field
            return textField;	
            
        } ); // End of ** column Cell Factory **
    
    } // End of method ** createTextField **
    
    /**
     * Sets event handlers for specified column
     * @param column Column for which to set event handlers
     * @param tblEl Table element with column field parameters
     * @param elList Associative list with table elements values
     */
    private void setEditEventHandlers( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        // Set event handler for start edit event
        column.setOnEditStart( event -> 
        {
            TableColumn col = (TableColumn) event.getSource();
            
            // Get current row number
            int rowNum = ((TableColumn.CellEditEvent) event).getTablePosition().getRow();
            
        } ); // End of ** event handler for start edit event **

        // Set event handler for on edit commit event
        column.setOnEditCommit( event -> 
        { 
            // Get current table view
            TableView tblView = ((TableColumn.CellEditEvent) event).getTableView();

            // Get items for for current table view
            ObservableList items = (ObservableList) tblView.getItems();
			
            // Get row number for current table position
            int rowNum = ((TableColumn.CellEditEvent) event).getTablePosition().getRow();

            // Get new value for current table cell
            String newValue = (String)((TableColumn.CellEditEvent) event).getNewValue();

            // Get current row data array
            ArrayList<Data> row = (ArrayList<Data>) items.get( rowNum );
            
            // Get current column number
            int colNum = tblView.getColumns().indexOf( column );

            // If data value is not specified for current cell
            if ( row.get( colNum ) == null )
                // Create Data object for current cell
                row.set( colNum, Data.create( newValue ) );
            else
                // Set value of current cell to new value
                row.get( colNum ).set( newValue );

            // If there is action to be performed on cell value change
            if ( tblEl.onChange != null )
                // Invoke lambda expression for on value change event
                tblEl.onChange.run( newValue, elList ); 
            
        } ); // End of ** event handler for on edit commit event **
    
    } // End of method ** setEditEventHandlers **
    
    /**
     * Creates column for Table line control button
     * @return Created column
     */
    private TableColumn createCtrlBtnColumn()
    {
        // Create new column
        TableColumn column = new TableColumn( "*" );
	
        // Set column cell factory
        column.setCellFactory( e -> 
        { 
            TableView tbl =  ((TableColumn) e).getTableView();
            
            // Get items for for current table view
            ObservableList tblItems = (ObservableList) tbl.getItems();
	    
            // Create button object with anonymous class extending TableCell
            TableCell controlButton = new TableCell()
            {
                @Override  
                public void startEdit() 
                {  
                    // Create Control button menu options
                    String[] menu = new String[]{ "Ins before", "Ins after", "Delete", "Esc" };

                    // Display List Dialog and get the result of selection
                    String result = new ListDialog( stage, menu, 75, 100 ).result( this );

                    // Get items for for current table view
                    ObservableList tblItems = (ObservableList) tbl.getItems();

                    // Set number of columns as the length of underlying data model array row
                    final int numCols = ( tblItems != null ? ((ArrayList<Data>) tblItems.get(0)).size() : 0 );

                    // Get row number for current table position
                    int rowNum  = tbl.getSelectionModel().getSelectedIndex();

                    try
                    {
                        // If Selected Ins menu item
                        if ( result.startsWith( "Ins" ) )
                        {
                            // Creaate ArrayList for new row
                            ArrayList<Data> newRow = new ArrayList<>();

                            for (  int i = 0; i < numCols; i++) 
                                // Fill all columns with null value
                                newRow.add(null);

                            // If it's Insert before menu item
                            if ( result.endsWith( "before" ) )
                                // Add new row before current row
                                tblItems.add( rowNum, newRow );

                            // If it's Insert after menu item
                            else if ( result.endsWith( "after" ) )
                                // Add new row after current row
                                tblItems.add( rowNum+1, newRow );
                        }
                        // If selected Delete menu item
                        else if ( result == "Delete" )
                            // Remove current row
                            tblItems.remove( rowNum );
                    }
                    catch ( Exception e )
                    {}

                    // Create button object and set it as cell graphic
                    setText( "*" );
                }
				
                // Gets called when cell item is updated
                @Override
                public void updateItem( Object item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );
			
                    // If item is not empty
                    if ( !empty )
                    {
                        // Get current TableView row number
                        int rowNum = this.getTableRow().getIndex();

                        ArrayList<Data> row;

                        // If row number is specified
                        if ( rowNum >= 0 )
                            // Get row from data model
                            row = (ArrayList<Data>) tblItems.get(rowNum);
                        else
                            // Get row from table view
                            row = (ArrayList<Data>) this.getTableRow().getItem();
                        
                        // If row is not empty
                        if ( row != null && rowEntered( row ) )
                            setText( "*" );
                        else
                            setGraphic( null );
                    }
                    else
                        setGraphic( null );
                    
                } // End of method ** updateItem **
                
            }; // End of ** anonymous class extending TableCell **
		
            // Set control button Editable attribute
            controlButton.setEditable( true );

            // Return created control buttons
            return  controlButton;
            
        } ); // End of ** column cell factory **

        return column;
    
    } // End of method ** createCtrlBtnColumn **
    
    /**
     * Creates Control button object for table view
     * @param tbl TableView object
     * @return Created Button object
     */
    private Button createCtrlBtn( TableView tbl )
    {
        // Create button object
        Button btn = new Button("*");
        btn.setPrefWidth( ctrlBtnWidth );
        btn.setPrefHeight(15);
        btn.setStyle("-fx-padding: 2;");
        
        // Set event handler for press button event
        btn.setOnAction( event -> 
        {
            // Create Control button menu options
            String[] menu = new String[]{ "Ins before", "Ins after", "Delete", "Esc" };

            // Display List Dialog and get the result of selection
            String result = new ListDialog( stage, menu, 75, 100 ).result( btn );

            // Get items for for current table view
            ObservableList tblItems = (ObservableList) tbl.getItems();
	    
            // Set number of columns as the length of underlying data model array row
            final int numCols = ( tblItems != null ? ((ArrayList<Data>) tblItems.get(0)).size() : 0 );
            
            // Get row number for current table position
            int rowNum  = tbl.getSelectionModel().getSelectedIndex();

            try
            {
                // If Selected Ins menu item
                if ( result.startsWith( "Ins" ) )
                {
                    // Creaate ArrayList for new row
                    ArrayList<Data> newRow = new ArrayList<>();
                    
                    for (  int i = 0; i < numCols; i++) 
                        // Fill all columns with null value
                        newRow.add(null);
                    
                    // If it's Insert before menu item
                    if ( result.endsWith( "before" ) )
                        // Add new row before current row
                        tblItems.add( rowNum, newRow );

                    // If it's Insert after menu item
                    else if ( result.endsWith( "after" ) )
                        // Add new row after current row
                        tblItems.add( rowNum+1, newRow );
                }
                // If selected Delete menu item
                else if ( result == "Delete" )
                    // Remove current row
                    tblItems.remove( rowNum );
            }
            catch ( Exception e )
            {}
        } ); // End of ** event handler for press button event **
        
        return btn;
        
    } // End of method ** createCtrlBtn **
            
    /**
     * Creates Tab object
     * @param tabNum Tab number
     * @return Created Tab
     */
    private Tab createTab( int tabNum )
    {
        // Create Tab object for current tab
        Tab tab = new Tab( );

        // If there is array of tab names
        if ( tabs != null )
            // Set name for current tab
            tab.setText( tabs[tabNum] );
        else
            // Set current column name as tab name
            tab.setText( tableElement[tabNum][0].columnName );

        // Set table as content of current cell
        tab.setContent( table.get(tabNum) );

        return tab;
    
    } // End of method ** createTab **
    
    public TableDialog( OneColumnDialog outerThis, RegistryItem doc )
    {
        tblObj = outerThis;
        dlgElementsList = tblObj.getDialogAttributesList();
        numRows = tblObj.get( "numRows" );
        stage = outerThis;
        tabs = tblObj.get( "tabs" );
        
        AssociativeList attr = doc.getAttributesList();    
        tableElement = (TableElement[][]) attr.get( "table" );
        
        addTableElements();
    }  
} // End of class ** TableDialog **
# Item Management
Lab 1: Item Management 
(FPT Univesity HCM)
(Java Desktop)

## Program Specifications:
In this assignment, you are required to build an items management application, in the form of a desktop application. The program has basic functions: login, add-delete-edit item and supplier’s information. You are required to use the basic components to design interfaces, use the tabbed pane to organize item management and supplier management on the one screen. The database used is the Microsoft SQL Server. Program organization must clearly separate functions according to MVC model.

## Features:
- Login
 o In order to access the supplier and item management, an authentication is required.
 o The actor enters userID and password, the function checks if the userID with the password is in the available user list, then grant the access permission. If not, a message would appear no notify that user is not found.
 o If login is successful, then go directly to the management screen.

- Display Supplier 
 o The screen is divided into 2 parts: Main information and Detailed information.
 o Main part: this part lists all available supplier with their information (supCode, supName, address) in the system.
 o Detail part: When you click a row on the table, the details of the respective supplier are displayed some information such as supCode(disabled), supName, address, collaborating.
 o In this detail part, 03 buttons is shown to perform following functions such as Add New, Save and Delete button.
 
- Add new Supplier
  o The user presses the Add New button to clear the information at detail part, preparing for new data entry.
  o The user inputs new supplier information. Then user clicks the Save button.
  o The program checks the validity of data, if data is not valid then display an error message, otherwise insert new supplier into the database.
  o The supplier table must be refreshed after new data has been successfully inserted.
  
- Edit Supplier
 o The user clicks on the supplier he wants to modify on the supplier table.
 o The details of the respective supplier are displayed.
 o The user changes the information of the supplier (not allow modify the supCode). Then user clicks the Save button.
 o The program checks the validity of data, if data is not valid then display an error message, otherwise system updates supplier information.
 o The supplier table must be refreshed after new data has been successfully updated.
 
- Delete Supplier
 o The user clicks on the supplier he wants to delete on the supplier table. Then user clicks the Delete button.
 o The program must display a message to confirm the deletion. If the user confirms, system will delete the selected supplier.
 o The supplier table must be refreshed after data has been successfully deleted.
 
- Display Items
 o The screen is divided into 2 parts: Main information and Detail information.
 o Main part: this part shows all available Item with their information such as itemCode, itemName, supplier(supCode-supName), unit, price, supply in the system.
 o Detailed part: When you click a row on the table, the details of the respective item are displayed with following information as itemCode(disabled), itemName, supplier, unit, price, supplying. The suppliers are displayed in to combo box (choice) component. All available suppliers are loaded into the Supplier combo box as supCode-supName format.
 o 03 buttons Add New, Save, Delete button are add to detail part.
 
- Add new Item
 o The user presses the Add New button to clear the information in detail part and system preparies for new data entry.
 o The user inputs new item information. Then user clicks the Save button.
 o The program checks the validity of data, if data is not valid then display an error message, otherwise system will insert new item into the database.
 o The item table must be refreshed after new data has been successfully inserted
 
- Update item
 o The user clicks on the item he wants to modify on the item table.
 o The details of the respective item are displayed.
 o The user changes the information of the item (not allow modify the itemCode). Then user clicks the Save button.
 o The program checks the validity of data, if data is not valid then display an error message, otherwise system will update item information.
 o The item table must be refreshed after data has been successfully updated
 
- Delete Item
 o The user clicks on the item he wants to delete on the item table. Then user clicks the Delete button.
 o The program must display a message to confirm the deletion. If the user confirms, system will delete the selected item.
 o The item table must be refreshed after data has been deleted.
 
## Connect me via 
1. [Facebook](https://fb.me/ngvbach2000)
2. [Email](mailto:ngvbach2000@gmail.com)

#### © 2020 by @ngvbach2000:cow:

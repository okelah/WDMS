#Web-Based Document Management System User Guide
-------------
#### Table of Contents
1. Overview of the user guide
  * Purpose of the user guide
  * Convention in this user guide
  * Recommended reading.
2. Overview of the system
  * Why use the system
  * Introduction to the system
  * Terminology used in the system
  * Accessing the website
3. Creating a new account
  * Finding the account creation page
  * Filling in account details & requesting permissions
4. Logging into and out of the system
  * Prerequisities
  * Logging-in
  * Logging-out
5. Uploading documents
  * Finding the document upload page 
  * Select the files to upload
6. Listing documets
  * Finding the list of documents page
  * Viewing and organizing the list
  * Selecting a file
7. Accessing Documents
  * Reading a file
  * Downloading a file
8. Updating Documents
9. Sharing Documents
10. Deleting Documents
11. Checking in and out documents
12. Encrypting and Decrypting Documents
13. System Administrator specific functionality
  *	List Users
  * List role requests
  *	Create, Delete, Modify User
14. Appendix
  * Installation
     *  Required environment for the system
     *	Steps to install the system
     *	How to start the system.
  * Trouble-shooting and limitations
  * References


###1. Overview of the user guide
======

 * #####Purpose of the user guide.

  This user guide is intended to explain the features of (Web Document Management System) WDMS and show how to use them, in a manner the typical user would comprehend. 

 * #####Convention in this user guide

  The user guide can be read through like a tutorial, or it can be used as a reference material to look up a specific function or set of functions.  More complex functionality may rely on a concept described earlier in the user guide, in that case a reference will be made to the appropriate section.

 * #####Recommended reading.

  If at any point you don’t understand the terms used in this document look to section 2.3 (Terminology used in the system), which explains terms used.  If you are unable to follow the directions, we suggest that you read “The Internet for Dummies” [1] which is a good starting point for someone with little to no internet experience.


###2. Overview of the system
======

 * #####Why use the system?

  This system provides a simple way to manage and collaborate by sharing documents between employees (and guests) within a single organization.  This system centralizes document management and ensures that documents are stored in a secure manner.

 * #####Introduction to the system

  The WDMS (Web Document Management System) is a website which can be accessed over the internet from any location, and at any time of the day or night.  The system (WDMS) will hold your documents, and allow you to share these documents with others including responsible department managers and corporate level users.  To use this system you must first have a computer with access to the internet.  If you are working with large documents, it is recommended that you use a good internet connection, or performance may be degraded.

 * #####Terminology used in the system

  WDMS – Web Document Management System
  This is the system which this user guide is introducing you to.
  
 * #####Accessing the website

  The first thing we must do is access the website.  To so this open your web-browser (Internet Explorer or FireFox or Chrome, etc.) and type into your address bar the following ‘https://10.0.2.111’.  The website should appear with a welcome screen for the WDMS.  If you don’t see the website refer to section B of the appendix under ‘Trouble-shooting and limitations’.
 
###3. Creating a new account
======

 * #####Finding the account creation page

  Now that you have accessed the website, we will need to get some details form you in order to create your account.  On the webpage you will notice that in the upper right hand of the screen there is a button labeled “Log in”.  Click this button once.  Two new options have appeared, one is to log-in to an existing account, and the second is to create a new account.  Click the button “create a new account”.
  ![alt tag](https://github.com/biubiuX/WDMS/blob/master/pic/1.jpg)

 * #####Filling in account details & requesting permissions

 At this point you are asked for your email address.  This is the email address which you will use the log-in to the system in the future.  You are asked to create a password which conforms to the password policy. Password should be alplhanumeric. It must contain 8-16 characters, with no whitespaces, and at least 1 lowercase & 1 upper case & 1 non-alphanumeric characters.  A third option is also present which asks what your role is in the organization.    This is necessary so that we can assign you the proper rights and permissions as designated by your role. Also the department to which the user belongs is selected from the drop downlist. Finally the captcha needs to be entered.  When you have finished entering all your data, click the button at the bottom of the page labeled “Create my account”.  This will send your request to the administrator who will approve or deny it as soon as possible.  For now you must wait until your account has been approved before you can do anything.
 ![alt tag](https://github.com/biubiuX/WDMS/blob/master/pic/2.jpg)
 
 
###4. Logging into and out of the system
======

 * #####Prerequisites

  At this stage you must have already created an account. And the account is approved by the system administrator

 * #####Logging-in

  Look at the top right hand of the webpage, there should be a button labeled “Log in / Create Account”, click that button.  Two new options have appeared, you will be using the first one, which is to log into an existing account.  Type in your email and password, then click the button labeled “Login”.  A new webpage should load.  Look in the top right hand corner of the screen where you previously saw the “Log in” button.  That button has been replaced with one labeled with your email address.
  
   * #####Logging-out

  Look at the top right hand of the webpage, there should be a button labeled with your email address, click that button.  A list of new options have appeared.  Click the option labeled “Log out”.  The page should return you to the login screen.

###5. Uploading documents
======
 Inorder to upload documents you need to first sign into the WBDS. Only users of type regular employee, department manage and coroporate management are eligible to upload documents.

 * #####Finding the  document upload page

  In order to upload a document you must first navigate to the correct page where you can specify which documents you wish to upload.  Look at the top of the webpage at the navigation menu.  There is a link labeled “Upload Documents”, click that link.

 * #####Select the files to upload

  Now you can select the files you wish to send to the website (upload).  To do this, click the button labeled “Add Files”.  A new window will appear listing the documents on your local computer system, which you can choose from.  Navigate to the document and click “open”, to select the document for upload.  You may select multiple documents to upload at once.  When you want to start the upload click “Start Upload”.  A progress bar will indicate the progress (in percentage) as the file is uploaded, any errors will be displayed here.
 ![alt tag](https://github.com/biubiuX/WDMS/blob/master/pic/3.jpg)
 
###6. Listing documents
======

 * #####Finding the list of documents page

  To view the list of documents you have uploaded and those which have been shared with you, navigate to the Documents page.  Look at the top of the webpage at the navigation menu.  There is a link labeled “Documents”, click that link.
 
 * #####Viewing and organizing the list
 ![alt tag](https://github.com/biubiuX/WDMS/blob/master/pic/4.jpg)
  This list displays documents which you can work with.  There are multiple actions (read, Update, Delete, Check-in/out, Share) which you can perform depending on what your permissions are.  To perform an action on a document simply click the action associated with that file.

###7. Accessing documents
======

 * #####Downloading a file

  If you are the owner (creator) of a document, then you have the ability to read the document.  Department managers and corporate level managers can also read documents for the departments they are responsible for.Once you are on the Documents page (this page lists documents you have access to), you can read a file.  To do this click on the button with the file name.

###8. Updating documents
======

  If you are the owner (creator) of a document, then you have the ability to update the document.  Department managers and corporate level managers can also update documents for the departments they are responsible for.
The process for updating a document which is already in the system is very much similar to uploading a new document.  First you will have to find the document you wish to update from the document list (refer to part 6: Listing Documents).  In the folder with that document click on ‘actions’ from the top of the window, then select ‘Upload files here’.   This will take you to the document upload page, upload a new version of the file, making sure that the file name is exactly the same.  You may not update a document which has been checked out by another user.

###9. Sharing documents
======

  If you are the owner (creator) of a document, then you have the ability to share the document with any other user in the system at your discretion.  Department managers and corporate level managers can also share documents for the departments they are responsible for.
First you will have to find the document from the document list (refer to part 6: Listing Documents).  Next click the link labeled “share”.  This will show a dialog where you will specify the email address with whom you wish to share the document.  The email address must be in the system already.  You will also be asked which permissions you wish to grant them (read, update, delete, lock a.k.a. check-in/out).  After the preferences have been set click the button labeled “Share”.
  You can not revoke shared permissions.
  
###10. Deleting documents
======

  If you are the owner (creator) of a document, then you have the ability to delete the document.  Department managers and corporate level managers can also delete documents for the departments they are responsible for.
  First you will have to find the document you wish to delete from the document list (refer to part 6: Listing Documents).  Next click the link labeled “delete”.  If you are deleting a shared document be aware that the other users you shared the document with will not retain a copy.  If you delete a file, they can no longer access the document either.  Deleting a document will remove all revisions of that document.

###11. Checking in and out documents
======

  If you are the owner (creator) of a document, then you have the ability to check-in and check-out the document with any other user in the system at your discretion.  Department managers and corporate level managers can also check-in/out documents for the departments they are responsible for.
 
  While a document is checked-out, no other user can update, delete, or check-in/out the file.  This prevents other people from overwriting or removing documents while you are working on them.
To check out a document, find the document in the document list then click the ‘Check-out’ button.  To check a document back in, again find it in the document list, and click the ‘Check-in’ button.  When a document is checked out you can upload new versions of the document, but no other user may do so.

###12. Encrypting and Decrypting Documents
======

  Users who upload documents can provide a key to encrypt the document as the file has been uploaded to the system.
When you are selecting documents to upload you can specify a password to encrypt the document.
  The password must be exactly 16 characters.
  To download a document, locate the document in the document list then click on the document name button. A ‘Decrypt and download’ dialog will appear, which will ask you for a decryption key.  If you specified an encryption key when uploading the document you must specify it when you download the file.  If no key was supplied then don’t enter a key and just click ‘Decrypt & download’.

###13. System Administrator specific functionality
======
 Once the system administrator logins with correct credentials he shall be able to see the web page containing details of users awaiting activation ,along with options of adding,deleting and modifying users.
 
 * #####Activate Users

  ToThe system has the privilege to list all the users in the system. He can do this by clicking list options his home page.
 ![alt tag](https://github.com/biubiuX/WDMS/blob/master/pic/5.jpg)
 
 * #####Create, Delete, Modify Users
  The system administrator is capable of creating users specifying proper credentials. He can also delete users by specifying emailaddress(username) of the specific user. The role of specific user could be modified by specifying the emailaddress of that user along with his new role.
 ![alt tag](https://github.com/biubiuX/WDMS/blob/master/pic/4.jpg)

 * #####Viewing system log
  The system administrator can view the system log of various operations performed on the system by clicking on  download log on top of the screen . Each line in the system log file represents a single record containing the serial number , comment of the record, category of the record (1-good,2-bad,3-neutral) and userid of the user committing the log in tab seperated format.

 ![alt tag](https://github.com/biubiuX/WDMS/blob/master/pic/4.jpg)

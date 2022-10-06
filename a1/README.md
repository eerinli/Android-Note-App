# A1 Notes
    Jiaai Li (j2469li 20874993)
 
    ## Setup
    * macOS 12.2.1
    * IntelliJ IDEA 2022.2.1 (Community Edition)
    * kotlin.jvm 1.7.10
    * Java SDK 17.0.2 (temurin)
 
    ## Enhancement 
    I added more sorting options for notes in the choice box: 
    * choose "Note Content (asc)" to sort by note contents in ascending orders (A to Z, then a to Z)
    * choose "Note Content (desc)" to sort by note contents in descending orders (z to a, then Z to A)
    * choose "Created Time (asc)" to sort by created time of the note in ascending orders (first to last)
    * choose "Created Time (asc)" to sort by created time of the note in ascending orders (last to first)
    * Note: note created when started also have a default create time in order
    
    I added an additional view in the view group: 
    * click "Desktop" Button to display the desktop view
    * drag any notes, including "special note", to move it to anywhere in the pane
    * Note created with "special note" will generate on the left corner
    * In desktop view, notes are displayed as square areas as in grid view except that they have borders
    * The order of the note stack (bottom to top) is based on sorting options
    * Special note is always on top for convenience

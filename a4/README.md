# A4 Notes
    Jiaai Li (j2469li 20874993)
 
    ## Setup
    * macOS 12.2.1
    * IntelliJ IDEA 2022.2.1 (Community Edition)
    * kotlin.jvm 1.7.10
    * Java SDK 17.0.17 (temurin)
    * Android 11.0 (R) API Level 30
 
    ## Enhancement 
    I changed the sorting behaviour:
    * instead of sorting by creation data within a category, sort notes by last-edit date.
    * Note that last-edit date will be update when entering the edit screen of a note.
    * If entering the edit screen, but do not modify, or change and then restore still updates last-edit date
    * If a note has not entering the edit screen, its last-edit date is its creation date


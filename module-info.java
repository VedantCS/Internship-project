/**
 * 
 */
/**
 * 
 */
module Inventory_Analyzer {
    requires java.sql;
    requires java.desktop;
}


/*javax.swing is part of java.desktop

When you use module-info.java, Java blocks access to modules you don’t declare

Without requires java.desktop;, Swing is invisible*/
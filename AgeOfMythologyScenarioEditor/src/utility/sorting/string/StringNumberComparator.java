
package utility.sorting.string;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This sorts string by dictionary order but in addition it recognises numbers and sorts by their value if they are
 * contained within a string.
 */
public class StringNumberComparator implements Comparator<String>, Serializable {
   
   /**
    * Provides the singleton instance to be used in external code.
    */
   public static final StringNumberComparator INSTANCE = new StringNumberComparator();
   
   private static final long serialVersionUID = -5632045601336304138L;
   
   /**
    * Prevent instantiation.
    */
   private StringNumberComparator() {
   }
   
   /**
    * Performs the number value comparison to get a bias of which one should come first.
    * @param sa First string
    * @param sb String to compare with
    * @return Bias according to number value
    */
   private int compareRight(String sa, String sb) {
      
      int bias = 0;
      int ia = 0;
      int ib = 0;
      
      // The longest run of digits wins. That aside, the greatest
      // value wins, but we can't know that it will until we've scanned
      // both numbers to know that they have the same magnitude, so we
      // remember it in BIAS.
      for (;; ia++, ib++) {
         char ca = charAt(sa, ia);
         char cb = charAt(sb, ib);
         
         if (!Character.isDigit(ca) && !Character.isDigit(cb)) {
            return bias;
         } else if (!Character.isDigit(ca)) {
            return -1;
         } else if (!Character.isDigit(cb)) {
            return +1;
         } else if (ca < cb) {
            if (bias == 0) {
               bias = -1;
            }
         } else if (ca > cb) {
            if (bias == 0) {
               bias = +1;
            }
         } else if (ca == 0 && cb == 0) {
            return bias;
         }
      }
   }
   
   @Override
   public int compare(String sao, String sbo) {
      
      String sa = sao.toUpperCase(); //Makes the compare case insensitive
      String sb = sbo.toUpperCase();
      
      int ia = 0;
      int ib = 0;
      int nza = 0;
      int nzb = 0;
      char ca;
      char cb;
      int result;
      
      while (true) {
         // only count the number of zeroes leading the last number compared
         nza = nzb = 0;
         
         ca = charAt(sa, ia);
         cb = charAt(sb, ib);
         
         // skip over leading spaces or zeros
         while (Character.isSpaceChar(ca) || ca == '0') {
            if (ca == '0') {
               nza++;
            } else {
               // only count consecutive zeroes
               nza = 0;
            }
            
            ia++;
            ca = charAt(sa, ia);
         }
         
         while (Character.isSpaceChar(cb) || cb == '0') {
            if (cb == '0') {
               nzb++;
            } else {
               // only count consecutive zeroes
               nzb = 0;
            }
            
            ib++;
            cb = charAt(sb, ib);
         }
         
         // process run of digits
         if (Character.isDigit(ca) && Character.isDigit(cb)) {
            if ((result = compareRight(sa.substring(ia), sb.substring(ib))) != 0) {
               return result;
            }
         }
         
         if (ca == 0 && cb == 0) {
            // The strings compare the same. Perhaps the caller
            // will want to call strcmp to break the tie.
            return nza - nzb;
         }
         
         if (ca < cb) {
            return -1;
         } else if (ca > cb) {
            return +1;
         }
         
         ia++;
         ib++;
      }
   }
   
   /**
    * Returns the character in the String as position index
    * @param string String to get the character from
    * @param index Index of the character to retrieve.
    * @return Char at the specified position
    */
   private static char charAt(String string, int index) {
      if (index >= string.length()) {
         return 0;
      } else {
         return string.charAt(index);
      }
   }
   
}

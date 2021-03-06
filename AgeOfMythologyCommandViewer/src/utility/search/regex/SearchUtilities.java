
package utility.search.regex;

/**
 * Provides useful utility methods when making a search text box.
 */
public class SearchUtilities {
   
   public static final String CASE_INSENSITIVE = "(?i)";
   
   /**
    * Prevent instantiation.
    */
   private SearchUtilities() {
   }
   
   /**
    * Prepares the search text for use with the regular expression matcher. The search wildcards need to be converted to work with regex. In addition
    * regex characters need to be escaped.
    * Character * acts as wildcard for any number of characters. Regex: *.
    * Character ? acts as a wildcard for a single character. Regex: .
    * @param search Search string to prepare.
    * @param allowPartialMatches Adds an implicit * at the end allowing for partial matches.
    * @param caseSensitive The regex will case sensitive.
    * @return Regex prepared search string.
    */
   public static String convertSearchTextForRegexMatch(String search, boolean allowPartialMatches, boolean caseSensitive) {
      if (allowPartialMatches) {
         //add implicit * to allow partially typed results to appear
         search = "*" + search + "*";
      }
      //escape all regular expression characters other than *
      search = search.replace("\\", "\\\\"); //do first as later replaces produces \ characters.
      search = search.replace(".", "\\.");
      search = search.replace("[", "\\[");
      search = search.replace("]", "\\]");
      search = search.replace("{", "\\{");
      search = search.replace("}", "\\}");
      search = search.replace("(", "\\(");
      search = search.replace(")", "\\)");
      search = search.replace("+", "\\+");
      search = search.replace("-", "\\-");
      search = search.replace("^", "\\^");
      search = search.replace("$", "\\$");
      search = search.replace("|", "\\|");
      //Convert the special search characters to the regex equivalents.
      search = search.replace("?", ".");
      search = search.replace("*", ".*?");
      //apply making case insensitive
      if (!caseSensitive) {
         search = CASE_INSENSITIVE + search;
      }
      return search;
   }
   
}


/*
 *  Package: Helpers
 *  File:    ValidationHelper.java
 *
 *  Created by: Justin A Langley
 *  Date: 2019AUG08
 */
package Helpers;

import java.text.ParseException;

import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ValidationHelper
{
    // clears any currently displayed errors
    public void clearErrorLabels (List<Label> errors)
    {
        setErrorLabels(errors, "", "", "");
    }

    // take a character and determine if it has occurences in a string
    private boolean duplicateChar (char c, String s)
    {
        boolean foundDuplicate = false;
        int     count          = 0;

        for (int i = 0; i < s.length(); i++)
            {
            if (s.charAt(i) == c)
                {
                count++;
                }
            }
        if (count > 1)
            {
            foundDuplicate = true;
            }

        return foundDuplicate;
    }

    // highlights invalid input by placing a red border around the input field
    public TextField highlightInvalidInput (TextField tf)
    {
        tf.setBorder(new Border(
            new BorderStroke(
                Color.web("#ff5050"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(0),
                new BorderWidths(3))));

        return tf;
    }

    // highlights valid input by placing a green border around the input field
    public TextField highlightValidInput (TextField tf)
    {
        tf.setBorder(new Border(
            new BorderStroke(
                Color.GREEN,
                BorderStrokeStyle.SOLID,
                new CornerRadii(0),
                new BorderWidths(3))));

        return tf;
    }

    /**
     * This function parses a string as currency.
     *
     * It operates this way:
     * Removes duplicate occurrences of '$' if there are any and remembers if there were
     * Removes duplicate occurrences of '.' if there are any and remembers if there were
     *
     * If there was no decimal, it adds one with 2 zeroes,
     * and parses the string with any non-numeric or decimal chars removed
     *
     * If there was a decimal, it checks if the next two chars are valid
     * If none are valid, it discards anything after the decimal and appends two zeroes
     * If only the first valid, it adds one more zero
     * If both were valid, it keeps them
     *
     * After the previous checks, all chars not 0-9 or . are removed
     * The string is then converted to a double
     *
     * @param s string representation of the currency being parsed
     * @return Double
     * @throws ParseException
     */
    public Double parseCurrency (String s) throws ParseException
    {
        Double  parsed;
        boolean hasDecimal = true;

        // if only symbols were entered, make it 0
        if (s.replaceAll("[^$0-9.]+", "").isEmpty())
            {
            s = "0.00";
            }
        for (int i = 1; i <= 1; i++)
            {
            int idx = -1;

            // get rid of multiple occurences of '$'
            if (duplicateChar('$', s))
                {
                String temp = null;

                idx = s.indexOf('$');
                if (idx == -1)
                    {
                    break;
                    }
                temp += s.substring(0, idx);
                for (int j = idx; j < s.length(); j++)
                    {
                    if (s.charAt(j) != '$')
                        {
                        temp += s.charAt(j);
                        }
                    }
                s = temp;
                }
            }
        for (int i = 1; i <= 1; i++)
            {
            int idx = -1;

            // get rid of multiple occurences of '.'
            if (duplicateChar('.', s))
                {
                String temp = null;

                idx = s.indexOf('.');
                if (idx == -1)
                    {
                    hasDecimal = true;

                    break;
                    }
                temp += s.substring(0, idx);
                for (int j = idx; j < s.length(); j++)
                    {
                    if (s.charAt(j) != '.')
                        {
                        temp += s.charAt(j);
                        }
                    }
                s = temp;
                }
            }
        if (!hasDecimal)
            {
            s = s + ".00";
            }

        // Truncate past 20 digits
        if (s.length() > 20)
            {
            // more than reasonable for a program storing part/product prices
            s = s.substring(0, 20);
            }
        parsed = Double.valueOf(s.replaceAll("[^0-9.]+", ""));

        return parsed;
    }

    // function to set validation error labels i.e. to provide user context for invalid inputs
    public List<Label> setErrorLabels (List<Label> errors, String title, String desc1, String desc2)
    {
        // set the error labels to provide validation hints to the user
        errors.get(0).setText(title);
        errors.get(1).setText(desc1);
        errors.get(2).setText(desc2);

        // return the updated labels
        return errors;
    }

    // Filter: Company Name, customized for certain allowed characters
    public boolean validateCompanyName (KeyEvent ev, String key, TextField tf, List<Label> errors)
    {
        boolean isValid = true;

        clearErrorLabels(errors);
        highlightValidInput(tf);
        if (("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ&#.,()'\"\t\b ").contains(
                key))
            {
            // Allow character
            return isValid;
            }
        else
            {
            isValid = false;
            setErrorLabels(errors,                     // Label Array
                           "Valid characters are:",    // Error Title
                           "Alphanumeric",             // Error Desc1
                           "( . , \' \" & # )");       // Error Desc2
            ev.consume();

            return isValid;
            }
    }

    /*
     *   Function to validate a field as currency
     *
     *  Displays errors if there is a pattern mismatch against the filters
     *  Allows the following formats:
     *  0     |   00.00   |      $0     |     -     |
     *  12    |   12.34   |      $12    |   $12.34  |
     *  1,000 |   $1,000  |   $1,234.56 |     -     |
     *
     *  If bad data is entered e.g. "$$2.42.5$2,52"
     *  the parse currency function above will still parse it into something
     *  to avoid exceptions from occuring, though it may not be what the user wanted
     */
    public boolean validateCurrency (int pos, KeyEvent ev, String key, TextField tf,
                                     List<Label> errors)
    {
        String  pattern = "\\$?([0-9]{1,3},([0-9]{3},)*[0-9]{3}|[0-9]+)(.[0-9][0-9]){0,1}";
        int     length  = tf.getText().length();
        boolean isValid = false;
        String  text;

        /*
         *  handle where to insert the key into the current text in the field
         * this is done because an OnType() event is used, and user may insert
         * a new character (key) anywhere in the TextField
         */
        if (pos == length)
            {
            // insertion was the end of the string, append the key to the input
            text = tf.getText() + key.trim();
            }
        else if (pos == 0)
            {
            // insertion position was the front, append input to the key
            text = key.trim() + tf.getText();
            }
        else
            {
            // splice the string and insert the key
            text = tf.getText(0, pos) + key.trim() + tf.getText(pos, length);
            }

        // check if the key was valid
        if (!"1234567890,.$\t\b".contains(key))
            {
            // disallow keys that are not numeric, a comma, decimal, or the dollar symbol
            setErrorLabels(errors,                     // Label Array
                           "Valid characters are:",    // Error Title
                           "0-9 , . $",                // Error Desc1
                           "");                        // Error Desc2
            ev.consume();

            return isValid;
            }
        clearErrorLabels(errors);
        highlightValidInput(tf);
        if (!text.matches(pattern))
            {
            // further check if input matches currency regex pattern at the start of this method
            isValid = false;
            highlightInvalidInput(tf);
            setErrorLabels(errors,                   // Label Array
                           "Enter Dollar Amount",    // Error Title
                           "e.g. 12.34  1234",       // Error Desc1
                           "$1234  $1,234.56");      // Error Desc2
            }

        // return if the input is valid or not
        return isValid;
    }

    public boolean validateMaxQty (int pos, String key, TextField minInvAmt, TextField maxInvAmt,
                                   List<Label> errors)
    {
        boolean isValid = false;
        int     minInv  = 0;
        int     maxInv  = 0;
        int     length  = maxInvAmt.getText().length();

        // Attempt to parse user inputs
        try
            {
            minInv = Integer.parseInt(minInvAmt.getText().trim());
            }
        catch (NumberFormatException e)
            {
            /*
             * This exception means minInput, or maxInput, or both are empty
             * when the try clause ran, it didn't have a way to convert ""
             * to a number.
             */

            // Field was empty, set minInv to -1
            minInv = -1;
            }
        try
            {
            // Get data from textfield with current key pressed inserted, assign minInv
            if (pos == length)
                {
                maxInv = Integer.parseInt(maxInvAmt.getText() + key.trim());
                }
            else if (pos == 0)
                {
                maxInv = Integer.parseInt(key.trim() + maxInvAmt.getText());
                }
            else
                {
                maxInv = Integer.parseInt(maxInvAmt.getText(
                    0,
                    pos) + key.trim() + maxInvAmt.getText(pos, length));
                }
            }
        catch (NumberFormatException e)
            {
            /*
             * This exception means minInput, or maxInput, or both are empty
             * when the try clause ran, it didn't have a way to convert ""
             * to a number.
             */

            // Field was empty, set maxInv to -1
            maxInv = -1;
            }
        if (minInv < 0)
            {
            // Skip validation until minInv has input
            return isValid;
            }

        // Check user input
        if (!(maxInv < 0))
            {
            // max input was not empty
            if (minInv > maxInv)
                {
                // invalid input, min larger than max
                highlightInvalidInput(minInvAmt);
                highlightInvalidInput(maxInvAmt);
                setErrorLabels(errors, "Invalid values:", "Max < Min", "");
                }
            else
                {
                // all input ok
                isValid = true;
                highlightValidInput(minInvAmt);
                highlightValidInput(maxInvAmt);
                }
            }
        else
            {
            // max input was empty
            highlightInvalidInput(maxInvAmt);
            setErrorLabels(errors, "Max cannot be empty", "You must enter a number", "");
            }

        return isValid;
    }

    public boolean validateMinQty (int pos, String key, TextField minInvAmt, TextField maxInvAmt,
                                   List<Label> errors)
    {
        boolean isValid = false;
        int     minInv  = 0;
        int     maxInv  = 0;
        int     length  = minInvAmt.getText().length();

        // Attempt to parse user inputs
        try
            {
            maxInv = Integer.parseInt(maxInvAmt.getText().trim());
            }
        catch (NumberFormatException e)
            {
            /*
             *  This exception means minInput, or maxInput, or both are empty
             * when the try clause ran, it didn't have a way to convert ""
             * to a number.
             */

            // Field was empty, set maxInv to -1
            maxInv = -1;
            }
        try
            {
            // Get data from textfield with current key pressed inserted, assign minInv
            if (pos == length)
                {
                minInv = Integer.parseInt(minInvAmt.getText() + key.trim());
                }
            else if (pos == 0)
                {
                minInv = Integer.parseInt(key.trim() + minInvAmt.getText());
                }
            else
                {
                minInv = Integer.parseInt(minInvAmt.getText(
                    0,
                    pos) + key.trim() + minInvAmt.getText(pos, length));
                }
            }
        catch (NumberFormatException e)
            {
            /*
             *  This exception means minInput, or maxInput, or both are empty
             * when the try clause ran, it didn't have a way to convert ""
             * to a number.
             */

            // Field was empty, set minInv to -1
            minInv = -1;
            }
        if (minInv < 0)
            {
            // Skip validation until minInv has input
            return isValid;
            }

        // Check user input
        if (!(minInv < 0))
            {
            // min input was not empty
            if (minInv > maxInv)
                {
                // invalid input, min larger than max
                highlightInvalidInput(minInvAmt);
                highlightInvalidInput(maxInvAmt);
                setErrorLabels(errors, "Invalid values:", "Min > Max", "");
                }
            else
                {
                // all input ok
                isValid = true;
                highlightValidInput(minInvAmt);
                highlightValidInput(maxInvAmt);
                }
            }
        else
            {
            // min input was empty
            highlightInvalidInput(minInvAmt);
            setErrorLabels(errors, "Min cannot be empty", "You must enter a number", "");
            }

        return isValid;
    }

    // function which validates a field against a numeric-only filter
    public boolean validateNumeric (KeyEvent ev, String key, TextField tf, List<Label> errors)
    {
        boolean isValid = true;

        clearErrorLabels(errors);
        highlightValidInput(tf);
        if ("1234567890\t\b".contains(key))
            {
            return isValid;
            }
        else
            {
            isValid = false;
            setErrorLabels(errors,                   // Label array
                           "Enter only numbers:",    // Error Title
                           "e.g. 12 or 345",         // Error Desc1
                           "");                      // Error Desc2
            ev.consume();

            return isValid;
            }
    }

    // function which validates a product name against a hard-coded filter
    public boolean validatePartName (KeyEvent ev, String key, TextField tf, List<Label> errors)
    {
        boolean isValid = true;

        clearErrorLabels(errors);
        highlightValidInput(tf);
        if (("1234567890abcdefghijklmnopqrstuvwxyz"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ&#.,()'\"\"\t\b ").contains(
                    key))
            {
            // Allow character
            return isValid;
            }
        else
            {
            isValid = false;
            setErrorLabels(errors,                     // Label Array
                           "Valid characters are:",    // Error Title
                           "Alphanumeric",             // Error Desc1
                           "( . , \' \" & # )");       // Error Desc2
            ev.consume();                              // Delete invalid input from the TextField

            return isValid;
            }
    }

    // functions which validates a product name against a hard-coded filter
    public boolean validateProductName (KeyEvent ev, String key, TextField tf, List<Label> errors)
    {
        boolean isValid = true;

        clearErrorLabels(errors);
        highlightValidInput(tf);
        if (("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ&#.,()'\"\t\b ").contains(
                key))
            {
            // Allow character
            return isValid;
            }
        else
            {
            isValid = false;
            setErrorLabels(errors,                     // Label Array
                           "Valid characters are:",    // Error Title
                           "Alphanumeric",             // Error Desc1
                           "( . , \' \" & # )");       // Error Desc2
            ev.consume();                              // Delete invalid input from the TextField

            return isValid;
            }
    }
}

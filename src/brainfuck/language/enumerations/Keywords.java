package brainfuck.language.enumerations;


import brainfuck.language.exceptions.IsNotACommandException;
import brainfuck.language.exceptions.IsNotAValidColorException;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Serrano Simon on 03/11/2016.
 */
public enum Keywords {

    INCR("INCR", "+", "#ffffff"),
    DECR("DECR", "-", "#4b0082"),
    LEFT("LEFT", "<", "#9400d3"),
    RIGHT("RIGHT", ">", "#0000ff"),
    OUT("OUT", ".", "#00ff00"),
    IN("IN", ",", "#ffff00"),
    JUMP("JUMP", "[", "#ff7f00"),
    BACK("BACK", "]", "#ff0000");

    private String word;
    private String shortcut;
    private String color;

    Keywords(String word, String shortcut, String color) {
        this.word = word;
        this.shortcut = shortcut;
        this.color = color;
    }

    //-----------------------------------------------------------------------------------------------------------------

    static final HashMap<String, Keywords> WORD_TO_KEYWORD_MAP; // Map that links words to keywords
    static{//Adding the links
        WORD_TO_KEYWORD_MAP = new HashMap<>();
        WORD_TO_KEYWORD_MAP.put("INCR", INCR);
        WORD_TO_KEYWORD_MAP.put("DECR", DECR);
        WORD_TO_KEYWORD_MAP.put("LEFT", LEFT);
        WORD_TO_KEYWORD_MAP.put("RIGHT", RIGHT);
        WORD_TO_KEYWORD_MAP.put("OUT", OUT);
        WORD_TO_KEYWORD_MAP.put("IN", IN);
        WORD_TO_KEYWORD_MAP.put("JUMP", JUMP);
        WORD_TO_KEYWORD_MAP.put("BACK", BACK);
    }


    static final HashMap<String, Keywords> SHORTCUT_TO_KEYWORD_MAP; // Map that links shortcuts to keywords
    static {//Adding the links
        SHORTCUT_TO_KEYWORD_MAP = new HashMap<>();
        SHORTCUT_TO_KEYWORD_MAP.put("+", INCR);
        SHORTCUT_TO_KEYWORD_MAP.put("-", DECR);
        SHORTCUT_TO_KEYWORD_MAP.put("<", LEFT);
        SHORTCUT_TO_KEYWORD_MAP.put(">", RIGHT);
        SHORTCUT_TO_KEYWORD_MAP.put(".", OUT);
        SHORTCUT_TO_KEYWORD_MAP.put(",", IN);
        SHORTCUT_TO_KEYWORD_MAP.put("[", JUMP);
        SHORTCUT_TO_KEYWORD_MAP.put("]", BACK);
    }

    static final HashMap<String, Keywords> COLOR_TO_KEYWORD_MAP; //Map that links colors to keywords
    static{//Adding the links
        COLOR_TO_KEYWORD_MAP = new HashMap<>();
        COLOR_TO_KEYWORD_MAP.put("#ffffff", INCR);
        COLOR_TO_KEYWORD_MAP.put("#4b0082", DECR);
        COLOR_TO_KEYWORD_MAP.put("#9400d3", LEFT);
        COLOR_TO_KEYWORD_MAP.put("#0000ff", RIGHT);
        COLOR_TO_KEYWORD_MAP.put("#00ff00", OUT);
        COLOR_TO_KEYWORD_MAP.put("#ffff00", IN);
        COLOR_TO_KEYWORD_MAP.put("#ff7f00", JUMP);
        COLOR_TO_KEYWORD_MAP.put("#ff0000", BACK);
    }


//---------------------------------------------------------------------------------------------------------------------

    /**
     * Checks whether the word is a valid word
     * @param word the word to be checked
     * @return true if the word is valid
     * @throws IsNotACommandException
     */
    public static boolean isWord(String word) throws IsNotACommandException{
        if (WORD_TO_KEYWORD_MAP.containsKey(word)) return true;
        else throw new IsNotACommandException();
    }

    /**
     * Checks whether the shortcut is a valid shortcut
     * @param shortcut the shortcut to be checked
     * @return true if the shortcut is valid
     * @throws IsNotACommandException
     */
    public static boolean isShortcut(String shortcut) throws IsNotACommandException{
        if (SHORTCUT_TO_KEYWORD_MAP.containsKey(shortcut)) return true;
        else throw new IsNotACommandException();
    }

    /**
     * Checks whether the color is a valid color
     * @param color the color to be checked
     * @return true if the shortcut is valid
     * @throws IsNotAValidColorException
     */
    public static boolean isColor(String color) throws IsNotAValidColorException{
        if (COLOR_TO_KEYWORD_MAP.containsKey(color)) return true;
        else throw new IsNotAValidColorException();
    }




    /**
     * transforms a word into a keyword
     * @param word the given word
     * @return the associated keyword
     */
    public static Keywords wordToKeyword(String word){ return WORD_TO_KEYWORD_MAP.get(word); }

    /**
     * transforms a shortcut into a keyword
     * @param shortcut the given shortcut
     * @return the associated keyword
     */
    public static Keywords shortcutToKeyword(String shortcut){ return SHORTCUT_TO_KEYWORD_MAP.get(shortcut); }

    /**
     * transforms a color into a keyword
     * @param color the given color
     * @return the associated keyword
     */
    public static Keywords colorToKeyword(String color){ return COLOR_TO_KEYWORD_MAP.get(color); }

    /**
     * Method that links a shortcut to a color
     * @param shortcut the shortcut to transform into a color
     * @return the color code associated to the shortcut
     */
    public static String shortcutToColor(String shortcut){
        Keywords keyword = SHORTCUT_TO_KEYWORD_MAP.get(shortcut);
        for (Map.Entry<String, Keywords> entry : COLOR_TO_KEYWORD_MAP.entrySet()){
            if (keyword.equals(entry.getValue())) return entry.getKey();
        }
        return null;
    }


}

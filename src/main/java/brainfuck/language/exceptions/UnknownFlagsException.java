package brainfuck.language.exceptions;

import static brainfuck.language.flag.Flags.showFlags;

/**
 * Created by Red_Panda on 30/11/2016.
 *
 * This exception is thrown when a flag is not a valid flag
 * Valid flags : -p -i -o --rewrite --check --translate --trace
 */
public class UnknownFlagsException extends Exception {
    public UnknownFlagsException(String flags) {
        super("Error Code -1 : The flag "+flags+" is not a valid flag.\n" + showFlags());
    }
}

package brainfuck.language.exceptions;


/**
 * Cette exception est levée lorsque une commande actuelle n'en est pas une,
 * et qu'elle n'est donc pas interpretable
 *
 * @author  Red_Panda
 */
public class IsNotACommandException extends RuntimeException {
    char message;
    /**
     * Constructeur pour IsNotACommandException
     */
    public IsNotACommandException(char message) {
        this.message = message;
    }

    /**
     * Renvoie le texte correspondant à la description de l'erreur
     * @return "One of the commands is not an available command" est toujours renvoié
     */
    @Override
    public String toString(){ return message + " is not a valid command.";}


}

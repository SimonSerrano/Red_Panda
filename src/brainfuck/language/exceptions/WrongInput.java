package brainfuck.language.exceptions;

/**
 * Catte exception est levée lorsqu'on appelle une commande IN et qu'on ne renvoie pas seulement UN caractere ascii
 *
 * @author  Red_Panda
 */
public class WrongInput extends RuntimeException {

    /**
     * Constructeur pour WrongInput
     */
    public WrongInput() {}

    /**
     * Renvoie le texte correspondant à la description de l'erreur
     * @return "Unavailable entered data" est toujours renvoié
     */
    @Override
    public String toString(){return "Unavailable entered data";}
}
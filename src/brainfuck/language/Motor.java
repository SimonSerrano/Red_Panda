package brainfuck.language;

import brainfuck.language.enumerations.Keywords;
import brainfuck.language.exceptions.*;
import brainfuck.language.readers.KernelReader;
import brainfuck.language.readers.LecteurFichiers;
import brainfuck.language.readers.LecteurImage;
import brainfuck.language.readers.LecteurTextuel;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static brainfuck.language.enumerations.Flags.*;

/**
 * Cette classe permet de communiquer avec toutes les autres classes. Elle relie le lecteur de console avec le lecteur de fichier
 * et ce dernier avec l'interpreteur.
 * C'est ici qu'on choisie le bon interpréteur et le bon lecteur pour le fichier
 *
 *@author  Red_Panda
 */
public class Motor {

    private String[] args;
    private KernelReader kernel;
    private LecteurTextuel lecteur;
    private Interpreter interpreter;
    private String texteALire;
    private String fichierALire;
    private ArrayList<Keywords> listeDeCommande;
    private boolean fileIsEmpty = false;


    /**
     * Constructeur de Motor. On y met quoi dedans? On ne doit pas créer d'instance pour le lecteur image/textuel
     * On peut initialiser le KernelReader mais rien de plus.
     * @param args les paramètres écrits dans la console. On les renverra au KernelReader
     */
    public Motor(String[] args) {
        Metrics.START_TIME = System.currentTimeMillis();
        this.args = args;
        kernel = new KernelReader();
        interpreter = new Interpreter();
    }

    /**
     * Méthode qui exécute le programme étape par étape. D'abords on lit les instructions de la console puis on les exécute
     * Ensuite, on lit le fichier du programme et on récupère toutes les commandes.
     * A l'aide du lecteur textuel, on identifie chacune des commandes du programme
     * Avec l'interpréteur textue, on effectue l'action appropriée à l'instruction
     */

    public void lancerProgramme() throws UnknownFlagsException, CheckFailedException, IsNotACommandException, ValueOutOfBoundException,
            OutOfMemoryException, FilePathNotFoundException, MainFlagNotFoundException, FileNotFoundException, WrongInputException, WrongMacroNameException {

        boolean aReWrite = false;
        boolean aCheck = false;
        boolean aTranslate = false;
        boolean aTracer =false;
        for (String arg : args) {
            if (Rewrite.equals(toFlag(arg))) aReWrite = true;
            if (Check.equals(toFlag(arg))) aCheck = true;
            if (Translate.equals(toFlag(arg))) aTranslate = true;
            if (Trace.equals(toFlag(arg))) aTracer=true;
        }


        callKernel(args);

        if (aReWrite) {
            System.out.println("La traduction de votre programme en syntaxe courte est : ");
            OperationTexte.toString(listeDeCommande);
            System.out.println();
        }
        else if (aCheck) {
            if (kernel.commandeCheck(texteALire)) System.out.println("Error Code : 0");
            else throw new CheckFailedException();
        }

        else if (aTranslate) {
            LecteurImage lecteurImage = new LecteurImage();
            String nomFichier = fichierALire.substring(0, fichierALire.indexOf("."));
            lecteurImage.translateFromShortcutToImage(listeDeCommande, nomFichier);
        }
        else if (aTracer){
            interpreter.iniATracer(fichierALire.replace("." + extensionFichier(fichierALire),""));
            callInterpreter(listeDeCommande);
        }
        else if (!fileIsEmpty) callInterpreter(listeDeCommande);
        Metrics.EXEC_TIME(System.currentTimeMillis());
    }

    /**
     *  Appelle un objet KernelReadear afin de lire les commandes
     * @param args la liste des arguments rentrées dans la console
     */
    public void callKernel(String[] args) throws FilePathNotFoundException, MainFlagNotFoundException, UnknownFlagsException, IsNotACommandException, FileNotFoundException, WrongMacroNameException {

        fichierALire = kernel.interpreterCommande(args);
        if (fichierALire != null) {
            String extensionFichier = this.extensionFichier(fichierALire);

            if ("bf".equals(extensionFichier)) {

                    LecteurFichiers lecteur = new LecteurFichiers();
                    texteALire = lecteur.reader(fichierALire);
                    if (!lecteur.isEmpty()) listeDeCommande = callLecteurTextuel(this.texteALire);
                    else fileIsEmpty = true;


            }
            else if (extensionFichier.equals("bmp")) {
                LecteurImage lecteurImage = new LecteurImage();
                try {
                    listeDeCommande = lecteurImage.read(fichierALire);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     *  Appelle le lecteur textuel. Il gérera la lecture et la différencation entre toutes les instructions
     * @param texteALire
     * @return commande une liste contenye toutes les instructions
     */

    public ArrayList<Keywords> callLecteurTextuel(String texteALire) throws IsNotACommandException, WrongMacroNameException {
        lecteur = new LecteurTextuel();
        lecteur.setTexteAAnalyser(texteALire);
        ArrayList<Keywords> instruction = lecteur.creeTableauCommande();

        return instruction;
    }

    /**
     *  Appelle l'interpréteur de texte. L'interpréteur de texte gérera ensuite l'éxution des instructions
     *
     * @param commandeAExecuter une liste de toutes les instructions contenues dans le fichier programme
     * @return true si tout à bien été exécuté SINON false si une instruction a posée problème
     */
    public void callInterpreter(ArrayList<Keywords> commandeAExecuter) throws ValueOutOfBoundException, OutOfMemoryException, WrongInputException {
        interpreter.keywordsExecution(commandeAExecuter);
    }

    /**
     * Récupère l'extenion du fichier qui va être lu. Permet de savoir quel lecteur et interpréteur on va utiliser
     * @param fichier le nom du fichier ex => "programme.bf" ou "programme.bmp"
     * @return l'extension en string. Uniquement ce qu'il y a après le point
     */

    public String extensionFichier(String fichier) {

        return (fichier.substring(fichier.indexOf(".") + 1));
    }
}

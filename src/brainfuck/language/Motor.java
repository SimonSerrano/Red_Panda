package brainfuck.language;

import brainfuck.language.enumerations.Keywords;
import brainfuck.language.readers.KernelReader;
import brainfuck.language.readers.LecteurFichiers;
import brainfuck.language.readers.LecteurImage;
import brainfuck.language.readers.LecteurTextuel;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static brainfuck.language.enumerations.Flags.*;

/**
 * @author BEAL Clément on 05/10/2016.
 *
 * Cette classe permet de communiquer avec toutes les autres classes. Elle relie le lecteur de console avec le lecteur de fichier et ce dernier avec l'interpreteur
 * C'est ici qu'on choisie le bon interpréteur et le bon lecteur pour le fichier
 *
 *
 * @version 1.0
 */
public class Motor {

    private String[] args;
    private KernelReader kernel;
    private Interpreter interpreter;
    private String programme;
    private String fichierALire;
    private ArrayList<Keywords> listeDeCommande;


    /**
     * Constructeur de Motor. On y met quoi dedans? On ne doit pas créer d'instance pour le lecteur image/textuel
     * On peut initialiser le KernelReader mais rien de plus.
     * @param args les paramètres écrits dans la console. On les renverra au KernelReader
     */
    public Motor(String[] args) {
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

    public void lancerProgramme() {
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

        if (aReWrite) rewrite();
        if (aCheck) check();
        if (aTranslate) translate();
        if (aTracer){
            interpreter.iniATracer(fichierALire.replace("." + extensionFichier(fichierALire),""));
        }
        callInterpreter(listeDeCommande);
    }

    /**
     *  Appelle un objet KernelReadear afin de lire les commandes
     * @param args la liste des arguments rentrées dans la console
     */
    private void callKernel(String[] args) {
        fichierALire = kernel.interpreterCommande(args);
        if (fichierALire != null) {
            String extensionFichier = this.extensionFichier(fichierALire);

            if ("bf".equals(extensionFichier)) {
                try {
                    LecteurFichiers reader = new LecteurFichiers();
                    programme = reader.reader(fichierALire);

                    listeDeCommande = callLecteurTextuel(this.programme);
                } catch (FileNotFoundException e) {
                    System.out.println(e.toString());
                }

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
     *  Appelle l'interpréteur de texte. L'interpréteur de texte gérera ensuite l'éxution des instructions
     *
     * @param commandeAExecuter une liste de toutes les instructions contenues dans le fichier programme
     * @return true si tout à bien été exécuté SINON false si une instruction a posée problème
     */
    private void callInterpreter(ArrayList<Keywords> commandeAExecuter) {
        interpreter.keywordsExecution(commandeAExecuter);
    }

    /**
     *  Appelle le lecteur textuel. Il gérera la lecture et la différencation entre toutes les instructions
     * @param programme
     * @return commande une liste contenye toutes les instructions
     */

    private ArrayList<Keywords> callLecteurTextuel(String programme){
        LecteurTextuel lecteur = new LecteurTextuel();
        lecteur.setTexteAAnalyser(programme);
        ArrayList<Keywords> instruction = lecteur.creeTableauCommande();

        return instruction;
    }

    /**
     * Récupère l'extenion du fichier qui va être lu. Permet de savoir quel lecteur et interpréteur on va utiliser
     * @param fichier le nom du fichier ex => "programme.bf" ou "programme.bmp"
     * @return l'extension en string. Uniquement ce qu'il y a après le point
     */

    public String extensionFichier(String fichier) {

        return (fichier.substring(fichier.indexOf(".") + 1));
    }

    public String getprogramme() {
        return programme;
    }

    public void rewrite() {
        System.out.println("La traduction de votre programme en syntaxe courte est : ");
        OperationTexte.toString(listeDeCommande);
        System.out.println();
    }

    public void check() {
        if (!kernel.commandeCheck(programme)) System.out.println("4");
    }

    public void translate() {
        LecteurImage lecteurImage = new LecteurImage();
        String nomFichier = fichierALire.substring(0, fichierALire.indexOf("."));
        lecteurImage.translateFromShortcutToImage(listeDeCommande, nomFichier);
    }
}


package brainfuck.language;


import brainfuck.language.Exceptions.OutOfMemoryException;
import brainfuck.language.Exceptions.ValueOutOfBoundException;
import brainfuck.language.Exceptions.WrongInput;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static brainfuck.language.Keywords.toKeyword;

/**
 * @author BEAL Clément, SERRANO Simon on 28/09/16.
 *
 * Cette classe interprète les mot clés d'un texte/image
 * En fonction de l'instruction, une action sur la classe Memoire sera effectuée
 *
 * @version 1.0
 */




public class Interpreter {
    
    private Memory memory = new Memory();

    /**
     * Compare le mot avec la liste des mots exécutables et agit en conséquence
     *
     * @return true si l'action a été réalisée avec succès
     */
    public void keywordsExecution(ArrayList<String> tableauCommande) throws OutOfMemoryException, ValueOutOfBoundException{

        int i = 0;
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // A VOIR AVEC LES AUTRES, REVOIR LA STRUCTURE POUR AVOIR LE I ET POUVOIR SAUTER LES ETAPES DEJA FAITES
        for(String commande : tableauCommande) {
            i++;
                switch (toKeyword(commande)) {
                    case INCR:
                        memory.incr();
                        break;
                    case DECR:
                        memory.decr();
                        break;

                    case LEFT:
                        memory.left();
                        break;

                    case RIGHT:
                        memory.right();
                        break;
                    case OUT:
                        outMethod();
                        break;

                    case IN:
                        try {
                            inMethod();
                        }
                        catch (WrongInput e) {
                            e.printStackTrace();
                        }
                        break;
                    case JUMP:
                        if (memory.getCellValue() == 0){
                            foncWhile(tableauCommande, i);
                            // ATTENTION NE PAS OUBLIER DE FAIRE LA FONC POUR SAUTER TOUT !!!
                            //test
                        }
                        else

                        break;
                    case BACK:

                        break;
                    default:
                }
        }

        memory.printMemory();

    }

    private int foncWhile(ArrayList<String> commandes, int i){
        int nbOuvrante = 0;
        int it = i;

        while (commandes.get(it) != "]" && nbOuvrante != 0){
            if (commandes.get(it) == "[") {
                nbOuvrante++;
            }
            if (commandes.get(it) == "]"){
                nbOuvrante--;
            }

            ArrayList<String> temp = new ArrayList<String>();
            temp.add(commandes.get(it));
            keywordsExecution(temp);
            it++;
        }

        if (memory.getCellValue() != 0)
            foncWhile(commandes, i);

        return it;
    }

    /**
     * Gère la commande IN
     * On gère le cas où on a rentré le -i et le cas par défaut (console)
     */
    public void inMethod() {
        String entree;

        if(KernelReader.filepathForReading == null) { // dans le cas où on n'a pas fait -i
            Scanner scanner = new Scanner(System.in);
            entree = scanner.nextLine();

            if (entree.length() != 1) {throw  new WrongInput();}
            else {
                char character = entree.charAt(0);

                try {
                    memory.updateMemory((short) character);
                } catch (ValueOutOfBoundException e) {
                    e.toString();
                }
            }
        }
        else { // dans le cas où on a fait i
            LecteurFichiers lecteurFichiers = new LecteurFichiers();

            try {
                short modifyMemory = Short.parseShort(lecteurFichiers.reader(KernelReader.filepathForReading));
                memory.updateMemory(modifyMemory);
            } catch (FileNotFoundException e) {
                e.toString();
            }
        }
    }

    /**
     * Gère la méthode OUT
     * On gère le cas où on a la commande -o et le cas par défaut (console)
     */

    public void outMethod() {
        if(KernelReader.filepathForWriting == null) {
            char numb = (char) memory.getCellValue();
            System.out.println(numb);
        } else{
            LecteurFichiers lecteurFichiers = new LecteurFichiers();

            try {
                lecteurFichiers.write(KernelReader.filepathForWriting, Integer.toString(memory.getCellValue()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void appellerMemoire() { memory.printMemory();}
}
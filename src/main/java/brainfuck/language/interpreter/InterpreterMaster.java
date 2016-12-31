package brainfuck.language.interpreter;

import brainfuck.language.Metrics;
import brainfuck.language.enumerations.Keywords;
import brainfuck.language.exceptions.WrongInputException;
import brainfuck.language.function.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Permet de gérer à la fois l'exécution des instructions et celles des fonctions
 * @author jamatofu on 31/12/16.
 */
public class InterpreterMaster {
    private FunctionInterpreter functionInterpreter;
    private KeywordInterpreter keywordInterpreter;
    private boolean needTrace;
    private int cursor = 0;
    protected List<Integer> placeCrochet = new ArrayList<>();

    private Map<Integer, Keywords> keywordsMap;
    private Map<Integer, Function> functionMap;

    public InterpreterMaster(boolean needTrace, Map<Integer, Keywords> keywordsMap, Map<Integer, Function> functionMap) {
        this.needTrace = needTrace;
        this.keywordsMap = keywordsMap;
        this.functionMap = functionMap;
    }

    /**
     * Initialise les deux interpréteurs
     * @param infilepath chemin d'accès -i
     * @param outfilepath chemin d'accès -o
     * @param tracePath chemin d'accès pour la trace
     * @param functionMap le catalogue des fonctions du programme
     */
    public void initializeInterpreters(String infilepath, String outfilepath, String tracePath, Map<String, Function> functionMap) {
        this.keywordInterpreter = new KeywordInterpreter(infilepath, outfilepath);
        if(this.needTrace)
            keywordInterpreter.iniATracer(tracePath);

        this.functionInterpreter = new FunctionInterpreter(infilepath, outfilepath, functionMap);
    }

    /**
     *
     */
    public void interpreterProgram() throws WrongInputException {
        List<Keywords> keywordsList = new ArrayList<>(keywordsMap.values());

        while (cursorIsInInterval()) {
            if(keywordsMap.containsKey(cursor)) {
                Keywords keywords = keywordsMap.get(cursor);
                switch (keywords) {
                    case JUMP:
                        jump(keywordsList);
                        break;
                    case BACK:
                        back(keywordsList);
                        break;
                }

                keywordInterpreter.identifyAndExecuteInstruction(keywords);
            }
            else if (functionMap.get(cursor).isProcedure()) {
                for(Keywords keywords : functionMap.get(cursor).getCode()) {
                    keywordInterpreter.identifyAndExecuteInstruction(keywords);
                }
            }
            else
                functionInterpreter.identifyAndExecuteInstruction(functionMap.get(cursor));

            cursor++;
        }

        Metrics.PROC_SIZE = cursor;
    }

    /**
     * Permet de faire les taches post-interprétation
     */
    public void finishInterpreter() {
        keywordInterpreter.endTrace();
        System.out.println(keywordInterpreter.writeStateOfMemory());
    }

    /**
     * Regarde si le curseur existe dans un des deux catalogues
     * @return vrai si dans intervalle
     */
    public boolean cursorIsInInterval() {
        return keywordsMap.containsKey(cursor) || functionMap.containsKey(cursor);
    }

    /**
     * Permet de connaitre rapidement la position du crochet ouvrant associé
     * au crochet fermant actuel
     *
     * @param tableauCommande La liste de commande que l'on est en train d'interpreter
     * @return La position dans la liste de commande du crochet ouvrantassocié au crochet fermant actuel
     */

    protected int retournePlace(List<Keywords> tableauCommande){
        int placeCrochetActu = 0;
        int j = 0;

        while (j < cursor) {
            if (tableauCommande.get(j) == Keywords.BACK)
                placeCrochetActu++;
            j++;
        }
        return placeCrochet.size() - placeCrochetActu - 1;
    }

    /**
     * Permet de connaitre le nombre d'instruction qui sont compris entre le crochet ouvrant
     * actuel et le crochet fermant associé
     *
     * @param tableauCommande La liste de commande que l'on est en train d'interpreter
     * @return Le nombre d'instructions entre le crochet ouvrant actuel et le crochet fermant associé
     */

    protected int countInstru(List<Keywords> tableauCommande) {
        int nbOuvrante = 1;
        int it = cursor + 1;

        while (nbOuvrante != 0) {
            if (tableauCommande.get(it) == Keywords.JUMP) {
                nbOuvrante++;
            }
            if (tableauCommande.get(it) == Keywords.BACK) {
                nbOuvrante--;
            }
            it++;
        }
        return it - cursor;
    }

    /**
     * Permet d'enregistrer la position de tous les crochets ouvrants présent dans le fichier
     * actuelle, dans une liste.
     *
     * @param tableauCommande La liste de commande que l'on veut analyser
     */

    protected void recenseCrochet(List<Keywords> tableauCommande) {
        for (Map.Entry<Integer, Keywords> entry : keywordsMap.entrySet()) {
            if(Keywords.JUMP.equals(entry.getValue()))
                placeCrochet.add(entry.getKey());
        }
    }

    /**
     * Méthode jump
     */
    private void jump(List<Keywords> keywordsList) {
        if(keywordInterpreter.getCurrentValue() == 0)
            cursor += countInstru(keywordsList);
    }

    /**
     * Méthode back
     */
    private void back(List<Keywords> keywordsList) {
        if(keywordInterpreter.getCurrentValue() == 0)
            cursor = placeCrochet.get(retournePlace(keywordsList));
    }
}
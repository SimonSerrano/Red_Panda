package test;

import brainfuck.language.readers.LecteurTextuel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author jamatofu on 13/11/16.
 */
public class LecteurTextuelTest {
    LecteurTextuel lecteurTextuel = new LecteurTextuel();

    @org.junit.Before
    public void setUp() throws Exception {
        LecteurTextuel lecteurTextuel = new LecteurTextuel();
    }

    @Test
    public void estShortcut() throws Exception {
        assertTrue(lecteurTextuel.estShortcut('+'));
        assertTrue(lecteurTextuel.estShortcut('-'));
        assertTrue(lecteurTextuel.estShortcut('<'));
        assertTrue(lecteurTextuel.estShortcut('>'));
        assertTrue(lecteurTextuel.estShortcut('.'));
        assertTrue(lecteurTextuel.estShortcut(','));
        assertTrue(lecteurTextuel.estShortcut('['));
        assertTrue(lecteurTextuel.estShortcut(']'));
        assertFalse(lecteurTextuel.estShortcut('*'));

    }

    @org.junit.Test
    public void estInstruction() throws Exception {

    }

    @org.junit.Test
    public void couperChaineCaractere() throws Exception {

    }

    @org.junit.Test
    public void creeTableauCommande() throws Exception {
        String[] a = { "CLEME", "CLEM", "CLE", "CL"};
        assertArrayEquals(a ,lecteurTextuel.couperChaineCaractere("CLEME"));

        String[] b = { "CLEM", "CLE", "CL"};
        assertArrayEquals(b ,lecteurTextuel.couperChaineCaractere("CLEM"));

        String[] c = { "CLE", "CL"};
        assertArrayEquals(c ,lecteurTextuel.couperChaineCaractere("CLE"));

        String[] d = { "CL"};
        assertArrayEquals(d ,lecteurTextuel.couperChaineCaractere("CL"));

        String[] e = { };
        assertArrayEquals(e, lecteurTextuel.couperChaineCaractere("C"));
    }

}
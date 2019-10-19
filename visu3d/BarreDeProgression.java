/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Affiche une barre de progression dans le terminal.
 *
 ******************************************************************************/

package org.xprov.visu3d;

/**
 * Classe BarreDeProgression
 * 
 * Affiche une barre de progression dans le terminal. Des caractères ASCII non
 * visibles sont utilisés ramener le curseur au début de la ligne. La barre est
 * donc toujours affichée au même endroit.
 */
public class BarreDeProgression {

    /**
     * Constructeur
     *
     * L'utilisateur précise un texte à afficher à gauche de la barre ainsi que
     * le nombre total d'étapes à effectuer.
     *
     * @param texte  le texte affiché à gauche de la barre
     * @param nbEtapes  nombre total d'étapes
     */
    public BarreDeProgression(String texte, int nbEtapes) {
        this(texte, nbEtapes, 20);
    }

    /**
     * Constructeur
     *
     * L'utilisateur précise un texte à afficher à gauche de la barre ainsi que
     * le nombre total d'étapes à effectuer ainsi que la largeur de la barre
     * exprimée en nombre de caractères.
     *
     * @param texte     le texte affiché à gauche de la barre
     * @param nbEtapes  nombre total d'étapes
     * @param taille    largeur de la barre
     */
    public BarreDeProgression(String texte, int nbEtapes, int taille) {
        this.texte = texte;
        this.nbEtapes = nbEtapes;
        this.taille = taille;
        this.prochainIncrement = nbEtapes / taille;
        this.n = 1;
    }

    /**
     * Incrémente la barre et la réaffiche incrémentée au besoin.
     *
     * Si le nombre d'étape est supérieur à la largeur de la barre, la barre
     * n'est réaffichée que si nécessaire.
     */
    public void incremente() {
        n += 1;
        if (n >= prochainIncrement) {
            String s = new String(texte + " : [");
            int nbPas = Math.min((taille * n) / nbEtapes, taille);
            prochainIncrement = (nbPas+1) * nbEtapes / taille;
            for (int i=0; i<nbPas; i++) {
                s += ".";
            }
            for (int i=nbPas; i<taille; i++) {
                s += " ";
            }
            s += "]\r";
            System.out.print(s);
        }
    }

    /**
     * Affiche la barre complétée et ajoute un retour de charriot.
     */
    public void fin() {
        n = nbEtapes;
        incremente();
        System.out.println("\n");
    }



    private String texte; // texte à afficher à gauche de la barre
    private int nbEtapes; // nombre total d'étapes
    private int taille;   // largeur de la barre
    private int prochainIncrement; // valeur où il faudra réafficher la barre
    private int n; // étant actuel de la progression
}

/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe implémente les couleurs qui apparaissent dans une image.
 *
 ******************************************************************************/

package org.xprov.visu3d;

import java.util.Map;
import java.util.HashMap;

import org.xprov.visu3d.linalg.Vecteur3d;

/**
 * Classe couleur
 * 
 * Une nouleur est représentée par trois entiers, chacun de 0 à 255, respectant
 * le principe RGB. Ainsi, le premier indique l'intensité de la couleur rouge,
 * le deuxième est celle du vert et le troisième est celle du bleu.
 *
 * Des couleurs prédéfinies permettent d'initialiser une couleur à partir d'une
 * chaîne de caractères, du moment que celle-ci correspond exactement au nom
 * d'une des couleurs prédéfinies.
 */
class Couleur
{
    /**
     * Dictionnaire des couleurs prédéfinies.
     */
    public static final Map<String, Couleur> dicoDeCouleurs = new HashMap<String, Couleur>() {{
        put("blanc",     new Couleur(255, 255, 255));
        put("noir",      new Couleur(  0,   0,   0));
        put("rouge",     new Couleur(255,   0,   0));
        put("vert",      new Couleur(  0, 255,   0));
        put("bleu",      new Couleur(  0,   0, 255));
        put("cyan",      new Couleur(  0, 255, 255));
        put("jaune",     new Couleur(255, 255,   0));
        put("magenta",   new Couleur(255,   0, 255));
        put("orange",    new Couleur(237, 127,  16));
        put("rose",      new Couleur(253, 108, 158));
        put("mauve",     new Couleur(212, 115, 212));
        put("turquoise", new Couleur( 37, 253, 233));
        put("violet",    new Couleur(102,   0, 153));
        put("gris",      new Couleur( 96,  96,  96));
        put("argent",    new Couleur(206, 206, 206));
        put("brun",      new Couleur( 91,  60,  17));
        put("beige",     new Couleur(200, 173, 127));
    }};


    /**
     * Constructeur à partir de trois entrier
     *
     * @param r  intensite du rouge
     * @param g  intensite du vert
     * @param b  intensite du bleu
     */
    public Couleur(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Constructeur à partir d'un seul entier dont :
     *  - les bits de 0 à 7 décrivent l'intensité du bleu
     *  - les bits de 8 à 15 décrivent l'intensité du vert
     *  - les bits de 16 à 23 décrivent l'intensité du rouge
     *
     * @param rgb  un entier décrivant la couleur RGB sur 24 bits.
     */
    public Couleur(int rgb) {
        this.r = (rgb >> 16) & 0xff;
        this.g = (rgb >> 8) & 0xff;
        this.b = rgb & 0xff;
    }

    /**
     * Constructeur à partir d'une chaîne de caractère
     *
     * Si la chaîne est le nom d'une couleur, celui-ci doit correspondre
     * exactement à une des couleurs définies dans le dictionnaire
     * `dicoDeCouleurs`.
     *
     * Sinon, la chaîne doit être un triplet de la forme "(r, g, b)" où r, g
     * et b sont des entiers de 0 à 255. Dans ce cas, la chaîne est lue comme
     * un vecteur. La coordonnée x décrit r, la coordonnée y décrit g et la
     * coordonnée z décrit b.
     */
    public Couleur(String s) {
        Couleur c = dicoDeCouleurs.get(s);
        if (c != null) {
            // Le nom a été trouvé dans le dictionnaire
            this.r = c.r;
            this.g = c.g;
            this.b = c.b;
        } else {
            // Lecture comme un vecteur de dimension 3
            Vecteur3d v = new Vecteur3d(s);
            this.r = (int) v.getX();
            this.g = (int) v.getY();
            this.b = (int) v.getZ();
        }
    }

    /**
     * Constructeur à partir d'un vecteur.
     *
     * Les trois valeur r,g,b sont extraite d'un vecteur de dimension 3.
     */
    public Couleur(Vecteur3d v) {
        this.r = (int) v.getX();
        this.g = (int) v.getY();
        this.b = (int) v.getZ();
    }

    /**
     * Retourne la couleur comme un Vecteur3d.
     *
     * La coordonnée x est l'intensité du rouge,
     * la coordonnée y est l'intensité du vert,
     * la coordonnée z est l'intensité du bleu.
     *
     * @return  le vecteur (r,g,b)
     */
    public Vecteur3d toVecteur() {
        return new Vecteur3d(this.r, this.g, this.b);
    }

    /**
     * Retourne la couleur sous la forme d'un entier dont :
     *  - les bits de 0 à 7 décrivent l'intensité du bleu
     *  - les bits de 8 à 15 décrivent l'intensité du vert
     *  - les bits de 16 à 23 décrivent l'intensité du rouge
     *
     *  @return  un entier décrivant la couleur sur 24 bits.
     */
    public int toInt() {
        return (this.r << 16) + (this.g << 8) + this.b;
    }

    public String toString() {
        return "Couleur(" + this.r + ", " + this.g + ", " + this.b + ")";
    }

    private int r; // intensité du rouge de 0 à 255
    private int g; // intensité du vert de 0 à 255
    private int b; // intensité du bleu de 0 à 255
}


/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe implémente une caméra positionnée dans une scène 3D. 
 *
 * Une caméra est constituée d'une position et de pixels. Le RayTracer utilise
 * la position de la caméra ainsi que la position de chacun de ses pixels afin
 * d'un calculer la couleur.
 *
 * La caméra propose également une fonction permettant de sauvegarder l'image
 * calculée dans un fichier.
 ******************************************************************************/

package org.xprov.visu3d;

import java.lang.IllegalArgumentException;
import java.lang.Math;

import org.xprov.visu3d.linalg.Vecteur3d;
import org.xprov.visu3d.linalg.Point3d;

/**
 * Classe Camera
 * 
 * La caméra est passive et ne fait aucun calcul lors de la génération d'une
 * image. Elle fournit simplement les informations nécessaire au RayTracer qui
 * lui effectue le travail.
 *
 * Une caméra est définie par 
 *  - la position de l'oeil dans la scène 3D
 *  - l'orientation dans laquelle la caméra regarde
 *  - la rotation de la caméra autour de l'axe dans lequel est dirigé le
 *    regard.
 *  - la distance entre l'oeil et l'écran. Une distance faible produit des
 *    image grand angle alors qu'une grande distance produit un zoom dans
 *    l'image.
 *  - la résolution de l'image, soit le nombre de lignes et de colonnes de l'image produite.
 */
public class Camera
{
    /**
     * Constructeur
     *
     * @param position             position de l'oeil de la caméra
     * @param orientationRegard      orientation dans laquelle la caméra regarde
     * @param orientationVersLeHaut  orientation dans dans laquelle pointe le haut
     *                             de la caméra (perpendiculaire à l'orientation
     *                             du regard)
     * @param distanceOeilEcran    distance entre l'oeil et l'écran
     * @param nbColonnesDansImage  nombre de colonnes dans l'image (décrit la résolution)
     * @param nbLignesDansImage    nombre de lignes dans l'image (décrit la résolution)
     */
    public Camera(Point3d position,
            Vecteur3d orientationRegard,
            Vecteur3d orientationVersLeHaut,
            double  distanceOeilEcran,
            int     nbColonnesDansImage,
            int     nbLignesDansImage,
            String  nomFichierImage)
    {
        this.position = position;
        this.orientationRegard = orientationRegard.normalise();
        this.orientationVersLeHaut = orientationVersLeHaut.normalise();
        this.distanceOeilEcran = distanceOeilEcran;
        this.nbColonnesDansImage = nbColonnesDansImage;
        this.nbLignesDansImage = nbLignesDansImage;
        this.largeurEcran = Math.sqrt(nbColonnesDansImage / (1.0 * nbLignesDansImage));
        this.hauteurEcran = 1.0 / largeurEcran;
        this.image = new Image(nbColonnesDansImage, nbLignesDansImage);
        this.nomFichierImage = nomFichierImage;

        // Check that upward orientation is perpendicular to orientationRegard
        double produitScalaire = orientationRegard.prodScal(orientationVersLeHaut);
        if (Math.abs(produitScalaire) > Constantes.EPSILON)
        {
            throw new IllegalArgumentException("Erreur à l'initalisation de la caméra," +
                    " le vecteur `orientationVersLeHaut=" + orientationVersLeHaut + 
                    "` doit être orthogonal au vecteur `orientationRegard=" +
                    orientationRegard + "`");
        }

        // Calcul du vecteur horizontal de l'écran (du coin sup gauche au coin sup droit)
        // 1. On calcule l'orientation du vecteur via un produit vectoriel
        vecteurHorizontalEcran = orientationVersLeHaut.prodVect(orientationRegard);
        // 2. On ajuste la longueur 
        vecteurHorizontalEcran = 
            vecteurHorizontalEcran.multiplie(largeurEcran / vecteurHorizontalEcran.norme()); 

        // Calcul du vecteur vertical dans l'écrant (du coin sup gauch au coin inf gauche)
        // Note : this.orientationVersLeHaut est unitaire
        vecteurVerticalEcran = this.orientationVersLeHaut.multiplie(-1.0 * hauteurEcran); 

        Point3d centerOfScreen = position.plus(orientationRegard.multiplie(distanceOeilEcran));
        coinSupGaucheEcran = centerOfScreen.plus(
                vecteurHorizontalEcran.multiplie(-0.5)).plus( vecteurVerticalEcran.multiplie(-0.5));
    }

    public String toString()
    {
        return "Camera\n" 
            + "  position                 : " + position + "\n"
            + "  orientation regard       : " + orientationRegard + "\n"
            + "  orientation vers le haut : " + orientationVersLeHaut + "\n"
            + "  dist. oeil-ecran         : " + String.format("%.2f", distanceOeilEcran) + "\n"
            + "  resolution de l'image    : " + nbColonnesDansImage + "x" + nbLignesDansImage + "\n"
            + "  dimensions de l'écran    : " + String.format("%.2f", largeurEcran) 
            + "x" + String.format("%.2f", hauteurEcran) + "\n"
            + "  pos sup. gauche          : " + coinSupGaucheEcran + "\n"
            + "  vect. hor. dans ecran    : " + vecteurHorizontalEcran + "\n"
            + "  vect. ver. dans ecran    : " + vecteurVerticalEcran + "\n"
            ;
    }

    /**
     * Affecte une couleur à un pixel.
     *
     * @param colonne  colonne du pixel
     * @param ligne    ligne du pixel
     * @param couleur  couleur affectée au pixel
     */
    public void setPixel(int colonne, int ligne, Couleur couleur)
    {
        image.setPixel(colonne, ligne, couleur);
    }

    /**
     * Écrit l'image dans le ficier spécifié. Si le fichier n'existe pas, alors
     * il est créé. Si le fichier existe déjà, alors il est écrasé.
     *
     * @param fichier nom du fichier
     */
    public void sauvegarderImage()
    {
        image.sauvegarder(nomFichierImage);
    }

    public String getNomFichierImage() {
        return nomFichierImage;
    }

    public Point3d getPosition() {
        return position;
    }

    public int getNbLignesImage() {
        return nbLignesDansImage;
    }

    public int getNbColonnesImage() {
        return nbColonnesDansImage;
    }

    /**
     * Retourne la position (dans la scène 3D) d'un pixel.
     * 
     * Calcule la position dans R^3 d'un des pixels de l'image.
     *
     * Le calcul est effectuée de la manière suivante : 
     * 
     * x, dans l'intervalle [0,1[, représente la position relative du pixel, de
     * gauche à droite. 
     *
     * y, dans l'intervalle [0,1[, représente la position relative du pixel, de
     * haut en bas.
     *
     * u est le vecteur correspondant à la largeur de l'image (de gauche à droite).
     *
     * v est le vecteur correspondant à la haute de l'image (du haut vers le bas).
     * 
     * O est la position dans R^3 du coin supérieur gauche de l'image.
     *
     *                               u
     *       |------------------------------------------->
     *
     *      O                                 x
     *   -   +-------------------------------------------+
     *   |   |                                |          |
     *   |   |                                |          |
     *   |  y| -  -  -  -  -  -  -  -  -  -  -+          |
     *   |   |                                           |
     *   |   |                                           |
     *  v|   |                                           |
     *   |   |                                           |
     *   |   |                                           |
     *   |   |                                           |
     *   |   |                                           |
     *   |   |                                           |
     *   V   +-------------------------------------------+
     *
     * La position du pixel est O + x*u + y*v.
     *
     * @returns la position du pixel dans la scène
     */
    public Point3d pixelPosition(int column, int line) {
        Point3d O = coinSupGaucheEcran;
        double x = (column+0.5) / nbColonnesDansImage;
        double y = (line+0.5) / nbLignesDansImage;
        Vecteur3d u = vecteurHorizontalEcran;
        Vecteur3d v = vecteurVerticalEcran;
        return O.plus(u.multiplie(x)).plus(v.multiplie(y));
    }



    private Point3d position; // position de l'oeil de la caméra dans l'espace
    private Vecteur3d orientationRegard; // unitaire
    private Vecteur3d orientationVersLeHaut; // unitaire
    private double distanceOeilEcran;  
    private int nbColonnesDansImage; // nombre de colonnes de l'image
    private int nbLignesDansImage; // number de lignes de l'image
    private double largeurEcran; // largeur de l'écran dans le repère de la scène
    private double hauteurEcran; // hauteur de l'écran dans le repère de la scène
    private Point3d coinSupGaucheEcran; // absolute position
    private Vecteur3d vecteurHorizontalEcran; // déplacement du bord gauche au bord droit de l'image.
    private Vecteur3d vecteurVerticalEcran; // déplacement du bord supérieur au bord inférieur de l'image.
    private Image image;  // L'image générée
    private String nomFichierImage; // ficher où sauvegarder l'image

}


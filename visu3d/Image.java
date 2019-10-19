/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe implémente une image.
 *
 * Permet de charger un fichier image (.jpg, .png ou .gif) et d'en consulter la
 * valeur des pixels.
 *
 * Permet également de créer une nouvelle image à partir du nombre de colonnes
 * et de lignes désirées.
 *
 ******************************************************************************/

package org.xprov.visu3d;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Classe Image
 *
 * Il s'agit principalement d'une surcouche sur la classe BufferedImage.
 *
 * Le principal intérêt est d'offrir un constructeur à partir d'un nom de
 * fichier et un accesseur aux pixels à partir de coordonnées relatives.
 *
 * Des coordonnées relatives sont des flottants dans l'intervalle [0,1[ qui
 * sont ensuite converties en entiers dans l'intervalle correspondant selon
 * qu'il s'agit de la coordonnée x ou y.
 */
public class Image 
{
    /**
     * Constructeur à partir d'un nombre de colonnes et de lignes.
     *
     * Initialise une image vide à partir de ses dimensions.
     *
     * @param nbColonnes  largeur de l'image
     * @param nbLignes    hauteur de l'image
     */
    public Image(int nbColonnes, int nbLignes) {
        bufImg = new BufferedImage(nbColonnes, nbLignes, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Constructeur à partir d'un fichier.
     *
     * Charge le fichier dans un objet BufferedImage.
     *
     * @param fichier  nom du fichier à charger
     */
    public Image(String fichier) {
        try {
            bufImg = ImageIO.read(new File(fichier));
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    public int getNbColonnes() {
        return bufImg.getWidth();
    }

    public int getNbLignes() {
        return bufImg.getHeight();
    }

    /**
     * Retourne la couleur du pixel.
     *
     * @param colonne  numéro de colonne du pixel
     * @param ligne  numéro de ligne du pixel
     * @return  la couleur du pixel
     */
    public Couleur getPixel(int colonne, int ligne) {
        return new Couleur(bufImg.getRGB(colonne, ligne));
    }

    /**
     * Retourne la couleur de la texture en fonction des cooronnées relatives
     * (x,y) où x et y sont dans l'intervalle [0,1[.
     *
     *
     *       0.0         x         1.0
     *   0.0 +---------------------+
     *       |                     |
     *       |                     |
     *    y  |                     |
     *       |                     |
     *       |                     |
     *       |                     |
     *   1.0 +---------------------+
     *
     * @param x  coordonnée x normalisée (0.0 <= x < 1.0)
     * @param y  coordonnée y normalisée (0.0 <= y < 1.0)
     * @return   la couleur de la texture à l'endroit spécifié
     */
    public Couleur getCouleurCoordsRelatives(double x, double y) {
        assert(0.0 <= x && x < 1.0 && 0.0 <= y && y < 1.0);
        int colonne = (int) Math.floor(x * getNbColonnes());
        int ligne   = (int) Math.floor(y * getNbLignes());
        return getPixel(colonne, ligne);
    }


    /**
     * Spécifie ou remplace la couleur du pixel.
     * 
     * @param colonne  numéro de colonne du pixel
     * @param ligne  numéro de ligne du pixel
     * @param couleur la nouvelle couleur du pixel
     */
    public void setPixel(int colonne, int ligne, Couleur couleur) {
        bufImg.setRGB(colonne, ligne, couleur.toInt());
    }

    /**
     * Sauvegarde l'image dans un fichier.
     *
     * Ce fichier doit être au format jpg, png ou gif.
     *
     * @param nomFichier le nom du fichier où l'image est sauvegardée
     */
    public void sauvegarder(String nomFichier) {
        // On vérifie que l'extension fournie correspond à une des extensions connues
        String extensionSpecifiee = nomFichier.substring(nomFichier.lastIndexOf(".") + 1);
        String[] extensions = {"jpg", "png", "gif"};
        String extension = null;
        for (String e : extensions) {
            if (e.equals(extensionSpecifiee)) {
                extension = e;
            }
        }
        // Si l'extension n'a pas été reconnue.
        if (extension == null) {
            throw new IllegalArgumentException("Format d'image (=" + extensionSpecifiee + ") inconnu.");
        }

        try {
            File file = new File(nomFichier);
            ImageIO.write(bufImg, extension, file);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    private BufferedImage bufImg; // conteneur de l'image

}



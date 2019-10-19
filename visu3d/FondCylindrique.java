/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe implémente fond statique de forme cylindrique.
 *
 * Une image (qu'on espère de type panoramique) est `mappée` autour de la scène
 * de manière cylindrique.
 *
 * Le haut et le bas de ce cylindre sont monochrome. La couleur du haut est 
 * déterminée par la couleur moyenne des pixels de la ligne du haut de l'image.
 * La couleur du bas est la moyenne des pixels de la ligne du bas de l'image.
 *
 * C'est façon de faire donne un résultat particulièrement moche mais c'est ça
 * qui est ça pour l'instant.
 *
 ******************************************************************************/

package org.xprov.visu3d;

import org.xprov.visu3d.linalg.Vecteur3d;

/**
 * Classe FondCylindrique
 *
 * Un fond cylindrique contient une image mappé de manière cylindrique.
 * L'orientation (1, 0, 0) pointe au milieu de l'image.
 *
 * Une orientation pointe vers à l'extérieure du cylindre si elle fait un angle
 * supérieure à 45 degrés vers le haut ou inférieure à 45 degrés vers le bas.
 *
 */
public class FondCylindrique implements Fond 
{
    /**
     * Constructeur à partir d'une image.
     *
     * @param fichierImage  fichier contenant l'image du cylindre
     */
    public FondCylindrique(String fichierImage) {
        image = new Image(fichierImage);
        this.fichierImage = fichierImage;
        int nbCol = image.getNbColonnes();
        int nbLignes = image.getNbLignes();
        Vecteur3d accumulateur;

        // Calcul de la couleur moyenne de la ligne du haut de l'image.
        accumulateur = new Vecteur3d(0.0, 0.0, 0.0);
        for (int i=0; i<nbCol; i++) {
            accumulateur = accumulateur.plus(image.getPixel(i, 0).toVecteur());
        }
        moyenneHaut = new Couleur(accumulateur.multiplie(1.0 / nbCol));

        // Calcul de la couleur moyenne de la ligne du bas de l'image.
        accumulateur = new Vecteur3d(0.0, 0.0, 0.0);
        for (int i=0; i<nbCol; i++) {
            accumulateur = accumulateur.plus(image.getPixel(i, nbLignes-1).toVecteur());
        }
        moyenneBas = new Couleur(accumulateur.multiplie(1.0 / nbCol));
    }

    /*
     * Détermine la couleur observée dans une orientation donnée.
     *
     * @param orientation  vecteur normalisé dans l'orientation du regard
     * @return  un impact contenant une couleur
     */
    public Impact getImpact(Vecteur3d orientation) {
        double x = orientation.getX();
        double y = orientation.getY();
        double z = orientation.getZ();

        Couleur couleur = null;

        //if (va_z < Math.max(va_x, va_y)) {
        if (z*z < x*x + y*y) {
            // dans le cylindre
           
            // l'angle obtenu par atan2 est de -pi à pi. On veut rapporter cette valeur 
            // de manière proportionnelle dans l'intervale [0, 1).
            double angle = Math.atan2(y, x);
            double ratioHorizontal = (angle + Math.PI) / (2.0*Math.PI);

            // ratio vertical, valeur normalisée dans [0,1). 0 correspond à un
            // angle de pi/4 vers le bas. 1 correspond à un angle de pi/4 vers
            // le haut.
            double ratioVertical = (1.0 - z / Math.sqrt(x*x + y*y + z*z)) / 2.0;
            couleur = image.getCouleurCoordsRelatives(ratioHorizontal, ratioVertical);
            
        } else {
            // à l'extérieur du cylindre
            if (z > 0.0) {
                couleur = moyenneHaut;
            } else {
                couleur = moyenneBas;
            }
        }
        return Impact.impactAvecJusteCouleur(couleur);
    }

    public String toString() {
        return "Fond cylindrique\n" 
            + "  Image : \"" + fichierImage;
    }



    private String fichierImage; // nom du fichier de l'image
    private Image image; // l'image mappée sur le cylindre
    private Couleur moyenneHaut; // couleur à l'extérieur du cylindre, en haut
    private Couleur moyenneBas; // couleur à l'extérieur du cylindre, en bas
}

/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe implémente fond sphérique autour d'une scène.
 *
 * Deux images sont `mappées`, l'une sur l'hémisphère nord et l'autre sur
 * l'hémisphère sud.
 *
 * Dans l'état actuel, les calculs utilisés pour effectuer le `mapping` est
 * images les déforme significativement.
 *
 * TODO réécrire cette classe.
 *
 ******************************************************************************/

package org.xprov.visu3d;

import org.xprov.visu3d.linalg.Vecteur3d;

/**
 * Classe FondSpherique
 *
 * Simule un fond sphérique autour de la scène. Les couleurs de ce fond sont
 * définis par deux images. L'une sert de texture pour les rayon dirigés vers
 * le haut et l'autre pour les rayons dirigés vers le bas.
 */
public class FondSpherique implements Fond 
{
    /**
     * Constructeur à partir de deux images.
     *
     * @param fichierImageDuHaut  texture de l'hémisphère nord
     * @param fichierImageDubas   texture de l'hémisphère sud
     */
    public FondSpherique(String fichierImageDuHaut, String fichierImageDessous) {
        imageDuHaut = new Image(fichierImageDuHaut);
        imageDuBas = new Image(fichierImageDuBas);
    }



    /**
     * Détermine la couleur observée dans une orientation donnée.
     *
     * TODO : revoir ces calculs pour obtenir une déformation moins importante.
     *
     * @param orientation  vecteur normalisé dans l'orientation du regard
     * @return  un impact contenant une couleur et une normale
     */
    public Impact getImpact(Vecteur3d orientation) {
        double x = orientation.getX();
        double y = orientation.getY();
        double z = orientation.getZ();

        Couleur couleur = null;
        if (z >= 0) {
            double x_rel = 0.5 + x/2.0;
            double y_rel = 0.5 + y/2.0;
            couleur = imageDuHaut.getCouleurCoordsRelatives(x_rel, y_rel);
        } else {
            double x_rel = 0.5 + x/2.0;
            double y_rel = 0.5 + y/2.0;
            couleur = imageDuBas.getCouleurCoordsRelatives(x_rel, y_rel);
        }

        return Impact.impactAvecJusteCouleur(couleur);
    }


    public String toString() {
        return "Fond sphérique\n" 
            + "  Image du haut : \"" + fichierImageDuHaut
            + "  Image du bas  : \"" + fichierImageDuBas;
    }



    private String fichierImageDuHaut; // nom du fichier de l'image du haut
    private String fichierImageDuBas;  // nom du fichier de l'image du haut
    private Image imageDuHaut;  // texture de l'hémisphère nord
    private Image imageDuBas;   // texture de l'hémisphère sud
}

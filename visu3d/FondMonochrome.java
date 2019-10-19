/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe implémente un fond monochrome.
 *
 * Il s'agit du fond le plus simple imaginable. Une couleur, toujours la même,
 * est retournée peu importe l'orientation du rayon.
 *
 */
package org.xprov.visu3d;

import org.xprov.visu3d.linalg.Vecteur3d;

/**
 * Classe FondMonochrome
 * 
 * Contient une couleur et cette couleur est utilisée dans toutes les
 * orientations.
 */
public class FondMonochrome implements Fond {

    /**
     * Constructeur
     *
     * @param couleur  la couleur du fond
     */
    public FondMonochrome(Couleur couleur) {
        this.couleur = couleur;
    }

    /**
     * Retourne la couleur, indépendamment de l'orientation du rayon
     * 
     * @param orientation  l'orientation du rayon
     * @return  la couleur du fond
     */
    public Impact getImpact(Vecteur3d orientation) {
        return Impact.impactAvecJusteCouleur(couleur);
    }


    public String toString() {
        return "Fond monochrome " + couleur;
    }

    private Couleur couleur; // la couleur du fond

}

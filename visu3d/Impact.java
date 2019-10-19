/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe n'est qu'un conteneur passif dans lequel on stoque toutes les
 * informations relatives à l'intersection d'un rayon avec un objet visible ou
 * le fond.
 *
 */
package org.xprov.visu3d;

import org.xprov.visu3d.linalg.Vecteur3d;
import org.xprov.visu3d.linalg.Point3d;

import java.lang.Math;


/**
 * Classe Impact
 *
 * Décrit les caractéritiques du point d'impact d'un rayon sur un objet visible
 * ou le fond d'une scène.
 *
 * Stoque les informations suivantes :
 * 
 *  - la position de l'impact avec l'objet
 *  - la distance (stockée au carrée pour accélérer les calculs)
 *  - le vecteur normal au plan tangent à l'objet au point d'impact
 *  - la couleur
 *  - le coefficient de réflexivité
 *
 * Note : certaines de ces informations peuvent ne pas être spécifiées.
 */
class Impact
{
    /**
     * Constructeur à partir de seulement une couleur.
     *
     * @param couleur  couleur perçue du point d'impact
     */
    static public Impact impactAvecJusteCouleur(Couleur couleur) {
        //return new Impact(null, Double.POSITIVE_INFINITY, null, couleur, 0.0);
        return new Impact(null, 1e6, null, couleur, 0.0);
    }

    /**
     * Constructeur à partir de seulement une couleur et un vecteur normal.
     *
     * @param couleur  couleur perçue du point d'impact
     * @param normale  vecteur normal à la surface au point d'impact
     */
    static public Impact impactAvecJusteCouleurEtNormale(Couleur couleur, Vecteur3d normale) {
        return new Impact(null, Double.POSITIVE_INFINITY, normale, couleur, 0.0);
    }

    /**
     * Constructeur à partir de toutes les informations stoquées.
     *
     * @param position  position du point d'impact
     * @param distanceCarree  le carré de la distance entre l'origine du rayon et l'impact
     * @param normale  vecteur normal à la surface au point d'impact
     * @param couleur  couleur perçue du point d'impact
     * @param reflexivite  coefficient de reflexivité au point d'impact
     */
    public Impact(
            Point3d position,
            double distanceCarree,
            Vecteur3d normale,
            Couleur couleur, 
            double reflexivite) {
        this.position = position;
        this.distanceCarree = distanceCarree;
        this.normale = normale;
        this.couleur = couleur;
        this.reflexivite = reflexivite;
    }


    public String toString() {
        return "Impact\n"
         + "  position    : " + position    + "\n"
         + "  distance    : " + Math.sqrt(distanceCarree) + "\n"
         + "  normale     : " + normale     + "\n"
         + "  couleur     : " + couleur     + "\n"
         + "  reflexivite : " + reflexivite;
    }

    public Point3d getPosition() {
        return position;
    }

    public double getDistanceAuCarree() {
        return distanceCarree;
    }

    public Vecteur3d getNormale() {
        return normale;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public double getReflexivite() {
        return reflexivite;
    }

    private Point3d position;      // position du point d'impact
    private double distanceCarree; // le carré de la distance entre l'origine du rayon et l'impact
    private Vecteur3d normale;     // vecteur normal à la surface au point d'impact
    private Couleur couleur;       // perçue du point d'impact
    private double reflexivite;    // coefficient de reflexivité au point d'impact
}


/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Interface des objets visible que meublent une scène.
 *
 * Un objet visible doit être en mesure de déterminer si un rayon l'intersecte
 * ou non.
 */
package org.xprov.visu3d;

import org.xprov.visu3d.linalg.Vecteur3d;
import org.xprov.visu3d.linalg.Point3d;

/**
 * Interface Visible
 * 
 * Pour être Visible, un objet doit implémenter la fonction qui détermine si un
 * rayon l'intersecte.
 */
public interface Visible
{
    /**
     * Détermine si le rayon émanent du point `origine` et émis dans l'orientation
     * `orientation` entre en contact avec l'objet visible.
     *
     * Si c'est le cas, un objet de type Impact décrivant les caractéristiques
     * du point d'impact est retourné.
     *
     * Si le rayon n'intersecte pas l'objet alors la valeur `null` est retournée.
     *
     * @return  la description de l'impact s'il y en a un, null sinon.
     */
    public Impact intersectionRayon(Point3d origine, Vecteur3d orientation);


}

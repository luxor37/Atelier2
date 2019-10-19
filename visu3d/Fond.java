/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Interface qui définit un fond statique (background) pour une scène 3D.
 *
 * Le fond sert à déterminer la couleur observée pour les rayons qui
 * n'intersectent aucun des objets visibles de la scène.
 *
 * Ce fond est dit `statique` car la couleur observée ne dépent pas de la
 * position d'où le rayon est émis.
 *
 ******************************************************************************/

package org.xprov.visu3d;

import org.xprov.visu3d.linalg.Vecteur3d;

/**
 * Interface Fond
 * 
 * Permet de calculer une couleur à partir d'une orientation.
 */
public interface Fond {
    
    /**
     * Couleur du fond en fonction de la orientation.
     *
     * @param orientation  vecteur unitaire décrivant l'orientation du rayon
     * @return  la couleur du fond dans cette direction
     */
    public Impact getImpact(Vecteur3d orientation);
}

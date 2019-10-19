/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe implémente une scène 3D
 *
 * Une scène est constituée d'objects visibles ainsi que de lumières.
 *
 * (Note : la gestion des lumières n'est pas encore implémentée)
 *
 * Lorsqu'on lance un rayon dans la scène, on teste si ce rayon intersecte
 * chacun des objects visibles. Si le rayon intersecte plusieurs objets, on
 * retient le point d'impact dont la distance au point de départ du rayon est
 * minimale.
 *
 * Dans le cas où un rayon n'intersecte aucun des objets de la scène, un objet
 * de type Fond permet de calculer la couleur perçue par le rayon à
 * partir d'une orientation, indépendamment de la position.
 *
 ******************************************************************************/

package org.xprov.visu3d;

import org.xprov.visu3d.linalg.Vecteur3d;
import org.xprov.visu3d.linalg.Point3d;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe Scene
 * 
 * Une scène est un environnement de dimension 3 où des objets sont
 * positionnés.
 */
public class Scene
{
    /**
     * Constructeur
     *
     * À l'initialisation, une scène ne contient aucun object visible, aucune
     * limuère et le fond par défaut retourne le couleur blanche, peu importe
     * l'orientation du rayon.
     */
    public Scene() {
        visibles = new ArrayList<Visible>();
        lumieres = new ArrayList<Lumiere>();
        fond = new FondMonochrome(new Couleur("blanc")); // par défaut
    }

    /**
     * Ajoute un objet visible dans la scène.
     *
     * @param v  l'objet ajouté à la scène
     */
    public void ajoute(Visible v) {
        visibles.add(v);
    }

    /**
     * Ajoute une lumière dans la scène.
     *
     * @param l  la lumière ajoutée à la scène
     */
    public void ajoute(Lumiere l) {
        lumieres.add(l);
    }

    /**
     * Définit ou remplace le fond de la scène. Le fond détermine la couleur
     * des rayons qui n'intersectent aucun objet visible.
     *
     * @param fond  le nouveau fond
     */
    public void setFond(Fond fond) {
        this.fond = fond;
    }

    /**
     * Calcule le premier impact du rayon avec un des objets visibles de la
     * scène.
     *
     * La fonction retourne un objet de la classe Impact. Cet objet agglomère
     * toutes les information qui ont pu être calculées au point d'impact du
     * rayon avec le premier objet visible intersecté.
     *
     * Si le rayon n'intersecte aucun objet visible alors l'impact est calculé
     * par le fond. Dans ce cas, l'impact n'a pas de position.
     *
     * @param origine    point de départ du rayon
     * @param orientation  orientation du rayon (unitaire)
     * @return  un objet Impact décrivant le point d'intersection du rayon.
     */
    public Impact intersectionRayon(Point3d origine, Vecteur3d orientation) {
        assert(orientation.estUnitaire());

        // Initialisation avec des valeurs bidons et distance à l'infini
        Impact premierImpact = new Impact(
                null, // position
                Double.POSITIVE_INFINITY, // distance carrée
                null, // normale
                null, // couleur
                0.0); // réflexivité

        Impact impact;
        // Itération sur tous les objets visibles de la scène
        for (Visible v : visibles) {
            impact = v.intersectionRayon(origine, orientation);
            if (impact != null) {
                MessageDebug.msg("+++ Impact avec un object visible +++");
                MessageDebug.msg("  Object : " + v);
                MessageDebug.msg("  Impact : " + impact);
            }
            // S'il y a bien impact avec l'objet et que celui-ci est devant les autres
            if (impact != null && impact.getDistanceAuCarree() < premierImpact.getDistanceAuCarree()) {
                premierImpact = impact; // MAJ de l'impact le plus près
            }
        }

        // Si le premier impact est à distance infinie, c'est qu'aucun objet
        // visible n'a été frappé par le rayon.
        if (premierImpact.getDistanceAuCarree() == Double.POSITIVE_INFINITY) {
            premierImpact = fond.getImpact(orientation);
        }

        return premierImpact;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Scene avec " + visibles.size() 
                + " objets visibles et " + lumieres.size() 
                + " lumieres :\n");
        for (int i=0; i<visibles.size(); i++) {
            Visible v = visibles.get(i);
            sb.append(" Visible #" + (i+1) + " : " + v + "\n");
        }
        for (int i=0; i<lumieres.size(); i++) {
            Lumiere l = lumieres.get(i);
            sb.append(" Lumiere #" + (i+1) + " : " + l + "\n");
        }
        sb.append(" Fond :" + fond);
        return sb.toString();
    }

    /**
     * Itérateur sur l'ensemble des lumières de la scène, sans ordre particulier.
     * @return itérateur sur les lumières
     */
    public Iterator<Lumiere> getIterateurDeLumieres() {
        return lumieres.iterator();
    }

    private ArrayList<Visible> visibles; // contient les objects visibles
    private ArrayList<Lumiere> lumieres; // contient les objets qui émettent de la lumière
    private Fond fond; // Le fond statique de la scène.
}


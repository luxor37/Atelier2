/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe implémente un vecteur dans l'espace RxRxR.
 *
 */
package org.xprov.visu3d.linalg;

import org.xprov.visu3d.Constantes;

import java.util.ArrayList;
import java.lang.Math;

/**
 * Classe Vecteur3d
 * 
 * Un vecteur de dimension 3 dont les compsantes sont représentées par trois
 * variables de type double.
 */
public class Vecteur3d extends PointVecteur3d
{

    public Vecteur3d(PointVecteur3d pv) {
        super(pv);
    }

    public Vecteur3d(double x, double y, double z) {
        super(x, y, z);
    }

    public Vecteur3d(String s) {
        super(s);
    }

    /**
     * Addition de vecteurs.
     *
     * @param v  un vecteur
     * @return  vecteur somme
     */
    public Vecteur3d plus(Vecteur3d v) {
        return new Vecteur3d(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    /**
     * Soustraction de vecteurs
     *
     * @param v  un vecteur
     * @return  le vecteur this - v
     */
    public Vecteur3d moins(Vecteur3d v) {
        return new Vecteur3d(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    /**
     * Multiplication par un scalaire.
     *
     * @param d  un scalaire
     * @return  le vecteur d * this.
     */
    public Vecteur3d multiplie(double d) {
        return new Vecteur3d(d*this.x, d*this.y, d*this.z);
    }

    /**
     * Produit scalaire.
     *
     * @param v  un vecteur
     * @return  le produit scalaire de this et v
     */
    public double prodScal(Vecteur3d v) {
        return this.x*v.x + this.y*v.y + this.z*v.z;
    }

    /**
     * Produit vectoriel.
     *
     * Calcule le produit vectoriel de ``this`` et ``v``.
     *
     * @param v  vecteur
     * @return   le produit vectoriel this x v
     */
    public Vecteur3d prodVect(Vecteur3d v) {
        return new Vecteur3d(this.y*v.z - this.z*v.y,
                           this.z*v.x - this.x*v.z,
                           this.x*v.y - this.y*v.x);
    }

    /**
     * Calcule le carré de la norme du vecteur.
     * 
     * En général, on préfère utiliser cette mesure de longueur plutôt que la
     * norme comme tel. Lorsqu'on cherche uniquement à comparer des vecteurs
     * entre eux, l'utilisation de la norme au carrée permet d'éviter le calcul
     * d'une racine carrée .
     *
     * @return  le carré de la norme de this
     */
    public double normeCarree() {
        return x*x + y*y + z*z;
    }

    /**
     * Norme du vecteur
     *
     * return  la norme du vecteur
     */
    public double norme() {
        return Math.sqrt(this.normeCarree());
    }

    /**
     * Retoune le vecteur unitaire possédant la même orientation que this.
     *
     * @return  le normalisé de this
     */
    public Vecteur3d normalise() {
        return this.multiplie(1.0 / this.norme());
    }

    /**
     * Test si le vecteur est unitaire.
     *
     * @return true si le vecteur est unitaire, false sinon
     */
    public boolean estUnitaire() {
        return Math.abs(normeCarree() - 1.0) < Constantes.EPSILON;
    }

    /**
     * Retourne la réflexion du Vecteur3d lorsqu'il frappe le plan dont le
     * Vecteur3d normal est `normale`.
     *
     * Autrement dit, retourne l'orientation du rebond lorsqu'une balle est
     * lancée dans la directoin de `this` et repondi sur un plan dont le
     * Vecteur3d normal est `normale`.
     *
     *              |   ∧ 
     *    normale   |   |
     *         \     | réflexion
     *          \    |  |
     *            \   | -   /
     *             \  |    /
     *  -----        \ |  /
     *       ------   \| /
     *             -----/
     *   |--this-->    /
     *                /
     *               /
     *            (plan)
     *
     * @param normale vecteur perpendiculaire du plan tangent
     * @return        vecteur normalisé dans l'orientation de la réflexion
     */
    public Vecteur3d reflexion(Vecteur3d normale) {
        assert(normale.estUnitaire());
        //
        // Exercice 3 : réflexion d'un vecteur en fonction du vecteur normal au plan tangent.
        // 
        return this; 
    }



}




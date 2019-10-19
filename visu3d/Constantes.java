/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe définit des constantes numériques.
 *
 * Note : les constantes déjà définies dans le module Math ne sont pas
 * redéfinies ici.
 *
 ******************************************************************************/

package org.xprov.visu3d;

public class Constantes
{
    // L'arithmétique avec des variables de type double pose un problème pour
    // tester l'égalité en raison des erreurs d'arrondi. La constantes EPSILON
    // est un nombre suffisemment petit pour que lorsque deux nombre diffèrent
    // d'une valeur inférieurs à EPSILON on considère qu'ils sont égaux.
    //
    // Le test ``z == 0`` devient dont ``abs(z) < Constantes.EPSILON``
    public static final double EPSILON = 1e-6;
};

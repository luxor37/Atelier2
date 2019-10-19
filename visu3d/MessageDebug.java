/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Outil destiné à afficher des messages de debug.
 *
 * L'affichage des messages peut être activée et désactivée pendant l'exécution
 * du programme.
 */

package org.xprov.visu3d;

public class MessageDebug
{
    public static void activeDebug() {
        actif = true;
    }

    public static void desactiveDebug() {
        actif = false;
    }

    public static void setEtatActif(boolean etat) {
        actif = etat;
    }

    public static void msg(String message) {
        if (actif) {
            System.out.println(message);
        }
    }

    private static boolean actif = false;
}


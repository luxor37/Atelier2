/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe implémente un rectagle dans une scène 3D.
 *
 * Un rectangle est un objet bidimensionnel, il n'a donc pas d'épaisseur.
 *
 * Pour améliorer les performances, un rectangle n'est visible que d'un côté,
 * soit celui pour lequel les points décrivant ses sommets tournent en sens
 * anti-horaire. Ainsi, lorsqu'on teste s'il y a collision avec un rayon, on
 * commence par tester si ce rayon provient du côté visible ou non. S'il vient
 * du côté non-visible, aucun calcul suplémentaire n'est effectué.
 */
package org.xprov.visu3d;

import org.xprov.visu3d.linalg.Vecteur3d;
import org.xprov.visu3d.linalg.Point3d;
//import org.xprov.visu3d.linalg.Matrice;

/**
 * Représente un rectangle 2D dans un espace en dimension 3.
 */
public class Rectangle implements Visible
{

    /**
     * Constructeur à partir de quatre points.
     *
     * Remarque : les quatre points droivent être orientés de manière à tourner
     * en sens anti-horaire du côté visible.
     *
     *    p0                        p3
     *      +----------------------+
     *      |                      |
     *      |                      |
     *      |                      |
     *      |                      |
     *      |                      |
     *      +----------------------+
     *    p1                        p2
     *  
     * Note : un couleur est exigée à l'initilisation même si, après coup, une
     * image peut être affectée comme texture au rectangle. Dans ce cas, la
     * texture remplace la couleur.
     *
     * @param p0  coin en haut à gauche
     * @param p1  coin en bas à gauche
     * @param p2  coin en bas à droite
     * @param p3  coin en haut à droite
     * @param couelur  couleur du rectangle
     */
    public Rectangle(Point3d p0, Point3d p1, Point3d p2, Point3d p3, Couleur couleur) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.couleur = couleur;
        //      
        // u : vecVectical
        // v : vecHorizontal
        //      p0
        //  -   +----------------------+ p3
        //  |   |                      |
        //  |   |                      |
        //  u   |                      |
        //  |   |                      |
        //  |   |                      |
        //  V   +----------------------+
        //    p1|--------- v ---------->p2
        //
        vecVertical = p1.moins(p0);
        vecHorizontal = p2.moins(p1);

        // On vérifie que p0, p1 et p2 forment bien un angle droit
        if (Math.abs(vecVertical.prodScal(vecHorizontal)) > Constantes.EPSILON) {
            throw new IllegalArgumentException("Initialisation d'un rectangle à partir " 
                    + "de points qui ne forment pas un angle droit");
        }

        // On vérifie que le 4e point complète bien le rectangle
        if (p0.plus(vecHorizontal).moins(p3).normeCarree() > Constantes.EPSILON) {
            throw new IllegalArgumentException("Initialisation d'un rectangle à partir "
                    + "de points qui ne forment pas un rectangle (4e point invalide)");
        }
        this.normale = vecHorizontal.prodVect(vecVertical).normalise();

        // Initilisation des options d'affichage avec les valeurs par déaut
        this.texture = null;
        this.reflexivite = 0.0;
    }

    /**
     * Spécifie ou remplace la valeur du coefficient de reflexivité de l'objet.
     *
     * @param reflexivite  le coefficient de reflexivite
     */
    public void setReflexivite(double reflexivite) {
        this.reflexivite = reflexivite;
    }

    /**
     * Spécifie ou remplace l'image de texture appliquée sur le rectangle.
     *
     * @param fichier  fichier de l'image à `mapper` sur le rectangle
     */
    public void setTexture(String fichier) {
        nomFichierTexture = fichier;
        texture = new Image(fichier);
    }


    public String toString() {
        return "Rectangle\n" 
            + "  points      : " + p0 + ", " + p1 + ", " + p2 + ", " + p3 + "\n"
            + "  normale     : " + normale       + "\n"
            + "  couleur     : " + couleur      + "\n"
            + "  texture     : " + nomFichierTexture   + "\n"
            + "  reflexivite : " + reflexivite;
    }


        
    /**
     * Calcule l'intersection d'un rayon avec le rectangle.
     *
     * Étant donné un rayon lancé depuis le point `origine` dans l orientation
     * `orientation`, le point d'intersection de ce rayon sur le rectangle est
     * calculé, s'il existe.
     *
     * @param origine   Le point depuis lequel le rayon est lancé.
     * @param orientation Vecteur normalisé indiquant l'orientation du rayon.
     * @return          Un object `Impact` décrivant le point d'impact s'il
     *                  existe, null sinon.
     */
    public Impact intersectionRayon(Point3d origine, Vecteur3d orientation) {
        //
        // Le calcul se fait en deux étapes. Soit P le plan défini par le rectangle.
        //
        // (1) On calcule le point d'intersection du rayon et du plan P (s'il existe)
        // (2) On teste si ce point est dans le rectangle ou non.
        //
        // Géométriquement,
        //
        // O........                                                         
        //          ........                          p0                        
        //                  ........                                                 
        //                          ........                                
        //                                  ........            p3  
        //                                          ........        
        //                                                  ........
        //                                 p1                        ........(d)
        //
        //                                            p2
        //
        //
        // O = origine, d = orientation
        // Le rayon est l'ensemble des points de la forme O + z*d, pour z>0.
        //
        //
        // On pose u = vecVectical   = p1 - p0
        //     et  v = vecHorizontal = p2 - p1 = p3 - p0
        //
        // Les points du plan P sont tous de la forme p0 + x*u + y*v où x et y sont quelconques.
        //
        // On cherche donc l'unique triplet (x,y,z) tel que 
        //
        //                              |        | |x|      
        // p0 + x*u + y*v = O + z*d <-> | u v -d |.|y| = O-p0
        //                              |        | |z| 
        //
        //                              |x|   |        |(-1)    
        //                          <-> |y| = | u v -d |     . (O-p0)
        //                              |z|   |        | 

        // TODO Cette fonction sera probablement le sujet du 3e atelier.

        return null;
    }




    private Point3d p0, p1, p2, p3; // les quantre coins du rectangle

    private Vecteur3d vecVertical;    // p1 - p0 (calculé à l'initialisation)
    private Vecteur3d vecHorizontal;  // p2 - p1 (calculé à l'initialisation)
    private Vecteur3d normale;        // vecteur normal normalisé (calculé à l'initilisation)

    private Couleur couleur;        // Couleur du rectangle
    private double reflexivite;     // Indice de reflexivite
    private String nomFichierTexture;  // Fichier de texture, s'il y en a un
    private Image texture;  // Texture, s'il y en a une
}

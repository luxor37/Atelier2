/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe implémente un point dans l'espace RxRxR.
 *
 * Par abus de notation, certaines fonctionnalités sont définies sur les points
 * en considérant leur `vecteur position` de manière transparente. Par exemple, 
 * la fonction ``plus`` permet d'additionner un vecteur à un point.
 *
 */
package org.xprov.visu3d.linalg;

/**
 * Classe Point3d
 *
 * Un point dans l'espace est déterminé par ses trois composantes (x,y,z)
 * chacune représentée par un double.
 */
public class Point3d extends PointVecteur3d
{

    public Point3d(PointVecteur3d pv) {
        super(pv);
    }

    public Point3d(double x, double y, double z) {
        super(x, y, z);
    }

    public Point3d(String s) {
        super(s);
    }


    /**
     * Calcule le vecteur normal au plan défini par trois points.
     *
     * L'orientation du vecteur normal calculé est donné par la règle la main
     * droite appliquée aux vecteurs B-A et C-B.
     *
     * Plus précisément, si u=B-A et v=C-B,
     *
     *                     C
     *                    ┐
     *                   /
     *                v /
     *                 /
     *                /
     *        u      /
     * A ---------> B
     *
     * Le vecteur retourné est le produit vectoriel u x v. Ainsi, si les trois
     * points sont colinéaires, alors le vecteur (0,0,0) est retourné.
     *
     * Le vecteur retourné est de norme 1.
     *
     * @param A  premier point
     * @param B  deuxième point
     * @param C  troisième point
     * @return   vecteur nomal au plan défini par A,B,C s'il exite, le vecteur nul sinon.
     */
    public static Vecteur3d normaleATroisPoints(Point3d A, Point3d B, Point3d C) {
        Vecteur3d u = B.moins(A);
        Vecteur3d v = C.moins(B);
        return u.prodVect(v).normalise();
    }

    /**
     * Retourne le point correspondant au déplacement de `this` par le vecteur v.
     *
     * Si P le point représenté par `this` alors le point Q tel que PQ = v est retourné.
     *
     * @param v  vecteur déplacement
     * @return point translaté
     */
    public Point3d plus(Vecteur3d v) {
        return new Point3d(this.x + v.x, this.y + v.y, this.z + v.z);
    }


    /**
     * Retourne le point correspondant au déplacement de `this` par le vecteur -v.
     *
     * Si P le point représenté par `this` alors le point Q tel que QP = v est retourné.
     *
     * @param v  vecteur déplacement
     * @return point translaté
     */
    public Point3d moins(Vecteur3d v) {
        return new Point3d(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    /**
     * Retourne le vecteur déplcement de p à this.
     *
     * @param p  point de départ du vecteur calculé
     * @return vecteur déplacement
     */
    public Vecteur3d moins(Point3d p) {
        return new Vecteur3d(this.x - p.x, this.y - p.y, this.z - p.z);
    }


}


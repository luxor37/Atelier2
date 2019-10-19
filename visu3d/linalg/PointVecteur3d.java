/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe abstraite regroupe tout ce qui est commun entre un Point3D et
 * un Vecteur3d.
 */
package org.xprov.visu3d.linalg;

/**
 * Classe PointVecteur3d
 *
 * Cette classe abstraite regroupe les méthodes communes aux points et aux
 * vecteurs.
 */
public abstract class PointVecteur3d
{

    /**
     * Constructeur de copie.
     *
     * @param pv  objet à copier
     */
    PointVecteur3d(PointVecteur3d pv) {
        this.x = pv.x;
        this.y = pv.y;
        this.z = pv.z;
    }

    /**
     * Constructeur à partir des composantes.
     *
     * @param x  composante en x
     * @param y  composante en y
     * @param z  composante en z
     */
    PointVecteur3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructeur à partir d'une chaîne de caractères.
     *
     * La chaîne doit être sous la forme "( <X> , <Y> , <Z> )" où
     * <X> est la composante en x, <Y> celle en y et <Z> celle en z.
     * Les parenthèses et les virgules ne sont pas optionnelles.
     * Les espaces sont optionnels et peuvent être multiples.
     *
     * @param s  description textuelle du vecteur
     */
    PointVecteur3d(String s) {
        assert(s.startsWith("(") && s.endsWith(")"));
        s = s.substring(1, s.length()-1);
        String[] t = s.split(",");
        if (t.length != 3) {
            throw new IllegalArgumentException(
                    "Initialisation d'un vecteur à partir de données invalides. "
                    + "Les vecteurs doivent obligatoirement être de dimension 3.");
        }
        this.x = Double.parseDouble(t[0]);
        this.y = Double.parseDouble(t[1]);
        this.z = Double.parseDouble(t[2]);
    }

    PointVecteur3d(double[] t) {
        if (t.length != 3) {
            throw new IllegalArgumentException(
                    "Initialisation d'un vecteur à partir de données invalides. "
                    + "Les vecteurs doivent obligatoirement être de dimension 3.");
        }
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
    }

    public String toString() {
        return "(" + String.format("%.2f", this.x)  + ", "
            + String.format("%.2f", this.y) + ", "
            + String.format("%.2f", this.z) + ")"; 
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    double x,y,z; // composantes

}

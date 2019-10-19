/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Interface décrivant un lumière dans un scène 3D
 *
 */
package org.xprov.visu3d;

import org.xprov.visu3d.linalg.Point3d;

public interface Lumiere
{
    public abstract double intensity(Point3d position);
}

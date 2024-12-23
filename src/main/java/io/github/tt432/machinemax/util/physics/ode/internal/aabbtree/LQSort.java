/*************************************************************************
 *                                                                       *
 * Open Dynamics Engine 4J                                               *
 * Copyright (C) 2017 Piotr Piastucki, Tilmann Zaeschke                  *
 * All rights reserved.  Email: ode4j@gmx.de   Web: www.ode4j.org        *
 *                                                                       *
 * This library is free software; you can redistribute it and/or         *
 * modify it under the terms of EITHER:                                  *
 *   (1) The GNU Lesser General Public License as published by the Free  *
 *       Software Foundation; either version 2.1 of the License, or (at  *
 *       your option) any later version. The text of the GNU Lesser      *
 *       General Public License is included with this library in the     *
 *       file LICENSE.TXT.                                               *
 *   (2) The BSD-style license that is included with this library in     *
 *       the file ODE-LICENSE-BSD.TXT and ODE4J-LICENSE-BSD.TXT.         *
 *                                                                       *
 * This library is distributed in the hope that it will be useful,       *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the files    *
 * LICENSE.TXT, ODE-LICENSE-BSD.TXT and ODE4J-LICENSE-BSD.TXT for more   *
 * details.                                                              *
 *                                                                       *
 *************************************************************************/
package io.github.tt432.machinemax.util.physics.ode.internal.aabbtree;

/*
 * Based on https://github.com/turbulenz/turbulenz_engine/blob/master/tslib/aabbtree.ts
 */
public class LQSort extends Sort {

	final static LQSort INSTANCE = new LQSort();

	void sortNodes(AABBTreeNode<?>[] nodes, int numNodes, int numNodesLeaf) {
		sortNodesRecursive(nodes, 0, numNodes, 0, false, numNodesLeaf);
	}

	void sortNodesRecursive(AABBTreeNode<?>[] nodes, int startIndex, int endIndex, int axis, boolean reverse,
                            int numNodesLeaf) {
		int splitNodeIndex = ((startIndex + endIndex) >> 1);
		nthElement(nodes, startIndex, splitNodeIndex, endIndex, axis, reverse);
		axis = (axis + 2) % 3;
		reverse = !reverse;
		if ((startIndex + numNodesLeaf) < splitNodeIndex) {
			sortNodesRecursive(nodes, startIndex, splitNodeIndex, axis, reverse, numNodesLeaf);
		}
		if ((splitNodeIndex + numNodesLeaf) < endIndex) {
			sortNodesRecursive(nodes, splitNodeIndex, endIndex, axis, reverse, numNodesLeaf);
		}
	}

	/**
	 * @param axis
	 *            0 = X, 1 = Y, 2 = Z
	 */
    protected double getkey(AABBTreeNode<?> node, int axis, boolean reverse) {
        double v;
        switch (axis) {
        case 0:
            v = node.minX + node.maxX;
            break;
        case 1:
            v = node.minY + node.maxY;
            break;
        default:
            v = node.minZ + node.maxZ;
            break;
        }
        return reverse ? -v : v;
    }

}

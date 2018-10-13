/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int gridSize;
    private WeightedQuickUnionUF uf;
    private boolean[][] sites2dArray;
    private final static boolean BLOCKED = false;
    private final static boolean OPEN = true;
    private int openSites;
    private int dummyTopSiteIndex = 0;
    private int dummyBottomSiteIndex;


    public Percolation(int n) { // create n-by-n grid, with all sites blocked
        if (n < 1) { //The constructor should throw a java.lang.IllegalArgumentException if n ≤ 0.
            throw new IllegalArgumentException();
        }
        gridSize = n;
        sites2dArray = new boolean[gridSize][gridSize]; // each element is false (blocked) by default
        uf = new WeightedQuickUnionUF(n * n + 2); // +2 for dummy nodes.
        // For gridSize == n, (0, gridSize ^ 2 + 1) with 0, gridSize ^ 2 + 1 top and bottom dummy nodes
        System.out.println("Number of untouched components in WU: " + uf.count() );
        dummyBottomSiteIndex = gridSize * gridSize + 1;
        // we connect the top dummy node with all elements of 1st row
        for (int row = 1, col = 1; col <= gridSize; col++) {
            uf.union(0, siteIndex(row, col));
            System.out.println("Connected top dummy node 0 with site " + siteIndex(row, col));
        }
        // and also connect the bottom dummy node with all elements of the last row
        for (int row = gridSize, col = 1; col <= gridSize; col++) {
            int bottomDummyNodeIndex = gridSize * gridSize + 1;
            uf.union(bottomDummyNodeIndex, siteIndex(row, col));
            System.out.println("Connected bottom dummy node " + bottomDummyNodeIndex + " with site "
                                       + siteIndex(row, col));
        }
    }

    public void open(int row, int col) {    // open site (row, col) if it is not open already
        // if it is closed then we open it and connect it to top, bottom, left and right if they are
        // 1) valid and 2) open
        if (!isValidIndex(row, col)) throw new IllegalArgumentException();

        if (!isOpen(row, col)) { // each time we open a site we first check if it is open already
            sites2dArray[row - 1][col - 1] = OPEN; // open the site
            openSites++;
            // Then we open it and connect it to top, bottom, left and right if valid and open
            if (isValidIndex(row - 1, col) && isOpen(row - 1, col)) { //connect to top
                uf.union(siteIndex(row, col), siteIndex(row - 1, col));
            }
            if (isValidIndex(row + 1, col) && isOpen(row + 1, col)) { //connect to bottom
                    uf.union(siteIndex(row, col), siteIndex(row + 1, col));
            }
            if (isValidIndex(row , col + 1) && isOpen(row, col + 1)) { // connect to right
                uf.union(siteIndex(row, col), siteIndex(row , col + 1));
            }
            if (isValidIndex(row , col - 1) && isOpen(row, col - 1)) { // connect to left
                uf.union(siteIndex(row, col), siteIndex(row , col - 1));
            }
        }
    }

    public boolean isOpen(int row, int col) { // is site (row, col) open?
        if (!isValidIndex(row, col)) {
            throw new IllegalArgumentException();
        }
        return sites2dArray[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) { // is site (row, col) full?
        // A full site is an open site that can be connected to an open site in the top row via a
        // chain of neighboring (left, right, up, down) open sites.
        if (!isValidIndex(row, col)) {
            throw new IllegalArgumentException();
        }
        return (isOpen(row, col) && uf.connected(dummyTopSiteIndex, siteIndex(row, col)));
    }

    public int numberOfOpenSites() { // number of open sites
        return openSites;
    }

    public boolean percolates() { // does the system percolate?
        return uf.connected(dummyTopSiteIndex, dummyBottomSiteIndex);
    }

    private boolean isValidIndex(int row, int col) { // Private method which to validate indices
        return (row >= 1 && row <= gridSize && col >= 1 && col <= gridSize);
    }

    private int siteIndex(int row, int col) { // maps 2D coordinates to 1D utilizing grid size.
        // (1,1) -> 1, (1, 2) -> 2 etc. Range: 1 to gridSize^2
        return (row - 1) * gridSize + col;
    }

}
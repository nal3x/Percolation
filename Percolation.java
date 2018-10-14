import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 *  Sites are represented by sites2dArray, a (n x n) boolean array. All sites are
 *  initially blocked (false). Each site is represented via siteIndex as a unique number in a
 *  WeightedQuickUnionUF structure. Whenever a site is opened, it is connected (union) to its open
 *  neighboors (if any). A full site is an open site that can be connected to an open site in the
 *  top row via a chain of neighboring (left, right, up, down) open sites. The system percolates if
 *  there is a full site in the bottom row. In this implementation two dummy sites are used. The
 *  top one is connected to all sites of the top row and the bottom one to all sites of the bottom
 *  row. This means that a site is full if it is open and connected to the top dummy site and that
 *  the system percolates when the two dummy sites are connected.
 */

public class Percolation {

    private int n; //grid size
    private WeightedQuickUnionUF uf;
    private boolean[][] sites2dArray;
    private final static boolean BLOCKED = false;
    private final static boolean OPEN = true;
    private int openSites;
    private int dummyTopSiteIndex;
    private int dummyBottomSiteIndex;

    public Percolation(int n) { // Construcor creates n-by-n grid, with all sites blocked
        if (n < 1) { // and throws a java.lang.IllegalArgumentException if n ≤ 0.
            throw new IllegalArgumentException();
        }
        this.n = n;
        sites2dArray = new boolean[n][n]; // each element is false (blocked) by default
        uf = new WeightedQuickUnionUF(n * n + 2); // +2 for dummy nodes.
        // Sites with indices 0 and n^2 + 1 are top and bottom dummy nodes respectively
        dummyTopSiteIndex = 0;
        dummyBottomSiteIndex = n * n + 1;
        // we connect the top dummy node with all elements of 1st row
        for (int row = 1, col = 1; col <= this.n; col++) {
            uf.union(dummyTopSiteIndex, siteIndex(row, col));
        }
        // and also connect the bottom dummy node with all elements of the last row
        for (int row = n, col = 1; col <= n; col++) {
            uf.union(dummyBottomSiteIndex, siteIndex(row, col));
        }
    }

    public void open(int row, int col) {    // open site (row, col) if it is not open already
        if (!isValidIndex(row, col)) throw new IllegalArgumentException();

        if (!isOpen(row, col)) { // each time we open a site we first check if it is open already
            sites2dArray[row - 1][col - 1] = OPEN; // open the site
            openSites++;
            // Then we connect it to top, bottom, left and right sites if they are valid and open
            if (isValidIndex(row - 1, col) && isOpen(row - 1, col)) { // top
                uf.union(siteIndex(row, col), siteIndex(row - 1, col));
            }
            if (isValidIndex(row + 1, col) && isOpen(row + 1, col)) { // bottom
                    uf.union(siteIndex(row, col), siteIndex(row + 1, col));
            }
            if (isValidIndex(row , col + 1) && isOpen(row, col + 1)) { // right
                uf.union(siteIndex(row, col), siteIndex(row , col + 1));
            }
            if (isValidIndex(row , col - 1) && isOpen(row, col - 1)) { // left
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
        return (row >= 1 && row <= n && col >= 1 && col <= n);
    }

    private int siteIndex(int row, int col) { // maps 2D coordinates to 1D utilizing grid size.
        // (1,1) -> 1, (1, 2) -> 2 etc. Range: 1 to n^2
        return (row - 1) * n + col;
    }

}

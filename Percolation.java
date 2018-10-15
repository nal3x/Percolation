import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 *  Sites are represented by the (n x n) boolean sites2dArray. All sites are initially blocked,
 *  denoted by the false value. Each site is mapped to a unique number via siteIndex and this value
 *  is used in the WeightedQuickUnionUF structures. Whenever a site is opened, it is connected
 *  (union) to its open neighboors (if any). A full site is an open site that can be connected to
 *  an open site in the top row via a chain of neighboring (left, right, up, down) open sites.
 *  The system percolates if there is a full site in the bottom row. In this implementation two
 *  union-find data structures are used. The first one includes two dummy sites: top is connected to
 *  all sites of the top row and the bottom one to all sites of the bottom row. The system
 *  percolates when the two dummy sites are connected. The second union-find data structure is used
 *  to check for full sites and includes only the top dummy site to avoid "backwash", a situation
 *  where some sites at the bottom of the grid are falsely characterized as full because the top
 *  dummy node has connected to the bottom after the system has percolated. Another solution for
 *  the backwash problem would be to completely eliminate the bottom node and keep only one union-
 *  find structure (fullSitesUnionFind). But then, in order to check if the system percolates, we
 *  would have to iterate over sites of the bottom row until we find a full site (nlogn). The
 *  automatic grader showed memory and timing issues (Memory: 6/8 tests passed Timing: 7/20 tests
 *  passed). Maybe another idea for solving the backwash problem would be to extend the union-find
 *  class and add a disconnect method, removing the bottom node as soon as the system percolates?
 *  The solution as is resulted in 100% score.
 */

public class Percolation {

    private static final boolean OPEN = true;
    private final int n; // grid size
    private final WeightedQuickUnionUF percolationUnionFind;
    private final WeightedQuickUnionUF fullSitesUnionFind; // used only to include full sites
    private boolean[][] sites2dArray;
    private int openSites;
    private final int dummyTopSiteIndex;
    private final int dummyBottomSiteIndex;

    public Percolation(int n) { // Construcor creates n-by-n grid, with all sites blocked
        if (n < 1) { // throws a java.lang.IllegalArgumentException if n ≤ 0.
            throw new IllegalArgumentException();
        }
        this.n = n;
        sites2dArray = new boolean[n][n]; // each element is false (blocked) by default
        percolationUnionFind = new WeightedQuickUnionUF(n * n + 2); // +2 for dummy nodes.
        // Sites with indices 0 and n^2 + 1 are top and bottom dummy nodes respectively
        fullSitesUnionFind = new WeightedQuickUnionUF(n * n + 1); // sites + dummyTopSite
        dummyTopSiteIndex = 0;
        dummyBottomSiteIndex = n * n + 1;
        // we connect the top dummy node with all elements of 1st row in both UFs
        for (int row = 1, col = 1; col <= this.n; col++) {
            percolationUnionFind.union(dummyTopSiteIndex, siteIndex(row, col));
            fullSitesUnionFind.union(dummyTopSiteIndex, siteIndex(row, col));
        }
        // for the percolation UF only, we connect the bottom dummy node with all elements of the
        // last row.
        for (int col = 1; col <= n; col++) {
            percolationUnionFind.union(dummyBottomSiteIndex, siteIndex(n, col));
        }
    }

    public void open(int row, int col) {    // open site (row, col) if it is not open already
        if (!isValidIndex(row, col)) throw new IllegalArgumentException();

        if (!isOpen(row, col)) { // each time we open a site we first check if it is open already
            sites2dArray[row - 1][col - 1] = OPEN; // open the site
            openSites++;
            // Then we connect it to top, bottom, left and right sites if they are valid and open
            if (isValidIndex(row - 1, col) && isOpen(row - 1, col)) { // top
                    percolationUnionFind.union(siteIndex(row, col), siteIndex(row - 1, col));
                    fullSitesUnionFind.union(siteIndex(row, col), siteIndex(row - 1, col));
            }
            if (isValidIndex(row + 1, col) && isOpen(row + 1, col)) { // bottom
                    percolationUnionFind.union(siteIndex(row, col), siteIndex(row + 1, col));
                    fullSitesUnionFind.union(siteIndex(row, col), siteIndex(row + 1, col));
            }
            if (isValidIndex(row, col + 1) && isOpen(row, col + 1)) { // right
                percolationUnionFind.union(siteIndex(row, col), siteIndex(row, col + 1));
                fullSitesUnionFind.union(siteIndex(row, col), siteIndex(row, col + 1));
            }
            if (isValidIndex(row, col - 1) && isOpen(row, col - 1)) { // left
                percolationUnionFind.union(siteIndex(row, col), siteIndex(row, col - 1));
                fullSitesUnionFind.union(siteIndex(row, col), siteIndex(row, col - 1));
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
        return (isOpen(row, col) && fullSitesUnionFind.connected(dummyTopSiteIndex, siteIndex(row, col)));
    }

    public int numberOfOpenSites() { // number of open sites
        return openSites;
    }

    public boolean percolates() { // does the system percolate?
        if (n == 1) { // corner case for 1 site only
            return isOpen(1, 1);
        } else {
            return percolationUnionFind.connected(dummyTopSiteIndex, dummyBottomSiteIndex);
        }

    }

    private boolean isValidIndex(int row, int col) { // Private method which to validate indices
        return (row >= 1 && row <= n && col >= 1 && col <= n);
    }

    private int siteIndex(int row, int col) { // maps 2D coordinates to 1D utilizing grid size.
        // (1,1) -> 1, (1, 2) -> 2 etc. Range: 1 to n^2
        return (row - 1) * n + col;
    }

}

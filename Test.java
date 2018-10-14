/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Test {
    public static void main (String[] args) { //tester, optional
        // int size = 3;
        // Percolation percolation = new Percolation(size);
        // percolation.open(1,1);
        // percolation.open(2,1);
        // //percolation.open(3,1);
        // boolean connected = percolation.isFull(2,1);
        // System.out.println(connected);
        // boolean percolates = percolation.percolates();
        // System.out.println(percolates);

        int gridSize = 2;
        int trials = 10000;
        PercolationStats percolationStats = new PercolationStats(gridSize, trials);
        System.out.println("mean = " + percolationStats.mean());
        System.out.println("stdev = " + percolationStats.stddev());
        System.out.println("95% confidence interval = ["
                                   + percolationStats.confidenceLo()
                                   + ", "
                                   + percolationStats.confidenceHi()
                                   + "]");
    }
}


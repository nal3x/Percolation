/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    static int totalOpenSites;
    static int trials;
    static int gridSize;

        public PercolationStats(int n, int trials) { // perform trials independent experiments on an n-by-n grid
            this.trials = trials;
            this.gridSize = n;
            if (n < 1 || trials < 1) {
                throw new IllegalArgumentException();

            }
            for (int trial = 0; trial < trials; trial++) {
                //totalOpenSites = 0;
                Percolation percolation = new Percolation(n);
                //open a site randomly
                do {
                    percolation.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
                } while (!percolation.percolates());
                totalOpenSites += percolation.numberOfOpenSites();
            }
        }
        public static double mean() { // sample mean of percolation threshold
            return (double)totalOpenSites / trials / (gridSize * gridSize);

        }
        public double stddev() { // sample standard deviation of percolation threshold
            return -1.0;
        }
        public double confidenceLo() { // low  endpoint of 95% confidence interval
            return -1.0;
        }
        public double confidenceHi() { // high endpoint of 95% confidence interval
            return -1.0;
        }


    public static void main(String[] args) { // test client (described below)
            if (args.length == 2) {
                int gridSize = Integer.parseInt(args[0]);
                int trials = Integer.parseInt(args[1]);
                PercolationStats percolationStats = new PercolationStats(gridSize, trials);
                System.out.println("Mean: " + mean());
        }

    }
}
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    /**
     *  Performs #trials of computational experiments on an n-by-n grid and provides mean, standard
     *  deviation, and the 95% confidence interval for the percolation threshold.
     */
    private static final double CONFIDENCE_95 = 1.96;
    private final int trials; // total trials
    private final double mean;
    private final double standardDeviation;

        public PercolationStats(int n, int trials) { // perform trials independent experiments on an n-by-n grid
            if (n < 1 || trials < 1) {
                throw new IllegalArgumentException();
            }
            this.trials = trials;
            double[] fractionOfOpenSitesInTrial = new double[trials];

            for (int trial = 0; trial < trials; trial++) {
                Percolation percolation = new Percolation(n); // create new percolation for trial
                do { // open a site randomly
                    percolation.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
                } while (!percolation.percolates());
                // when system percolates save fraction of open sites to array
                fractionOfOpenSitesInTrial[trial] = (double) percolation.numberOfOpenSites()
                        / (n * n);
            }
            mean = StdStats.mean(fractionOfOpenSitesInTrial);
            standardDeviation = StdStats.stddev(fractionOfOpenSitesInTrial);
        }
        public double mean() { // sample mean of percolation threshold
            return mean;
        }
        public double stddev() { // sample standard deviation of percolation threshold
            return standardDeviation;
        }
        public double confidenceLo() { // low  endpoint of 95% confidence interval
            return mean - (CONFIDENCE_95 * standardDeviation) / Math.sqrt(trials);
        }
        public double confidenceHi() { // high endpoint of 95% confidence interval
            return mean + (CONFIDENCE_95 * standardDeviation) / Math.sqrt(trials);
        }

        public static void main(String[] args) {
            if (args.length == 2) {
                int n = Integer.parseInt(args[0]);
                int trials = Integer.parseInt(args[1]);
                PercolationStats percolationStats = new PercolationStats(n, trials);
                System.out.println("mean = " + percolationStats.mean());
                System.out.println("stddev = " + percolationStats.stddev());
                System.out.println("95% confidence interval = [" + percolationStats.confidenceLo()
                                           + ", "
                                           + percolationStats.confidenceHi()
                                           + "]");
            }
        }
}

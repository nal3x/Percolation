/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Test {
    public static void main (String[] args) { //tester, optional
        int size = 3;
        Percolation percolation = new Percolation(size);
        // for (int row = 1; row <= size; row++) {
        //     for (int col = 1; col <= size; col++) {
        //         System.out.println("Element (" + row + "," + col + ") maps to "
        //                                    + String.valueOf(siteIndex(row, col)));
        //     }
        // }
        percolation.open(1,1);
        percolation.open(2,1);
        percolation.open(3,1);
        boolean connected = percolation.isFull(2,1);
        System.out.println(connected);
        boolean percolates = percolation.percolates();
        System.out.println(percolates);

    }
}

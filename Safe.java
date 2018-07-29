package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Holds state for given safe
 *
 * @author Michael Jones
 * @author John Hsiao
 */

public class Safe {
    public int row, col;  // row is amount of rows, col is amount of cols
    public char[][] grid;  // 2D char array of the grid


    /**
     * constructor to build safe from file
     * @param filename - safe file
     * @throws FileNotFoundException
     */
    public Safe(String filename) throws FileNotFoundException {
        Scanner keyboard = new Scanner(new File(filename));
        this.row = keyboard.nextInt();
        this.col = keyboard.nextInt();
        this.grid = new char[this.row][this.col];
        for (int r = 0; r < this.row; r++)
        {
            for (int c = 0; c < col; c++)
            {
                char a = keyboard.next().charAt(0);
                this.grid[r][c] = a;
            }
        }
        keyboard.close();
    }

    /**
     * constructor to build safe out of other safe
     * @param o - other safe
     */
    public Safe(Safe o){
        this.grid = new char[o.row][o.col];
        this.row = o.row;
        this.col = o.col;
        for (int i = 0; i < o.row; i++) {
            System.arraycopy(o.grid[i],0,this.grid[i],0,o.col);
        }
    }
}

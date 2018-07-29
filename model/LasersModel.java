package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

/**
 * Model for Laser game simulation
 *
 * @author Michael Jones
 * @author John Hsiao
 */
public class LasersModel extends Observable {

    private String message; // message to user
    private ArrayList<Integer> errorAt = new ArrayList<>(); // coordinates of error
    private Safe safe;  // internal safe

    /**
     * constructor for file input
     * @param filename - string file name
     * @throws FileNotFoundException
     */
    public LasersModel(String filename) throws FileNotFoundException {
        this.safe = new Safe(filename);
        setMessage(filename + " loaded.");
        announceChange();
    }

    /**
     * constructor to copy a model
     * @param o - other model
     */
    public LasersModel(LasersModel o){
        this.safe = new Safe(o.safe);
        this.message = o.message;
    }

    /**
     * removes laser from (r,c)
     * @param r - row
     * @param c - col
     */
    public void remove(int r, int c)
    {
        if ((r < 0) || (c < 0) || (r >= safe.row) || (c >= safe.col) || (safe.grid[r][c] != 'L'))
        {
            setMessage("Error removing laser at: (" + r + ", " + c + ")");
        }
        else
        {
            if(!laser(r, c))
            {
                safe.grid[r][c] = '.';
            }
            else
            {
                if(laserH(r,c)){
                    if(laserV(r,c)){
                        safe.grid[r][c] = 'c';
                    }
                    else {
                        safe.grid[r][c] = 'h';
                    }
                }
                else{
                    safe.grid[r][c] = 'v';
                }

            }
            int currR = r;
            int currC = c;

            // removing beams up
            while(currR > 0)
            {
                currR--;
                if((safe.grid[currR][c] == 'h') || (safe.grid[currR][c] == 'v') || (safe.grid[currR][c] == 'c'))
                {
                    if (laser(currR, c))
                    {
                        if(laserH(currR,c)){
                            if(laserV(currR,c)){
                                safe.grid[currR][c] = 'c';
                            }
                            else {
                                safe.grid[currR][c] = 'h';
                            }
                        }
                        else{
                            safe.grid[currR][c] = 'v';
                        }
                    }
                    else
                    {
                        safe.grid[currR][c] = '.';
                    }
                }
                else
                {
                    break;
                }
            }
            currR = r;
            // removing beams down
            while(currR < safe.row-1){
                currR++;
                if((safe.grid[currR][c] == 'h') || (safe.grid[currR][c] == 'v') || (safe.grid[currR][c] == 'c'))
                {
                    if (laser(currR, c))
                    {
                        if (laserH(currR, c)) {
                            if (laserV(currR, c)) {
                                safe.grid[currR][c] = 'c';
                            }
                            else {
                                safe.grid[currR][c] = 'h';
                            }
                        }
                        else {
                            safe.grid[currR][c] = 'v';
                        }
                    }
                    else {
                        safe.grid[currR][c] = '.';
                    }
                }
                else
                {
                    break;
                }
            }
            // lasers going left
            while(currC > 0){
                currC--;
                if((safe.grid[r][currC] == 'h') || (safe.grid[r][currC] == 'v') || (safe.grid[r][currC] == 'c'))
                {
                    if (laser(r, currC))
                    {
                        if(laserH(r,c)){
                            if(laserV(r,c)){
                                safe.grid[r][currC] = 'c';
                            }
                            else {
                                safe.grid[r][currC] = 'h';
                            }
                        }
                        else{
                            safe.grid[r][currC] = 'v';
                        }
                    }
                    else
                    {
                        safe.grid[r][currC] = '.';
                    }
                }
                else
                {
                    break;
                }
            }
            currC = c;
            // lasers going right
            while(currC < safe.col-1){
                currC++;
                if((safe.grid[r][currC] == 'h') || (safe.grid[r][currC] == 'v') || (safe.grid[r][currC] == 'c'))
                {
                    if (laser(r, currC))
                    {
                        if(laserH(r,c)){
                            if(laserV(r,c)){
                                safe.grid[r][currC] = 'c';
                            }
                            else {
                                safe.grid[r][currC] = 'h';
                            }
                        }
                        else{
                            safe.grid[r][currC] = 'v';
                        }
                    }
                    else
                    {
                        safe.grid[r][currC] = '.';
                    }
                }
                else
                {
                    break;
                }
            }
            setMessage("Laser removed at: (" + r + ", " + c + ")");
        }
        announceChange();
    }

    /**
     * checks to see if there is a laser pointing at another
     * @param r - row
     * @param c - col
     * @return - true if laser pointed at (r,c)
     */
    public boolean laser(int r, int c)
    {
        int currR = r;
        int currC = c;
        // looking up
        while(currR > 0){
            currR--;
            if(safe.grid[currR][c] == 'L')
            {
                return true;
            }
            else if(Character.isLowerCase(safe.grid[currR][c])){
                continue;
            }
            else{break;}
        }
        currR = r;
        // looking down
        while(currR < safe.row-1){
            currR++;
            if(safe.grid[currR][c] == 'L')
            {
                return true;
            }
            else if(Character.isLowerCase(safe.grid[currR][c])){
                continue;
            }
            else{break;}
        }
        // looking left
        while(currC > 0){
            currC--;
            if(safe.grid[r][currC] == 'L')
            {
                return true;
            }
            else if(Character.isLowerCase(safe.grid[r][currC])){
                continue;
            }
            else{break;}
        }
        currC = c;
        // looking right
        while(currC < safe.col-1){
            currC++;
            if(safe.grid[r][currC] == 'L')
            {
                return true;
            }
            else if(Character.isLowerCase(safe.grid[r][currC])){
                continue;
            }
            else{break;}
        }
        return false;
    }

    /**
     * checks if a laser is horizontal to this point
     * @param r - row
     * @param c - col
     * @return - true if laser pointed at (r,c) horizontally
     */
    public boolean laserH(int r,int c){
        int currC = c;
        // looking left
        while(currC > 0){
            currC--;
            if(safe.grid[r][currC] == 'L')
            {
                return true;
            }
            else if(Character.isLowerCase(safe.grid[r][currC])){
                continue;
            }
            else{break;}
        }
        currC = c;
        // looking right
        while(currC < safe.col-1){
            currC++;
            if(safe.grid[r][currC] == 'L')
            {
                return true;
            }
            else if(Character.isLowerCase(safe.grid[r][currC])){
                continue;
            }
            else{break;}
        }
        return false;
    }

    /**
     * checks if a laser is vertical to this point
     * @param r - row
     * @param c - col
     * @return - true if laser pointed at (r,c) vertically
     */
    public boolean laserV(int r,int c){
        int currR = r;
        // looking up
        while(currR > 0){
            currR--;
            if(safe.grid[currR][c] == 'L')
            {
                return true;
            }
            else if(Character.isLowerCase(safe.grid[currR][c])){
                continue;
            }
            else{break;}
        }
        currR = r;
        // looking down
        while(currR < safe.row-1){
            currR++;
            if(safe.grid[currR][c] == 'L')
            {
                return true;
            }
            else if(Character.isLowerCase(safe.grid[currR][c])){
                continue;
            }
            else{break;}
        }
        return false;
    }

    /**
     * adds a laser to (r,c)
     * @param r - row
     * @param c - col
     */
    public void add(int r, int c)
    {
        if ((r < 0) || (c < 0) || (r >= safe.row) || (c >= safe.col) || ((safe.grid[r][c] != '.') && (safe.grid[r][c] != 'h') && (safe.grid[r][c] != 'v') && (safe.grid[r][c] != 'c')))
        {
            setMessage("Error adding laser at: (" + r + ", " + c + ")");
        }
        else
        {
            safe.grid[r][c] = 'L';
            int currR = r;
            int currC = c;
            // lasers going up
            while(currR > 0){
                currR--;
                if((safe.grid[currR][c] == '.') || (safe.grid[currR][c] == 'v'))
                {
                    safe.grid[currR][c] = 'v';
                }
                else if(safe.grid[currR][c] == 'h'){
                    safe.grid[currR][c] = 'c';
                }
                else
                {
                    break;
                }
            }
            currR = r;
            // lasers going down
            while(currR < safe.row-1){
                currR++;
                if((safe.grid[currR][c] == '.') || (safe.grid[currR][c] == 'v'))
                {
                    safe.grid[currR][c] = 'v';
                }
                else if(safe.grid[currR][c] == 'h'){
                    safe.grid[currR][c] = 'c';
                }
                else
                {
                    break;
                }
            }
            // lasers going left
            while(currC > 0){
                currC--;
                if((safe.grid[r][currC] == '.') || (safe.grid[r][currC] == 'h'))
                {
                    safe.grid[r][currC] = 'h';
                }
                else if(safe.grid[r][currC] == 'v'){
                    safe.grid[r][currC] = 'c';
                }
                else
                {
                    break;
                }
            }
            currC = c;
            // lasers going right
            while(currC < safe.col-1){
                currC++;
                if((safe.grid[r][currC] == '.') || (safe.grid[r][currC] == 'h'))
                {
                    safe.grid[r][currC] = 'h';
                }
                else if(safe.grid[r][currC] == 'v'){
                    safe.grid[r][currC] = 'c';
                }
                else
                {
                    break;
                }
            }
            setMessage("Laser added at: (" + r + ", " + c + ")");
        }
        announceChange();
    }

    /**
     * quits program
     */
    public static void quit()
    {
        System.exit(0);
    }

    /**
     * getters and a setter
     * self-explanatory
     */
    public char[][] getGrid(){return safe.grid;}
    public int getRow(){return safe.row;}
    public int getCol(){return safe.col;}
    public String getMessage(){return message;}
    public void setMessage(String message){
        this.message = message;
        announceChange();
    }
    public ArrayList getError(){return errorAt;}
    public void removeError(){errorAt.clear();}
    public char getVal(int r, int c){
        return safe.grid[r][c];
    }

    /**
     * tells whether the current state is a solution
     * @return - true if soloution
     */
    public boolean verify()
    {
        errorAt = new ArrayList<>();
        for (int r = 0; r < safe.row; r++)
        {
            for (int c = 0; c < safe.col; c++)
            {
                if (safe.grid[r][c] == '.')
                {
                    setMessage("Error verifying at: (" + r + ", " + c + ")");
                    errorAt.add(r);
                    errorAt.add(c);
                    announceChange();
                    return false;
                }

                else if (safe.grid[r][c] == 'L')
                {
                    if (this.laser(r,c))
                    {
                        setMessage("Error verifying at: (" + r + ", " + c + ")");
                        errorAt.add(r);
                        errorAt.add(c);
                        announceChange();
                        return false;
                    }
                }
                else
                {
                    if (Character.isDigit(safe.grid[r][c]))
                    {
                        int i = Character.digit(safe.grid[r][c], 10);
                        int tar = 0;
                        if (c > 0 && safe.grid[r][c-1] == 'L')
                        {
                            tar++;
                        }
                        if (c < safe.col-1 && safe.grid[r][c+1] == 'L')
                        {
                            tar++;
                        }
                        if (r > 0 &&safe.grid[r-1][c] == 'L')
                        {
                            tar++;
                        }
                        if (r < safe.row - 1 && safe.grid[r+1][c] == 'L')
                        {
                            tar++;
                        }
                        if (tar != i)
                        {
                            setMessage("Error verifying at: (" + r + ", " + c + ")");
                            errorAt.add(r);
                            errorAt.add(c);
                            announceChange();
                            return false;
                        }
                    }
                }
            }
        }
        setMessage("Safe is fully verified!");
        return true;
    }


    /**
     * A utility method that indicates the model has changed and
     * notifies observers
     */
    public void announceChange() {
        setChanged();
        notifyObservers();
    }
}

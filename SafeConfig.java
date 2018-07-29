package backtracking;

import model.LasersModel;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The class represents a single configuration of a safe.  It is
 * used by the backtracker to generate successors, check for
 * validity, and eventually find the goal.
 *
 * This class is given to you here, but it will undoubtedly need to
 * communicate with the model.  You are free to move it into the model
 * package and/or incorporate it into another class.
 *
 * @author Sean Strout @ RIT CS
 * @author Michael Jones
 * @author John Hsiao
 */
public class SafeConfig implements Configuration {

    private LasersModel model;
    private int currRow;
    private int currCol;

    /**
     * constructor for making a config from a file
     * @param filename - safe file
     * @throws FileNotFoundException
     */
    public SafeConfig(String filename) throws FileNotFoundException{
        model = new LasersModel(filename);
        currRow = 0;
        currCol = -1;
    }

    /**
     * constructor for making a config from another config
     * @param o - other config
     */
    public SafeConfig(SafeConfig o){
        this.currRow = o.currRow;
        this.currCol = o.currCol;
        this.model = new LasersModel(o.model);
    }

    /**
     * constructor for building a config around a model
     * @param model - lasermodel
     */
    public SafeConfig(LasersModel model){
        this.model = new LasersModel(model);
        currRow = 0;
        currCol = -1;
    }

    @Override
    public Collection<Configuration> getSuccessors()
    {
        currCol++;
        if (currCol == model.getCol())
        {
            currRow++;
            currCol = 0;
        }

        if(currRow == model.getRow())
        {
            return new ArrayList<>();
        }

        while((this.model.getVal(currRow, currCol) != '.'))
        {
            currCol++;
            if (currCol == model.getCol()) {
                currRow++;
                currCol = 0;
            }
            if(currRow == model.getRow()){return new ArrayList<>();}
        }
        SafeConfig s1 = new SafeConfig(this);
        s1.model.add(currRow,currCol);
        SafeConfig s2 = new SafeConfig(this);
        Collection<Configuration> successors = new ArrayList<>();
        successors.add(s1);
        successors.add(s2);
        return successors;
    }

    @Override
    public boolean isValid()
    {
        if(model.getVal(currRow,currCol) == 'L') {

            if (currCol != 0) {
                if (Character.isDigit(model.getVal(currRow, currCol - 1))) {
                    if(!pillarValid(currRow,currCol-1)){return false;}
                }
            }
            if (currCol != model.getCol()-1) {
                if (Character.isDigit(model.getVal(currRow, currCol+1))) {
                    if(!pillarValid(currRow,currCol+1)){return false;}
                }
            }
            if (currRow != 0) {
                if (Character.isDigit(model.getVal(currRow-1, currCol))) {
                    if(!pillarValid(currRow-1,currCol)){return false;}
                }
            }
            if (currRow != model.getRow()-1) {
                if (Character.isDigit(model.getVal(currRow+1, currCol))) {
                    if(!pillarValid(currRow+1,currCol)){return false;}
                }
            }
        }
        return true;
    }

    @Override
    public boolean isGoal() {
        for (int i = 0; i < model.getRow(); i++) {
            for (int j = 0; j < model.getCol(); j++) {
                // checking lasers around pillars
                if(Character.isDigit(model.getVal(i,j))){
                    int num = Character.getNumericValue(model.getVal(i,j));
                    int lasers = 0;
                    if(j != 0){
                        if(model.getVal(i,j-1) == 'L'){lasers++;}
                    }
                    if(j != model.getCol()-1){
                        if(model.getVal(i,j+1) == 'L'){lasers++;}
                    }
                    if(i != 0){
                        if(model.getVal(i-1,j) == 'L'){lasers++;}
                    }
                    if(i != model.getRow()-1){
                        if(model.getVal(i+1,j) == 'L'){lasers++;}
                    }
                    if (num != lasers){return false;}
                }

                // checking for empty spaces
                if(model.getVal(i,j) == '.'){return false;}
            }
        }
        return true;
    }

    /**
     * gets model of the config
     * @return - model
     */
    public LasersModel getModel(){
        return model;
    }

    /**
     * finds the new laser to be placed in hint
     * @param config1 - old config
     * @param config2 - new config
     * @return - coordinates of new laser
     */
    public static int[] findNewLaser(SafeConfig config1, SafeConfig config2){
        int[] coords = new int[2];
        for (int i = 0; i < config1.model.getRow(); i++) {
            for (int j = 0; j < config1.model.getCol(); j++) {
                if(config1.model.getVal(i,j) != 'L'){
                    if(config2.model.getVal(i,j) == 'L'){
                        coords[0] = i;
                        coords[1] = j;
                    }
                }
            }
        }
        return coords;
    }

    /**
     * checks pillars around a laser
     * @param currRow
     * @param currCol
     * @return
     */
    private boolean pillarValid(int currRow, int currCol) {
        int num = Character.getNumericValue(model.getVal(currRow, currCol));
        int lasers = 0;
        int beams = 0;
        if (currCol != 0) {
            if (model.getVal(currRow, currCol - 1) == 'L') {
                lasers++;
            }
            if (Character.isLowerCase(model.getVal(currRow, currCol - 1))) {
                beams++;
            }
        }
        if (currCol != model.getCol() - 1) {
            if (model.getVal(currRow, currCol + 1) == 'L') {
                lasers++;
            }
            if (Character.isLowerCase(model.getVal(currRow, currCol + 1))) {
                beams++;
            }
        }
        if (currRow != 0) {
            if (model.getVal(currRow - 1, currCol) == 'L') {
                lasers++;
            }
            if (Character.isLowerCase(model.getVal(currRow - 1, currCol))) {
                beams++;
            }
        }
        if (currRow != model.getRow() - 1) {
            if (model.getVal(currRow + 1, currCol) == 'L') {
                lasers++;
            }
            if (Character.isLowerCase(model.getVal(currRow + 1, currCol))) {
                beams++;
            }
        }
        if (num < lasers) {
            return false;
        }
        if (num+beams > 4  ) {
            return false;
        }
        return true;
    }
}

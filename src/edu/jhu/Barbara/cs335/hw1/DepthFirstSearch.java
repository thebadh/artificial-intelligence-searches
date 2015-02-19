package edu.jhu.Barbara.cs335.hw1;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.Stack;

/**
 * Created by Barbara on 2/10/2015.
 */
public class DepthFirstSearch {
    public Stack<Entry> stax = new Stack<Entry>();
    private DataReader.Compass nav;
    private Entry start;
    private boolean reachedGoal = false;
    private String path;
    private StringBuilder printout = new StringBuilder();
    private int printMargin = 1;
    private int totalCost = 0;
    private int nodesExpanded = -1;
    private String newList;

    private class Entry {
        private String value;
        private Entry parent;
        private DataReader.Coordinate coordinate;
        private int posX;
        private int posY;
        private int level;
        boolean leftSearched = false; boolean rightSearched = false;
        boolean upSearched = false; boolean downSearched = false;
        Entry (Integer x, Integer y, Integer i, Entry prev) {
            this.parent = prev;
            this.posX = x;
            this.posY = y;
            this.level = i;
            DataReader.Coordinate data = nav.getCurrent(x, y);
            this.value = data.data;
            this.coordinate = data;
        }
    }

    public DepthFirstSearch(DataReader.Compass compass) {
        this.nav = compass;
    }

    public Entry directStack(Entry e) {
        if (!reachedGoal) {
            if (!e.leftSearched) {
                Entry left = new Entry(e.posX - 1, e.posY, e.level + 1, e);
                left.rightSearched = true;
                nodesExpanded++;
                if (left.value.toLowerCase().equals("g")) {
                    this.reachedGoal = true;
                    System.out.println(printPath(left));
                    return e;
                } else if (!left.value.equals("#")) {
                    this.stax.push(left);
                    e.leftSearched = true;
                    return directStack(left);
                }
            }
            if (!e.rightSearched) {
                Entry right = new Entry(e.posX + 1, e.posY, e.level + 1, e);
                right.leftSearched = true;
                nodesExpanded++;
                if (right.value.toLowerCase().equals("g")) {
                    this.reachedGoal = true;
                    System.out.println(printPath(right));
                    return e;
                } else if (!right.value.equals("#")) {
                    this.stax.push(right);
                    e.rightSearched = true;
                    return directStack(right);
                }
            }
            if (!e.upSearched) {
                Entry up = new Entry(e.posX, e.posY - 1, e.level + 1, e);
                up.downSearched = true;
                nodesExpanded++;
                if (up.value.toLowerCase().equals("g")) {
                    this.reachedGoal = true;
                    System.out.println(printPath(up));
                    return e;
                } else if (!up.value.equals("#")) {
                    this.stax.push(up);
                    e.upSearched = true;
                    return directStack(up);
                }
            }
            if (!e.downSearched) {
                Entry down = new Entry(e.posX, e.posY + 1, e.level + 1, e);
                down.upSearched = true;
                nodesExpanded++;
                if (down.value.toLowerCase().equals("g")) {
                    this.reachedGoal = true;
                    System.out.println(printPath(down));
                    return e;
                } else if (!down.value.equals("#")) {
                    this.stax.push(down);
                    e.downSearched = true;
                    return directStack(down);
                }
            }
        }
        return e;
    }

    private String printPath(Entry e) {
        //Calculate the total cost:
        if (e.value.equals(".")) {
            totalCost++;
        } else if (e.value.equals(",")) {
            totalCost += 2;
        }

        //Build the string detailing the path to the goal:
        this.printout.append(" >- ]" + e.posY + " ," + e.posX +"[ '" + e.value + "'");
        if (e.parent != start) {
            return printPath(e.parent);
        }
        this.printout.append(" >- ]" + e.parent.posY + " ," + e.parent.posX +"[ '" + e.parent.value + "'");
        this.printout.delete(0, 4);
        this.printout.reverse();
        totalCost++; //add one more cost value for the step into the goal

        //Format the string to conform to a "margin":
        if (this.printout.length() > 70) {
            printMargin = 1;
            while (this.printout.length() > 70*printMargin + (printMargin - 1)) {
                this.printout.insert(70*printMargin + (printMargin - 1), "\n");
                printMargin++;
            }
        }

        this.printout.append("\nTotal Cost of Path: " + totalCost + "\nNumber of Nodes Expanded During Search: "
                + nodesExpanded + "\n");
        this.path = printout.toString();
        return path;
    }

    public void findGoal() {
        this.start = new Entry(this.nav.startX, this.nav.startY, 0, null);
        directStack(this.start);
    }
}

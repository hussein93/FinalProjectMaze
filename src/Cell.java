import java.util.*;


// To represent a single Cell in a Maze
public class Cell {
    int x;
    int y;

    // The parent of this Cell when using Depth algorithm
    Cell parent;

    // Representing the walls
    // Once Maze is set up (in constructor), 
    // the Cells are pointing to themselves since walls are everywhere
    Cell above; 
    Cell below;
    Cell left;
    Cell right;

    // has this node already been visited?
    boolean visited;

    // Did the node come back (find a dead-end?)
    boolean backsies;

    // Is the maze at this Cell at the moment?
    boolean theOne;

    // Neighbors of this Cell
    ArrayList<Cell> neighbors;

    // Constructor for initializing a Cell 
    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.parent = this;
        this.visited = false;
        this.backsies = true;
        this.theOne = false;
        this.above = this;
        this.below = this;
        this.left = this;
        this.right = this;
        this.neighbors = new ArrayList<Cell>();
    }

    // Check if two Cells are the same
    public boolean equals(Object o) {
        if (o instanceof Cell) {
            return this.hashCode() == o.hashCode();
        }
        else {
            return false;
        }
    }

    // Returns a unique hashCode for this particular Cell
    public int hashCode() {
        return (this.x * (3 * this.y) + x);
    }

    // Perform onTick using the Depth-First Search Algorithm
    public Cell onTickDepth() {
        this.visited = true;

        for (int i = 0; i < this.neighbors.size(); i += 1) {
            if (!this.neighbors.get(i).visited) {
                this.backsies = false;
                Cell neigC = this.neighbors.get(i);
                neigC.theOne = true;
                neigC.parent = this;
                this.theOne = false;
                return neigC;
            }
        }

        this.backsies = true;
        this.theOne = false;
        if (this.parent.equals(this)) {
            return this;

        }
        else {
            return this.parent.onTickDepth();
        }
    }

    // Add the neighbors of this Cell
    public void addNeighbors() {
        if (!this.above.equals(this)) {
            this.neighbors.add(this.above);
        }
        if (!this.below.equals(this)) {
            this.neighbors.add(this.below);
        }
        if (!this.right.equals(this)) {
            this.neighbors.add(this.right);
        }
        if (!this.left.equals(this)) {
            this.neighbors.add(this.left);
        }
    }
}



















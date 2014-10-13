
import java.util.ArrayList;
import java.util.HashMap;

import javalib.funworld.World;

// To represent the class MazeGame
public class MazeGame extends World {
    // To represent the grid of the Maze using a HashMap
    HashMap<CartPt, Cell> hashGrid;
    int sizeX;
    int sizeY;

    // The current Cell going through Breadth algorithm
    Cell current;

    // Should the maze start?
    String gameState;

    // Levels to check for Breadth Search
    ArrayList<Cell> firstLevel;
    ArrayList<Cell> neighborLevel;

    // Constructor for the class MazeGame
    MazeGame(int x, int y) {
        this.sizeX = x;
        this.sizeY = y;
        this.current = new Cell(1, 1);
        this.gameState = "menu";
        this.hashGrid = new HashMap<CartPt, Cell>();
        this.firstLevel = new ArrayList<Cell>();
        this.neighborLevel = new ArrayList<Cell>();
        this.begin();
    }

    // Main method for the Maze
    public static void main(String[] argv) {
        MazeGame mg = new MazeGame(3, 3);
        // Ask user for dimensions
        mg.begin();
    }

    // Initializes the MazeGame by forming the Maze and Edges
    public void begin() {
        this.initGraph();
        this.initEdges();
    }

    // Initialize the graph
    public void initGraph() {
        for (int x = 1; x <= this.sizeX; x++) {
            for (int y = 1; y <= this.sizeY; y++) {
                Cell one = new Cell(x, y);
                CartPt p = new CartPt(x, y);
                this.hashGrid.put(p, one);
            }
        }

        this.current.theOne = true;
        this.current = this.hashGrid.get(new CartPt(1, 1));
    }

    // Initialize the edges of the Graph
    public void initEdges() {
        ArrayList<Wall> edges = new ArrayList<Wall>();

        for (int y = 1; y <= this.sizeY; y++) {
            for (int x = 1; x <= this.sizeX; x++) {
                if (x == sizeX) {
                    CartPt p = new CartPt(x, y);
                    CartPt p1 = new CartPt(x, y - 1);

                    if (!this.outOfBounds(p1)) {
                        Wall t = new Wall(this.hashGrid.get(p1),
                                this.hashGrid.get(p));
                        edges.add(t);
                    }
                }
                else {
                    CartPt p = new CartPt(x, y);
                    CartPt p1 = new CartPt(x + 1, y);
                    CartPt p2 = new CartPt(x, y - 1);

                    if (!this.outOfBounds(p1)) {
                        Wall t = new Wall(this.hashGrid.get(p), 
                                this.hashGrid.get(p1));
                        edges.add(t);
                    }
                    if (!this.outOfBounds(p2)) {
                        Wall t = new Wall(this.hashGrid.get(p), 
                                this.hashGrid.get(p2));
                        edges.add(t);
                    }
                }
            }
        }

        edges = edges.get(0).sortByVal(edges);

        CartPt p = new CartPt(1, 1);
        this.kruskal(edges);
        this.firstLevel.add(this.hashGrid.get(p));
    }

    // Kruskal's Algorithm to randomly generate the Maze without any cycles
    public void kruskal(ArrayList<Wall> walls) {
        while (!walls.isEmpty()) {
            Wall w = walls.get(0);
            if (!this.connection(w.one, w.two, new Cell(-100, -100))) {
                this.change(w.one, w.two);
                walls.remove(0);
            }
            else {
                walls.remove(0);
            }
        }

        for (int x = 1; x <= this.sizeX; x++) {
            for (int y = 1; y <= this.sizeY; y++) {
                CartPt p = new CartPt(x, y);
                this.hashGrid.get(p).addNeighbors();
            }
        }
    }

    // Modifies the connections between Cells
    public void change(Cell c, Cell other) {
        CartPt p2 = new CartPt(c.x + 1, c.y);
        CartPt p3 = new CartPt(c.x, c.y + 1);
        CartPt p4 = new CartPt(c.x - 1, c.y);
        CartPt p5 = new CartPt(c.x, c.y - 1);

        if (!this.outOfBounds(p2)) {
            if (this.hashGrid.get(p2).equals(other)) {
                c.right = other;
                other.left = c;
            }
        }
        if (!this.outOfBounds(p3)) {
            if (this.hashGrid.get(p3).equals(other)) {
                c.below = other;
                other.above = c;
            }
        }
        if (!this.outOfBounds(p4)) {
            if (this.hashGrid.get(p4).equals(other)) {
                c.left = other;
                other.right = c;
            }
        }
        if (!this.outOfBounds(p5)) {
            if (this.hashGrid.get(p5).equals(other)) {
                c.above = other;
                other.below = c;
            }
        }
    }

    // Are the given Cells connected? Can Cell two reach Cell one.
    public boolean connection(Cell one, Cell two, Cell recur) {
        if (one.above.equals(two) || one.below.equals(two) || 
                one.right.equals(two) || one.left.equals(two)) {
            return true;
        }
        if (!one.above.equals(one) && !one.above.equals(recur)) {
            if (this.connection(one.above, two, one)) {
                return true;
            }
        }
        if (!one.below.equals(one) && !one.below.equals(recur)) {
            if (this.connection(one.below, two, one)) {
                return true;
            }
        }
        if (!one.right.equals(one) && !one.right.equals(recur)) {
            if (this.connection(one.right, two, one)) {
                return true;
            }
        }
        if (!one.left.equals(one) && !one.left.equals(recur)) {
            if (this.connection(one.left, two, one)) {
                return true;
            }
        }
        return false;
    }

    // Is the given CartPt within the Maze dimensions?
    public boolean outOfBounds(CartPt p) {
        return (p.x < 1 || p.x > this.sizeX || p.y < 1 || p.y > this.sizeY);
    }


    // Runs the game per Tick depending on which state that the game is in
    public MazeGame onTick() {
        if (this.gameState.equals("breadth")) {
            this.onTickBreadth(this.firstLevel);
        }
        else if (this.gameState.equals("depth")) {
            this.current = this.current.onTickDepth();
        }
        return this;

    }

    // Begins the maze according to the key event
    // Or if key is pressed during the maze
    public MazeGame onKeyEvent(String key) {
        // Generate a new random Maze
        if (key.equals("r")) {
            return new MazeGame(this.sizeX, this.sizeY);
        }
        if (key.equals("s")) {
            this.gameState = "stop";
            return this;
        }
        // Go through maze manually
        // Must be before the if where it makes the gameState to  "non"
        if (this.gameState == "non") {
            this.solo(key);
        }
        // Solve maze manually
        if (key.equals(" ")) {
            this.gameState = "non";
        }
        // Solve Maze using the Breadth algorithm
        if (key.equals("0")) {
            this.gameState = "breadth";
        }
        // Solve Maze using the Depth algorithm
        if (key.equals("1")) {
            this.gameState = "depth";
        }
        return this;
    }

    // Go solo (manual) through the maze
    public MazeGame solo(String ke) {
        if (ke.equals("up") || ke.equals("down") ||
                ke.equals("left") || ke.equals("right")) {

            // The current Cell the Maze is at
            Cell c = this.current;

            // End Cell
            CartPt end = new CartPt(this.sizeX, this.sizeY);
            Cell lastCell = this.hashGrid.get(end);

            if (lastCell.theOne) {
                this.gameState = "";
                return this;
            }
            if (ke.equals("up")) {
                if (!c.above.equals(c)) {
                    CartPt newP = new CartPt(this.current.x, 
                            this.current.y - 1);
                    Cell newC = this.hashGrid.get(newP);
                    newC.theOne = true;
                    newC.visited = false;
                    this.current = newC;
                    c.visited = true;
                    c.theOne = false;
                    return this;
                }
            }
            if (ke.equals("down")) {
                if (!c.below.equals(c)) {
                    CartPt newP = new CartPt(this.current.x,
                            this.current.y + 1);
                    Cell newC = this.hashGrid.get(newP);
                    newC.theOne = true;
                    newC.visited = false;
                    this.current = newC;
                    c.visited = true;
                    c.theOne = false;       
                    return this;
                }
            }
            if (ke.equals("left")) {
                if (!c.left.equals(c)) {
                    CartPt newP = new CartPt(this.current.x - 1,
                            this.current.y);
                    Cell newC = this.hashGrid.get(newP);
                    newC.theOne = true;
                    newC.visited = false;
                    this.current = newC;                  
                    c.visited = true;
                    c.theOne = false;
                    return this;
                }
            }
            if (ke.equals("right")) {
                if (!c.right.equals(c)) {
                    CartPt newP = new CartPt(this.current.x + 1,
                            this.current.y);
                    Cell newC = this.hashGrid.get(newP);
                    newC.theOne = true;
                    newC.visited = false;
                    this.current = newC;
                    c.visited = true;
                    c.theOne = false;
                    return this;
                }
            }
        }

        return this;
    }

    // Breadth-First Searching Algorithm
    public void onTickBreadth(ArrayList<Cell> alist) {
        CartPt end = new CartPt(this.sizeX, this.sizeY);
        Cell lastCell = this.hashGrid.get(end);

        while (!alist.isEmpty()) {
            Cell c = alist.get(0);
            for (Cell b: c.neighbors) {
                if (!b.visited) {
                    b.parent = c;
                    b.theOne = true;
                    this.neighborLevel.add(b);
                }
            }
            if (c.equals(lastCell)) {
                c.backsies = true;
                this.firstLevel = new ArrayList<Cell>();
                this.neighborLevel = new ArrayList<Cell>();
                break;
            }
            c.visited = true;
            c.theOne = false;
            alist.remove(0);
        }

        this.firstLevel = this.neighborLevel;
        this.neighborLevel = new ArrayList<Cell>();

    }

    // Draw the current State of the Game
    public WorldImage makeImage() {
        WorldImage img = new OverlayImages(new FrameImage(new Posn(1, 1),
                this.sizeX * 300, this.sizeY * 300, new Black()),
                new RectangleImage(new Posn(1, 1), 100000, 100000, 
                        new Black()));
        if (this.gameState.equals("menu")) {
            img = new OverlayImages(img, 
                    new TextImage(new Posn(this.sizeX * 25, this.sizeY * 15),
                            "Tap '1' to Watch the Depth-First", 
                            this.sizeX * 2, this.sizeY, new White()));
            img = new OverlayImages(img, 
                    new TextImage(new Posn(this.sizeX * 25, this.sizeY * 10),
                            "Tap '0' to Watch the Breadth-First", 
                            this.sizeX * 2, this.sizeY, new White()));
            img = new OverlayImages(img, 
                    new TextImage(new Posn(this.sizeX * 25, this.sizeY * 35),
                            "Tap 'r' to Start Over with a New Maze", 
                            this.sizeX * 2, this.sizeY, new Red()));
            img = new OverlayImages(img,
                    new TextImage(new Posn(this.sizeX * 25, this.sizeY * 30),
                            "Tap 's' to Pause", 
                            this.sizeX * 2, this.sizeY, new Red()));
            img = new OverlayImages(img,
                    new TextImage(new Posn(this.sizeX * 25, this.sizeY * 20),
                            "Tap 'space' to Solve it Manually Using Arrows",
                            this.sizeX * 2, this.sizeY, new White()));
            return img;
        }
        else {
            int big = 50;
            int lil = 25;
            for (int x = 1; x <= this.sizeX; x += 1) {
                for (int y = 1; y <= this.sizeY; y += 1) {
                    // Temporary Cell to check what color to draw it
                    Cell cell = this.hashGrid.get(new CartPt(x, y));
                    Posn forEachCell = new Posn(x * big - lil, y * big - lil);
                    if (cell.theOne) {
                        img = new OverlayImages(img,
                                new DiskImage(forEachCell, 10, new Red()));
                    }
                    if (x == sizeX && y == sizeY) {
                        img = new OverlayImages(img,
                                new DiskImage(forEachCell, 20, new White()));
                    }
                    if (cell.visited) {
                        img = new OverlayImages(img,
                                new TextImage(forEachCell, "X", 30, 40, 
                                        new Yellow()));
                    }
                    if (x == 1 && y == 1) {
                        img = new OverlayImages(img,
                                new DiskImage(forEachCell, 20, new White()));
                    }
                    if (!cell.backsies) {
                        img = new OverlayImages(img,
                                new DiskImage(forEachCell, 20, new White()));
                    }
                    if (cell.above.equals(cell)) {
                        Posn left = new Posn(x * big - big, y * big - big);
                        Posn right = new Posn((x + 1) * big - big, 
                                y * big - big);

                        img = new OverlayImages(img,
                                new LineImage(left, right, new Green()));
                    }
                    if (cell.below.equals(cell)) {
                        Posn left = new Posn(x * big - big, y * big - 1);
                        Posn right = new Posn((x + 1) * big - big, 
                                y * big - 1);

                        img = new OverlayImages(img,
                                new LineImage(left, right, new Green()));
                    }
                    if (cell.left.equals(cell)) {
                        Posn top = new Posn(x * big - big, y * big - big);
                        Posn bottom = new Posn(x * big - big, 
                                (y + 1) * big - big);
                        img = new OverlayImages(img,
                                new LineImage(top, bottom, new Green()));
                    }
                    if (cell.right.equals(cell)) {
                        Posn top = new Posn(x * big - 1, y * big - big);
                        Posn bottom = new Posn(x * big - 1, 
                                (y + 1) * big - big);
                        img = new OverlayImages(img,
                                new LineImage(top, bottom, new Green()));
                    }
                }
            }
            if (this.hashGrid.get(new CartPt(this.sizeX, this.sizeY)).theOne) {
                this.current = this.hashGrid.get(new CartPt(this.sizeX, 
                        this.sizeY));

                while (!this.current.parent.equals(this.current)) {
                    this.current.backsies = false;
                    this.current = this.current.parent;
                }
                return new OverlayImages(new OverlayImages(img,
                        new TextImage(new Posn(this.sizeX * 25, 
                                this.sizeY * 20), 
                                "SOLVED!", this.sizeX * 4, this.sizeY, 
                                new Red())),
                                new TextImage(new Posn(this.sizeX * 25, 
                                        this.sizeY * 30), 
                                        "Press 'r' to return to Menu.", 
                                        this.sizeX * 4, 
                                        this.sizeY, new Red()));
            }
        }
        return img;
    }
}











import java.util.*;

import javalib.colors.Black;
import javalib.colors.Red;
import javalib.colors.White;
import javalib.worldimages.FrameImage;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;
import tester.*;

// Represents the example class
public class ExamplesFinalProject {

    // MazeGame
    MazeGame mg = new MazeGame(20, 15);

    // empty constructor
    public ExamplesFinalProject() {
        // empty
    }


    // Examples of Cells
    Cell c1 = new Cell(1, 1);
    Cell c2 = new Cell(1, 1);
    Cell c3 = new Cell(1, 2);

    // Other Cells
    Cell c4 = new Cell(2, 1);
    Cell c5 = new Cell(2, 2);

    //Examples of Walls
    Wall w1 = new Wall(c1, c3);
    Wall w2 = new Wall(c1, c4);
    Wall w3 = new Wall(c4, c5);
    Wall w4 = new Wall(c4, c1);

    //Initialize the Example Walls
    void initWall() {
        w1.value = 2;
        w2.value = 3;
        w3.value = 4;

    }

    //Example of ArrayLists to test for the sorting of the walls
    ArrayList<Wall> wlist = new ArrayList<Wall>(Arrays.asList(w1, w2, w3));
    ArrayList<Wall> wlistS = new ArrayList<Wall>(Arrays.asList(w3, w1, w2));


    // ArrayLists of neighbors for each example cell
    ArrayList<Cell> empty = new ArrayList<Cell>();
    ArrayList<Cell> c1s = new ArrayList<Cell>(Arrays.asList(c2, c3));
    ArrayList<Cell> c3s = new ArrayList<Cell>(Arrays.asList(c1, c4));
    ArrayList<Cell> c4s = new ArrayList<Cell>(Arrays.asList(c3, c5));
    ArrayList<Cell> c5s = new ArrayList<Cell>(Arrays.asList(c4, c3));

    // Sample drawing of the menu of the game
    WorldImage img = new OverlayImages(new FrameImage(new Posn(1, 1),
            2 * 300, 2 * 300, new Black()),
            new RectangleImage(new Posn(1, 1), 100000, 100000, 
                    new Black()));

    // Initializing the Image to test
    public void initImage() {

        img = new OverlayImages(img, 
                new TextImage(new Posn(2 * 25, 2 * 15),
                        "Tap '1' to Watch the Depth-First", 
                        2 * 2, 2, new White()));
        img = new OverlayImages(img, 
                new TextImage(new Posn(2 * 25, 2 * 10),
                        "Tap '0' to Watch the Breadth-First", 
                        2 * 2, 2, new White()));
        img = new OverlayImages(img, 
                new TextImage(new Posn(2 * 25, 2 * 35),
                        "Tap 'r' to Start Over with a New Maze", 
                        2 * 2, 2, new Red()));
        img = new OverlayImages(img,
                new TextImage(new Posn(2 * 25, 2 * 30),
                        "Tap 's' to Pause", 
                        2 * 2, 2, new Red()));
        img = new OverlayImages(img,
                new TextImage(new Posn(2 * 25, 2 * 20),
                        "Tap 'space' to Solve it Manually Using Arrows",
                        2 * 2, 2, new White()));
    }

    // Sample Maze
    MazeGame sampleMaze = new MazeGame(2, 2);

    // MazeGame
    MazeGame maze = new MazeGame(2, 2);
    MazeGame mazeS = new MazeGame(2, 2);
    MazeGame mazeN = new MazeGame(2, 2);
    MazeGame mazeB = new MazeGame(2, 2);
    MazeGame mazeD = new MazeGame(2, 2);

    // Initialize a simple 2x2 maze
    public void initMaze() {
        CartPt p1 = new CartPt(1, 1);
        CartPt p2 = new CartPt(1, 2);
        CartPt p3 = new CartPt(2, 1);
        CartPt p4 = new CartPt(2, 2);

        this.sampleMaze.hashGrid.put(p1, c1);
        this.sampleMaze.hashGrid.put(p2, c3);
        this.sampleMaze.hashGrid.put(p3, c4);
        this.sampleMaze.hashGrid.put(p4, c5);

        // Initialize the up down left right of the cells
        c1.left = c1;
        c1.right = c1;
        c1.above = c1;
        c1.below = c3;

        c3.left = c3;
        c3.right = c5;
        c3.above = c1;
        c3.below = c3;

        c4.left = c4;
        c4.right = c4;
        c4.above = c4;
        c4.below = c5;

        c5.left = c3;
        c5.right = c5;
        c5.above = c4;
        c5.below = c5;
    }

    // Initialize the Game States for Tests
    public void init() {
        this.mazeS.gameState = "stop";
        this.mazeN.gameState = "non";
        this.mazeB.gameState = "breadth";
        this.mazeD.gameState = "depth";
    }

    //Test the Sorting Method for the Walls
    void testSort(Tester t) {
        initWall();
        t.checkExpect(w1.sortByVal(wlistS).equals(wlist));
        t.checkExpect(w1.sortByVal(wlist).equals(wlist));
    }

    // Test the equals and hashCode methods of Cells
    public boolean testMaze(Tester t) {
        initMaze();
        initImage();
        return t.checkExpect(sampleMaze.makeImage(), img);    
    }

    // Test the MazeGame methods
    public boolean testKey(Tester t) {
        init();
        return t.checkExpect(maze.onKeyEvent("r").gameState, maze.gameState) &&
                t.checkExpect(maze.onKeyEvent("s").gameState, mazeS.gameState) 
                &&
                t.checkExpect(maze.onKeyEvent(" ").gameState, mazeN.gameState) 
                &&  
                t.checkExpect(maze.onKeyEvent("0").gameState, mazeB.gameState)
                && 
                t.checkExpect(maze.onKeyEvent("1").gameState, mazeD.gameState);
    }

    // Test the equals and hashCode methods of Cells
    public boolean testCells(Tester t) {
        initMaze();
        return t.checkExpect(c1.equals(c2), true) &&
                t.checkExpect(c1.equals(c3), false) && 
                t.checkExpect(c1.hashCode(), c2.hashCode()) &&
                t.checkExpect(c3.hashCode(), 7) &&
                t.checkExpect(c4.above.equals(c4), true) &&
                t.checkExpect(c4.left.equals(c4), true) &&
                t.checkExpect(c1.below.equals(c3), true) &&
                t.checkExpect(c1.left.equals(c1), true) &&
                t.checkExpect(c5.left.equals(c3), true);
    }

    //To Test the Depth-First Search Algorithm
    public boolean testDepth(Tester t) {
        return t.checkExpect(c1.parent.equals(c1), true) &&
                t.checkExpect(c2.parent.equals(c2), true) &&
                t.checkExpect(c3.parent.equals(c3), true) &&
                t.checkExpect(c1.visited, false) &&
                t.checkExpect(c2.visited, false) &&
                t.checkExpect(c3.visited, false) &&
                t.checkExpect(c4.visited, false) &&
                t.checkExpect(c1.onTickDepth().visited, true) &&
                t.checkExpect(c2.onTickDepth().visited, true) &&
                t.checkExpect(c3.onTickDepth().visited, true) &&
                t.checkExpect(c4.onTickDepth().visited, true) &&
                t.checkExpect(c1.onTickDepth().theOne, false) &&
                t.checkExpect(c2.onTickDepth().theOne, false) &&
                t.checkExpect(c3.onTickDepth().theOne, false) &&
                t.checkExpect(c4.onTickDepth().theOne, false) &&
                t.checkExpect(c1.onTickDepth().backsies, true) &&
                t.checkExpect(c2.onTickDepth().backsies, true) &&
                t.checkExpect(c3.onTickDepth().backsies, true) &&
                t.checkExpect(c4.onTickDepth().backsies, true);

    }

    //Test the On-Tick
    public boolean testOnTick(Tester t) {
        init();
        initMaze();

        return 
                t.checkExpect(mazeN.onTick().equals(mazeN)) &&
                t.checkExpect(mazeS.onTick().equals(mazeS)) &&
                t.checkExpect(mazeB.onTick().equals(mazeB)) &&
                t.checkExpect(mazeD.onTick().equals(mazeD));
    }

    //Tests the On-Key Event for Manual Searching
    public boolean testOnKey(Tester t) {
        initMaze();
        sampleMaze.solo("down");
        return t.checkExpect(sampleMaze.sizeX == 2) &&
                t.checkExpect(sampleMaze.sizeY == 2);
    }



    //Tests the Breadth-First Searching Algorithm
    public boolean testBreadth(Tester t) {
        init();
        initMaze();
        maze.onTickBreadth(c1s);
        mazeS.onTickBreadth(c5s);
        return t.checkExpect(maze.current.equals(c1)) &&
                t.checkExpect(maze.neighborLevel.equals(c1s)) &&
                t.checkExpect(maze.firstLevel.equals(c1s)) &&
                t.checkExpect(mazeS.firstLevel.equals(empty));
    }



    // to test the Maze Game
    void testMazeGame(Tester t) {
        mg.bigBang(mg.sizeX * 50, mg.sizeY * 50, 0.15);
    }
}

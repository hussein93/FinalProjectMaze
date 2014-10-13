import java.util.*;

// to represent Connections in the Maze
public class Wall {

    // The two Cells being connected
    Cell one;
    Cell two;

    // The value of the Wall between these two cells
    int value;

    // Constructor for initializing the Wall class at beginning
    // Points to itself (the root)
    Wall(Cell one, Cell two) {
        Random r = new Random();
        this.one = one;
        this.two = two;
        this.value = r.nextInt(100);
    }

    // insertion sort method that will sort the Walls by their value
    // from least to greatest.
    ArrayList<Wall> sortByVal(ArrayList<Wall> wlist) {

        int n = 0;

        // increment
        if (wlist.size() > 0) {
            n = (wlist.size() - 1);
        }

        while (n != 0) {
            this.insertWall(n, wlist, wlist.get(0));
            wlist.remove(0);
            n -= 1;
        }
        
        return wlist;

    }


    // Helper for sortByVal, that inserts the Wall
    // at the correct index of the sorted list of Walls
    ArrayList<Wall> insertWall(int index, ArrayList<Wall> wlist, 
            Wall wa) {
        int bound = wlist.size();
        int val = wa.value;

        if (bound == 0) {
            wlist.add(wa);
        }

        // place the Wall in the correct place in sorted list
        for (int i = index; i < bound; i += 1) {
            if (val - wlist.get(i).value <= 0 &&
                    (i + 1 != bound)) {
                wlist.add(i, wa);
                return wlist;
            }
            if (val - wlist.get(i).value <= 0 &&
                    (i + 1 == bound)) {
                wlist.add(i, wa);
                return wlist;
            }
            if (val - wlist.get(i).value > 0 &&
                    (i + 1 == bound)) {
                wlist.add(wa);
                return wlist;
            }
        }

        return wlist;
    }
}

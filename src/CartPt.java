
// To represent a CartPt
class CartPt {
    int x;
    int y;
    
    CartPt(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Overrides the equals method
    public boolean equals(Object o) {
        if (o instanceof CartPt) {
            return this.x == ((CartPt) o).x &&
                    this.y == ((CartPt) o).y;
        }
        else {
            return false;
        }
    }
    
    // Overrides the hashCode method
    public int hashCode() {
        return this.x + this.y;
    }
}
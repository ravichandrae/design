public class Board {
    private int size;
    private Cell [][] cells;
    public Board(int size) {
        this.setSize(size);
        setCells(new Cell[size][size]);
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                getCells()[i][j] = new Cell();
            }
        }
    }

    public void place(Player player, int []coords) {
        cells[coords[0]][coords[1]].setValue(player.getName());
    }

    public void draw() {
        drawHorizontalLine(size);
        for(int i = 0; i < size; i++) {
            System.out.print("|");
            for(int j = 0; j < size; j++) {
                if(cells[i][j].getValue().isEmpty()) {
                    System.out.print(" ");
                } else {
                    System.out.print(cells[i][j].getValue());
                }
                System.out.print("|");
            }
            System.out.println();
            drawHorizontalLine(size);
        }
    }

    private void drawHorizontalLine(int len) {
        System.out.print("-");
        System.out.println("--".repeat(Math.max(0, len)));
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }
}

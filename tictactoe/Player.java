import java.util.Scanner;

public class Player {
    private String name;

    public Player(String name) {
        this.setName(name);
    }

    public int[] play() {
        System.out.println("Player " + getName() + " turn");
        System.out.println("Enter Cell coordinates - x y: ");
        Scanner scanner = new Scanner(System.in);
        int []coordinates = new int[2];
        coordinates[0] = scanner.nextInt();
        coordinates[1] = scanner.nextInt();
        return coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

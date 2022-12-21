import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // testDateRangeOverlap();
        // testDateRangeSort();
        Room r1 = new Room(1, 2);
        Room r2 = new Room(2, 5);
        Room r3 = new Room(3, 10);
        List<Room> rooms = new ArrayList<>(List.of(r1, r2, r3));
        MeetingScheduler scheduler = new MeetingScheduler(rooms);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime period1 = LocalDateTime.parse("2022-12-21 10:00", dateTimeFormatter);
        LocalDateTime period2 = LocalDateTime.parse("2022-12-21 11:00", dateTimeFormatter);

        DateRange slot1 = new DateRange(period1, period2);
        Meeting m1 = new Meeting(slot1, 4);
        Room room = scheduler.getRoom(m1);
        if (room != null) {
            System.out.println(room);
            room.block(m1);
        } else {
            System.out.println("No Room available");
        }
        Meeting m2 = new Meeting(slot1, 2);
        room = scheduler.getRoom(m2);
        if (room != null) {
            System.out.println(room);
            room.block(m2);
        } else {
            System.out.println("No Room available");
        }

        LocalDateTime period3 = LocalDateTime.parse("2022-12-21 09:00", dateTimeFormatter);
        DateRange slot2 = new DateRange(period3, period1);
        Meeting m3 = new Meeting(slot2, 2);
        room = scheduler.getRoom(m3);
        if (room != null) {
            System.out.println(room);
            room.block(m3);
        } else {
            System.out.println("No Room available");
        }

        room = scheduler.getRoom(m3);
        if (room != null) {
            System.out.println(room);
            room.block(m3);
        } else {
            System.out.println("No Room available");
        }

        room = scheduler.getRoom(m3);
        if (room != null) {
            System.out.println(room);
            room.block(m3);
        } else {
            System.out.println("No Room available");
        }

        room = scheduler.getRoom(m3);
        if (room != null) {
            System.out.println(room);
            room.block(m3);
        } else {
            System.out.println("No Room available");
        }
    }

    static class MeetingScheduler {
        List<Room> rooms;
        List<Integer> capacities;

        public MeetingScheduler(List<Room> rooms) {
            Collections.sort(rooms);
            this.rooms = rooms;
            capacities = new ArrayList<>();
            for (Room r : rooms) {
                capacities.add(r.capacity);
            }
        }

        public Room getRoom(Meeting meeting) {
            Room result = null;
            int index = Collections.binarySearch(capacities, meeting.attendees);
            if (index < 0) {
                index = -index - 1;
            }
            while (index < rooms.size()) {
                Room currentRoom = rooms.get(index);
                if (isAvailable(currentRoom.blocked, meeting.slot)) {
                    result = currentRoom;
                    break;
                }
                index++;
            }
            return result;
        }

        private boolean isAvailable(List<DateRange> blocked, DateRange slot) {
            if (blocked.isEmpty()) {
                return true;
            }
            int index = Collections.binarySearch(blocked, slot);
            if (index < 0) {
                index = -index - 1;
            }
            DateRange nearestRange = blocked.get(index);
            return !nearestRange.isOverlap(slot);
        }
    }

    static class Meeting {
        DateRange slot;
        int attendees;

        public Meeting(DateRange slot, int attendees) {
            this.slot = slot;
            this.attendees = attendees;
        }
    }

    static class Room implements Comparable<Room> {
        int id;
        int capacity;

        List<DateRange> blocked;

        public Room(int id, int capacity) {
            this.id = id;
            this.capacity = capacity;
            this.blocked = new ArrayList<>();
        }

        public void block(Meeting meeting) {
            int index = Collections.binarySearch(blocked, meeting.slot);
            if (index < 0) {
                index = -index - 1;
            }
            blocked.add(index, meeting.slot);
        }

        @Override
        public int compareTo(Room o) {
            return this.capacity - o.capacity;
        }

        @Override
        public String toString() {
            return "Room #" + this.id + " Capacity: " + this.capacity;
        }
    }

    static class DateRange implements Comparable<DateRange> {
        LocalDateTime start, end;

        public DateRange(LocalDateTime start, LocalDateTime end) {
            this.start = start;
            this.end = end;
        }

        public boolean isOverlap(DateRange period) {
            return (period.start.compareTo(this.start) >= 0 && period.start.compareTo(this.end) < 0)
                    || (period.end.compareTo(this.start) > 0 && period.end.compareTo(this.end) < 0);
        }

        @Override
        public int compareTo(DateRange o) {
            if (this.start.compareTo(o.start) == 0)
                return this.end.compareTo(o.end);
            return this.start.compareTo(o.start);
        }

        @Override
        public String toString() {
            return this.start.toString() + " - " + this.end.toString();
        }
    }

    private static void testDateRangeOverlap() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime period1 = LocalDateTime.parse("2022-12-21 10:00", dateTimeFormatter);
        LocalDateTime period2 = LocalDateTime.parse("2022-12-21 11:00", dateTimeFormatter);

        DateRange range1 = new DateRange(period1, period2);

        LocalDateTime period3 = LocalDateTime.parse("2022-12-21 09:00", dateTimeFormatter);
        LocalDateTime period4 = LocalDateTime.parse("2022-12-21 09:30", dateTimeFormatter);
        LocalDateTime period5 = LocalDateTime.parse("2022-12-21 10:00", dateTimeFormatter);

        System.out.println(range1.isOverlap(new DateRange(period3, period4))); //Expected False
        System.out.println(range1.isOverlap(new DateRange(period3, period5))); //Expected False

        LocalDateTime period6 = LocalDateTime.parse("2022-12-21 10:30", dateTimeFormatter);
        System.out.println(range1.isOverlap(new DateRange(period3, period6))); //Expected True

        LocalDateTime period7 = LocalDateTime.parse("2022-12-21 10:45", dateTimeFormatter);
        System.out.println(range1.isOverlap(new DateRange(period6, period7))); //Expected True

        LocalDateTime period8 = LocalDateTime.parse("2022-12-21 11:15", dateTimeFormatter);
        System.out.println(range1.isOverlap(new DateRange(period7, period8))); //Expected True

        System.out.println(range1.isOverlap(new DateRange(period2, period8))); //Expected False
        LocalDateTime period9 = LocalDateTime.parse("2022-12-21 11:45", dateTimeFormatter);
        System.out.println(range1.isOverlap(new DateRange(period8, period9))); //Expected False
    }

    private static void testDateRangeSort() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime period1 = LocalDateTime.parse("2022-12-21 10:00", dateTimeFormatter);
        LocalDateTime period2 = LocalDateTime.parse("2022-12-21 11:00", dateTimeFormatter);
        LocalDateTime period3 = LocalDateTime.parse("2022-12-21 09:00", dateTimeFormatter);
        LocalDateTime period4 = LocalDateTime.parse("2022-12-21 09:30", dateTimeFormatter);
        LocalDateTime period6 = LocalDateTime.parse("2022-12-21 10:30", dateTimeFormatter);
        LocalDateTime period7 = LocalDateTime.parse("2022-12-21 11:15", dateTimeFormatter);
        LocalDateTime period8 = LocalDateTime.parse("2022-12-21 11:45", dateTimeFormatter);

        DateRange range1 = new DateRange(period1, period2); //(10:00 - 11:00)
        DateRange range2 = new DateRange(period3, period1); //(09:00 - 10:00)
        DateRange range3 = new DateRange(period3, period4); //(09:00 - 09:30)
        DateRange range4 = new DateRange(period3, period6); //(09:00 - 10:30)
        DateRange range5 = new DateRange(period2, period8); //(11:00 - 11:45)
        DateRange range6 = new DateRange(period2, period7); //(11:00 - 11:15)
        DateRange range7 = new DateRange(period4, period1); //(09:30 - 10:00)
        DateRange range8 = new DateRange(period6, period2); //(10:30 - 11:00)

        List<DateRange> dateRanges = new ArrayList<>(List.of(range1, range2, range3, range4, range5, range6, range7, range8));
        Collections.sort(dateRanges);

        for (DateRange dateRange : dateRanges) {
            System.out.println(dateRange);
        }
    }
}

import java.util.*;

public class TimetableScheduler {

    int numFaculty;
    HashMap<Integer, List<Integer>> facultySubjects;
    boolean isSaturdayClass;
    HashMap<String, Map<Integer, String>> schedule;
    HashMap<Integer, Integer> lastScheduledTimeSlot;
    HashSet<Integer> unscheduledFaculty;

// Constructor
    public TimetableScheduler(int numFaculty, boolean isSaturdayClass) {
        this.numFaculty = numFaculty;
        this.facultySubjects = new HashMap<>();
        this.isSaturdayClass = isSaturdayClass;
        this.lastScheduledTimeSlot = new HashMap<>();
        this.unscheduledFaculty = new HashSet<>();
        createSchedule();
    }

// crate schedule according to total days
//    and also check if saturday gave class of not.
    public void createSchedule() {
        schedule = new HashMap<>();
        String[] weekDay = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        if (isSaturdayClass) {
            String[] upWeekDay = Arrays.copyOf(weekDay, weekDay.length + 1);
            upWeekDay[weekDay.length] = "Saturday";
            weekDay = upWeekDay;
        }

        for (String day : weekDay) {
            Map<Integer, String> daySchedule = new HashMap<>();
            for (int h = 8; h <= 20; h += 4) {
                daySchedule.put(h, null);
            }
            schedule.put(day, daySchedule);
        }

        for (int faculty = 1; faculty <= numFaculty; faculty++) {
            unscheduledFaculty.add(faculty);
        }
    }

//    Assign subjects for faculty.
    public void assignSubjectsToFaculty() {
        Scanner s = new Scanner(System.in);

        for (int faculty = 1; faculty <= numFaculty; faculty++) {
            System.out.println("Enter the number of subjects for Faculty " + faculty + ": ");
            int numSubjects = s.nextInt();
            List<Integer> subjects = new ArrayList<>();

            for (int i = 1; i <= numSubjects; i++) {
                System.out.println("Enter subject " + i + " for Faculty " + faculty + ": ");
                int subject = s.nextInt();
                subjects.add(subject);
            }

            facultySubjects.put(faculty, subjects);
        }
    }

//    generate the time Table.
    public void generateTimetable() {
        int totalDays = schedule.size();
        int totalTimeSlots = schedule.get("Monday").size();
        int totalClasses = totalDays * totalTimeSlots;

        Random random = new Random();

        for (String day : schedule.keySet()) {
            List<Integer> unscheduledTimeSlots = new ArrayList<>(schedule.get(day).keySet());
            Set<Integer> scheduledSubjects = new HashSet<>();

            for (int faculty : facultySubjects.keySet()) {
                List<Integer> subjects = facultySubjects.get(faculty);

                if (subjects.isEmpty()) {
                    if (unscheduledFaculty.contains(faculty)) {
                        if (!unscheduledTimeSlots.isEmpty()) {
                            int timeSlot = unscheduledTimeSlots.remove(random.nextInt(unscheduledTimeSlots.size()));
                            schedule.get(day).put(timeSlot, "Faculty " + faculty + ", No Subject");
                            System.out.println("Faculty " + faculty + " has no subjects scheduled on " + day + " at " + timeSlot + ":00");
                            unscheduledFaculty.remove(faculty);
                        }
                    }
                } else {
                    if (!unscheduledTimeSlots.isEmpty()) {
                        int timeSlot = unscheduledTimeSlots.remove(random.nextInt(unscheduledTimeSlots.size()));
                        int subject;
                        do {
                            subject = subjects.get(random.nextInt(subjects.size()));
                        } while (scheduledSubjects.contains(subject));

                        scheduledSubjects.add(subject);

                        schedule.get(day).put(timeSlot, "Faculty " + faculty + ", Subject " + subject);
                        System.out.println("Faculty " + faculty + " for Subject " + subject + " on " + day + " at " + timeSlot + ":00");
                        unscheduledFaculty.remove(faculty);
                    }
                }
            }

// check remaining time slots with classes from other subjects

            for (int remainingTimeSlot : unscheduledTimeSlots) {
                for (int faculty : facultySubjects.keySet()) {
                    List<Integer> subjects = facultySubjects.get(faculty);
                    if (!subjects.isEmpty()) {
                        int subject;
                        do {
                            subject = subjects.get(random.nextInt(subjects.size()));
                        } while (scheduledSubjects.contains(subject));

                        scheduledSubjects.add(subject);

                        schedule.get(day).put(remainingTimeSlot, "Faculty " + faculty + ", Subject " + subject);
                        System.out.println("Faculty " + faculty + " for Subject " + subject + " on " + day + " at " + remainingTimeSlot + ":00");
                        break;
                    }
                }
            }
        }

//        Check is their any unscheduled faculty or not.

        if (!unscheduledFaculty.isEmpty()) {
            for (int faculty : unscheduledFaculty) {
                System.out.println("Faculty " + faculty + " has no more subjects to be scheduled");
            }
        }

        System.out.println("Total Number of Classes: " + totalClasses);
    }


//    Display Time Table
    public void displayTimetable() {
        System.out.println("\nTimetable is :");
        for (String day : schedule.keySet()) {
            System.out.println(day + ":");
            for (Map.Entry<Integer, String> entry : schedule.get(day).entrySet()) {
                if (entry.getValue() != null) {
                    System.out.println("  " + entry.getKey() + ":00 - " + (entry.getKey() + 4) + ":00: " + entry.getValue());
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.println("Enter the total number of faculty: ");
        int numFaculty = s.nextInt();

        System.out.println("Does Saturday have classes? (true/false): ");
        boolean isSaturdayClass = s.nextBoolean();

        TimetableScheduler timetableScheduler = new TimetableScheduler(numFaculty, isSaturdayClass);
        timetableScheduler.assignSubjectsToFaculty();
        timetableScheduler.generateTimetable();
        timetableScheduler.displayTimetable();
    }
}

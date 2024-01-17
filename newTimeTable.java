import java.util.*;

public class newTimeTable {

    int numFaculty;

    HashMap<Integer, List<String>> facultySubjects;

    boolean isSaturdayClass;

    HashMap<String, Map<Integer, String>> schedule;

    HashMap<String, Integer> maxClasses;

    HashMap<String, Integer> countClasses;

    HashMap<Integer, HashMap<String, HashMap<Integer, String>>> finalTimeTable;


    public newTimeTable(int numFaculty, boolean isSaturdayClass) {
        this.numFaculty = numFaculty;
        this.facultySubjects = new HashMap<>();
        this.isSaturdayClass = isSaturdayClass;
        this.maxClasses = new HashMap<>();
        this.finalTimeTable = new HashMap<>();
        this.countClasses = new HashMap<>();
        createSchedule();
    }

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
            for (int h = 8; h < 20; h += 4) {
                daySchedule.put(h, null);
            }
            schedule.put(day, daySchedule);
        }
    }

    public void assignSubjectsToFaculty() {
        Scanner s = new Scanner(System.in);

        for (int faculty = 1; faculty <= numFaculty; faculty++) {
            System.out.println("Enter the total number of subjects for Faculty " + faculty + ": ");
            int numSubjects = s.nextInt();
            List<String> subjects = new ArrayList<>();

            for (int i = 1; i <= numSubjects; i++) {
                System.out.println("Enter subject " + i + " for Faculty " + faculty + ": ");
                String subject = s.next();
                System.out.println("Enter the total classes of " + subject);
                int maxClassesForEverySubject = s.nextInt();
                maxClasses.put(subject, maxClassesForEverySubject);
                subjects.add(subject);
            }
            facultySubjects.put(faculty, subjects);
        }
    }

    public void generateTimeTable() {
        Set<Integer> facultySet = facultySubjects.keySet();
        Set<String> scheudleSet = schedule.keySet();
        Set<Integer> slotTotal = schedule.get("Monday").keySet();
        for (int everyFaculty : facultySet) {
            HashMap<String, HashMap<Integer, String>> innerMap = new HashMap<>();
            for (String everyDay : scheudleSet) {
                HashMap<Integer, String> innerMap2 = new HashMap<>();
                for (int everySlot : slotTotal) {
                    innerMap2.put(everySlot, null);
                }
                innerMap.put(everyDay, innerMap2);
            }
            finalTimeTable.put(everyFaculty, innerMap);
        }
    }

    public void assignSubjectToEveryFaculty() {
        Set<Integer> fillCountClasses = facultySubjects.keySet();
        for (int i : fillCountClasses) {
            List<String> list = facultySubjects.get(i);
            for (int k = 0; k < list.size(); k++) {
                countClasses.put(list.get(k), 0);
            }
        }
        Set<Integer> finalSet = finalTimeTable.keySet();
        for (int i : finalSet) {
            List<String> list = facultySubjects.get(i);
            HashMap<String, HashMap<Integer, String>> innerMap = finalTimeTable.get(i);
            Set<String> daySet = innerMap.keySet();
            int num = 0;
            int total = 0;
            while (num!=list.size() && total!=2) {
                for (String j : daySet) {
                    HashMap<Integer, String> innerMap2 = innerMap.get(j);
                    Set<Integer> innerSet2 = innerMap2.keySet();
                    for (int k : innerSet2) {
                        for (int value = 0; value < list.size(); value++) {
                            if ((!innerMap2.containsValue(list.get(value)) && countClasses.get(list.get(value)) < maxClasses.get(list.get(value))) && innerMap2.get(k) == null) {
                                innerMap2.put(k, list.get(value));
                                countClasses.put(list.get(value), countClasses.get(list.get(value)) + 1);
                            }
                            if (countClasses.get(value) == maxClasses.get(value)) {
                                num++;
                            }
                        }
                    }
                }
                total++;
            }
        }
    }
    public void displayTimeTable() {
        Set<Integer> facultySet = finalTimeTable.keySet();
        for (int faculty : facultySet) {
            System.out.println("For Faculty " + faculty + ":");
            HashMap<String, HashMap<Integer, String>> innerMap = finalTimeTable.get(faculty);
            Set<String> daySet = innerMap.keySet();

            for (String day : daySet) {
                System.out.println("\n" + day + ":");
                HashMap<Integer, String> innerMap2 = innerMap.get(day);
                Set<Integer> slotSet = innerMap2.keySet();

                for (int slot : slotSet) {
                    String subject = innerMap2.get(slot);
                    System.out.println("  " + slot + ": " + (subject != null ? subject : "No class"));
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

        newTimeTable newTime = new newTimeTable(numFaculty, isSaturdayClass);
        newTime.assignSubjectsToFaculty();
        newTime.generateTimeTable();
        newTime.assignSubjectToEveryFaculty();
        newTime.displayTimeTable();
    }
}

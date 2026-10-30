import java.util.ArrayList;

public class StudentService {

    private ArrayList<Student> students = new ArrayList<>();

    public void addStudent(Student student) {

        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        students.add(student);
    }

    public int getStudentCount() {
        return students.size();
    }

    public Student getStudent(int index) {

        if (index < 0 || index >= students.size()) {
            throw new IllegalArgumentException("Invalid student index");
        }

        return students.get(index);
    }
}
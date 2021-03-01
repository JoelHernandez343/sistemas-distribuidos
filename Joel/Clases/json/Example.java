import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Timestamp;

public class Example {
    static class Employee {
        String name;
        int age;
        float salary;
        Timestamp dateOfAdmission;

        Employee boss;

        Employee(String name, int age, float salary, Timestamp dateOfAdmission, Employee boss){
            this.name = name;
            this.age = age;
            this.salary = salary;
            this.dateOfAdmission = dateOfAdmission;
            this.boss = boss;
        }
    }

    public static void main(String[] args){
        Employee[] employees = new Employee[3];
        
        employees[0] = new Employee("Hugo", 20, 1000, Timestamp.valueOf("2020-01-01 20:10:00"), employees[0]); // Auto reference
        employees[1] = new Employee("Paco",21,2000,Timestamp.valueOf("2019-10-01 10:15:00"), employees[0]);
        employees[2] = new Employee("Luis",22,3000,Timestamp.valueOf("2018-11-01 00:00:00"), employees[1]);

        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS").setPrettyPrinting().create();

        String s = json.toJson(employees);

        System.out.println(s);

        Employee[] others = (Employee [])json.fromJson(s, Employee[].class);

        for (int i = 0; i < others.length; i++)
            System.out.println(others[i].name + " " + others[i].age + " " + others[i].salary + " " + others[i].dateOfAdmission + " " + (others[i].boss != null ? others[i].boss.name : null));
    }
}

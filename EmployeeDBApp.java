import java.sql.*;
import java.util.Scanner;

public class EmployeeDBApp {
    // Database credentials and URL - update these
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employeedb";
    private static final String USER = "dbuser";
    private static final String PASS = "dbpassword";

    public static void main(String[] args) {
        try {
            // Load the JDBC driver (optional for modern drivers, but good practice)
            // Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 Scanner scanner = new Scanner(System.in)) {
                System.out.println("Database connected successfully!");
                boolean exit = false;
                while (!exit) {
                    System.out.println("\nEmployee Database Menu:");
                    System.out.println("1. Add Employee");
                    System.out.println("2. View Employees");
                    System.out.println("3. Update Employee");
                    System.out.println("4. Delete Employee");
                    System.out.println("5. Exit");
                    System.out.print("Enter your choice: ");
                    int choice = Integer.parseInt(scanner.nextLine());

                    switch (choice) {
                        case 1:
                            addEmployee(conn, scanner);
                            break;
                        case 2:
                            viewEmployees(conn);
                            break;
                        case 3:
                            updateEmployee(conn, scanner);
                            break;
                        case 4:
                            deleteEmployee(conn, scanner);
                            break;
                        case 5:
                            exit = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private static void addEmployee(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter position: ");
        String position = scanner.nextLine();
        System.out.print("Enter salary: ");
        double salary = Double.parseDouble(scanner.nextLine());

        String sql = "INSERT INTO employees (name, position, salary) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, position);
            pstmt.setDouble(3, salary);
            pstmt.executeUpdate();
            System.out.println("Employee added successfully!");
        }
    }

    private static void viewEmployees(Connection conn) throws SQLException {
        String sql = "SELECT * FROM employees";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Current Employees ---");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Position: %s, Salary: $%.2f%n",
                                  rs.getInt("id"), rs.getString("name"),
                                  rs.getString("position"), rs.getDouble("salary"));
            }
            System.out.println("-------------------------");
        }
    }

    private static void updateEmployee(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter employee ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new position: ");
        String position = scanner.nextLine();
        System.out.print("Enter new salary: ");
        double salary = Double.parseDouble(scanner.nextLine());

        String sql = "UPDATE employees SET position = ?, salary = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, position);
            pstmt.setDouble(2, salary);
            pstmt.setInt(3, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee updated successfully!");
            } else {
                System.out.println("Employee ID not found.");
            }
        }
    }

    private static void deleteEmployee(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter employee ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee deleted successfully!");
            } else {
                System.out.println("Employee ID not found.");
            }
        }
    }
}
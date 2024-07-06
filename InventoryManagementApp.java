package zzz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
public class InventoryManagementApp {

	    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/inventory_management";
	    private static final String JDBC_USER = "root";
	    private static final String JDBC_PASSWORD = "Shruti@30";

	    private static Connection connection;
	    private static Scanner scanner;

	    public static void main(String[] args) {
	        try {
	            // Connect to the database
	            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	            scanner = new Scanner(System.in);

	            // Initialize menu
	            while (true) {
	                System.out.println("\n===== Inventory Management System =====");
	                System.out.println("1. Add New Item");
	                System.out.println("2. View Items by Category");
	                System.out.println("3. Update Item Quantity");
	                System.out.println("4. Delete Item");
	                System.out.println("5. Add New Category");
	                System.out.println("6. View All Categories");
	                System.out.println("7. Exit");
	                System.out.print("Enter your choice: ");

	                int choice = scanner.nextInt();
	                scanner.nextLine(); 

	                switch (choice) {
	                    case 1:
	                        addItem();
	                        break;
	                    case 2:
	                        viewItemsByCategory();
	                        break;
	                    case 3:
	                        updateItemQuantity();
	                        break;
	                    case 4:
	                        deleteItem();
	                        break;
	                    case 5:
	                        addCategory();
	                        break;
	                    case 6:
	                        viewAllCategories();
	                        break;
	                    case 7:
	                        System.out.println("Exiting...");
	                        return;
	                    default:
	                        System.out.println("Invalid choice. Please enter a number between 1 and 7.");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            // Close resources
	            try {
	                if (connection != null) {
	                    connection.close();
	                }
	                if (scanner != null) {
	                    scanner.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    private static void addItem() throws SQLException {
	        System.out.print("Enter item name: ");
	        String name = scanner.nextLine().trim();
	        System.out.print("Enter item quantity: ");
	        int quantity = scanner.nextInt();
	        scanner.nextLine(); // Consume newline
	        System.out.print("Enter category ID (or 0 to skip): ");
	        int categoryId = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        String sql = "INSERT INTO Items (name, quantity, category_id) VALUES (?, ?, ?)";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, name);
	            statement.setInt(2, quantity);
	            statement.setInt(3, categoryId);
	            int rowsInserted = statement.executeUpdate();
	            if (rowsInserted > 0) {
	                System.out.println("A new item has been added.");
	            }
	        }
	    }

	    private static void viewItemsByCategory() throws SQLException {
	        System.out.print("Enter category ID: ");
	        int categoryId = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        String sql = "SELECT id, name, quantity FROM Items WHERE category_id = ?";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setInt(1, categoryId);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                System.out.println("\n===== Items in Category =====");
	                System.out.printf("%-5s %-20s %-10s%n", "ID", "Name", "Quantity");
	                System.out.println("-------------------------------------");
	                while (resultSet.next()) {
	                    int id = resultSet.getInt("id");
	                    String name = resultSet.getString("name");
	                    int quantity = resultSet.getInt("quantity");
	                    System.out.printf("%-5d %-20s %-10d%n", id, name, quantity);
	                }
	            }
	        }
	    }

	    private static void updateItemQuantity() throws SQLException {
	        System.out.print("Enter item ID to update quantity: ");
	        int itemId = scanner.nextInt();
	        scanner.nextLine(); // Consume newline
	        System.out.print("Enter new quantity: ");
	        int newQuantity = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        String sql = "UPDATE Items SET quantity = ? WHERE id = ?";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setInt(1, newQuantity);
	            statement.setInt(2, itemId);
	            int rowsUpdated = statement.executeUpdate();
	            if (rowsUpdated > 0) {
	                System.out.println("Item quantity updated successfully.");
	            } else {
	                System.out.println("Item not found.");
	            }
	        }
	    }

	    private static void deleteItem() throws SQLException {
	        System.out.print("Enter item ID to delete: ");
	        int itemId = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        String sql = "DELETE FROM Items WHERE id = ?";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setInt(1, itemId);
	            int rowsDeleted = statement.executeUpdate();
	            if (rowsDeleted > 0) {
	                System.out.println("Item deleted successfully.");
	            } else {
	                System.out.println("Item not found.");
	            }
	        }
	    }

	    private static void addCategory() throws SQLException {
	        System.out.print("Enter category name: ");
	        String categoryName = scanner.nextLine().trim();

	        String sql = "INSERT INTO Categories (name) VALUES (?)";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, categoryName);
	            int rowsInserted = statement.executeUpdate();
	            if (rowsInserted > 0) {
	                System.out.println("A new category has been added.");
	            }
	        }
	    }

	    private static void viewAllCategories() throws SQLException {
	        String sql = "SELECT id, name FROM Categories";
	        try (Statement statement = connection.createStatement();
	             ResultSet resultSet = statement.executeQuery(sql)) {
	            System.out.println("\n===== All Categories =====");
	            System.out.printf("%-5s %-20s%n", "ID", "Name");
	            System.out.println("-------------------------------------");
	            while (resultSet.next()) {
	                int id = resultSet.getInt("id");
	                String name = resultSet.getString("name");
	                System.out.printf("%-5d %-20s%n", id, name);
	            }
	        }
	    }
	}

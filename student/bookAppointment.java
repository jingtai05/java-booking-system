package student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class bookAppointment {

    // Initialize the "Make Appointment" page
    public static void makeAppointment1(JFrame page, String name, String StudentOwnID) {
        page.setTitle("Student Page");
        page.setLayout(null);

        // Clear the frame
        page.getContentPane().removeAll();

        // Top menu panel
        JPanel menu = new JPanel(new FlowLayout());
        menu.setBounds(0, 10, page.getWidth(), 50);

        // Menu buttons
        JButton day = new JButton("Day Schedule");
        JButton make = new JButton("Make Appointment");
        JButton historic = new JButton("History Appointment");

        menu.add(day);
        menu.add(make);
        menu.add(historic);
        page.add(menu);

        // Welcome label
        JLabel welcome = new JLabel("Welcome, " + name);
        welcome.setFont(new Font("Serif", Font.BOLD, 20));
        welcome.setBounds(10, 53, 300, 30);
        page.add(welcome);

        // Logout button
        JButton logout = new JButton("Log Out");
        logout.setBounds(390, 56, 100, 28);
        page.add(logout);

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.SelectRole.selectrole1(null);
            }
        });

        // Menu button actions
        day.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                student.studentPage.studentPage1(page, name, StudentOwnID); // Navigate to the day page
            }   
        });

        make.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                student.bookAppointment.makeAppointment1(page, name, StudentOwnID); // Navigate to the day page
            }   
        });

        historic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                student.historyAppointment.historyAppointment1(page, name, StudentOwnID); // Navigate to the day page
            }   
        });

        // Buttons for booking and rescheduling
        JButton bookAppointButton = new JButton("Book Appointment");
        bookAppointButton.setBounds(37, 92, 220, 31);
        page.add(bookAppointButton);

        JButton rescheduleButton = new JButton("Reschedule Appointment");
        rescheduleButton.setBounds(267, 92, 220, 31);
        page.add(rescheduleButton);

        rescheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                student.rescheduleAppointment.rescheduleAppointment1(page, name, StudentOwnID);
            }
        });        

        // Panel to display available lecturers
        JPanel bookAppointPanel = new JPanel();
        bookAppointPanel.setLayout(new BoxLayout(bookAppointPanel, BoxLayout.Y_AXIS));
        bookAppointPanel.setBorder(BorderFactory.createTitledBorder("Available Lecturer"));

        JScrollPane scrollPane = new JScrollPane(bookAppointPanel);
        scrollPane.setBounds(12, 127, page.getWidth() - 40, 355);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        page.add(scrollPane);

        // Load available lecturer IDs
        Set<String> availableLecturerIDs = loadAvailableLecturerIDs("bookingOption.txt");

        // Load lecturer details
        List<String[]> lecturerDetails = loadLecturerDetails("users.txt", availableLecturerIDs);

        // Add lecturers to the panel
        for (String[] lecturer : lecturerDetails) {
            String lecturerName = lecturer[0];
            String lecturerID = lecturer[1];

            JPanel lecturerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            lecturerPanel.setBorder(BorderFactory.createTitledBorder("Lecturer Details"));

            JLabel lecturerNameLabel = new JLabel("Name: " + lecturerName + " | TP Number: " + lecturerID);
            JButton selectButton = new JButton("Select");

            selectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    displayBookingDetails(page, bookAppointPanel, lecturerName, lecturerID, StudentOwnID, name);
                }
            });            

            lecturerPanel.add(lecturerNameLabel);
            lecturerPanel.add(selectButton);

            bookAppointPanel.add(lecturerPanel);
        }

        // Refresh the frame
        page.revalidate();
        page.repaint();
    }

    // Load available lecturer IDs
    private static Set<String> loadAvailableLecturerIDs(String filePath) {
        Set<String> lecturerIDs = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    lecturerIDs.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lecturerIDs;
    }

    // Load lecturer details
    private static List<String[]> loadLecturerDetails(String filePath, Set<String> availableLecturerIDs) {
        List<String[]> details = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && availableLecturerIDs.contains(parts[0].trim())) {
                    details.add(new String[]{parts[3].trim(), parts[0].trim()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return details;
    }

    // Display booking details
    private static void displayBookingDetails(JFrame page, JPanel bookAppointPanel, String lecturerName, String lecturerID, String StudentOwnID, String name) {
        bookAppointPanel.removeAll();

        JLabel titleLabel = new JLabel("Available Bookings for " + lecturerName);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 16));
        bookAppointPanel.add(titleLabel);

        boolean hasBookings = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("bookingOption.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].trim().equals(lecturerID)) {
                    hasBookings = true;

                    JPanel bookingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    bookingPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));

                    JLabel bookingLabel = new JLabel("Date: " + parts[1] + " | Start: " + parts[2] + " | End: " + parts[3]);
                    JButton bookButton = new JButton("Book");

                    bookButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            handleBookingSelection(lecturerID, parts, StudentOwnID, page);
                            JPanel bookAppointPanel = (JPanel) bookingPanel.getParent();
                            bookAppointPanel.remove(bookingPanel);
                            bookAppointPanel.revalidate();
                            bookAppointPanel.repaint();
                        }
                    });
                    
                    bookingPanel.add(bookingLabel);
                    bookingPanel.add(bookButton);
                    bookAppointPanel.add(bookingPanel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!hasBookings) {
            JLabel noBookingsLabel = new JLabel("No bookings available for this lecturer.");
            bookAppointPanel.add(noBookingsLabel);
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeAppointment1(page, name, StudentOwnID);
            }
        });
        bookAppointPanel.add(backButton);

        bookAppointPanel.revalidate();
        bookAppointPanel.repaint();
    }

    // Handle booking selection
    private static void handleBookingSelection(String lecturerID, String[] booking, String StudentOwnID, JFrame page) {
        try {
            List<String> allLines = Files.readAllLines(Paths.get("bookingOption.txt"));
            List<String> updatedLines = new ArrayList<>();
            boolean bookingRemoved = false;

            for (String line : allLines) {
                if (line.equals(String.join(",", booking))) {
                    bookingRemoved = true;
                } else {
                    updatedLines.add(line);
                }
            }

            Files.write(Paths.get("bookingOption.txt"), updatedLines);

            if (bookingRemoved) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("appointment.txt", true))) {
                    writer.write(String.join(",", lecturerID, StudentOwnID, booking[1], booking[2], booking[3], "", "", "A"));
                    writer.newLine();
                }

                JOptionPane.showMessageDialog(page, "Appointment booked successfully!");
            } else {
                JOptionPane.showMessageDialog(page, "Booking could not be found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

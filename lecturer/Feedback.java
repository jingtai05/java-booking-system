package lecturer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feedback {
//1.1//////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void Feedback1(JFrame page, String OwnerID, String name) {
        page.setTitle("Lecturer Page");
        page.setLayout(null);
        page.getContentPane().removeAll();

        // Top menu
        JPanel menu = new JPanel();
        menu.setLayout(new FlowLayout());
        menu.setBounds(0, 10, page.getWidth(), 50);

        JButton day = new JButton("Day Schedule");
        JButton booking = new JButton("Manage Booking");
        JButton rejectOrApprove = new JButton("Reject / Approve");
        JButton feedback = new JButton("Feedback");

        menu.add(day);
        menu.add(booking);
        menu.add(rejectOrApprove);
        menu.add(feedback);
        page.add(menu);

        day.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lecturer.TodaySchedule.lecturerPage1(page, name, OwnerID); // Navigate to the Schedule Calendar
            }
        });

        // Action for Schedule button (Clicking it will show the calendar)
        booking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lecturer.OpenBooking.OpenBooking1(page, name, OwnerID);
            }
        });

        rejectOrApprove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lecturer.RejectOrApprove.RejectOrApprove1(page, name, OwnerID);
            }
        });

        feedback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lecturer.Feedback.Feedback1(page, OwnerID, name); // Navigate to the Schedule Calendar
            }
        });       

        // Welcome label
        JLabel welcome = new JLabel("Welcome, " + name);
        welcome.setFont(new Font("Serif", Font.BOLD, 20));
        welcome.setBounds(10, 53, 300, 30);
        page.add(welcome);

        JButton logout = new JButton("Log Out");
        logout.setBounds(390, 56, 100, 28);
        page.add(logout);

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.SelectRole.selectrole1(null);
            }
        });

//1.2//////////////////////////////////////////////////////////////////////////////////////////////////////
        // Panel for feedback
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.Y_AXIS));
        feedbackPanel.setBounds(12, 95, page.getWidth() - 40, 354);
        feedbackPanel.setBorder(BorderFactory.createTitledBorder("Completed Appointments"));

        // Adding JScrollPane
        JScrollPane scrollPane = new JScrollPane(feedbackPanel);
        scrollPane.setBounds(12, 95, page.getWidth() - 40, 353);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scrollPane);

        // Load appointments and display relevant ones
        List<String[]> appointments = loadAppointments();
        updateFeedbackPanel(page, feedbackPanel, appointments, OwnerID);

        page.revalidate();
        page.repaint();
    }

//2.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static List<String[]> loadAppointments() {
        List<String[]> appointments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] appointment = line.split(",");
                appointments.add(appointment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointments;
    }

//2.1//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static Map<String, String> loadUserNames(String filePath) {
    Map<String, String> userMap = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                String tpNumber = parts[0].trim();
                String fullName = parts[3].trim(); // Full name is at index 3
                userMap.put(tpNumber, fullName);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return userMap;
}
    
//3.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void updateFeedbackPanel(JFrame page, JPanel feedbackPanel, List<String[]> appointments, String lecturerTP) {
        feedbackPanel.removeAll();
        boolean hasAppointments = false;

        Map<String, String> userMap = loadUserNames("users.txt");

        for (String[] appointment : appointments) {
            String status = appointment[7].trim(); // Status
            String appointmentLecturerTP = appointment[0].trim(); // Lecturer TP

            // Only process status "C" and for the specific lecturer
            if (status.equals("C") && appointmentLecturerTP.equals(lecturerTP)) {
                String studentTP = appointment[1].trim(); // Student TP number
                String studentFeedback = appointment[6].trim(); // Student feedback
                String date = appointment[2]; // Appointment date
                String time = appointment[3] + " - " + appointment[4]; // Time range
                String studentName = "Student: " + userMap.getOrDefault(studentTP, "Unknown"); // Map TP to name
                String tpNumber = "TP: " + appointment[1]; // TP Number

                hasAppointments = true;
                addFeedbackRow(page, feedbackPanel, appointment, date, time, studentFeedback, studentName, tpNumber);
            }
        }

        if (!hasAppointments) {
            JLabel noAppointments = new JLabel("No completed appointments found.");
            noAppointments.setFont(new Font("Arial", Font.ITALIC, 14));
            noAppointments.setAlignmentX(Component.CENTER_ALIGNMENT);
            feedbackPanel.add(noAppointments);
        }

        feedbackPanel.revalidate();
        feedbackPanel.repaint();
    }


//4.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void addFeedbackRow(JFrame page, JPanel feedbackPanel, String[] appointment, String date, String time, String studentFeedback, String studentName, String tpNumber) {
        JPanel feedbackRow = new JPanel();
        feedbackRow.setLayout(null);
        feedbackRow.setPreferredSize(new Dimension(feedbackPanel.getWidth() - 40, 102));
        feedbackRow.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel dateLabel = new JLabel("Date: " + date);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setBounds(10, 5, 300, 20);
        feedbackRow.add(dateLabel);

        JLabel timeLabel = new JLabel("Time: " + time);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setBounds(206, 5, 300, 20);
        feedbackRow.add(timeLabel);

        JLabel studentNameLabel = new JLabel(studentName);
        studentNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        studentNameLabel.setBounds(10, 25, 300, 20);
        feedbackRow.add(studentNameLabel);

        JLabel tpNumberLabel = new JLabel(tpNumber);
        tpNumberLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        tpNumberLabel.setBounds(206, 25, 200, 20);
        feedbackRow.add(tpNumberLabel);

        JLabel feedbackLabel = new JLabel("Student Feedback: " + (studentFeedback.isEmpty() ? "None" : studentFeedback));
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        feedbackLabel.setBounds(10, 45, 500, 20);
        feedbackRow.add(feedbackLabel);

        JLabel lecturerFeedbackLabel = new JLabel("Your Feedback:");
        lecturerFeedbackLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        lecturerFeedbackLabel.setBounds(10, 66, 300, 20);
        feedbackRow.add(lecturerFeedbackLabel);

        JTextField lecturerFeedbackField = new JTextField();
        lecturerFeedbackField.setBounds(120, 68, 200, 20);
        lecturerFeedbackField.setText(appointment[5].trim()); // Pre-fill if feedback exists
        feedbackRow.add(lecturerFeedbackField);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(340, 66, 75, 25);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveLecturerFeedback(page, appointment, lecturerFeedbackField.getText());
            }
        });
                feedbackRow.add(submitButton);
        feedbackPanel.add(feedbackRow);
    }

//5.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void saveLecturerFeedback(JFrame page, String[] appointment, String feedback) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("appointment.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] attributes = line.split(",");
                    boolean isMatchingAppointment = true;
                    for (int i = 0; i < attributes.length; i++) {
                        if (!attributes[i].trim().equals(appointment[i].trim())) {
                            isMatchingAppointment = false;
                            break;
                        }
                    }
                    if (isMatchingAppointment) {
                        attributes[5] = feedback; // Update lecturer feedback
                        line = String.join(",", attributes);
                    }
                    lines.add(line);
                }
            }

            // Write updated data and rename file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("updated_appointments.txt"))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            new File("appointment.txt").delete();
            new File("updated_appointments.txt").renameTo(new File("appointment.txt"));

            JOptionPane.showMessageDialog(page, "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(page, "Failed to save feedback.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

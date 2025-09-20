package lecturer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RejectOrApprove {
//1.1//////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void RejectOrApprove1(JFrame page, String name, String OwnerID) {
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
                main.SelectRole.selectrole1(null);;
            }
        });

//1.2//////////////////////////////////////////////////////////////////////////////////////////////////////
        // Panel for displaying appointments to reject or approve
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.Y_AXIS));
        schedulePanel.setBounds(12, 95, page.getWidth() - 40, 354);
        schedulePanel.setBorder(BorderFactory.createTitledBorder("Appointments to Approve / Reject"));

        // Adding JScrollPane to handle overflow
        JScrollPane scrollPane = new JScrollPane(schedulePanel);
        scrollPane.setBounds(12, 95, page.getWidth() - 40, 353);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Force vertical scroll
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scroll
        page.add(scrollPane);

        // Load appointments and user details
        List<String[]> appointments = loadAppointments();
        Map<String, String> users = loadUsers();

        // Method to display appointments with status "A"
        updateSchedulePanel(schedulePanel, appointments, users, OwnerID);

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
        //refresh page
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

        // Refresh the frame to show updated components
        page.revalidate();
        page.repaint();
    }

//2.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void updateSchedulePanel(JPanel schedulePanel, List<String[]> appointments, Map<String, String> users, String lecturerTP) {
        schedulePanel.removeAll();
        boolean hasAppointments = false;
    
        for (String[] appointment : appointments) {
            String status = appointment[7].trim();  // Appointment status
            String appointmentLecturerTP = appointment[0].trim();  // Lecturer TP
    
            // Only show appointments with status "A" (Approval Pending) for the current lecturer
            if (status.equals("A") && appointmentLecturerTP.equals(lecturerTP)) {
                String studentTP = appointment[1];  // Student TP
                String studentName = users.getOrDefault(studentTP, "Unknown Student");
                String date = appointment[2];  // Appointment Date
                String startTime = appointment[3];  // Start time
                String endTime = appointment[4];  // End time
                String time = startTime + " - " + endTime;
    
                String Studentname = "Name: " + studentName;
                String studentID = "TP Number: " + studentTP;
    
                // Highlighting pending appointments in red
                Color timeColor = Color.RED;
    
                hasAppointments = true;
                addActionableEventToPanel(schedulePanel, appointment, date, time, Studentname, studentID, timeColor);
            }
        }
    
        // If no appointments are found
        if (!hasAppointments) {
            JLabel noAppointments = new JLabel("No appointments scheduled.");
            noAppointments.setFont(new Font("Arial", Font.ITALIC, 14));
            noAppointments.setAlignmentX(Component.CENTER_ALIGNMENT);
            schedulePanel.add(noAppointments);
        }
    
        schedulePanel.revalidate();
        schedulePanel.repaint();
    }

//3.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void addActionableEventToPanel(JPanel panel, String[] appointment, String date, String time, String name, String studentID, Color color) {
        JPanel eventPanel = new JPanel();
        eventPanel.setLayout(null);
        eventPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    
        // Set a fixed preferred size for each appointment panel
        eventPanel.setPreferredSize(new Dimension(panel.getWidth() - 40, 92));  // Fixed width and height (adjust height as needed)
    
        // Date label
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dateLabel.setForeground(color);
        dateLabel.setBounds(10, 10, 200, 30);  // Adjust size and position as needed
        eventPanel.add(dateLabel);
    
        // Time label
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setForeground(color);
        timeLabel.setBounds(10, 42, 200, 30);  // Adjust size and position as needed
        eventPanel.add(timeLabel);
    
        // Description label (Student Name)
        JLabel NameLabel = new JLabel(name);
        NameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        NameLabel.setForeground(Color.BLACK);
        NameLabel.setBounds(170, 10, 200, 30);  // Adjust size and position as needed
        eventPanel.add(NameLabel);
    
        // TP Number label (align it to the right)
        JLabel tpNumberLabel = new JLabel(studentID);
        tpNumberLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        tpNumberLabel.setForeground(Color.GRAY);
        tpNumberLabel.setBounds(295, 16, 200, 20);  // Adjusted to align TP number on the right side of the time
        eventPanel.add(tpNumberLabel);
    
        // Accept button
        JButton acceptButton = new JButton("✔ Accept");
        acceptButton.setBounds(170, 42, 102, 30);  // Adjust button size and position
        acceptButton.setBackground(Color.decode("#41C77B"));
        acceptButton.setForeground(Color.WHITE);
        acceptButton.setOpaque(true); //show button color
        acceptButton.setBorderPainted(false); //show word colour
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAppointmentStatus(appointment, "B", eventPanel);
            }
        });
                eventPanel.add(acceptButton);
    
        // Reject button
        JButton rejectButton = new JButton("✖ Reject");
        rejectButton.setBounds(301, 42, 102, 30);  // Adjust button size and position
        rejectButton.setBackground(Color.decode("#E14545"));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setOpaque(true); //show button color
        rejectButton.setBorderPainted(false); //show word colour
        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAppointment(appointment, eventPanel);
            }
        });
                eventPanel.add(rejectButton);
    
        panel.add(eventPanel);
    }    

//4.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void updateAppointmentStatus(String[] appointment, String newStatus, JPanel eventPanel) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("appointment.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] attributes = line.split(",");
                    if (Arrays.equals(attributes, appointment)) {
                        attributes[7] = newStatus;
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

            // Remove the event panel from the UI and refresh
            JPanel parentPanel = (JPanel) eventPanel.getParent();
            parentPanel.remove(eventPanel);
            parentPanel.revalidate();
            parentPanel.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//5.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void deleteAppointment(String[] appointment, JPanel eventPanel) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("appointment.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] attributes = line.split(",");
                    if (Arrays.equals(attributes, appointment)) {
                        continue;  // Skip this appointment
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

            // Remove the event panel from the UI and refresh
            JPanel parentPanel = (JPanel) eventPanel.getParent();
            parentPanel.remove(eventPanel);
            parentPanel.revalidate();
            parentPanel.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//6.0//////////////////////////////////////////////////////////////////////////////////////////////////////
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

    private static Map<String, String> loadUsers() {
        Map<String, String> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                users.put(user[0], user[3]);  // Map TP Number to Full Name
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}

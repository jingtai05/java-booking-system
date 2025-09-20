package lecturer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TodaySchedule {
//1.1//////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void lecturerPage1(JFrame page, String name, String OwnerID) {
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

        day.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lecturer.TodaySchedule.lecturerPage1(page, name, OwnerID); // Navigate to Day Schedule
            }
        });

        booking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lecturer.OpenBooking.OpenBooking1(page, name, OwnerID); // Navigate to Manage Booking
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
                lecturer.Feedback.Feedback1(page, OwnerID, name); // Navigate to Feedback
            }
        });        

        // Welcome label
        JLabel welcome = new JLabel("Welcome, " + name);
        welcome.setFont(new Font("Serif", Font.BOLD, 20));
        welcome.setBounds(10, 53, 300, 30);
        page.add(welcome);

        //import calendar image 
        ImageIcon calendar = new ImageIcon("imageStorage/UnclickCalendar_icon.png");
        //Resize the image
        Image imageSize = calendar.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        ImageIcon resizedCalendar = new ImageIcon(imageSize); //resize image
        //calendar button
        JButton calendarIcon = new JButton(resizedCalendar);
        calendarIcon.setBounds(173,43,48,48);
        calendarIcon.setBorderPainted(false);    // Remove button background
        page.add(calendarIcon);
        //add the menu panel here so that won't block calendarIcon
        page.add(menu);

        calendarIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lecturer.ScheduleCalendar.ScheduleCalendar1(page, name, OwnerID); // Navigate to the Schedule Calendar
            }
        });

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
        JLabel dateLabel = new JLabel("Select Date: ");
        dateLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
        dateLabel.setBounds(15, 90, 100, 30);
        page.add(dateLabel);

        // Date spinner
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setBounds(93,90,110,30);
        JSpinner.DateEditor dateformat = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateformat);
        dateSpinner.setValue(new Date()); // Default to today's date
        page.add(dateSpinner);
        
        JButton loadDateButton = new JButton("Load Schedule");
        loadDateButton.setBounds(203,90,123,30);
        page.add(loadDateButton);

        // Panel for displaying today's schedule
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.Y_AXIS));
        schedulePanel.setBounds(12, 127, page.getWidth() - 40, 356);
        schedulePanel.setBorder(BorderFactory.createTitledBorder("Today's Schedule"));

        JScrollPane scrollPane = new JScrollPane(schedulePanel);
        scrollPane.setBounds(12, 127, page.getWidth() - 40, 355);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Force vertical scroll
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scroll
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        page.add(scrollPane);

//1.3//////////////////////////////////////////////////////////////////////////////////////////////////////
        // Load appointments and user details
        List<String[]> appointments = loadAppointments();
        Map<String, String> users = loadUsers();

        // Method to update schedule panel based on selected date
        ActionListener loadScheduleListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date selectedDate = (Date) dateSpinner.getValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String selectedDateString = dateFormat.format(selectedDate);
        
                updateSchedulePanel(schedulePanel, appointments, users, selectedDateString, OwnerID);
            }
        };        

        // Add listener to load button
        loadDateButton.addActionListener(loadScheduleListener);

        // Initial load for today's date
        loadScheduleListener.actionPerformed(null);

        // Refresh the frame to show updated components
        page.revalidate();
        page.repaint();
    }
//2.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static List<String[]> loadAppointments() {
        List<String[]> appointments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] attribute = Arrays.stream(line.split(","))
                        .map(String::trim)
                        .toArray(String[]::new);
                if (attribute.length == 8) {
                    appointments.add(attribute);
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return appointments;
    }    
//3.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static Map<String, String> loadUsers() {
        Map<String, String> users = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] attribute = line.split(",");
                if (attribute.length >= 4) {
                    String tpNumber = attribute[0];
                    String fullName = attribute[3];
                    users.put(tpNumber, fullName);
                } else {
                    System.err.println("Invalid user format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
        }

        return users;
    }
//4.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void updateSchedulePanel(JPanel schedulePanel, List<String[]> appointments, Map<String, String> users, String selectedDate, String lecturerTP) {
        schedulePanel.removeAll();
    
        boolean hasAppointments = false;
    
        // Filter and collect appointments for the selected date and lecturer
        List<String[]> filteredAppointments = new ArrayList<>();
        for (String[] appointment : appointments) {
            String appointmentDate = appointment[2].trim();
            String appointmentLecturerTP = appointment[0].trim();
    
            if (appointmentDate.equals(selectedDate) && appointmentLecturerTP.equals(lecturerTP)) {
                filteredAppointments.add(appointment);
            }
        }
    
        // Sort appointments by start time
        filteredAppointments.sort(Comparator.comparing(timestart -> timestart[3])); // timestart[3] is the start time
    
        // Add sorted appointments to the panel
        for (String[] appointment : filteredAppointments) {
            String studentTP = appointment[1];
            String studentName = users.getOrDefault(studentTP, "Unknown Student");
            String startTime = appointment[3];
            String endTime = appointment[4];
            String status = appointment[7].trim();
            String description = "Student: " + studentName;
    
            // Determine color based on status
            Color timeColor;
            switch (status) {
                case "C": timeColor = Color.decode("#149696"); break;
                case "B": timeColor = Color.decode("#F18E67"); break;
                case "A": timeColor = Color.decode("#EE5858"); break;
                default: timeColor = Color.BLACK; break;
            }
    
            hasAppointments = true;
            addScheduleINTOPanel(schedulePanel, startTime + " - " + endTime, description, timeColor, studentTP,status, appointments, selectedDate, lecturerTP, users);
        }
    
        // Display a message if no appointments are found
        if (!hasAppointments) {
            JLabel noAppointments = new JLabel("No appointments scheduled for this date.");
            noAppointments.setFont(new Font("Arial", Font.ITALIC, 14));
            noAppointments.setAlignmentX(Component.CENTER_ALIGNMENT);
            schedulePanel.add(noAppointments);
        }
    
        schedulePanel.revalidate();
        schedulePanel.repaint();
    }
    
//5.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void addScheduleINTOPanel(JPanel panel, String time, String description, Color color, String studentTP, String status,List<String[]> appointments, String selectedDate, String lecturerTP,Map<String, String> users) {
        JPanel eventPanel = new JPanel();
        eventPanel.setLayout(null);
        eventPanel.setPreferredSize(new Dimension(eventPanel.getWidth() - 40, 72));
        eventPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Time label
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setForeground(color);
        timeLabel.setBounds(10, 10, 200, 30);
        eventPanel.add(timeLabel);

        JLabel statusLabel = new JLabel();
        switch (status) {
            case "C":
                statusLabel.setText("Appointment Finished");
                statusLabel.setForeground(Color.decode("#149696"));
                break;
            case "B":
                statusLabel.setText("Slot Booked");
                statusLabel.setForeground(Color.decode("#F18E67"));
                break;
            case "A":
                statusLabel.setText("Waiting to Approve");
                statusLabel.setForeground(Color.decode("#EE5858"));
                break;
            default:
                statusLabel.setText("No Status");
                statusLabel.setForeground(Color.GRAY);
                break;
        }
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setBounds(10, 36, 200, 20);
        eventPanel.add(statusLabel);

        // Description label
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setForeground(Color.BLACK);
        descriptionLabel.setBounds(153, 9, 400, 30);
        eventPanel.add(descriptionLabel);

        JLabel tpNumberLabel = new JLabel("TP: " + studentTP);
        tpNumberLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        tpNumberLabel.setForeground(Color.GRAY);
        tpNumberLabel.setBounds(153, 45, 200, 20);
        eventPanel.add(tpNumberLabel);

        JButton deleteButton = new JButton("Cancel Schedule");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 9));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(Color.decode("#E14545"));
        deleteButton.setOpaque(true); //show button color
        deleteButton.setBorderPainted(false); //show word colour
        deleteButton.setBounds(327, 21, 120, 30);
        eventPanel.add(deleteButton);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Find and remove the matching appointment from the list
                Iterator<String[]> iterator = appointments.iterator();
                while (iterator.hasNext()) {
                    String[] appointment = iterator.next();
                    if (appointment[0].trim().equals(lecturerTP) && 
                        appointment[1].trim().equals(studentTP) && 
                        appointment[2].trim().equals(selectedDate) &&
                        appointment[3].trim().equals(time.split(" - ")[0]) &&
                        appointment[4].trim().equals(time.split(" - ")[1])) {
        
                        iterator.remove(); // Remove from the list
        
                        // Write updated appointments to file
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("appointment.txt"))) {
                            for (String[] app : appointments) {
                                writer.write(String.join(",", app));
                                writer.newLine();
                            }
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
        
                        break;
                    }
                }
        
                // Remove the event panel from the UI
                JPanel parentPanel = (JPanel) eventPanel.getParent();
                parentPanel.remove(eventPanel);
                parentPanel.revalidate();
                parentPanel.repaint();
            }
        }); 
        
        panel.add(eventPanel);
    }

}

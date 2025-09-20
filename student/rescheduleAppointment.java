package student;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class rescheduleAppointment {
    private static JPanel appointmentDetailsPanel;
    private static JScrollPane scrollPane;

    public static void rescheduleAppointment1(JFrame page, String name, String StudentOwnID) {
        page.setTitle("Student Page");
        page.setLayout(null);
        page.getContentPane().removeAll();

        // Menu and header setup
        JPanel menu = new JPanel();
        menu.setLayout(new FlowLayout());
        menu.setBounds(0, 10, page.getWidth(), 50);

        JButton day = new JButton("Day Schedule");
        JButton make = new JButton("Make Appointment");
        JButton historic = new JButton("History Appointment");

        menu.add(day);
        menu.add(make);
        menu.add(historic);
        page.add(menu);

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

        // Buttons for booking and rescheduling
        JButton bookAppointButton = new JButton("Book Appointment");
        bookAppointButton.setBounds(18, 92, 220, 31);
        page.add(bookAppointButton);

        bookAppointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                page.remove(appointmentDetailsPanel);
                student.bookAppointment.makeAppointment1(page, name, StudentOwnID);
            }
        });

        JButton rescheduleButton = new JButton("Reschedule Appointment");
        rescheduleButton.setBounds(248, 92, 220, 31);
        page.add(rescheduleButton);

        // Appointment Details Panel
        appointmentDetailsPanel = new JPanel();
        appointmentDetailsPanel.setLayout(new BoxLayout(appointmentDetailsPanel, BoxLayout.Y_AXIS));
        appointmentDetailsPanel.setBorder(BorderFactory.createTitledBorder("Your Appointments"));

        // Add Scroll Pane
        scrollPane = new JScrollPane(appointmentDetailsPanel);
        scrollPane.setBounds(19, 135, page.getWidth() - 40, 325);
        page.add(scrollPane);

        // Read appointments
        List<String[]> appointmentDetails = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 7 && (details[7].equals("A") || details[7].equals("B")) && details[1].equals(StudentOwnID)) {
                    appointmentDetails.add(details);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add appointment panels
        for (String[] appointment : appointmentDetails) {
            JPanel appointmentPanel = new JPanel();
            appointmentPanel.setLayout(null);
            appointmentPanel.setBorder(BorderFactory.createTitledBorder("Appointment Details"));
            appointmentPanel.setPreferredSize(new java.awt.Dimension(scrollPane.getWidth() - 40, 80));

            JLabel appointmentInfo = new JLabel("Lecturer ID: " + appointment[0] + " | Date: " + appointment[2] + " | Time: " + appointment[3] + " - " + appointment[4]);
            appointmentInfo.setBounds(10, 16, 450, 20);
            appointmentPanel.add(appointmentInfo);

            JLabel statusLabel = new JLabel("Status: " + appointment[7]);
            statusLabel.setBounds(10, 40, 100, 20);
            appointmentPanel.add(statusLabel);

            JButton selectButton = new JButton("Select");
            selectButton.setBounds(348, 40, 80, 30);
            selectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    transitionToRescheduleAppointment(page, name, StudentOwnID, appointmentDetailsPanel, appointment);
                }
            });
            appointmentPanel.add(selectButton);

            appointmentDetailsPanel.add(appointmentPanel);
        }

        // Final refresh
        page.revalidate();
        page.repaint();
    }

    private static void transitionToRescheduleAppointment(JFrame page, String name, String StudentOwnID, JPanel appointmentDetailsPanel, String[] appointmentDetails) {
        // Remove appointmentDetailsPanel and its scroll pane
        page.remove(scrollPane);
        page.remove(appointmentDetailsPanel);

        // Reschedule Appointment Panel
        JPanel reschedulePanel = new JPanel();
        reschedulePanel.setLayout(null);
        reschedulePanel.setBounds(19, 135, page.getWidth() - 40, 325);
        reschedulePanel.setBorder(BorderFactory.createTitledBorder("Reschedule Appointment"));

         // Get current appointment details
        String date = appointmentDetails[2];
        String startTime = appointmentDetails[3];
        String endTime = appointmentDetails[4];

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date appointmentDate = null;
        try {
            appointmentDate = dateFormat.parse(date);  // Parsing the string into Date
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(10, 14, 100, 30);
        reschedulePanel.add(dateLabel);

        JDateChooser bookingDateCalendar = new JDateChooser();
        bookingDateCalendar.setBounds(45, 14, 120, 30);
        bookingDateCalendar.setDate(appointmentDate); // Set current date
        bookingDateCalendar.setDateFormatString("dd/MM/yyyy");
        reschedulePanel.add(bookingDateCalendar);

        JLabel startTimeLabel = new JLabel("Start Time:");
        startTimeLabel.setBounds(56, 285, 150, 30);
        reschedulePanel.add(startTimeLabel);

        JTextField startTimeField = new JTextField(startTime);
        startTimeField.setBounds(126, 285, 100, 30);
        reschedulePanel.add(startTimeField);

        JLabel dash = new JLabel("-");
        dash.setBounds(246,293,10,10);
        reschedulePanel.add(dash);    

        JLabel endTimeLabel = new JLabel("End Time:");
        endTimeLabel.setBounds(276, 285, 150, 30);
        reschedulePanel.add(endTimeLabel);

        JTextField endTimeField = new JTextField(endTime);
        endTimeField.setBounds(346, 285, 100, 30);
        reschedulePanel.add(endTimeField);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(400, 14, 100, 30);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeAppointmentDateTime(appointmentDetails, bookingDateCalendar.getDate(), startTimeField.getText(), endTimeField.getText(), page);
                JOptionPane.showMessageDialog(page, "Reschedule Request Submitted Successfully","Submit Successfully",JOptionPane.WARNING_MESSAGE);

            }
        });
        reschedulePanel.add(submitButton);

        JButton backbutton = new JButton("Back");
        backbutton.setBounds(300,14,100,30);
        reschedulePanel.add(backbutton);
        backbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                rescheduleAppointment1(page, name, StudentOwnID);
            }
        });

        // Add reschedule panel to page
        page.add(reschedulePanel);

        // Refresh the page
        page.revalidate();
        page.repaint();
    }

    private static void changeAppointmentDateTime(String[] oldAppointmentDetails, Date newDate, String newStartTime, String newEndTime, JFrame page) {
        try {
            // Read the current appointment data
            List<String> lines = Files.readAllLines(Paths.get("appointment.txt"));
            List<String> updatedLines = new ArrayList<>();
    
            // Update the status to 'A' and modify the date and times
            for (String line : lines) {
                String[] details = line.split(",");
                if (details.length >= 7 && details[0].equals(oldAppointmentDetails[0]) && details[1].equals(oldAppointmentDetails[1]) &&
                details[2].equals(oldAppointmentDetails[2]) && details[3].equals(oldAppointmentDetails[3]) && details[4].equals(oldAppointmentDetails[4])) {
                    details[2] = new SimpleDateFormat("dd/MM/yyyy").format(newDate);
                    details[3] = newStartTime;
                    details[4] = newEndTime;
                    details[7] = "A"; // Change status to A
                }
                updatedLines.add(String.join(",", details));
            }
    
            // Write the updated appointment data back to the file
            Files.write(Paths.get("appointment.txt"), updatedLines);
    
            // Show updated appointment details after rescheduling
            //showAppointmentDetails(page, oldAppointmentDetails[1]);
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

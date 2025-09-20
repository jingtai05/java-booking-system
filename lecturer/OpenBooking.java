package lecturer;

import javax.swing.*;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.util.Calendar;
import java.util.Date;

public class OpenBooking {  //set to class variable to remove while changing page (Open Appointment Booking -> Provided Appointment)
    private static JPanel SetBookingPanel;
    private static JButton submitButton;
    private static JPanel ProvidedAppointmentPanel;
    private static JScrollPane scrollPane;
    private static JPanel appointmentPanel;
//1.1//////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void OpenBooking1(JFrame page, String name, String OwnerID) {
        page.setLayout(null);
        page.getContentPane().removeAll();

        JPanel menu = new JPanel(new FlowLayout());
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
                lecturer.TodaySchedule.lecturerPage1(page, name, OwnerID);
            }
        });

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
                lecturer.Feedback.Feedback1(page, OwnerID, name);
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
                main.SelectRole.selectrole1(null);;
            }
        });

        JButton setBooking = new JButton("Open Appointment Booking");
        setBooking.setBounds(41,93,220,31);
        page.add(setBooking);
        OpenAppointmentBooking(page, OwnerID);

        setBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenAppointmentBooking(page, OwnerID);
            }
        });

        JButton appointmentProvide = new JButton("Provided Appointment");
        appointmentProvide.setBounds(273,93,220,31);
        page.add(appointmentProvide);

        appointmentProvide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppointmentProvided(page, OwnerID);
            }
        });

        page.revalidate();
        page.repaint();
    }


//Set Open Booking Hour
//2.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void OpenAppointmentBooking (JFrame page, String OwnerID){
        if (ProvidedAppointmentPanel != null) {
            page.remove(ProvidedAppointmentPanel);
        }
        if (scrollPane != null) {
            page.remove(scrollPane);
        }
        if (appointmentPanel != null) {
            page.remove(appointmentPanel);
        }
        
        SetBookingPanel = new JPanel(); 
        SetBookingPanel.setLayout(null);
        SetBookingPanel.setBounds(19, 135, page.getWidth() - 40, 325);
        SetBookingPanel.setBorder(BorderFactory.createTitledBorder(""));
        page.add(SetBookingPanel);

//2.1//////////////////////////////////////////////////////////////////////////////////////////////////////
        // Current Date and Time
        Date currentDate = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String currentTime = timeFormat.format(currentDate);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(10, 14, 100, 30);
        SetBookingPanel.add(dateLabel);

        JDateChooser bookingDateCalendar = new JDateChooser();
        bookingDateCalendar.setBounds(45,14,120,30); //textfield
        bookingDateCalendar.setDate(currentDate); // Set current date
        bookingDateCalendar.setDateFormatString("dd/MM/yyyy");
        SetBookingPanel.add(bookingDateCalendar);
    
        JCalendar calendar = bookingDateCalendar.getJCalendar();
        calendar.setPreferredSize(new Dimension(516, 187));

//2.2//////////////////////////////////////////////////////////////////////////////////////////////////////
        JLabel startTimeLabel = new JLabel("Start Time:"); //24:00 //HH:mm
        //startTimeLabel.setFont(new Font("MS Sans Serif", Font.PLAIN,10));
        startTimeLabel.setBounds(48, 265, 150, 30);
        SetBookingPanel.add(startTimeLabel);

        JTextField startTimeField = new JTextField(currentTime);
        startTimeField.setBounds(118, 265, 100, 30);
        SetBookingPanel.add(startTimeField);

        JLabel dash = new JLabel("-");
        dash.setBounds(238,273,10,10);
        SetBookingPanel.add(dash);

        JLabel endTimeLabel = new JLabel("End Time:");
        endTimeLabel.setBounds(268, 265, 150, 30);
        SetBookingPanel.add(endTimeLabel);

        JTextField endTimeField = new JTextField();
        endTimeField.setBounds(338, 265, 100, 30);
        SetBookingPanel.add(endTimeField);

//2.3//////////////////////////////////////////////////////////////////////////////////////////////////////
        submitButton = new JButton("Submit");
        submitButton.setBounds(210, 471, 100, 30);
        page.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date OptionDate = bookingDateCalendar.getDate();
                String OptionStartTime = startTimeField.getText().trim();
                String OptionEndTime = endTimeField.getText().trim();

                if (OptionEndTime.isEmpty()) {
                    JOptionPane.showMessageDialog(page, "Fill in the time to END the appointment.");
                    return;
                }

                try {
                    SimpleDateFormat timeFormat = new SimpleDateFormat ("HH:mm");
                    Date startTime = timeFormat.parse(OptionStartTime);
                    Date endTime = timeFormat.parse(OptionEndTime);

                    if (startTime.after(endTime)) {
                        JOptionPane.showMessageDialog(page, "End time must be after start time.");
                        return;
                    }
                    //Write into file
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("bookingOption.txt", true))) {
                        writer.write(String.format(OwnerID + "," + new SimpleDateFormat("dd/MM/yyyy").format(OptionDate) + "," + timeFormat.format(startTime) + "," + timeFormat.format(endTime)));
                        writer.newLine(); // Enter new line for next entry
                        JOptionPane.showMessageDialog(page, "Booking Slot added successfully!");
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(page, "Error adding Booking Slot. Please try again.");
                        ioException.printStackTrace();
                    }

                }catch (ParseException parseException){
                    JOptionPane.showMessageDialog(page, "Invalid time format. Use 24:00 (HH:mm)");
                }
            }
        });   

        page.revalidate();
        page.repaint();
    }



//Set Provided Appointment
//3.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void AppointmentProvided (JFrame page, String OwnerID){
        if (SetBookingPanel != null) {
            page.remove(SetBookingPanel);
        }
        if (submitButton != null){
            page.remove(submitButton);
        }
        
//3.1//////////////////////////////////////////////////////////////////////////////////////////////////////
        ProvidedAppointmentPanel = new JPanel(); 
        ProvidedAppointmentPanel.setLayout(new BoxLayout(ProvidedAppointmentPanel, BoxLayout.Y_AXIS));
        ProvidedAppointmentPanel.setBounds(19, 135, page.getWidth() - 40, 325);
        ProvidedAppointmentPanel.setBorder(BorderFactory.createTitledBorder(""));

        scrollPane = new JScrollPane(ProvidedAppointmentPanel);
        scrollPane.setBounds(12, 127, page.getWidth() - 40, 355);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Force vertical scroll
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scroll
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        page.add(scrollPane);

//3.2//////////////////////////////////////////////////////////////////////////////////////////////////////
        try (BufferedReader reader = new BufferedReader(new FileReader("bookingOption.txt"))){
            String line;
            boolean hasAppointments = false;

            while ((line = reader.readLine()) != null) {
                //OwnerID,Date,StartTime,EndTime [4]
                String [] attribute = line.split(",");
                if (attribute.length ==4){
                    String owner = attribute[0];
                    String date = attribute[1];
                    String starttime = attribute[2];
                    String endtime = attribute[3];

                    if (owner.equals(OwnerID)) {
                        //individual panel for each appointment
                        appointmentPanel = new JPanel();
                        appointmentPanel.setLayout(null);
                        appointmentPanel.setPreferredSize(new Dimension(page.getWidth() -80,100));
                        appointmentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                        //add details into panel
                        JLabel dateLabel = new JLabel (date);
                        dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
                        dateLabel.setBounds(57,15,200,30);
                        appointmentPanel.add(dateLabel);

                        JLabel startendtimelabel = new JLabel (starttime + "-" + endtime);
                        startendtimelabel.setFont(new Font("Arial", Font.BOLD, 16));
                        startendtimelabel.setBounds(57,48,200,30);
                        appointmentPanel.add(startendtimelabel);

                        JButton cancelbutton = new JButton ("Cancel");
                        cancelbutton.setBounds(317,32,95,28);
                        cancelbutton.setForeground(Color.WHITE);
                        cancelbutton.setBackground(Color.decode("#E14545"));
                        cancelbutton.setOpaque(true); //show button color
                        cancelbutton.setBorderPainted(false); //show word colour
                        appointmentPanel.add(cancelbutton);

                        cancelbutton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // Read and update the file contents
                                try (BufferedReader reader = new BufferedReader(new FileReader("bookingOption.txt"))) {
                                    // List to hold updated data
                                    StringBuilder updatedData = new StringBuilder();
                                    String line;
                        
                                    // Iterate over each line to find and remove the matching appointment
                                    while ((line = reader.readLine()) != null) {
                                        String[] parts = line.split(",");
                                        if (parts.length == 4) {
                                            String owner = parts[0];
                                            String date = parts[1];
                                            String starttime = parts[2];
                                            String endtime = parts[3];
                        
                                            // Check if this line matches the appointment to cancel
                                            if (!(owner.equals(OwnerID) && 
                                                  date.equals(dateLabel.getText()) && 
                                                  starttime.equals(startendtimelabel.getText().split("-")[0]) && 
                                                  endtime.equals(startendtimelabel.getText().split("-")[1]))) {
                                                // Append non-matching lines to updatedData
                                                updatedData.append(line).append(System.lineSeparator());
                                            }
                                        }
                                    }
                        
                                    // Write back the updated content to the file
                                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("bookingOption.txt"))) {
                                        writer.write(updatedData.toString());
                                    }
                        
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                        
                                // Remove the event panel from the UI
                                ProvidedAppointmentPanel.remove(appointmentPanel);
                                ProvidedAppointmentPanel.revalidate();
                                ProvidedAppointmentPanel.repaint();
                            }
                        });
                        

                        ProvidedAppointmentPanel.add(appointmentPanel);

                        hasAppointments = true;

                    }
    
                }
                 
            }

            //If no appointment
            if (!hasAppointments) {
                JLabel noAppointmentsLabel = new JLabel("No appointments found.");
                noAppointmentsLabel.setFont(new Font("Serif", Font.ITALIC, 16));
                noAppointmentsLabel.setBounds(10, 10, 300, 30);
                ProvidedAppointmentPanel.add(noAppointmentsLabel);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        page.revalidate();
        page.repaint();
    }
}
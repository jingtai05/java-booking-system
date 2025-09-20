package lecturer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScheduleCalendar {
    JPanel calendarPanel;
    private Map<Date, List<String>> appointments = new HashMap<>();
    JComboBox<String> monthComboBox, YearComboBox;
    int SelectedMonth, SelectedYear; // These variables must store within class & outside method 
                                     // because these variables need to be shared across methods

//1.1//////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void ScheduleCalendar1(JFrame page, String name, String OwnerID) {
        // Clear the frame
        page.getContentPane().removeAll();

        ScheduleCalendar scheduleCalendar = new ScheduleCalendar();

        // Top menu
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
                lecturer.Feedback.Feedback1(page, OwnerID, name); // Navigate to the Schedule Calendar
            }
        });        

        JLabel welcome = new JLabel("Welcome, " + name);
        welcome.setFont(new Font("Serif", Font.BOLD, 20));
        welcome.setBounds(10, 53, 300, 30);
        page.add(welcome);

        JButton logout = new JButton("Log Out");
        logout.setBounds(390,56,100,28);
        page.add(logout);

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.SelectRole.selectrole1(null);;
            }
        });

        //import calendar image 
        ImageIcon calendar = new ImageIcon("imageStorage/Calendar_icon.png");
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
                lecturer.TodaySchedule.lecturerPage1(page, name, OwnerID); // Navigate to the Schedule Calendar
            }
        });
//1.2//////////////////////////////////////////////////////////////////////////////////////////////////////
        //call these 3 methods
        scheduleCalendar.SelectMonthandYear(page);
        scheduleCalendar.loadAppointments(OwnerID);
        scheduleCalendar.CalendarUpdate();

        // Refresh the frame to show new components
        page.revalidate();
        page.repaint();
    }

//2.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private void SelectMonthandYear(JFrame page) {  //draw calendar //read appointment.txt function call here, do outside
        // Create panel (container) for selecting month and year
        JPanel SELECTmonthyearPanel = new JPanel();
        SELECTmonthyearPanel.setLayout(null);
        SELECTmonthyearPanel.setBounds(120, 83, 300, 50);

        // Month Combo Box and it Selection (Jan-Dec)
        String[] month = new DateFormatSymbols().getMonths();
        monthComboBox = new JComboBox<>(month);
        Calendar currentMonthYear = Calendar.getInstance(); //get current date and time (here is getting MONTH nad YEAR)
        SelectedMonth = currentMonthYear.get(Calendar.MONTH);
        SelectedYear = currentMonthYear.get(Calendar.YEAR);
        monthComboBox.setSelectedIndex(SelectedMonth); // set current month to selection
        monthComboBox.setBounds(10, 10, 120, 30); //set location within panel
        monthComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectedMonth = monthComboBox.getSelectedIndex();
                CalendarUpdate(); // if change month/year, calendar will update
            }
        });
        
        // Year Selection
        YearComboBox = new JComboBox<>();
        for (int i = SelectedYear - 5; i <= SelectedYear + 5; i++) {  // Current year -5 +5 (2019-2029) option in the combo box
            YearComboBox.addItem(String.valueOf(i)); // add the calculated year inside combo box
        }
        YearComboBox.setSelectedItem(String.valueOf(SelectedYear)); // set current year as default selection
        YearComboBox.setBounds(150, 10, 120, 30);
        YearComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectedYear = Integer.parseInt((String) YearComboBox.getSelectedItem());
                CalendarUpdate(); // if change month/year, calendar will update
            }
        });

        SELECTmonthyearPanel.add(monthComboBox);
        SELECTmonthyearPanel.add(YearComboBox);
        page.add(SELECTmonthyearPanel);

        calendarPanel = new JPanel(new GridLayout(0, 7));
        calendarPanel.setBounds(57, 144, 430, 330);
        page.add(calendarPanel);

        CalendarUpdate();
    }

//2.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private void loadAppointments(String OwnerID) {
        SimpleDateFormat dateMonthyearFormat = new SimpleDateFormat("dd/MM/yyyy");
        // check exist appointment
        try (BufferedReader reader = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) { // read all the line while txt file is not empty
                String[] attribute = line.split(",");
                if (attribute.length > 7) { //attribute length total got 8 value, length=9 //if index=9
                    String lecturerTP = attribute[0].trim();
                    if (!lecturerTP.equals(OwnerID)) {
                        continue; // Skip appointments not for this lecturer
                    }
                    String dateValue = attribute[2].trim();
                    String statusValue = attribute[7].trim();
                    Date date = dateMonthyearFormat.parse(dateValue);
                    appointments.computeIfAbsent(date, k -> new ArrayList<>()).add(statusValue);
                }

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

//3.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private void CalendarUpdate() {
        calendarPanel.removeAll(); // Remove existing calendar to update new calendar

        // 1. Store day names
        String[] dayOfweek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : dayOfweek) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setBorder(new LineBorder(Color.GRAY));
            calendarPanel.add(dayLabel);
        }

        // 2. Set up calendar Month and Year
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, SelectedYear);
        calendar.set(Calendar.MONTH, SelectedMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        
        //3. Set first day and total day in a month
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        int totalDayMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int startDay = (firstDayOfMonth == Calendar.SUNDAY ? 7 : firstDayOfMonth - 1) - 1;

        // 4. Add empty blanks for days before the start day of the month
        for (int blank = 0; blank < startDay; blank++) {
            calendarPanel.add(new JLabel(""));
        }
        //5. Date format
        SimpleDateFormat dateMonthyearFormat = new SimpleDateFormat("dd/MM/yyyy");

        // 6. Create panel for each day in the month
        for (int day = 1; day <= totalDayMonth; day++) {
            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setPreferredSize(new Dimension(60, 60));
        //7. Create button with day value and added into dayPanel
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setBackground(Color.WHITE);
            dayButton.setBorder(new LineBorder(Color.GRAY));
            dayButton.setFocusable(false);

            dayPanel.add(dayButton,BorderLayout.CENTER);

            JPanel dotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0, 0)); // Adjust grid layout for multiple dots
            dotsPanel.setOpaque(false);
            dayPanel.add(dotsPanel, BorderLayout.SOUTH);

            try {
                Date date = dateMonthyearFormat.parse(String.format("%02d/%02d/%04d", day, SelectedMonth + 1, SelectedYear));
                Date key = dateMonthyearFormat.parse(dateMonthyearFormat.format(date));

                if (appointments.containsKey(date)) {
                    List<String> statusList = appointments.get(date);

                    List<Color> statusColors = new ArrayList<>();
                    for (String s : statusList) {
                        if (s.contains("C")) {
                            statusColors.add(Color.decode("#149696"));
                        }
                        if (s.contains("B")) {
                            statusColors.add(Color.decode("#F18E67"));
                        }
                        if (s.contains("A")) {
                            statusColors.add(Color.decode("#EE5858"));
                        }
                    }

                    statusColors.sort((c1, c2) -> {
                        if (c1.equals(Color.decode("#149696"))) return -1;
                        if (c2.equals(Color.decode("#149696"))) return 1;
                        if (c1.equals(Color.decode("#F18E67"))) return -1;
                        if (c2.equals(Color.decode("#F18E67"))) return 1;
                        return 0;
                    });

                    for (Color color : statusColors) {
                        dotsPanel.add(createStatusDot(color));
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendarPanel.add(dayPanel);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

//4.0//////////////////////////////////////////////////////////////////////////////////////////////////////
    private JPanel createStatusDot(Color color) {  // Method to create a  circular dot
        // Create a JPanel for the dot
        JPanel dotPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(color); // Set the dot's color
                g.fillOval(0, 0, getWidth(), getHeight()); // Draw the circle

            }
        };

        dotPanel.setPreferredSize(new Dimension(10, 10)); // Set size of the dot
        dotPanel.setOpaque(false); // Make sure it's transparent (we draw on top)
        dotPanel.setLayout(new BorderLayout()); // This helps with positioning

        return dotPanel;
    }
}

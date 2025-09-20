package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*; //JSeries

public class SelectRole {
    public static void selectrole1(String[] args) {
        JFrame page = new JFrame("Main Page");
        page.setSize(550,550);
        page.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close program while close window
        page.setLayout(new GridBagLayout());


        GridBagConstraints location = new GridBagConstraints();
        location.insets = new Insets(10, 10, 10, 10);

        JLabel title =new JLabel("Psychology Consultation System");
        title.setFont(new Font("MS Sans Serif", Font.BOLD,26));
        location.gridx=0;
        location.gridy=0;
        location.gridwidth=2;
        location.anchor = GridBagConstraints.CENTER;
        page.add(title, location);

        JLabel APU = new JLabel("Asia Pacific University");
        APU.setFont(new Font("Serif", Font.PLAIN, 25));
        location.gridx=0;
        location.gridy=1;
        location.gridwidth=3;
        page.add(APU,location);

        JButton Lecturer = new JButton("Lecturer");
        location.gridx=0;
        location.gridy=3;
        location.gridwidth=2;
        page.add(Lecturer, location);

        Lecturer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                main.LecturerWelcomePage.main(page);
            }
        });

        JButton student = new JButton("Student");
        location.gridx=0;
        location.gridy=4;
        location.gridwidth=2;
        page.add(student, location);

        student.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                main.StudentwelcomePage.main(page);
            }
        });

        page.setVisible(true); // Make the frame visible

    }
    
    public static void main(String[] args) {
        selectrole1(args);    }
    
}

package main;

import lecturer.TodaySchedule;
import student.studentPage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*; //JSeries

public class LecturerWelcomePage{
    public static void main (JFrame page){
        page.setTitle ("Lecturer Login Page");
        page.getContentPane().removeAll();
        page.setLayout(null);

        //Import back button image
        ImageIcon back = new ImageIcon("imageStorage/Back_icon.png");
        Image imagesize = back.getImage().getScaledInstance(45,45, Image.SCALE_SMOOTH);
        ImageIcon resizedbackButton = new ImageIcon(imagesize); //resize image
        //Back Button
        JButton backButton = new JButton(resizedbackButton);
        backButton.setBorderPainted(false);    // Remove button background
        backButton.setBounds(10,10,45,45);
        page.add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                main.SelectRole.main(null);
            }
        });


        //Use back gridbaglayout for this page except back button
        JPanel gridBagPanel = new JPanel(new GridBagLayout());
        gridBagPanel.setBounds(0, 60, 550, 490); // Position panel for GridBagLayout
        page.add(gridBagPanel);

        GridBagConstraints location = new GridBagConstraints();
        location.insets = new Insets(10, 10, 10, 10);

        JLabel title =new JLabel("Psychology Consultation System");
        title.setFont(new Font("MS Sans Serif", Font.BOLD,26));
        location.gridx=0;
        location.gridy=0;
        location.gridwidth=2;
        location.anchor = GridBagConstraints.CENTER; // Center alignment
        gridBagPanel.add(title, location);

        JLabel APU = new JLabel("Asia Pacific University");
        APU.setFont(new Font("Serif", Font.PLAIN, 24));
        location.gridx=0;
        location.gridy=1;
        location.gridwidth=2;
        gridBagPanel.add(APU,location);
////////////////////////////////////////////////////////////////////////////////////////////////////////
        JPanel tpPanel = new JPanel();
        JLabel tp = new JLabel("TC Number:   ");
        JTextField tpField = new JTextField("TC",15);
        tpField.setPreferredSize(new Dimension(0, 27));
        tpField.setCaretPosition(2); // Place cursor after "TP"
        tpField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped (KeyEvent TPandNumbers){
                String input = tpField.getText();
                
                if (!Character.isDigit(TPandNumbers.getKeyChar()) && input.length() >= 2){
                    TPandNumbers.consume();
                }if (!input.startsWith("TC")) {
                    tpField.setText("TC");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            // Ensure "TP" is always at the beginning
                if (!tpField.getText().startsWith("TC")) {
                    tpField.setText("TC");
                }
            }
        });
        tpPanel.add(tp);
        tpPanel.add(tpField);
        location.gridx = 0;
        location.gridy = 2;
        location.gridwidth = 2; // Span across two columns
        location.insets = new Insets(20, 10, 10, 10); // Padding around components
        location.anchor = GridBagConstraints.CENTER; // Center alignment//Ensure tprField only except numbers except first two "TP" // and ensure start with TP
        gridBagPanel.add(tpPanel, location); 

        JPanel passwordPanel = new JPanel();
        JLabel password = new JLabel(" Password:    ");
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(0, 27));
        passwordPanel.add(password);
        passwordPanel.add(passwordField);
        location.gridx = 0;
        location.gridy = 3;
        location.insets = new Insets(10, 10, 10, 10);
        location.anchor = GridBagConstraints.CENTER; // Center alignment
        gridBagPanel.add(passwordPanel, location); 

        JButton login = new JButton("Log In");
        location.gridy=4;
        location.anchor = GridBagConstraints.LAST_LINE_END;
        gridBagPanel.add(login,location); 

        JButton signup = new JButton("Sign Up");
        location.gridy=4;
        location.anchor = GridBagConstraints.LAST_LINE_START;
        gridBagPanel.add(signup,location); 
////////////////////////////////////////////////////////////////////////////////////////////////////////
        
            login.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    String tpNumber = tpField.getText();
                    String password = new String(passwordField.getPassword());
                    String[] userInfo = new String[2]; //[0] for role, [1] for name
                    
                    try{
                        String result = main.login.login1(tpNumber, password, userInfo);

                        if (result.equals("success")){
                            String role = userInfo[0];
                            String name = userInfo[1];

                        
                            if (role.equals("Student")) {
                                studentPage.studentPage1(page,name,tpNumber); 
                            } else if (role.equals("Lecturer")) {
                                TodaySchedule.lecturerPage1(page,name,tpNumber);
                            } 
                        
                        }else if (result.equals("null account")){
                            JOptionPane.showMessageDialog(page, "No Account in this System \n Please register an account first","New User?",JOptionPane.WARNING_MESSAGE);
                        } else if (result.equals("fail")){
                            JOptionPane.showMessageDialog(page, "This TC Number haven't registered an account. Please try agian.","TC Number Does Not Exist",JOptionPane.WARNING_MESSAGE);
                        } else if (result.equals("blank")) {
                            JOptionPane.showMessageDialog(page, "Please fill all the required field first","Field Blank",JOptionPane.WARNING_MESSAGE);
                        } else if (result.equals("not telly")){
                            JOptionPane.showMessageDialog(page, "TC Number and Password are not telly \n Please try again","Password Incorrect",JOptionPane.WARNING_MESSAGE);
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(page, "An error occurred. Please try again.");
                    }
                }
            });
////////////////////////////////////////////////////////////////////////////////////////////////////////
        signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent signupEvent){
                JDialog register = new JDialog(page,"Register Lecturer Account", false);
                register.setSize(430,400);
                register.setLocationRelativeTo(page);
                register.setLayout(null);

                JLabel w_Register = new JLabel("Register a Free Account");
                w_Register.setFont(new Font("Serif", Font.BOLD, 24));
                w_Register.setBounds(70, 15, 350, 50);
                register.add(w_Register);
                
                JLabel tp_Register = new JLabel("TC Number: ");
                tp_Register.setFont(new Font("Serif", Font.ITALIC, 20));
                tp_Register.setBounds(60, 80, 350, 50);
                register.add(tp_Register);

                JTextField tprField = new JTextField("TC", 15);
                tprField.setBounds(170, 96, 150, 27);
                //Ensure tprField only except numbers except first two "TP" // and ensure start with TP
                tprField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped (KeyEvent TPandNumbers){
                        String input = tprField.getText();
                        
                        if (!Character.isDigit(TPandNumbers.getKeyChar()) && input.length() >= 2){
                            TPandNumbers.consume();
                        }if (!input.startsWith("TC")) {
                            tprField.setText("TC");
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    // Ensure "TP" is always at the beginning
                        if (!tprField.getText().startsWith("TC")) {
                            tprField.setText("TC");
                        }
                    }
                });

                register.add(tprField);

                JLabel password_Register = new JLabel("Password: ");
                password_Register.setFont(new Font("Serif", Font.ITALIC, 20));
                password_Register.setBounds(60, 120, 350, 50);
                register.add(password_Register);

                JTextField pwrField = new JTextField(15);
                pwrField.setBounds(170, 136, 150, 27);
                register.add(pwrField);

                JLabel name_Register = new JLabel("Name: ");
                name_Register.setFont(new Font("Serif", Font.ITALIC, 20));
                name_Register.setBounds(60, 160, 350, 50);
                register.add(name_Register);

                JTextField name_r_Field = new JTextField(15);
                name_r_Field.setBounds(170, 176, 150, 27);
                register.add(name_r_Field);
                //Ensure name Field only accept alphabet
                name_r_Field.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent alphabetOnly){
                        char input = alphabetOnly.getKeyChar(); //input

                        if(!Character.isLetter(input) && !Character.isWhitespace(input)){
                            alphabetOnly.consume();
                        }
                    }
                });

                JLabel role_Register = new JLabel("Role: ");
                role_Register.setFont(new Font("Serif", Font.ITALIC, 20));
                role_Register.setBounds(60, 200, 350, 50);
                register.add(role_Register);

                String[] roleOption = {"Lecturer"};
                JComboBox<String> roleComboBox = new JComboBox<>(roleOption);
                roleComboBox.setBounds(170, 216, 150, 27); 
                roleComboBox.setBackground(Color.WHITE);
                register.add(roleComboBox);
            
                JButton submit = new JButton("Submit");
                submit.setBounds(230,256,89,30);
                register.add(submit);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed (ActionEvent submitInfo){
                        String tpNumber = tprField.getText();
                        String password = pwrField.getText();
                        String name = name_r_Field.getText();
                        String role = (String) roleComboBox.getSelectedItem();

                        String displayEmpty = "";
                        if (tpNumber.equals("TC")){
                            displayEmpty += "TC Number is required to filled in\n";
                        }if (password.isEmpty()){
                            displayEmpty += "Password is required to filled in too...\n";
                        }if (role.equals("Please Select One")){
                            displayEmpty += "Please select you are a student or lecturer\n";
                        }if (name.isEmpty()){
                            displayEmpty += "Name is required to filled in too...\n";
                        }

                        if(!displayEmpty.isEmpty()){
                            JOptionPane.showMessageDialog(register, displayEmpty,"Input Error",JOptionPane.WARNING_MESSAGE);
                        }else{
                            createAccount.createAccount1(tpNumber, password, role,name, register);
                            register.dispose();
                        }
                    }
                });
                register.setVisible(true);
            }
        });
        page.setVisible(true);

    page.revalidate();
    page.repaint();
    }
    
}
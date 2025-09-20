package main;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;

public class createAccount {
    public static void createAccount1(String tpNumber, String password, String role, String name, JDialog register){
        String filePath = "users.txt";
        boolean duplicateTP = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            
            //Read line one by one if file got details
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields[0].trim().equals(tpNumber.trim())){
                    duplicateTP = true;
                    break;
                }
            }

            if(duplicateTP){
                JDialog duplicate = new JDialog(); //frame, "Duplicate User",true
                duplicate.setSize(250,150);
                duplicate.setLocationRelativeTo(register);
                duplicate.setLayout(null);

                JLabel errorMessage = new JLabel("This TP Number has been registered");
                errorMessage.setBounds(16,16,250,30);
                JButton ok = new JButton("OK");
                ok.setBounds(90,59,60,27);
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed (ActionEvent closeDialog){
                        duplicate.dispose();
                    }
                });
                duplicate.add(errorMessage);
                duplicate.add(ok);
                
                duplicate.setVisible(true);
            }else{
                JDialog success = new JDialog(); //frame, "Duplicate User",true
                success.setSize(250,150);
                success.setLocationRelativeTo(register);
                success.setLayout(null);

                JLabel completeMessage = new JLabel("Account registered complete");
                completeMessage.setBounds(43,16,200,30);
                JButton ok = new JButton("OK");
                ok.setBounds(90,59,60,27);
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed (ActionEvent closeDialog){
                        success.dispose();
                    }
                });
                success.add(completeMessage);
                success.add(ok);

                success.setVisible(true);



                //If no duplicate found
                try (BufferedWriter newUser = new BufferedWriter(new FileWriter(filePath,true))){
                    newUser.write(tpNumber + "," + password + "," + role + "," + name); //newUser
                    newUser.newLine(); // Move to a new line for the next entry (enter)

                }catch (IOException errorOutput) {
                    errorOutput.printStackTrace();
                }
            }
        }catch (IOException errorOutput) {
            errorOutput.printStackTrace();
        }
    }
}

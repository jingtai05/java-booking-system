package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class login {
    public static String login1(String tpNumber, String password, String[] userInfo) {
        String filePath = "users.txt";

        if (tpNumber.isEmpty() || password.isEmpty()) {
            return "blank";
        }

        
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            boolean emptyFile = true;
            boolean tpFound = false;

            while((line = reader.readLine()) != null){
                emptyFile = false;

                String[] userDetails = line.split(",");
                if (userDetails[0].equals(tpNumber)){
                    tpFound = true;

                    if (userDetails[1].equals(password)){
                        userInfo[0] = userDetails[2];
                        userInfo[1] = userDetails[3];
                        return"success";
                    }else {
                        return "not telly";
                    }
                }
            }
            
            if (emptyFile){
                return "null account";
            }

            if (!tpFound){
                return "fail";
            }
        }catch (IOException e){
            e.printStackTrace();
            return "error";
        }
        return "fail"; //for any unexpected
    }
}

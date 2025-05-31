package com.mycompany.tetris;

import java.io.*;

public class ScoreFile {
    private static String FILE_SCORE = "scorefile.txt";
    private static String FILE_USER = "userfile.txt";

    public String getUser(){
        File file = new File(FILE_USER);
        if (!file.exists()) {
            return null;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_USER))){
            String user = reader.readLine();
            if(user == null){
                return null;
            }
            return user;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUser(String user){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_USER))) {
            writer.write(user);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public int getScore(){
        File file = new File(FILE_SCORE);
        if (!file.exists()) {
            return 0;
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_SCORE))){
            String score = reader.readLine();
            if(score == null){
                return 0;
            }
            return Integer.parseInt(score);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setScore(int score){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_SCORE))) {
            writer.write(String.valueOf(score));
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}

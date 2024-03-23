package com.byteclass.noteslide.dao;
import com.byteclass.noteslide.model.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class NoteDAO implements DAO<Note> {

    private NoteDAO(){}

    private static NoteDAO instance;
    private Connection connection;


    public static NoteDAO getInstance()  {
        if (instance == null){
            synchronized (NoteDAO.class){

                instance = new NoteDAO();
                try{
                    instance.connection = DriverManager.getConnection("jdbc:sqlite:notes-db.db");
                    System.out.println("Database connection made successfully");
                    instance.createTable();
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return instance;
    }

    private void createTable() throws SQLException {
        PreparedStatement statement;

        statement = connection.prepareStatement(" CREATE TABLE IF NOT EXISTS notes_table " +
                 "(id INTEGER NOT NULL PRIMARY KEY," +
                 "topic TEXT NOT NULL, " +
                 "note TEXT NOT NULL )");

         statement.executeUpdate();

    }


    @Override
    public boolean addNote(Note value)
    {
        PreparedStatement statement;

        try{

            statement = connection.prepareStatement("INSERT INTO notes_table(topic, note) " +
                    "VALUES(?,?)");

            statement.setString(1, value.getTopic().toUpperCase());
            statement.setString(2, value.getNote());

            statement.executeUpdate();
            System.out.println("Notes inserted successfully");

            return true;
        }catch (SQLException e){
            System.out.println("@ addNote(): " + e.getMessage());
        }
        return false;
    }

    @Override
    public ObservableList<Note> getAllNotes()
    {
        PreparedStatement statement;
        ResultSet res;
        ObservableList<Note> noteObservableList = FXCollections.observableArrayList();

        try{
            statement = connection.prepareStatement("SELECT * FROM notes_table");
            res = statement.executeQuery();

            while (res.next()){

                String topic = res.getString("topic");
                String note = res.getString("note");

                Note noteInstance = new Note(topic, note);
                noteObservableList.add(noteInstance);
            }
            return noteObservableList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }
}

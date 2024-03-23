package com.byteclass.noteslide.dao;

import com.byteclass.noteslide.model.Note;
import javafx.collections.ObservableList;

public interface DAO<T> {

    boolean addNote(T value);
    ObservableList<Note> getAllNotes();
}

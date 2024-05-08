package ru.alex.databases;

import java.time.OffsetDateTime;

public class Databases {
    public static void main(String[] args) {
        Author author01 = new Author();
        author01.setUniqueName("aut001");

        Author author02 = new Author();
        author02.setUniqueName("aut002");

        AuthorDao authorDao = new AuthorDao();
        authorDao.createTable();
        authorDao.insert(author01);
        authorDao.insert(author02);
//
        Author authorFromDB = authorDao.getByUniqueName("aut001");
        System.out.println(authorFromDB.getId());
        System.out.println(authorFromDB.getUniqueName());
        System.out.println(authorFromDB.getRegisteredAt());
        Author authorFromDB2 = authorDao.getByUniqueName("aut002");
        System.out.println(authorFromDB2.getId());
//
        Note note = new Note();
        note.setTitle("Запись1");
        note.setText("Какой-то текст");
        note.setCreateAt(OffsetDateTime.now());
        note.setAuthor(authorFromDB);
//
        Note note2 = new Note();
        note2.setTitle("Запись2");
        note2.setText("Какой-то текст2");
        note2.setCreateAt(OffsetDateTime.now());
        note2.setAuthor(authorFromDB2);


        NotesDao notesDao = new NotesDao();
        notesDao.create();
        notesDao.insert(note);
        notesDao.insert(note2);
        Note noteFromDB = notesDao.getById(23L);
        System.out.println(noteFromDB.getId());
    }
}

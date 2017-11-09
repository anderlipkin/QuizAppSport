package com.quiz.quizappsport.interfaces;

import com.quiz.quizappsport.User;

public interface IDatabaseHandler {
    void addUser(User user);
    User getUser(int id);
    int getIdUser(String email, String pass);

    /*
    public List<User> getAllUsers();
    public int getUserCount();
    public int updateContact(User user);
    public void deleteContact(User user);
    public void deleteAll();
    */
}

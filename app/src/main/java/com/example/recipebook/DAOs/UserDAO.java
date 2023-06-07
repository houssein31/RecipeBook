package com.example.recipebook.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.recipebook.Entities.User;

@Dao
public interface UserDAO {

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM user WHERE email = :email")
    User getUserByEmail(String email);

    @Query("SELECT full_name FROM user WHERE email = :email")
    String getFullName(String email);

    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE email = :email)")
    boolean userExists(String email);

}

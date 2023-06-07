package com.example.recipebook;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.recipebook.DAOs.UserDAO;
import com.example.recipebook.Entities.User;

@Database(entities = {User.class}, version = 1)
public abstract class DatabaseConnect extends RoomDatabase {

    public abstract UserDAO getUserDAO();

    private static DatabaseConnect INSTANCE;

    public static DatabaseConnect getDBInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseConnect.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;

    }


}

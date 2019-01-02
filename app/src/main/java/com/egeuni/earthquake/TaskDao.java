package com.egeuni.earthquake;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    List<TaskEntry> loadAllTasks();

    @Insert
    void insertTask(TaskEntry taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntry taskEntry);

    @Delete
    void deleteTask(TaskEntry taskEntry);

    @Query("DELETE FROM task")
    void delete();

    /**
     *
     * TaskUser configuration ...*/


    @Query("SELECT * FROM user")
    List<TaskUser> loadAllProfiles();

    @Insert
    void insertUser(TaskUser taskUser);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(TaskUser taskUser);

    @Query("DELETE FROM user")
    void deleteAllUser();
}

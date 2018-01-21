package com.photos.kilopixels.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.offline.model.ModelConstants
import com.photos.kilopixels.model.entity.PhotosEntity

/**
 * Created by rahul on 20/1/18.
 */
@Database(entities = [(PhotosEntity::class)], version = 1, exportSchema = false)
abstract class LocalDatabase: RoomDatabase() {
    companion object {
        private var instance: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            if (instance == null) {
                @Synchronized
                if (instance == null) {
                    instance = Room
                            .databaseBuilder<LocalDatabase>(context.applicationContext, LocalDatabase::class.java, ModelConstants.DB_NAME)
                            .build()
                }
            }
            return instance as LocalDatabase
        }
    }

    abstract fun getDaoInterface(): DaoInterface
}
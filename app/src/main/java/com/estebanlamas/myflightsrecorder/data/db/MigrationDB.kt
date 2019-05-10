package com.estebanlamas.myflightsrecorder.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE positions ADD COLUMN speed REAL NOT NULL DEFAULT 0")
    }
}
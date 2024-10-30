package com.example.cp06

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "peliculas.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_PELICULAS = "peliculas"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_DESCRIPCION = "descripcion"
        const val COLUMN_IMAGEN = "imagen"
        const val COLUMN_DURACION = "duracion"
        const val COLUMN_ANO = "ano"
        const val COLUMN_PAIS = "pais"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_PELICULAS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_NOMBRE TEXT," +
                "$COLUMN_DESCRIPCION TEXT," +
                "$COLUMN_IMAGEN INTEGER," +
                "$COLUMN_DURACION INTEGER," +
                "$COLUMN_ANO INTEGER," +
                "$COLUMN_PAIS TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PELICULAS")
        onCreate(db)
    }
}

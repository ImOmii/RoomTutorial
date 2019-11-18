package com.example.architectureexample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDoa noteDoa();

    public static synchronized NoteDatabase getInstance(Context context) {

        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDoa noteDoa;

        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDoa = db.noteDoa();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDoa.insert(new Note("Title 1", "description 1", 1 ));
            noteDoa.insert(new Note("Title 2", "description 2", 2 ));
            noteDoa.insert(new Note("Title 3", "description 3", 3 ));
            noteDoa.insert(new Note("Title 4", "description 4", 4 ));

            return null;
        }
    }
}

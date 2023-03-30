package androidkotlin.trainning.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidkotlin.trainning.Note
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID

private val TAG = "storage"

fun persistNote(context: Context, note: Note){
    //if(note:filename == null || note.filename!!.isEmpty()){ note.filname == UUID.randomUUID().toString() + ".note" }
    if(TextUtils.isEmpty(note.filename)){
        note.filename = UUID.randomUUID().toString() + ".note"
    }

    val fileOutput = context.openFileOutput(note.filename, Context.MODE_PRIVATE)
    val outputStream = ObjectOutputStream(fileOutput)
    outputStream.writeObject(note)
    outputStream.close()

}

fun loadNote(context: Context) : MutableList<Note>{
    val notes = mutableListOf<Note>()

    val notesDir = context.filesDir
    for (filename in notesDir.list()) {
        var note = loadNote(context, filename)
        Log.i(TAG, "Loaded note $note")
        notes.add(note)
    }
    return notes
}

fun deleteNote(context: Context, note: Note){
    context.deleteFile(note.filename)
}



private fun loadNote(context: Context, filename: kotlin.String) : Note{
    val fileInput = context.openFileInput(filename)
    val inputStream = ObjectInputStream(fileInput)
    val note = inputStream.readObject() as Note
    inputStream.close()

    return note
}

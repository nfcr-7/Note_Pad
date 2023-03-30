package androidkotlin.trainning

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothCsipSetCoordinator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidkotlin.trainning.utils.deleteNote
import androidkotlin.trainning.utils.loadNote
import androidkotlin.trainning.utils.persistNote
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var notes: MutableList<Note>
    lateinit var adapter: NoteAdapter
    lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        findViewById<FloatingActionButton>(R.id.create_note_fab).setOnClickListener(this)

        notes = loadNote(this)
        adapter = NoteAdapter(notes, this)

        val recyclerView = findViewById(R.id.notes_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        coordinatorLayout = findViewById(R.id.coordinator_layout) as CoordinatorLayout
    }

//    private fun setSupportActionBar(toolbar: Toolbar) {
//
//    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        when(requestCode) {
            NoteDetailActivity.REQUEST_EDIT_NOTE -> processEditNoteResult(data)
        }
    }

    private fun processEditNoteResult(data: Intent){
        val noteIndex = data.getIntExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, -1)

        when (data.action){
            NoteDetailActivity.ACTION_SAVE_NOTE ->{
                val note = data.getParcelableExtra<Note>(NoteDetailActivity.EXTRA_NOTE)
                saveNote(note, noteIndex)
            }
            NoteDetailActivity.ACTION_DELETE_NOTE -> {
                deleteNote(noteIndex)
            }
        }


    }



    override fun onClick(view: View) {
        if (view.tag != null){
            showNoteDetail(view.tag as Int)
        } else{
            when(view.id){
                R.id.create_note_fab -> createNote()
            }
        }
    }

    fun createNote() {
        showNoteDetail(-1)
    }



    @SuppressLint("NotifyDataSetChanged")
    fun saveNote(note: Note?, noteIndex: Int) {
        persistNote(this, note!!)
        if(noteIndex < 0){
            notes.add(0 , note)
        }else{
            notes[noteIndex] = note
        }
        adapter.notifyDataSetChanged()
    }

    private fun deleteNote(noteIndex: Int) {
         if(noteIndex < 0){
             return
         }
        val note = notes.removeAt(noteIndex)
        deleteNote(this, note)
        adapter.notifyDataSetChanged()

        Snackbar.make(coordinatorLayout, "${note.title} supprime", Snackbar.LENGTH_SHORT)
            .show()
    }

     fun showNoteDetail(noteIndex: Int){
        val note = if(noteIndex < 0) Note() else notes[noteIndex]

        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE, note as Parcelable)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, noteIndex)
        startActivityForResult(intent, NoteDetailActivity.REQUEST_EDIT_NOTE)
    }
}
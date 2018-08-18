package mobi.mateam.androidthingtest1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var myRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFb()
        btnSend.setOnClickListener {
            myRef.setValue(etText.text.toString())
        }
    }

    private fun initFb() {
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        myRef = database.getReference("message")
    }

}

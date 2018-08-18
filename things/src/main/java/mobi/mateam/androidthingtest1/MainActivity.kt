package mobi.mateam.androidthingtest1

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener




/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */

val TAG = "MAIN TEST"
class MainActivity : Activity() {
    private  var ttsEngine: TextToSpeech? = null
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initTtsEngine()
        setTestSpeakButton()
        initFb()
    }

    private fun setTestSpeakButton() {
        btnSpeakButton.setOnClickListener {
            speak("Hello world! I'm a machine and I'm here to kill all of you")
        }
    }

    private fun speak(textToSpeech: String) {
        Log.d(TAG, "speak - text is: [$textToSpeech]")
        runOnUiThread {
            ttsEngine?.speak(textToSpeech, TextToSpeech.QUEUE_ADD, null, "testID")
        }
    }

    private fun initFb() {
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        myRef = database.getReference("message")
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)?: "Data changed but some error happened"
                Log.d(TAG, "Value is: $value")
                speak(value)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }


    private fun initTtsEngine() {
        ttsEngine = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsEngine?.language = Locale.US
               // ttsEngine?.setPitch(1f)
                //ttsEngine?.setSpeechRate(1f)
                speak("I'm ready to speak")
            } else {
                Log.w(TAG, "Could not open TTS Engine (onInit status=$status)")
                ttsEngine = null
            }
        })
    }
}

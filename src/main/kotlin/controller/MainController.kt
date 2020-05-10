package controller

import ThreadSpeech
import edu.cmu.sphinx.api.*
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.stage.Stage
import window.Settings
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger


class MainController {
    @FXML
    private lateinit var start: Button

    @FXML
    private lateinit var stop: Button

    @FXML
    private lateinit var settings: Button
    private val st = Settings()
    private val logger = Logger.getLogger("MainController")
    private val configuration : Configuration = Configuration()
    private lateinit var recognizer : LiveSpeechRecognizer
    private lateinit var thread : ThreadSpeech

    @FXML
    fun initialize() {
        stop.isDisable = true

        configuration.acousticModelPath = "resource:/edu/cmu/sphinx/models/en-us/en-us"
        configuration.dictionaryPath = "Vocabulary/vocabulary.dic"
        configuration.languageModelPath = "Vocabulary/vocabulary.lm"

        try {
            recognizer = LiveSpeechRecognizer(configuration)
        } catch ( e : IOException) {
            logger.log(Level.SEVERE, null, e)
        }
    }

    fun onClickStart(event: ActionEvent) {
        start()

        recognizer.startRecognition(true)
        thread = ThreadSpeech(recognizer)


        thread.start()
    }

    fun onClickStop(event: ActionEvent) {
        stop()

        thread.stop()
        recognizer.stopRecognition()
    }

    fun onClickSettings(event: ActionEvent) {
        st.show()
    }

    fun start() {
        start.isDisable = true
        stop.isDisable = false
    }

    fun stop() {
        start.isDisable = false
        stop.isDisable = true
    }
}
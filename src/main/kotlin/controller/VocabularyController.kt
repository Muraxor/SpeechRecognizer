package controller

import SwingKeyAdapter
import extensions.emptyFieldsInfo
import extensions.repeatKeyWordInfo
import extensions.successSavingInfo
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import singleton.KeyValueRepository
import utils.writeToFile
import java.awt.event.KeyEvent
import java.io.File

class VocabularyController  {
    @FXML
    private lateinit var keyTextField: TextField
    @FXML
    private lateinit var start_stop: Button
    @FXML
    private lateinit var save: Button
    @FXML
    private lateinit var valuesTextField: TextArea
    @FXML
    private lateinit var exit: Button

    private val globalKeyListener = GlobalKeyListenerExample()
    var valuesWithSeparator : String = ""
    val swingKeyAdapter = SwingKeyAdapter()
    var javaCodesList : ArrayList<Int> = arrayListOf()
    var codesWithSeparator : String = ""

    @FXML
    fun onClickExit(event: ActionEvent) =
        if(isNotRecording())
            (exit.scene.window as Stage).close()
        else extensions.stopRecording()
    @FXML
    fun onClickRecord(event: ActionEvent) {
        stopRecording()

        if(start_stop.text =="Старт")
            return

        try {
            GlobalScreen.registerNativeHook()
        }catch (ex : NativeHookException) {
            System.err.println("There was a problem registering the native hook.")
            System.err.println(ex.message)
        }
        GlobalScreen.addNativeKeyListener(globalKeyListener)
    }
    private fun stopRecording() {
        if(isNotRecording()) {
            start_stop.text = "Стоп"
            return
        }
        try {
            GlobalScreen.removeNativeKeyListener(globalKeyListener)
            GlobalScreen.unregisterNativeHook()
        } catch (ex: NativeHookException) {
            ex.printStackTrace()
        }
        start_stop.text = "Старт"
    }
    @FXML
    fun onClickSave(event: ActionEvent) {
        if(keyTextField.text.isEmpty() || valuesTextField.text.isEmpty())
            emptyFieldsInfo()

        if (!isNotRecording())
            stopRecording()

        validationVocab()

        keyTextField.clear()
        valuesTextField.clear()
    }

    inner class GlobalKeyListenerExample : NativeKeyListener {
        override fun nativeKeyPressed(e: NativeKeyEvent) {

            val code = NativeKeyEvent.getKeyText(e.keyCode)
            valuesWithSeparator+="$code;"
            valuesTextField.appendText("$code ")

            val javaCode = swingKeyAdapter.getJavaKeyEvent(e).keyCode
            javaCodesList.add(javaCode)
            codesWithSeparator+="$javaCode;"
        }

        override fun nativeKeyReleased(e: NativeKeyEvent) {
            println("Key Released: " + NativeKeyEvent.getKeyText(e.keyCode))
        }

        override fun nativeKeyTyped(e: NativeKeyEvent) {
            println("Key Typed: " + NativeKeyEvent.getKeyText(e.keyCode))
        }
    }

    private fun validationVocab() {
        val keyFile = File(KeyValueRepository.keyFileName)
        var exitFlag = false
        val key = keyTextField.text.toUpperCase()
        var emptyFile = true

        keyFile.forEachLine {
            if(it == key)
                exitFlag = true
            emptyFile = false
        }

        if(exitFlag) {
            repeatKeyWordInfo()
            return
        }
        writeToFile(keyFile,key,emptyFile)
        KeyValueRepository.addKey(key)

        val valueFile = File(KeyValueRepository.valueFileName)
        val value = valuesWithSeparator.toUpperCase()
        writeToFile(valueFile,value,emptyFile)
        KeyValueRepository.addValue(value)
        valuesWithSeparator = ""

        val codesFile = File(KeyValueRepository.codesFileName)
        writeToFile(codesFile,codesWithSeparator,emptyFile)
        KeyValueRepository.addCodesList(javaCodesList.toMutableList())
        javaCodesList.clear()
        codesWithSeparator = ""

        successSavingInfo()
       }

    private fun isNotRecording() : Boolean = start_stop.text == "Старт"
}


package controller

import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.stage.Stage
import network.RetrofitModule
import singleton.KeyValueRepository
import utils.rewrite
import window.Vocab
import java.io.File


class SettingsController {
    @FXML
    lateinit var add: Button
    @FXML
    lateinit var change: Button
    @FXML
    lateinit var delete: Button
    @FXML
    lateinit var exit: Button

    @FXML
    lateinit var  keysField: ListView<Any>

    @FXML
    lateinit var valuesField: ListView<Any>

    private lateinit var keyList : ArrayList<String>
    private lateinit var valueList : ArrayList<String>
    private lateinit var codesList : ArrayList<MutableList<Int>>
    private var retrofit = RetrofitModule()

    @FXML
    fun initialize() {
        refresh()
        keysField.setOnMouseClicked {
            valuesField.selectionModel.clearSelection()
            valuesField.selectionModel.selectIndices(keysField.selectionModel.selectedIndex)
        }

        valuesField.setOnMouseClicked {
            keysField.selectionModel.clearSelection()
            keysField.selectionModel.selectIndices(valuesField.selectionModel.selectedIndex)
        }
    }

    fun onClickAdd(event: ActionEvent) {
        Vocab().show()
    }

    fun onClickLoadVocab(event: ActionEvent) {
        retrofit.uploadFile()
        refresh()
    }

    fun onClickDelete(event: ActionEvent) {
        keyList.removeAt(keysField.selectionModel.selectedIndex)
        valueList.removeAt(valuesField.selectionModel.selectedIndex)
        codesList.removeAt(valuesField.selectionModel.selectedIndex)

        refresh()

        val codesStringList = changeToArrayString()

        rewrite(File(KeyValueRepository.keyFileName), keyList)
        rewrite(File(KeyValueRepository.valueFileName),valueList)
        rewrite(File(KeyValueRepository.codesFileName),codesStringList)
    }
    @FXML
    fun onClickExit(event: ActionEvent) = (exit.scene.window as Stage).close()

    private fun refresh() {
        keyList = KeyValueRepository.getKeys()
        valueList = KeyValueRepository.getValues()
        codesList = KeyValueRepository.getCodes()

        keysField.items = FXCollections.observableArrayList(keyList)
        valuesField.items = FXCollections.observableArrayList(valueList)

        keysField.refresh()
        valuesField.refresh()
    }

    fun changeToArrayString() : ArrayList<String> {
        val codesStringList : ArrayList<String> = arrayListOf()
        for(str : MutableList<Int> in codesList) {
            var string = ""
            for (item : Int in str) {
                string+="$item;"
            }
            codesStringList.add(string)
        }
        return codesStringList
    }
}
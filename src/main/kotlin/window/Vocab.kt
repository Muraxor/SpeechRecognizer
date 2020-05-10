package window

import controller.VocabController
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.ListView
import javafx.stage.Modality
import javafx.stage.Stage

class Vocab () {
    private val layout = "/fxml/vocab.fxml"
    private val loader = FXMLLoader()
    private val stage = Stage()

    init {
        loader.location = Vocab::class.java.getResource(layout)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.scene = Scene(FXMLLoader.load(loader.location))
    }

    fun show() {
        stage.show()
    }
}
package window

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage

class Settings  {
    private val layout = "/fxml/settings.fxml"
    private val loader = FXMLLoader()
    private val stage = Stage()
    init {
        loader.location = Settings::class.java.getResource(layout)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.scene = Scene(FXMLLoader.load(loader.location))
    }
    fun show() {
        stage.show()
    }
}
package application

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import java.awt.*
import java.awt.event.ActionEvent
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.SwingUtilities


class App : Application() {
    private val iconImageLoc = "/images/Ikonka.png"
    private val MAIN_TITLE = "Голосовой помощник"
    private lateinit var stage : Stage

    override fun start(primaryStage: Stage) {
        stage = primaryStage

        Platform.setImplicitExit(false)

        SwingUtilities.invokeLater(this::addAppToTray)

        val loader  = FXMLLoader()
        loader.location = App::class.java.getResource("/fxml/main.fxml")
        stage.scene = Scene(FXMLLoader.load(loader.location))
        stage.title = MAIN_TITLE

        stage.show()
    }
    fun show() {
        launch(App::class.java)
    }

    /**
     * Sets up a system tray icon for the application.
     */
    private fun addAppToTray() {
        try {
            // ensure awt toolkit is initialized.
            Toolkit.getDefaultToolkit()

            // app requires system tray support, just exit if there is no support.
            if (!SystemTray.isSupported()) {
                println("No system tray support, application exiting.")
                Platform.exit()
            }

            // set up a system tray icon.
            val tray = SystemTray.getSystemTray()
            val imageLoc = this::class.java.getResource(iconImageLoc)
            println(imageLoc)

            val image: Image = ImageIO.read(imageLoc)
            val trayIcon = TrayIcon(image)


            val exitItem = MenuItem("Exit")
            exitItem.addActionListener { event: ActionEvent? ->
                Platform.exit()

                tray.remove(trayIcon)
            }

            val popup = PopupMenu()
            popup.add(exitItem)
            trayIcon.popupMenu = popup

            // if the user double-clicks on the tray icon, show the main app stage.
            trayIcon.addActionListener {
                Platform.runLater { showStage() }
            }

            // add the application tray icon to the system tray.
            tray.add(trayIcon)
        } catch (e: AWTException) {
            println("Unable to init system tray")
            e.printStackTrace()
        } catch (e: IOException) {
            println("Unable to init system tray")
            e.printStackTrace()
        }
    }

    /**
     * Shows the application stage and ensures that it is brought ot the front of all stages.
     */
    private fun showStage() {
        stage.show()
        stage.toFront()
    }
}
package extensions

import javafx.scene.control.Alert

private val alert  = Alert(Alert.AlertType.INFORMATION)
private val errorSaving = "Ошибка сохранения"
private val errorAdding = "Ошибка добавления"
private val successSaving = "Успех"
private val stopRecording = "Ошибка"

fun emptyFieldsInfo() {
    alert.title = errorSaving
    alert.contentText = "Должны быть заполнены оба поля!"
    alert.showAndWait()
}
fun repeatKeyWordInfo() {
    alert.title = errorAdding
    alert.contentText = "Такое слово уже есть в словаре"
    alert.showAndWait()
}
fun successSavingInfo() {
    alert.title = successSaving
    alert.contentText = "Слово добавлено в словарь"
    alert.showAndWait()
}
fun stopRecording() {
    alert.title = stopRecording
    alert.contentText = "Остановите запись!"
    alert.showAndWait()
}

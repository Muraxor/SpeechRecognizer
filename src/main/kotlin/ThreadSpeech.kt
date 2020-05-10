import edu.cmu.sphinx.api.LiveSpeechRecognizer
import utils.RobotWriter
import java.util.logging.Level
import java.util.logging.Logger

class ThreadSpeech (private val recognizer: LiveSpeechRecognizer) : Thread(){
    private var result = ""
    private val logger = Logger.getLogger("ThreadSpeech")

    override fun run() {
        val writer = RobotWriter()

        logger.log(Level.INFO,"Можно говорить...")
        while(true) {
            logger.log(Level.INFO, "Старт")
            val speechResult = recognizer.result
            if(speechResult != null) {
                result = speechResult.hypothesis
                writer.write(result)
                println("Ты сказал: $result")
            } else
                logger.log(Level.INFO,"Не понял")
        }
    }
}
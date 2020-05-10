package utils

import singleton.KeyValueRepository
import java.awt.Robot

class RobotWriter
{
    private val robot = Robot()
    private var keyList = KeyValueRepository.getKeys()
    private var codesList = KeyValueRepository.getCodes()

    fun write(expression: String) {
        val index = keyList.indexOf(expression)
        if(index==-1)
            return
        val list = codesList[index]
        for (item in list) {
            robot.keyPress(item)
            robot.keyRelease(item)
        }
    }
}
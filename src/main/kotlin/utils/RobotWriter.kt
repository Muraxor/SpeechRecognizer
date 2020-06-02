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
        var indexRelease = 0
        if(index==-1)
            return
        val list = codesList[index]

        if (list.contains(0))
            return

        for (i in list.indices) {

            when(list[i]) {
                16,17,18 -> {
                    robot.keyPress(list[i])
                    indexRelease+=1
                }
                else -> {
                    robot.keyPress(list[i])
                    robot.keyRelease(list[i])
                    when(indexRelease) {
                        1-> robot.keyRelease(list[i-1])
                        2 -> {
                            robot.keyRelease(list[i-2])
                            robot.keyRelease(list[i-1])
                        }
                        else ->{}
                    }
                    indexRelease = 0
                }
            }

            if(indexRelease>0 && i == list.size-1 )
                while(indexRelease!=0) {
                    robot.keyRelease(list[i+1-indexRelease])
                    indexRelease -= 1
                }

        }
    }
}

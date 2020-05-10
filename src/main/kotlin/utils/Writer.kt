package utils

import java.io.File
import java.io.FileOutputStream

fun writeToFile(fileName : File, content : String, emptyFile : Boolean) {
    FileOutputStream(fileName,true).bufferedWriter().use { writer->
        writer.apply {
            if(!emptyFile)
                newLine()
            append(content)
            flush()
        }
    }.close()
}

fun <T>rewrite(fileName: File, list: ArrayList<T>) {
    val bufferedWriter = FileOutputStream(fileName,false).bufferedWriter()
    val lastStr = list.last().toString()
    bufferedWriter.use { writer->
        writer.apply {
            for (items : T in list) {
                write(items.toString())
                if(items != lastStr)
                    newLine()
            }
            flush()
        }
    }.close()

}
package singleton

import java.io.File
import kotlin.collections.ArrayList

object KeyValueRepository {
    private val keyList = ArrayList<String>()
    private val valueList = ArrayList<String>()
    private var codesList : ArrayList<MutableList<Int>> = arrayListOf()

    val keyFileName = "Vocabulary/keys.txt"
    val valueFileName = "Vocabulary/values.txt"
    val codesFileName = "Vocabulary/codes.txt"

    init {
        readKeysFile()
        readValuesFile()
        readCodesFile()
    }

    private fun readKeysFile() {
        File(keyFileName).forEachLine { keyList.add(it)  }
    }

    private fun readValuesFile() {
        File(valueFileName).forEachLine { valueList.add(it) }
    }

    private fun readCodesFile() {
        File(codesFileName).forEachLine {
            val array : List<String> = it.split(';')
            val list : MutableList<Int> = mutableListOf()
            for(item : String in array.dropLast(1)) {
                list.add(item.toInt())
            }
            codesList.add(list)
        }
    }

    fun getValues() = valueList
    fun getKeys() = keyList
    fun getCodes() = codesList

    fun addKey(item : String) {
        keyList.add(item)
    }

    fun addValue(item : String) {
        valueList.add(item)
    }

    fun addCodesList(item : MutableList<Int>) {
        codesList.add(item)
    }
}
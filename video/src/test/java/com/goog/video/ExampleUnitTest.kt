package com.goog.video

import org.junit.Test

import org.junit.Assert.*
import java.io.File
import java.io.FileWriter

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val inputDir = File("E:\\Code\\AndroidProject\\video\\video\\src\\main\\assets\\filters")
        val outDir = File("E:\\Code\\AndroidProject\\video\\video\\src\\main\\java\\com\\goog\\video\\filter")
        val fileMap = mutableMapOf<String, MutableList<File>>()
        for (file in inputDir.listFiles() ?: arrayOf()) {
            if (file.isDirectory) {
                continue
            }
            val name = file.name
            val index = name.lastIndexOf(".")
            val key = name.substring(0, index)
            val list = fileMap[key] ?: mutableListOf()
            fileMap[key] = list
            list.add(file)
        }
        for (entry in fileMap.entries) {
            val fileName = "GL${entry.key.capitalize()}Filter.kt"
            val clsName = "GL${entry.key.capitalize()}Filter"
            val file = File(outDir, fileName)
            if (file.exists()) {
                continue
            }

            file.createNewFile()
            val writer = FileWriter(file, false)

            writer.write(
                "package com.goog.video.filter\n" +
                        "\n" +
                        "import com.goog.video.filter.core.GLFilter\n" +
                        "import com.goog.video.utils.loadFilterFromAsset\n"
            )
            val s = "class $clsName :GLFilter() {\n"
            writer.write(s)
            for (item in entry.value) {
                val endName = item.name
                if (endName.endsWith(".fsh")) {
                    writer.write(
                        "    override fun getFragmentShader(): String {\n" +
                                "        return loadFilterFromAsset(\"${endName}\")\n" +
                                "    }\n"
                    )
                } else if (endName.endsWith(".vsh")) {
                    writer.write(
                        " override fun getVertexShader(): String {\n" +
                                "        return loadFilterFromAsset(\"${endName}\")\n" +
                                "    }"
                    )
                }
            }
            writer.write("}")
            writer.flush()
            writer.close()
        }

    }
}
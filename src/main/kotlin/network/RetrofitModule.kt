package network

import extensions.errorRefreshVocabulary
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*


class RetrofitModule {
    private val retrofit : Retrofit = RetrofitModule()
    private lateinit var url : String
    private val service = retrofit.create(UploadService::class.java)
    fun RetrofitModule() = Retrofit.Builder()
            .baseUrl("http://www.speech.cs.cmu.edu/cgi-bin/tools/lmtool/run/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun uploadFile() {
        val file = File("Vocabulary/keys.txt")
        val fileUri = file.toURI()
        val requestFile =  RequestBody.create(
            MediaType.parse(fileUri.toString()),
            file
        )

        val body = MultipartBody.Part.createFormData(
            "corpus",
            file.name,
            requestFile
        )

        val descriptionString = "description"
        val description = RequestBody.create(
            MultipartBody.FORM,
            descriptionString
        )

        val call = service.upload(description,body)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                errorRefreshVocabulary()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var name = ""
                response.body()?.charStream()?.forEachLine {
                    if(it.startsWith("<p>The base name for this set is ")) {
                        name =it.substring(36,40)
                    }
                }
                if(name == "") {
                    return
                }
                url = response.raw().request().url().toString()
                downloadFile(name,".dic")
                downloadFile(name,".lm")
            }

        })
    }
    fun downloadFile(fileName : String,type : String) {
        val callD = service.download("$url$fileName$type")
        callD.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                response.body()?.let {
                    writeResponseBodyToDisk(it,"Vocabulary/vocabulary$type")
                }
            }
        })

    }
    fun writeResponseBodyToDisk(body : ResponseBody,fileName: String) : Boolean {
        try {
            val file = File(fileName)

            var inputStream : InputStream? = null
            var outputStream : OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body.contentLength()
                var fileSizeDownload = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)

                while(true) {
                    val read = inputStream.read(fileReader)

                    if(read ==-1)
                        break

                    outputStream.write(fileReader,0,read)

                    fileSizeDownload+=read
                }

                outputStream.flush()

                return true
            }catch (e : IOException) {
                return false
            }finally {
                inputStream?.close()
                outputStream?.close()
            }
        }catch (e : IOException) {
            return false
        }
    }
}
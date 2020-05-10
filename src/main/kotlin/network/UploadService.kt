package network

import com.sun.org.apache.xml.internal.resolver.helpers.FileURL
import okhttp3.HttpUrl
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.net.URI

interface UploadService {
    @Multipart
    @POST("lmtool/run")
    fun upload(
        @Part("formtype") formtype : RequestBody,
        @Part file : MultipartBody.Part
    ) : Call<ResponseBody>

    @GET
    fun download(
        @Url fileURL: String
    ) : Call<ResponseBody>
}
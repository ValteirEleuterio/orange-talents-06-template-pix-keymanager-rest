package br.com.zupacademy.valteir.pix.remove

import br.com.zupacademy.valteir.RemoveChavePixRequest
import br.com.zupacademy.valteir.RemovePixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import java.util.*

@Controller
class RemoveChavePixController(
    private val grpcClient: RemovePixServiceGrpc.RemovePixServiceBlockingStub
) {

    @Delete("/api/v1/clientes/{idTitular}/pix/{pixId}")
    fun remove(@PathVariable idTitular: UUID, @PathVariable pixId: UUID): HttpResponse<Any> {

        grpcClient.remove(
            RemoveChavePixRequest.newBuilder()
                .setIdTitular(idTitular.toString())
                .setPixId(pixId.toString())
                .build()
        )

        return HttpResponse.ok()
    }
}
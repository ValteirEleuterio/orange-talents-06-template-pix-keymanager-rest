package br.com.zupacademy.valteir.pix.registra

import br.com.zupacademy.valteir.PixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import java.util.*
import javax.validation.Valid

@Controller
@Validated
class RegistraChavePixController(
    private val grpcClient: PixServiceGrpc.PixServiceBlockingStub
) {

    @Post("/api/v1/clientes/{idTitular}/pix")
    fun registra(@PathVariable idTitular: UUID, @Body @Valid request: RegistraChavePixRequest): HttpResponse<Any> {
        val pixRequest = request.toPixRequest(idTitular)

        val pixResponse = grpcClient.cadastrar(pixRequest)

        val uri = UriBuilder.of("/api/v1/clientes/{idTitular}/pix/{id}")
            .expand(mutableMapOf("id" to pixResponse.pixId, "idTitular" to idTitular))

        return HttpResponse.created(uri)
    }
}
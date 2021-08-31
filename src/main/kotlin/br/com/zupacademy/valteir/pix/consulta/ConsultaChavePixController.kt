package br.com.zupacademy.valteir.pix.consulta

import br.com.zupacademy.valteir.ConsultaChavePixRequest
import br.com.zupacademy.valteir.ConsultaPixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import java.util.*

@Controller
class ConsultaChavePixController(
    private val grpcClient: ConsultaPixServiceGrpc.ConsultaPixServiceBlockingStub
) {

    @Get("/api/v1/clientes/{idTitular}/pix/{pixId}")
    fun consulta(@PathVariable idTitular: UUID, @PathVariable pixId: UUID) : HttpResponse<ChavePixResponse> {

        val resposta = grpcClient.consulta(ConsultaChavePixRequest.newBuilder()
            .setPixId(ConsultaChavePixRequest.FiltroPorPixId.newBuilder()
                .setIdTitular(idTitular.toString())
                .setPixId(pixId.toString())
                .build()
            ).build()
        )

        return HttpResponse.ok(ChavePixResponseConverter().convert(resposta))
    }
}
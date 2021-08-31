package br.com.zupacademy.valteir.pix.consulta

import br.com.zupacademy.valteir.ConsultaChavePixRequest
import br.com.zupacademy.valteir.ConsultaPixServiceGrpc
import br.com.zupacademy.valteir.ListaChavesPixRequest
import br.com.zupacademy.valteir.ListaPixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import java.util.*

@Controller
class ConsultaChavePixController(
    private val consultaChavePixClientGrpc: ConsultaPixServiceGrpc.ConsultaPixServiceBlockingStub,
    private val listaChavesPixClientGrpc: ListaPixServiceGrpc.ListaPixServiceBlockingStub,
) {

    @Get("/api/v1/clientes/{idTitular}/pix/{pixId}")
    fun consulta(@PathVariable idTitular: UUID, @PathVariable pixId: UUID) : HttpResponse<DetalheChavePixResponse> {

        val resposta = consultaChavePixClientGrpc.consulta(ConsultaChavePixRequest.newBuilder()
            .setPixId(ConsultaChavePixRequest.FiltroPorPixId.newBuilder()
                .setIdTitular(idTitular.toString())
                .setPixId(pixId.toString())
                .build()
            ).build()
        )

        return HttpResponse.ok(ChavePixResponseConverter().convert(resposta))
    }

    @Get("/api/v1/clientes/{idTitular}/pix")
    fun lista(@PathVariable idTitular: UUID) : HttpResponse<List<ChavePixResponse>> {

        val resposta = listaChavesPixClientGrpc.lista(
            ListaChavesPixRequest.newBuilder()
                .setIdTitular(idTitular.toString())
                .build()
        )

        return HttpResponse.ok(resposta.chavesList.map(::ChavePixResponse))
    }
}
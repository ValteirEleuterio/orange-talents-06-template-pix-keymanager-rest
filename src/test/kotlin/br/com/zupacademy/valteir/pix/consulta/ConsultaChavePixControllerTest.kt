package br.com.zupacademy.valteir.pix.consulta

import br.com.zupacademy.valteir.*
import br.com.zupacademy.valteir.outros_sistemas.GrpcClientFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*

@MicronautTest
internal class ConsultaChavePixControllerTest {


    @field:Inject
    lateinit var consultaChavePixClientGrpc: ConsultaPixServiceGrpc.ConsultaPixServiceBlockingStub

    @field:Inject
    lateinit var listaChavePixClientGrpc: ListaPixServiceGrpc.ListaPixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient


    @Test
    fun `deve consultar uma chave pix`() {
        //cenario
        val idTitular = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = ConsultaChavePixResponse.newBuilder()
            .setClientId(idTitular)
            .setPixId(pixId)
            .setChave(
                ConsultaChavePixResponse.ChavePix.newBuilder()
                    .setTipo(TipoChave.EMAIL)
                    .setChave("teste@email.com")
                    .setCriadaEm(Timestamp.newBuilder().setNanos(111).setSeconds(111).build())
                    .setConta(
                        ConsultaChavePixResponse.ChavePix.Conta.newBuilder()
                            .setTipo(TipoConta.CONTA_CORRENTE)
                            .setInstituicao("ITAU")
                            .setCpfTitular("111-111-111-11")
                            .setNomeTitular("JOAOZINHO")
                            .setAgencia("0111")
                            .setNumeroDaConta("124433")
                            .build()
                    )
                    .build()
            )
            .build()

        given(consultaChavePixClientGrpc.consulta(Mockito.any())).willReturn(respostaGrpc)

        //acao
        val request: HttpRequest<Any> = HttpRequest.GET("/api/v1/clientes/$idTitular/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        //validacao
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        with(response.body() as DetalheChavePixResponse) {
            assertEquals(idTitular, this.idTitular)
            assertEquals(pixId, this.pixId)
        }
    }


    @Test
    fun `deve listar chaves pix`() {
        //cenario
        val idTitular = UUID.randomUUID().toString()
        val respostaGrpc = ListaChavesPixResponse.newBuilder()
            .addAllChaves(
                mutableListOf(
                    ListaChavesPixResponse.ChavePix.newBuilder()
                        .setPixId(UUID.randomUUID().toString())
                        .setIdTitular(idTitular)
                        .setTipo(TipoChave.EMAIL)
                        .setChave("teste@email.com")
                        .setTipoConta(TipoConta.CONTA_CORRENTE)
                        .setCriadaEm(
                            Timestamp.newBuilder().setSeconds(LocalDateTime.now().second.toLong())
                                .setNanos(LocalDateTime.now().nano)
                        )
                        .build(),
                    ListaChavesPixResponse.ChavePix.newBuilder()
                        .setPixId(UUID.randomUUID().toString())
                        .setIdTitular(idTitular)
                        .setTipo(TipoChave.CELULAR)
                        .setChave("+44998605172")
                        .setTipoConta(TipoConta.CONTA_CORRENTE)
                        .setCriadaEm(
                            Timestamp.newBuilder().setSeconds(LocalDateTime.now().second.toLong())
                                .setNanos(LocalDateTime.now().nano)
                        )
                        .build()
                    )
            )
            .build()

        given(listaChavePixClientGrpc.lista(Mockito.any())).willReturn(respostaGrpc)

        //acao
        val request: HttpRequest<Any> = HttpRequest.GET("/api/v1/clientes/${idTitular}/pix")
        val resposta = client.toBlocking().exchange(request, List::class.java)

        //validacao
        assertEquals(HttpStatus.OK, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(2, resposta.body()!!.size)
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubConsultaChavePixMock() = Mockito.mock(ConsultaPixServiceGrpc.ConsultaPixServiceBlockingStub::class.java)

        @Singleton
        fun stubListaChavePixMock() = Mockito.mock(ListaPixServiceGrpc.ListaPixServiceBlockingStub::class.java)
    }
}
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*

@MicronautTest
internal class ConsultaChavePixControllerTest {


    @field:Inject
    lateinit var grpcClient: ConsultaPixServiceGrpc.ConsultaPixServiceBlockingStub

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
            .setChave(ConsultaChavePixResponse.ChavePix.newBuilder()
                .setTipo(TipoChave.EMAIL)
                .setChave("teste@email.com")
                .setCriadaEm(Timestamp.newBuilder().setNanos(111).setSeconds(111).build())
                .setConta(ConsultaChavePixResponse.ChavePix.Conta.newBuilder()
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

        given(grpcClient.consulta(Mockito.any())).willReturn(respostaGrpc)

        //acao
        val request: HttpRequest<Any> = HttpRequest.GET("/api/v1/clientes/$idTitular/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        //validacao
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        with(response.body() as ChavePixResponse) {
            assertEquals(idTitular, this.idTitular)
            assertEquals(pixId, this.pixId)
        }
    }


    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(ConsultaPixServiceGrpc.ConsultaPixServiceBlockingStub::class.java)
    }
}
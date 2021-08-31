package br.com.zupacademy.valteir.pix.remove

import br.com.zupacademy.valteir.RemoveChavePixRequest
import br.com.zupacademy.valteir.RemoveChavePixResponse
import br.com.zupacademy.valteir.RemovePixServiceGrpc
import br.com.zupacademy.valteir.outros_sistemas.GrpcClientFactory
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
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

@MicronautTest
internal class RemoveChavePixControllerTest {


    @field:Inject
    lateinit var removeStub : RemovePixServiceGrpc.RemovePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient


    @Test
    fun `deve remover uma chave pix`() {
        //cenario
        val idTitular = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = RemoveChavePixResponse.newBuilder().setIdTitular(idTitular).setPixId(pixId).build()

        given(removeStub.remove(Mockito.any())).willReturn(respostaGrpc)

        //acao
        val request: HttpRequest<Any> = HttpRequest.DELETE("/api/v1/clientes/$idTitular/pix/$pixId")
        val resposta = client.toBlocking().exchange(request, Any::class.java)

        //validacao
        assertEquals(HttpStatus.OK, resposta.status)
    }



    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = Mockito.mock(RemovePixServiceGrpc.RemovePixServiceBlockingStub::class.java)
    }
}
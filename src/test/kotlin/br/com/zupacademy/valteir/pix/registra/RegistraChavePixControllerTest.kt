package br.com.zupacademy.valteir.pix.registra

import br.com.zupacademy.valteir.PixResponse
import br.com.zupacademy.valteir.PixServiceGrpc
import br.com.zupacademy.valteir.TipoChave
import br.com.zupacademy.valteir.TipoConta
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
import java.util.*

@MicronautTest
internal class RegistraChavePixControllerTest {


    @field:Inject
    lateinit var registraStub : PixServiceGrpc.PixServiceBlockingStub;

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve cadastrar uma chave pix`() {
        val idTitular = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val responseGrpc = PixResponse.newBuilder()
            .setPixId(pixId)
            .build()


        given(registraStub.cadastrar(Mockito.any())).willReturn(responseGrpc)

        val chavePixRequest = RegistraChavePixRequest(
            TipoChave.EMAIL.toString(),
            "teste@email.com",
            TipoConta.CONTA_CORRENTE.toString()
        )

        val request = HttpRequest.POST("/api/v1/clientes/${idTitular}/pix", chavePixRequest)
        val response = client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(pixId))
    }


    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(PixServiceGrpc.PixServiceBlockingStub::class.java)
    }




}
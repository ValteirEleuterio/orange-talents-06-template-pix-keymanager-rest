package br.com.zupacademy.valteir.compartilhado.handlers

import com.google.rpc.BadRequest
import com.google.rpc.Code
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import jakarta.inject.Singleton
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@Singleton
internal class ExceptionHandlersTest {

    val requestGenerica = HttpRequest.GET<Any>("/")

    val statusRuntimeExceptionHandler = ExceptionHandlers().statusRuntimeExceptionHandler()
    
    @Nested
    inner class StatusRuntimeExceptionHandler {

        @Test
        fun `deve retornar 422 quando statusException for already exists`() {
            //cenario
            val mensagem = "já existente"
            val alreadyExists = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(mensagem))

            //acao
            val resposta = statusRuntimeExceptionHandler.handle(requestGenerica, alreadyExists)

            //validacao
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.status)
            assertNotNull(resposta.body())
            assertEquals(mensagem, resposta.body()!!.erro)
        }

        @Test
        fun `deve retornar 400 quando statusException for failed precondition`() {
            //cenario
            val mensagem = "Falha ao registrar a chave Pix no Banco Central do Brasil (BCB)"
            val failedPrecondition = StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(mensagem))

            //acao
            val resposta = statusRuntimeExceptionHandler.handle(requestGenerica, failedPrecondition)

            //validacao
            assertEquals(HttpStatus.BAD_REQUEST, resposta.status)
            assertNotNull(resposta.body())
            assertEquals(mensagem, resposta.body()!!.erro)
        }

        @Test
        fun `deve retornar 400 quando statusException for invalid argument`() {
            //cenario
            val details = BadRequest.newBuilder()
                .addAllFieldViolations(
                    listOf(
                        BadRequest.FieldViolation.newBuilder()
                            .setField("idTitular")
                            .setDescription("não deve estar em branco")
                            .build(),
                        BadRequest.FieldViolation.newBuilder()
                            .setField("valorChave")
                            .setDescription("não deve estar em branco")
                            .build()
                    )
                )
                .build()

            val statusProto = com.google.rpc.Status.newBuilder()
                .setCode(Code.INVALID_ARGUMENT_VALUE)
                .setMessage("Dados inválidos")
                .addDetails(com.google.protobuf.Any.pack(details))
                .build()

            val statusRuntimeException = StatusProto.toStatusRuntimeException(statusProto)

            val invalidArgument = statusRuntimeException.status.asRuntimeException(statusRuntimeException.trailers)

            //acao
            val resposta = statusRuntimeExceptionHandler.handle(requestGenerica, invalidArgument)

            //validacao
            assertEquals(HttpStatus.BAD_REQUEST, resposta.status)
            assertNotNull(resposta.body())

            assertThat(resposta.body()!!.erros, containsInAnyOrder(
                "idTitular não deve estar em branco",
                "valorChave não deve estar em branco"
            ))
        }


        @Test
        fun `deve retornar 500 quando statusException for um erro desconhecido`() {
            //cenario
            val mensagem = "server erro"
            val erroDesconhecido = StatusRuntimeException(Status.UNKNOWN.withDescription(mensagem))

            //acao
            val resposta = statusRuntimeExceptionHandler.handle(requestGenerica, erroDesconhecido)

            //validacao
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resposta.status)
            assertNotNull(resposta.body())
            assertEquals(mensagem, resposta.body.get().erro)
        }

    }

}
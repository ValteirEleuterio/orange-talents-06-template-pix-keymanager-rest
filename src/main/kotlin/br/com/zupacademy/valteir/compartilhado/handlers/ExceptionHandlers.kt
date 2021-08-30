package br.com.zupacademy.valteir.compartilhado.handlers

import com.google.rpc.BadRequest
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.http.HttpResponse
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.validation.ConstraintViolationException

@Factory
class ExceptionHandlers {

    @Bean
    fun constraintViolationExceptionHandler() : ExceptionHandler<ConstraintViolationException, HttpResponse<ErroResponse>> {
        return ExceptionHandler { _, e ->
            val erros = e.constraintViolations.map {
                it.propertyPath.last().name + " " + it.message
            }

            HttpResponse.badRequest(ErroResponse.comErros(erros))
        }
    }

    @Bean
    fun statusRuntimeExceptionHandler() : ExceptionHandler<StatusRuntimeException, HttpResponse<ErroResponse>> {
        return ExceptionHandler { _, e ->
            when(e.status.code) {
                Status.ALREADY_EXISTS.code ->
                    HttpResponse.unprocessableEntity<Any>().body(ErroResponse.comErro(e.status.description!!))
                Status.FAILED_PRECONDITION.code ->
                    HttpResponse.badRequest(ErroResponse.comErro(e.status.description!!))
                Status.INVALID_ARGUMENT.code -> {
                    val statusProto = StatusProto.fromThrowable(e)
                        ?: return@ExceptionHandler HttpResponse.badRequest(ErroResponse.comErro(e.status.description!!))

                    val details = statusProto.detailsList[0].unpack(BadRequest::class.java)
                    val erros = details.fieldViolationsList.map {
                        "${it.field}  ${it.description}"
                    }

                    HttpResponse.badRequest(ErroResponse.comErros(erros))
                }
                else -> HttpResponse.serverError(ErroResponse.comErro(e.status.description!!))
            }
        }
    }

}
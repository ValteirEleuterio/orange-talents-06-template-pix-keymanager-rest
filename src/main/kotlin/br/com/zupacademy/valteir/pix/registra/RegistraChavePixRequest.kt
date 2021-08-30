package br.com.zupacademy.valteir.pix.registra

import br.com.zupacademy.valteir.PixRequest
import br.com.zupacademy.valteir.TipoChave
import br.com.zupacademy.valteir.TipoConta
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Introspected
data class RegistraChavePixRequest(
    @field:NotBlank
    @field:Pattern(regexp = "CPF|EMAIL|CELULAR|ALEATORIA")
    val tipoChave: String?,
    @field:Size(max = 77)
    val chave: String?,
    @field:NotBlank
    @field:Pattern(regexp = "CONTA_CORRENTE|CONTA_POUPANCA")
    val tipoConta: String?,
) {


    fun toPixRequest(idTitular: UUID) : PixRequest =
        PixRequest.newBuilder()
            .setIdTitular(idTitular.toString())
            .setValorChave(chave ?: "")
            .setTipo(TipoChave.valueOf(tipoChave!!))
            .setConta(TipoConta.valueOf(tipoConta!!))
            .build()
}

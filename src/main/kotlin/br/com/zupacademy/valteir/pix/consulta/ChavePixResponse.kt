package br.com.zupacademy.valteir.pix.consulta

import br.com.zupacademy.valteir.TipoChave
import br.com.zupacademy.valteir.TipoConta
import com.fasterxml.jackson.annotation.JsonFormat
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected
data class ChavePixResponse(
    val idTitular: String,
    val pixId: String,
    val chave: ChavePix,
)

@Introspected
data class ChavePix(
    val tipo: TipoChave,
    val chave: String,
    val conta: Conta,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val criadaEm: LocalDateTime
)

@Introspected
data class Conta(
   val tipoConta: TipoConta,
   val instituicao: String,
   val nomeTitular: String,
   val cpfTitular: String,
   val agencia: String,
   val numeroDaConta: String,
)
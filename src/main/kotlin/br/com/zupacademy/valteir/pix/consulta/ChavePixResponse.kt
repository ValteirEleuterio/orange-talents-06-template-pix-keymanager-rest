package br.com.zupacademy.valteir.pix.consulta

import br.com.zupacademy.valteir.ListaChavesPixResponse
import br.com.zupacademy.valteir.TipoChave
import br.com.zupacademy.valteir.TipoConta
import com.fasterxml.jackson.annotation.JsonFormat
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ChavePixResponse(chave: ListaChavesPixResponse.ChavePix) {

    val pixId: String = chave.pixId
    val idTitular: String = chave.idTitular
    val chave: String = chave.chave
    val tipoChave: TipoChave = chave.tipo
    val tipoConta: TipoConta = chave.tipoConta
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val criadaEm: LocalDateTime = chave.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
}
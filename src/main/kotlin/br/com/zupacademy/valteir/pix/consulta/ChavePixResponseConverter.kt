package br.com.zupacademy.valteir.pix.consulta

import br.com.zupacademy.valteir.ConsultaChavePixResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class ChavePixResponseConverter {

    fun convert(consultaChavePixResponse: ConsultaChavePixResponse): ChavePixResponse {
        return with(consultaChavePixResponse) {
            val dadosConta = Conta(
                chave.conta.tipo,
                chave.conta.instituicao,
                chave.conta.nomeTitular,
                chave.conta.cpfTitular,
                chave.conta.agencia,
                chave.conta.numeroDaConta
            )

            val criadaEm = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(chave.criadaEm.seconds, chave.criadaEm.nanos.toLong()),
                ZoneOffset.UTC
            )

            val dadosChave = ChavePix(
                chave.tipo,
                chave.chave,
                dadosConta,
                criadaEm
            )

            ChavePixResponse(
                clientId,
                pixId,
                dadosChave
            )
        }
    }
}
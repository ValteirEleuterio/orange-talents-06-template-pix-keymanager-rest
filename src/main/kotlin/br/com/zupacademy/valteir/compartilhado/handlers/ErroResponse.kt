package br.com.zupacademy.valteir.compartilhado.handlers

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class ErroResponse {
    var erro: String? = null
        private set
    var erros: List<String>? = null
        private set

    companion object {

        fun comErro(mensagem: String) : ErroResponse {
            val erro = ErroResponse()
            erro.erro = mensagem
            return erro
        }

        fun comErros(mensagens: List<String>) : ErroResponse {
            val erro = ErroResponse()
            erro.erros = mensagens
            return erro
        }

    }

}

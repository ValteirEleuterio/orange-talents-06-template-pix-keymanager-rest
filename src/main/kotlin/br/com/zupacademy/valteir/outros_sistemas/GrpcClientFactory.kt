package br.com.zupacademy.valteir.outros_sistemas

import br.com.zupacademy.valteir.PixServiceGrpc
import br.com.zupacademy.valteir.RemovePixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory
class GrpcClientFactory(
    @GrpcChannel("keymanager") val channel: ManagedChannel
) {

    @Singleton
    fun registraChave() = PixServiceGrpc.newBlockingStub(channel)


    @Singleton
    fun removeChave() = RemovePixServiceGrpc.newBlockingStub(channel)
}
package br.com.zupacademy.valteir.outros_sistemas

import br.com.zupacademy.valteir.PixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun keymanagerClientStub(@GrpcChannel("keymanager") channel: ManagedChannel) :PixServiceGrpc.PixServiceBlockingStub? {
        return PixServiceGrpc.newBlockingStub(channel)
    }
}
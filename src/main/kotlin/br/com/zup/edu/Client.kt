package br.com.zup.edu

import io.grpc.ManagedChannelBuilder

fun main() {

    val channel = ManagedChannelBuilder
                        .forAddress("localhost", 50051)
                        .usePlaintext()
                        .build()
    val client = FuncionarioServiceGrpc.newBlockingStub(channel)

    val request = FuncionarioRequest.newBuilder()
        .setNome("Samara")
        .setCpf("111.000.000-11")
        .setIdade(20)
        .setCargo(Cargo.DEV)
        .setSalario(2500.00)
        .setAtivo(true)
        .addEnderecos(FuncionarioRequest.Endereco.newBuilder()
            .setLogradouro("Vila das Rosas")
            .setCep("55667-000")
            .setComplemento("Casa 20")
            .build())
        .build()

    val response = client.cadastrar(request)

    println(response)
}
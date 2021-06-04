package br.com.zup.edu

import java.io.FileInputStream
import java.io.FileOutputStream

fun main() {
    val request = FuncionarioRequest.newBuilder()
        .setNome("Samara")
        .setCpf("111.000.000-11")
        .setCargo(Cargo.DEV)
        .setSalario(2500.00)
        .setAtivo(true)
        .addEnderecos(FuncionarioRequest.Endereco.newBuilder()
                            .setLogradouro("Vila das Rosas")
                            .setCep("55667-000")
                            .setComplemento("Casa 20")
                            .build())
        .build()

    println(request)

    // escrevemos o objeto em disco
    request.writeTo(FileOutputStream("funcionario-request.bin"))

    // lemos o objeto
    val request2 = FuncionarioRequest.newBuilder()
        .mergeFrom(FileInputStream("funcionario-request.bin"))

    request2.setCargo(Cargo.GERENTE).build()

    println(request2)

}
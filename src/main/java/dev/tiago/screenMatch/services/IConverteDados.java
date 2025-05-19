package dev.tiago.screenMatch.services;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
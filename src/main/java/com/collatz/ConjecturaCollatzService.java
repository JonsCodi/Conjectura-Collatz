package com.collatz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Conjectura de Collatz
 * ----------------------
 * <p>
 * fonte: http://en.wikipedia.org/wiki/Collatz_conjecture
 * <p>
 * A seguinte sequência iterativa é definida pelo conjunto de inteiros positivos onde:
 * <p>
 * n = n/2 (se n é par)
 * n = 3n + 1 (se n é impar)
 * <p>
 * Por exemplo, usando as regras acima e começando pelo número 13, nós geraríamos a seguinte sequência:
 * <p>
 * 13 40 20 10 5 16 8 4 2 1
 * <p>
 * O que pode ser observado dessa sequência (começando no 13 e terminando no 1) é que ela contém 10 items. Embora ainda não esteja matematicamente provado, é esperando que, dado um numero inteiro positivo qualquer, a sequencia sempre chegará em 1.
 * <p>
 * Pergunta: Qual inteiro positivo abaixo de 1 milhão produz a sequencia com mais items?
 */
public class ConjecturaCollatzService {

    private static final int QUASE_UM_MILHAO = 999999;
    private static final int CEM_MIL = 100000;
    private static final int THREADS_NUMERO = 10;

    public int inicioSequencia;
    public int finalSequencia;
    public ConcurrentHashMap<Integer, List<Integer>> concurrentHashMap;

    public ConjecturaCollatzService(int inicioSequencia, int finalSequencia) {
        this.concurrentHashMap = new ConcurrentHashMap<>();
        this.inicioSequencia = inicioSequencia;
        this.finalSequencia = finalSequencia;
    }

    public void processa() {
        List<Integer> sequencias;
        for (int i = inicioSequencia; i <= this.finalSequencia; i++) {

            sequencias = new ArrayList<>();
            sequencias.add(i);

            this.concurrentHashMap.put(i, sequencias);

            int sequencia = i;

            while (sequencia != 1) {
                sequencia = this.calculaConjecturaCollatz(sequencia);
                if (sequencia < 1) break; //Há possibilidade de aparecer número negativo como resultado...

                sequencias.add(sequencia);
                this.concurrentHashMap.put(i, sequencias);
            }
        }
    }

    private int calculaConjecturaCollatz(int sequencia) {
        if (sequencia % 2 == 0) {
            return sequencia / 2;
        } else {
            return (sequencia * 3) + 1;
        }
    }

    public ConcurrentHashMap<Integer, Integer> mapSequenciaEItemSize() {
        ConcurrentHashMap<Integer, Integer> mapTamanhoItems = new ConcurrentHashMap<>();
        this.concurrentHashMap.forEach((key, value) -> mapTamanhoItems.put(key, value.size()));

        return mapTamanhoItems;
    }

    public static class MyRunnable implements Runnable {

        public ConjecturaCollatzService conjecturaCollatzService;
        public ConcurrentHashMap<Integer, Integer> mapTamanhoItems;

        public MyRunnable(ConjecturaCollatzService conjecturaCollatzService) {
            this.conjecturaCollatzService = conjecturaCollatzService;
            this.mapTamanhoItems = new ConcurrentHashMap<>();
        }

        @Override
        public void run() {
            conjecturaCollatzService.processa();

            this.mapTamanhoItems = conjecturaCollatzService.mapSequenciaEItemSize();
        }
    }

    public static void main(String... args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMERO);
        Map<Integer, Integer> concurrentHashMap = new HashMap<>();

        List<MyRunnable> myRunnables = new ArrayList<>();

        for (int i = 0; i < QUASE_UM_MILHAO; i += CEM_MIL) {
            MyRunnable worker = new MyRunnable(new ConjecturaCollatzService(i, i + CEM_MIL));

            executor.execute(worker);

            myRunnables.add(worker);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {}

        myRunnables.forEach(myRunnable -> myRunnable.mapTamanhoItems.forEach(concurrentHashMap::put));

        int maiorNumeroPositivo = Collections.max(concurrentHashMap.entrySet(),
                Map.Entry.comparingByValue())
                .getKey();

        System.out.println("Pergunta: Qual inteiro positivo abaixo de 1 milhão produz a sequencia com mais items? " + maiorNumeroPositivo);
    }
}

# Conjectura de Collatz

[Fonte](http://en.wikipedia.org/wiki/Collatz_conjecture)

## Problema
A seguinte sequência iterativa é definida pelo conjunto de inteiros positivos onde: <br>
````
n = n/2 (se n é par)
n = 3n + 1 (se n é impar)
````
Por exemplo, usando as regras acima e começando pelo número 13, nós geraríamos a seguinte sequência: ``13 40 20 10 5 16 8 4 2 1``

O que pode ser observado dessa sequência (começando no 13 e terminando no 1) é que ela contém 10 items. Embora ainda não esteja matematicamente provado, é esperando que, dado um numero inteiro positivo qualquer, a sequencia sempre chegará em 1.
### Pergunta: 
Qual inteiro positivo abaixo de 1 milhão produz a sequencia com mais items?

# How to run
`
$ gradle run
`

## Resultado

````text
Pergunta: Qual inteiro positivo abaixo de 1 milhão produz a sequencia com mais items? 
910107
````

# To do
 * UnitTest
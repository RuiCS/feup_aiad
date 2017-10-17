# Combate a Incêndios com Agentes BDI (T05)

Um repositório para o projeto Combate a Incêndios com Agentes BDI para a UC de AIAD, na FEUP.

## Objetivo
Implementar um Sistema Multi-Agente que permita a simulação do combate a um incêndio sobre um terreno com determinadas caraterísticas. Devem ser implementados agentes bombeiros cujos comportamentos são guiados por crenças, desejos e intenções (BDI).

## Descrição
Todos os anos muitos incêndios devastam as florestas portuguesas, provocando elevados prejuízos humanos, ecológicos e financeiros. O fogo é um elemento de difícil previsibilidade, já que depende de múltiplas grandezas cujos valores são muitas vezes desconhecidos. De facto, a ignição e propagação de fogos florestais são fenómenos complexos que dependem de fatores como o tipo e o estado da vegetação existente, o perfil do terreno, o vento e a temperatura e humidade ambiente.

O combate a um incêndio é uma tarefa de equipa que se reveste de grande complexidade e perigosidade. Para além de todos os factores de incerteza já descritos, os bombeiros que combatem o fogo têm de ter também em consideração a defesa de bens ecológicos e materiais bem como a segurança das populações e, evidentemente, a sua própria segurança. A simulação de um ambiente distribuído de tal complexidade apresenta, por tudo isto, um motivo de interesse para a Inteligência Artificial e em particular para Sistemas Multi-Agente.

O trabalho proposto consiste na criação de um modelo de simulação da propagação de incêndios e de combate aos mesmos usando Agentes com arquitetura BDI. Para simplificar o cenário considere, como caraterísticas ambientais que influenciam a propagação do fogo, apenas o vento (direção e velocidade) e a vegetação (o fogo propaga-se mais rapidamente num terreno com vegetação mais densa).

Os agentes podem movimentar-se em todas as direções e extinguir o fogo atirando água sobre os elementos em combustão. O salvamento de pessoas em perigo poderá ser outra ação possível. Os agentes podem comunicar entre si.

## Material

* [Introdução e conceitos gerais deste projeto](http://jasss.soc.surrey.ac.uk/13/1/4.html)
* [Jason](http://jason.sourceforge.net/mini-tutorial/eclipse-plugin/#id.iyxfqy5c7qw)
* [Exemplo de 2005/2006](https://paginas.fe.up.pt/~eol/AIAD/TRABALHOS_ANT/Pyrofighter_0506.pdf)
* [Getting started with Jason](http://jason.sourceforge.net/mini-tutorial/getting-started/)
* [Agentspeak tutorial](http://jason.sourceforge.net/jBook/SlidesJason.pdf)

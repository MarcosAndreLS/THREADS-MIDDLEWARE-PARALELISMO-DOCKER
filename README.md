# 📦 Sistema Distribuído em Java - Mestre-Escravo com Threads e Docker

Este projeto implementa um **sistema distribuído em Java puro**, utilizando **threads**, **middleware REST** e **containers Docker**, baseado na arquitetura **mestre-escravo**. O sistema é composto por três componentes principais: um **cliente com interface gráfica**, um **servidor mestre** e dois **servidores escravos** responsáveis por processamentos distintos.

## 🎯 Objetivo

Desenvolver uma aplicação distribuída capaz de processar arquivos de texto contendo letras e números, distribuindo o processamento entre dois escravos de forma paralela e controlada, utilizando Java puro e execução em containers Docker.

## ⚙️ Arquitetura do Sistema

### 🖥️ Cliente (Notebook 1)
- Desenvolvido com GUI em **Java Swing ou JavaFX**
- Envia arquivos `.txt` contendo letras e números
- Se comunica via **requisições HTTP (REST)**
- Apenas envia e recebe dados — **não realiza processamento**

### 🧠 Servidor Mestre (Container Docker - Notebook 2)
- Recebe os arquivos do cliente via HTTP
- Inicia **duas threads paralelas**, cada uma responsável por um escravo
- Cada thread:
  - Verifica a disponibilidade do escravo
  - Envia os dados para processamento
- Combina os resultados dos dois escravos e devolve a resposta ao cliente

### ⚙️ Escravos (Containers Docker - Notebook 2)
- **Escravo 1:** Expõe o endpoint `/letras`, responsável por contar a quantidade de **letras** no texto
- **Escravo 2:** Expõe o endpoint `/numeros`, responsável por contar a quantidade de **números** no texto

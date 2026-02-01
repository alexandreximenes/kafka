# 🚀 Apache Kafka – Producers & Consumers Playground

[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-Streaming-black)](https://kafka.apache.org/)
[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Kafka-green)](https://spring.io/projects/spring-kafka)

Branch principal: `master`
👉 **[https://github.com/alexandreximenes/kafka](https://github.com/alexandreximenes/kafka/tree/master)**

---

## 📌 Visão Geral

Este repositório reúne **exemplos práticos de Producers e Consumers utilizando Apache Kafka**, organizados por tipo de payload, com foco em **clareza, boas práticas e fácil experimentação**.

O projeto foi pensado para servir como:

* Playground de Kafka
* Base de estudos
* Referência para arquiteturas orientadas a eventos

---

## 🧱 Estrutura do Projeto

```
kafka/
├── json-producer
├── json-consumer
├── string-producer
└── string-consumer
```

### 📦 Módulos

| Módulo            | Descrição                             |
| ----------------- | ------------------------------------- |
| `json-producer`   | Publica mensagens com payload JSON    |
| `json-consumer`   | Consome e desserializa mensagens JSON |
| `string-producer` | Publica mensagens no formato String   |
| `string-consumer` | Consome mensagens String              |

---

## 🎯 Objetivo

Demonstrar, de forma isolada e didática:

* Produção e consumo de mensagens Kafka
* Diferença entre payloads **JSON vs String**
* Serialização e desserialização
* Consumo em grupo
* Base para retry, DLQ e observabilidade

---

## 🔄 Arquitetura (Visão Macro)

```
Producer (JSON / String)
        |
        v
     Kafka Topic
        |
        v
Consumer (JSON / String)
```

### Componentes

* **Producer**: envia eventos para tópicos Kafka
* **Kafka Broker**: persiste, replica e distribui mensagens
* **Consumer**: processa eventos conforme regras de negócio

---

## ⚙️ Configuração Essencial

Configurações comuns a todos os módulos:

```properties
bootstrap.servers=localhost:9092
group.id=example-consumer-group
```

### Parâmetros Importantes

| Propriedade          | Função                   |
| -------------------- | ------------------------ |
| `bootstrap.servers`  | Endereço do broker Kafka |
| `group.id`           | Grupo do consumidor      |
| `key.serializer`     | Serializer da chave      |
| `value.serializer`   | Serializer do valor      |
| `key.deserializer`   | Desserializer da chave   |
| `value.deserializer` | Desserializer do valor   |

---

## 🧪 Casos de Uso

* Testar serialização JSON no Kafka
* Simular múltiplos consumers no mesmo grupo
* Validar comportamento de offsets
* Base para retry e Dead Letter Queue (DLQ)
* Debug e aprendizado de Kafka na prática

---

## 🚀 Próximos Passos (Roadmap)

* [x] Docker Compose (Kafka / KRaft)
* [ ] Retry Topics
* [ ] Dead Letter Queue (DLQ)
* [ ] Schema Registry
* [ ] Observabilidade (Metrics / Tracing)

---

## 📚 Referências

* Apache Kafka — [https://kafka.apache.org](https://kafka.apache.org)
* Spring Kafka — [https://spring.io/projects/spring-kafka](https://spring.io/projects/spring-kafka)

---

✨ Projeto mantido por **Alexandre Ximenes**
Se este repositório te ajudou, deixe uma ⭐

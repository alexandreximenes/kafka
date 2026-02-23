# 🚀 Apache Kafka – Producers & Consumers Playground

[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-Streaming-black)](https://kafka.apache.org/)
[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Kafka-green)](https://spring.io/projects/spring-kafka)

Branch principal: `master`  
👉 https://github.com/alexandreximenes/kafka

---

## 📌 Visão Geral

Este repositório reúne **exemplos práticos de Producers e Consumers utilizando Apache Kafka**, organizados por tipo de payload e estratégia de serialização.

O objetivo é oferecer um ambiente claro, didático e pronto para experimentação, servindo como:

- 🎯 Playground de Kafka  
- 📚 Base de estudos  
- 🏗 Referência para arquiteturas orientadas a eventos  
- 🔬 Laboratório para testes de serialização e contratos  

---

## 🧱 Estrutura do Projeto

```
kafka/
├── json-producer
├── json-consumer
├── string-producer
├── string-consumer
├── schema-registry-confluent
└── schema-registry-apicurio
```

---

## 📦 Módulos

| Módulo                      | Descrição |
|-----------------------------|-----------|
| `json-producer`             | Publica mensagens com payload JSON |
| `json-consumer`             | Consome e desserializa mensagens JSON |
| `string-producer`           | Publica mensagens no formato String |
| `string-consumer`           | Consome mensagens String |
| `schema-registry-confluent` | Integração com Confluent Schema Registry |
| `schema-registry-apicurio`  | Integração com Apicurio Registry |

---

## 🎯 Objetivos Técnicos

Demonstrar de forma isolada e didática:

- Produção e consumo de mensagens Kafka
- Diferença entre payloads **String, JSON e Avro**
- Serialização e desserialização
- Versionamento de schemas
- Consumo em grupo
- Base para retry, DLQ e observabilidade

---

## 🔄 Arquitetura (Visão Macro)

```
Producer (String / JSON / Avro)
        |
        v
     Kafka Topic
        |
        v
Consumer (String / JSON / Avro)
```

### Componentes

- **Producer** → Envia eventos para tópicos Kafka  
- **Kafka Broker** → Persiste, replica e distribui mensagens  
- **Schema Registry** → Gerencia contratos (Avro) e versionamento  
- **Consumer** → Processa eventos conforme regras de negócio  

---

## 🧬 Schema Registry

O projeto suporta duas abordagens de gerenciamento de schema:

### 🔹 Confluent Schema Registry

- Padrão amplamente adotado
- Integração direta com Avro
- Compatibilidade configurável (BACKWARD, FORWARD, FULL)

### 🔹 Apicurio Registry

- Alternativa open-source
- Suporte a múltiplos formatos
- Boa integração com ecossistema Quarkus/Spring

---

## ⚙️ Configuração Essencial

Configurações comuns:

```properties
bootstrap.servers=localhost:9092
group.id=example-consumer-group
```

### Propriedades Importantes

| Propriedade | Função |
|-------------|--------|
| `bootstrap.servers` | Endereço do broker Kafka |
| `group.id` | Grupo do consumidor |
| `key.serializer` | Serializer da chave |
| `value.serializer` | Serializer do valor |
| `key.deserializer` | Desserializer da chave |
| `value.deserializer` | Desserializer do valor |
| `schema.registry.url` | URL do Schema Registry |

---

## 🐳 Ambiente com Docker

O projeto inclui suporte a:

- Kafka (modo KRaft)
- Schema Registry (Confluent e Apicurio)
- Configuração pronta para testes locais

---

## 🧪 Casos de Uso

- Testar serialização JSON e Avro
- Simular múltiplos consumers no mesmo grupo
- Validar comportamento de offsets
- Experimentar versionamento de schemas
- Base para Retry e Dead Letter Queue (DLQ)
- Debug e aprendizado prático de Kafka

---

## 🛣 Roadmap

- [x] Docker Compose (Kafka / KRaft)
- [x] Schema Registry (Confluent)
- [x] Schema Registry (Apicurio)
- [ ] Retry Topics
- [ ] Dead Letter Queue (DLQ)
- [ ] Observabilidade (Metrics / Tracing)
- [ ] Testcontainers para testes automatizados

---

## 📚 Referências

- Apache Kafka — https://kafka.apache.org  
- Spring Kafka — https://spring.io/projects/spring-kafka  
- Confluent Schema Registry — https://docs.confluent.io  
- Apicurio Registry — https://www.apicur.io  

---

## 👨‍💻 Autor

Projeto mantido por **Alexandre Ximenes**

Se este repositório te ajudou, considere deixar uma ⭐  
Contribuições são bem-vindas 🚀

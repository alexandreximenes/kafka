# kafka# kafka

Visão geral
-----------
Repositório com quatro módulos para demonstração e integração com Apache Kafka:
- `json-producer` — produtor que emite mensagens com payload JSON.
- `json-consumer` — consumidor que recebe e processa payloads JSON.
- `string-producer` — produtor que envia mensagens como `String`.
- `string-consumer` — consumidor que lê mensagens como `String`.

Objetivo
--------
Fornecer exemplos simples e separados por tipo de payload para desenvolver, testar e validar padrões de integração com Kafka (produção, consumo, serialização, tolerância a falhas e observabilidade).

Arquitetura (macro)
-------------------
Componentes principais:
- Produtores: enviam mensagens para tópicos específicos.
- Broker(s) Kafka: roteia, persiste e replica mensagens.
- Consumidores: leem mensagens de tópicos e executam lógica de processamento.
- (Opcional) Schema Registry / Message validation para `json-*`.

Fluxo simplificado:

Producer (JSON/String)
        |
        v
Kafka Topic(s)
        |
        v
Consumer (JSON/String)

Módulos e responsabilidades
---------------------------
- `json-producer`
  - Emite eventos estruturados (JSON).
  - Deve incluir validação do payload e esquema (opcional: JSON Schema).
  - Tópicos alvo: `topic.json.*` (convenção sugestiva).
- `json-consumer`
  - Desserializa JSON e aplica regras de negócio.
  - Trata falhas de desserialização e reprocessamento.
- `string-producer`
  - Emite payloads simples de texto (logs, chaves simples).
  - Tópicos alvo: `topic.string.*`.
- `string-consumer`
  - Consome e processa mensagens de texto.

Configuração essencial
----------------------
Parâmetros comuns (exemplos via variáveis de ambiente ou `application.properties`):
- `BOOTSTRAP_SERVERS` / `bootstrap.servers` — endereço(s) do broker Kafka (ex: `localhost:9092`).
- `GROUP_ID` / `group.id` — id do grupo de consumidores.
- `KEY_SERIALIZER` / `VALUE_SERIALIZER` — serializers para produtores.
- `KEY_DESERIALIZER` / `VALUE_DESERIALIZER` — deserializers para consumidores.

Exemplo mínimo (variáveis de ambiente):
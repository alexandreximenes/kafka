# Projeto Java (Maven)

## Docker Compose

Esta seção mostra como subir uma stack mínima com Zookeeper, Kafka e Kafdrop, parar os serviços e realizar uma limpeza básica de recursos.

### Requisitos
- Docker
- Docker Compose

### Subir os serviços
Executar no diretório onde está o `docker-compose.yml`:
```bash
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  kafka:
    image: confluentinc/cp-kafka:7.4.0
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
    ports:
      - "9092:9092"

  kafdrop:
    image: obsidiandynamics/kafdrop:3.27.0
    depends_on:
      - kafka
    environment:
      KAFKA_BROKERCONNECT: kafka:9092
    ports:
      - "9000:9000"
```
### Comandos uteis
````
docker-compose down -v
docker rm -f kafka kafdrop
docker network prune -f
docker volume prune -f
docker system prune -a -f
docker-compose up -d
docker logs kafka

docker exec -it kafka bash

/opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --describe \
  --topic __consumer_offsets
  
 /opt/kafka/bin/kafka-consumer-groups.sh \
  --bootstrap-server localhost:9092 \
  --list
  
 kafka-consumer-groups.sh \
  --bootstrap-server localhost:9092 \
  --describe \
  --group group-1
  
kafka-console-producer.sh \
  --bootstrap-server localhost:9092 \
  --topic str-topic
````
### Criando Cluster ID Kafka
Caso necessário, crie um Cluster ID para o Kafka:
```bash
 docker run --rm confluentinc/cp-kafka:7.5.0 kafka-storage random-uuid
```
Em seguida, adicione a variável de ambiente `KAFKA_CLUSTER_ID` no serviço Kafka do `docker-compose.yml`
````aiignore
 KAFKA_CLUSTER_ID: xpBLkEjuTy2FGmRh26oB3w
````
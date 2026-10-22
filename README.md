# To Do List API

## Objetivo
Esse é repositório de um api de To Do List em Quarkus em Arquitetura Limpa, com o objetivo de praticar o que foi estudado.

## Técnologias
- Quarkus
- Hibernate
- H2

## Estrutura do projeto

```text
br.com.kervincandido.portfolio
├── domain
│   ├── model          → entidades, value objects, enums
│   ├── exception      → exceções de regra de negócio
│   └── service        → serviços de domínio (regras que não cabem em uma entidade)
│
├── application
│   ├── port
│   │   ├── in         → casos de uso (interfaces chamadas pelo mundo externo)
│   │   └── out        → interfaces de que a aplicação precisa (repositórios, gateways)
│   ├── service        → implementações dos casos de uso
│   └── dto            → comandos e respostas dos casos de uso
│
└── infrastructure
    ├── persistence    → entidades JPA, Spring Data, adapters dos repositórios
    ├── web            → controllers REST, DTOs de request/response
    ├── messaging      → produtores/consumidores de Kafka, RabbitMQ etc.
    └── config         → beans, configurações de framework
```

## Regra de negócio

A principal entidade da api é a Tarefa.

### Composição das Entidades
**Task** (Tarefa)
- Title (Título): Texto curto.
- Description (Descrição): Texto longo (opcional).
- Status (Situação): Estado atual da tarefa no fluxo.
- Priority (Prioridade): Nível de urgência da tarefa.
- CreatedAt (Data de Criação): Registro de tempo automático de quando a tarefa foi aberta.
- DueDate (Data de Entrega): Data limite estabelecida como prazo.
- CompletedAt (Data de Conclusão): Registro de tempo de quando a tarefa foi concluída.
**Status** (Situação):
- To Do (Fazer)
- Doing (Fazendo)
- Done (Feito)
**Priority** (Prioridade):
- Low (Baixa)
- Medium (Média)
- High (Alta)
- Urgent (Urgente)

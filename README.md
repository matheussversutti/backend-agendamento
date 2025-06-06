# Agendamento Odontológico - Backend

Este projeto é um sistema de agendamento para clínicas odontológicas, desenvolvido em Java com Spring Boot.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- Maven
- H2 Database (ambiente de desenvolvimento)
- Lombok
- Docker

## Arquitetura

O projeto segue a arquitetura em camadas:

- **Controller**: Recebe e trata as requisições HTTP.
- **Service**: Lógica de negócio.
- **DTO**: Objetos de transferência de dados.
- **Mapper**: Conversão entre entidades e DTOs.
- **Molde (Model/Entity)**: Entidades do domínio.
- **Repository**: Acesso ao banco de dados.

### Paciente

- `GET /pacientes` — Lista todos os pacientes.
- `GET /pacientes/{id}` — Busca paciente por ID.
- `POST /pacientes` — Cadastra novo paciente.
- `PUT /pacientes/{id}` — Atualiza dados do paciente.
- `DELETE /pacientes/{id}` — Remove paciente e suas consultas.

### Consultas

- `GET /consultas` — Lista todas as consultas.
- `POST /consultas` — Agenda nova consulta.
- `PUT /consultas/{id}` — Atualiza consulta.
- `DELETE /consultas/{id}` — Remove consulta.

> Outros endpoints para administradores e funcionários podem estar presentes conforme a implementação.****

## Regras de Negócio

- Não é permitido cadastrar e-mails ou CPFs duplicados entre pacientes, administradores e funcionários.
- Ao remover um paciente, suas consultas também são removidas.

## Configuração de CORS

O CORS está configurado para aceitar requisições de `http://localhost` e `http://localhost:80`.

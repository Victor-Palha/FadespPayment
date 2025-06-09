# FadespPayment - API de Pagamentos

Esta API tem como objetivo possibilitar o recebimento e gerenciamento de pagamentos de débitos realizados por pessoas físicas ou jurídicas. Foi desenvolvida como parte de um desafio técnico utilizando Java e Spring Boot.

---

## Funcionalidades

- **Receber pagamento** de um débito
- **Atualizar status** de um pagamento
-  **Listar pagamentos** com filtros por:
    - Código do débito
    - CPF/CNPJ do pagador
    - Status do pagamento
-  **Exclusão lógica** de pagamentos pendentes
-  Banco de dados **H2**

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot - 3.5.0
- Spring Data JPA
- H2 Database
- Maven

---

## Como Executar o Projeto

### Pré-requisitos

- Java 17+
- Maven 3+

### Rodando localmente

```bash
git clone https://github.com/Victor-Palha/FadespPayment.git
cd FadespPayment
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

---

## Regras de Negócio

* Pagamento começa com status **PENDENTE**
* Só pode ser atualizado para:

    * `PROCESSADO_SUCESSO` ou `PROCESSADO_FALHA`
* Pagamentos com `PROCESSADO_SUCESSO` **não podem** ser alterados
* Pagamentos com `PROCESSADO_FALHA` só podem voltar para `PENDENTE`
* Exclusão lógica **só é permitida se estiver PENDENTE**

---

## Testes

Para executar os testes da aplicação, basta rodar no terminal:
`./mvnw test`

## Endpoints

**Base URL:** `http://localhost:8080/api`

---

## Enums disponíveis

### `PaymentStatusType`

* `PENDENTE`
* `PROCESSADO_SUCESSO`
* `PROCESSADO_FALHA`

### `PaymentType`

* `BOLETO`
* `PIX`
* `CARTAO_CREDITO`
* `CARTAO_DEBITO`


---

## Criar Pagamento

**POST** `/payment`

Cria um novo pagamento.

### Requisição

```json
{
  "debitCode": 9999,
  "creditCardNumber": "1111222233334444",
  "documentId": "12.345.678/0001-90",
  "paymentMethod": "CARTAO_CREDITO",
  "amount": 100
}
```

### Resposta: `201 Created`

```json
{
  "id": "c520270c-12c5-4e24-b4d8-0ffa9cbeee07",
  "debitCode": 9999,
  "creditCardNumber": "1111222233334444",
  "documentId": "12345678000190",
  "paymentMethod": "CARTAO_CREDITO",
  "amount": 100,
  "paymentStatus": "PENDENTE",
  "documentType": "CNPJ",
  "active": true
}
```

- OBS: documentId representa CNPJ ou CPF e pode ser enviado tanto com ., -, / ou apenas númerico. O sistema consegue identificar o tipo de documento e salva em uma coluna para consultas futuras.
- OBS 2: `creditCardNumber` passa pelo algoritmo de Luhn, então somente cartões validos pelo algoritmo podem ser cadastrados.
---

## Listar Pagamentos

**GET** `/payment`

Retorna todos os pagamentos, com filtros opcionais.

### Parâmetros de Query (opcionais)

| Parâmetro       | Tipo                | Descrição               |
| --------------- | ------------------- | ----------------------- |
| `debitCode`     | `Long`              | Código do débito        |
| `documentId`    | `String`            | Documento (CPF ou CNPJ) |
| `paymentStatus` | `PaymentStatusType` | Status do pagamento     |

### Resposta: `200 OK`

```json
[
  {
    "id": "uuid",
    "debitCode": 123456,
    "creditCardNumber": null,
    "documentId": "12345678909",
    "paymentMethod": "PIX",
    "amount": 75.50,
    "paymentStatus": "PROCESSADO_SUCESSO",
    "documentType": "CPF",
    "active": true
  },
  ...
]
```

---

## Atualizar Status do Pagamento

**PATCH** `/payment/{id}/status`

Atualiza o status de um pagamento.

### Path Variable

| Nome | Tipo | Descrição       |
| ---- | ---- | --------------- |
| `id` | UUID | ID do pagamento |

### Requisição

```json
{
  "status": "PROCESSADO_SUCESSO"
}
```

### Resposta: `200 OK`

```json
{
  "id": "c520270c-12c5-4e24-b4d8-0ffa9cbeee07",
  "debitCode": 9999,
  "creditCardNumber": "1111222233334444",
  "documentId": "12345678000190",
  "paymentMethod": "CARTAO_CREDITO",
  "amount": 100,
  "paymentStatus": "PROCESSADO_SUCESSO",
  "documentType": "CNPJ",
  "active": true
}
```

---

## Desativar Pagamento

**DELETE** `/payment/{id}`

Desativa (soft delete) um pagamento.

### Path Variable

| Nome | Tipo | Descrição       |
| ---- | ---- | --------------- |
| `id` | UUID | ID do pagamento |

### Resposta: `204 No Content`

---
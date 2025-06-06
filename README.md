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
````

A aplicação estará disponível em: `http://localhost:8080`

### Acessar o banco H2

* URL: `http://localhost:8080/h2-console`
* JDBC URL: `jdbc:h2:mem:testdb`
* Usuário: `sa`
* Senha: *(deixe em branco)*

---

## Regras de Negócio

* Pagamento começa com status **PENDENTE**
* Só pode ser atualizado para:

    * `PROCESSADO_SUCESSO` ou `PROCESSADO_FALHA`
* Pagamentos com `PROCESSADO_SUCESSO` **não podem** ser alterados
* Pagamentos com `PROCESSADO_FALHA` só podem voltar para `PENDENTE`
* Exclusão lógica **só é permitida se estiver PENDENTE**

---


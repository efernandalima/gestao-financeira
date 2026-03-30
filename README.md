# 💰 Sistema de Gestão Financeira

API REST para controle de receitas e despesas pessoais, desenvolvida em **Java 17** com **Spring Boot**.

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![H2](https://img.shields.io/badge/H2-2.2.224-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

---

## 📋 Sobre o Projeto

Sistema completo de gestão financeira que permite:
- ✅ Cadastro de receitas e despesas
- ✅ Organização por categorias personalizadas
- ✅ Relatórios financeiros por período
- ✅ Cálculo automático de saldo
- ✅ Filtros avançados (tipo, categoria, data)
- ✅ Validações de dados
- ✅ Documentação automática com Swagger
- ✅ Testes unitários de regras de negócio de transações

**Projeto desenvolvido como parte do portfólio de desenvolvedor Java.**

---

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17** (LTS)
- **Spring Boot 3.2.1**
- **Spring Data JPA** (persistência)
- **Hibernate** (ORM)
- **Bean Validation** (validações)
- **Lombok** (redução de boilerplate)
- **Spring Security** (configuração básica)

### Banco de Dados
- **H2 Database** (desenvolvimento)
- **PostgreSQL** (produção)

### Documentação
- **Swagger/OpenAPI 3.0** (documentação interativa da API)

### Ferramentas
- **Maven** (gerenciamento de dependências)
- **Git** (controle de versão)
- **JUnit 5 + Spring Test** (testes automatizados)

---

## 📁 Arquitetura do Projeto

O projeto está organizado por **domínio funcional**, e não apenas por camada técnica:

```text
src/main/java/br/com/financeiro
├── categoria
│   ├── CategoriaController.java
│   ├── CategoriaRepository.java
│   ├── CategoriaService.java
│   └── model
│       └── Categoria.java
│
├── transacao
│   ├── TransacaoController.java
│   ├── TransacaoRepository.java
│   ├── TransacaoService.java
│   └── model
│       ├── Transacao.java
│       └── TipoTransacao.java
│
├── relatorio
│   ├── RelatorioController.java
│   └── RelatorioService.java
│
├── dto
│   ├── TransacaoRequestDTO.java
│   └── TransacaoResponseDTO.java
│
├── shared
│   └── exception
│       ├── ApiError.java
│       ├── GlobalExceptionHandler.java
│       └── ResourceNotFoundException.java
│
├── config
│   ├── SecurityConfig.java
│   └── SwaggerConfig.java
│
└── GestaoFinanceiraApplication.java
```

**Padrão de arquitetura:**

- **Domínios (categoria, transacao, relatorio)** concentrando controllers, services, repositories e entidades.
- **DTOs** para separar contrato de API das entidades de persistência.
- **Camada compartilhada** (`shared.exception`) para tratamento de erros reutilizável.
- **Camada de configuração** (`config`) para segurança e Swagger.

---

## ⚙️ Como Executar

### **Pré-requisitos**
- Java 17 ou superior
- Maven 3.6+
- PostgreSQL 15+ (opcional – H2 já configurado)

### **1. Clonar o repositório**

```bash
git clone https://github.com/efernandalima/gestao-financeira.git
cd gestao-financeira
```

### **2. Configurar banco de dados (opcional)**

Para usar **H2** (padrão):  
Não precisa fazer nada, o projeto já vem configurado para H2 em memória.

Para usar **PostgreSQL**:

```sql
CREATE DATABASE gestao_financeira;
```

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestao_financeira
spring.datasource.username=postgres
spring.datasource.password=sua_senha
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### **3. Compilar o projeto**

```bash
mvn clean install
```

### **4. Executar a aplicação**

```bash
mvn spring-boot:run
```

### **5. Acessar a aplicação**

- **API base:** http://localhost:8081
- **Swagger UI:** http://localhost:8081/swagger-ui.html
- **H2 Console:** http://localhost:8081/h2-console
  - **JDBC URL:** `jdbc:h2:mem:financeiro`
  - **User:** `sa`
  - **Password:** *(vazio)*

---

## 📚 Documentação da API

### Swagger UI

A documentação interativa completa está disponível em:

```text
http://localhost:8081/swagger-ui.html
```

### Endpoints Principais

#### **Categorias**

| Método | Endpoint                      | Descrição                          |
|--------|-------------------------------|------------------------------------|
| GET    | `/api/categorias`            | Listar todas as categorias         |
| GET    | `/api/categorias/{id}`       | Buscar categoria por ID            |
| GET    | `/api/categorias/tipo/{tipo}`| Listar por tipo (RECEITA/DESPESA) |
| POST   | `/api/categorias`            | Criar nova categoria               |
| PUT    | `/api/categorias/{id}`       | Atualizar categoria                |
| DELETE | `/api/categorias/{id}`       | Deletar categoria                  |

#### **Transações**

| Método | Endpoint                                   | Descrição                        |
|--------|--------------------------------------------|----------------------------------|
| GET    | `/api/transacoes`                         | Listar transações paginadas      |
| GET    | `/api/transacoes/{id}`                    | Buscar transação por ID          |
| GET    | `/api/transacoes/tipo/{tipo}`             | Listar por tipo                  |
| GET    | `/api/transacoes/periodo`                 | Listar por período               |
| GET    | `/api/transacoes/categoria/{id}`          | Listar por categoria             |
| GET    | `/api/transacoes/tipo/{tipo}/periodo`     | Listar por tipo e período        |
| GET    | `/api/transacoes/total/{tipo}`            | Total por tipo em um período     |
| GET    | `/api/transacoes/saldo`                   | Calcular saldo no período        |
| POST   | `/api/transacoes`                         | Criar nova transação             |
| PUT    | `/api/transacoes/{id}`                    | Atualizar transação              |
| DELETE | `/api/transacoes/{id}`                    | Deletar transação                |

#### **Relatórios**

| Método | Endpoint                        | Descrição                 |
|--------|---------------------------------|---------------------------|
| GET    | `/api/relatorios/periodo`       | Gerar relatório completo  |

---

## 🧪 Testes Automatizados

Os testes unitários cobrem regras de negócio da camada de serviço:

- `TransacaoServiceTest` valida:
  - Cálculo de totais por tipo e período.
  - Cálculo de saldo no período.
  - Validação de período (data inicial > final).
  - Validação de compatibilidade entre tipo da transação e tipo da categoria.
  - Cenários de busca e tratamento de `ResourceNotFoundException`.

Para executar os testes:

```bash
mvn test
```

---

## 🧪 Exemplos de Uso

### 1. Criar uma Categoria

```http
POST http://localhost:8081/api/categorias
Content-Type: application/json

{
  "nome": "Salário",
  "descricao": "Salário mensal",
  "tipo": "RECEITA"
}
```

### 2. Criar uma Receita (DTO de requisição)

```http
POST http://localhost:8081/api/transacoes
Content-Type: application/json

{
  "descricao": "Salário Janeiro",
  "valor": 5000.00,
  "data": "2026-01-05",
  "tipo": "RECEITA",
  "categoriaId": 1,
  "observacao": "Salário do mês"
}
```

### 3. Gerar Relatório do Mês

```http
GET http://localhost:8081/api/relatorios/periodo?inicio=2026-01-01&fim=2026-01-31
```

---

## 🎯 Funcionalidades Implementadas

### Regras de Negócio

- ✅ Validação de dados (Bean Validation)
- ✅ Categoria compatível com tipo da transação
- ✅ Tratamento de exceções personalizado
- ✅ Cálculos financeiros (saldo, totais por período)
- ✅ Filtros por tipo, categoria e período

### Boas Práticas

- ✅ Organização por domínio (`categoria`, `transacao`, `relatorio`)
- ✅ Camadas bem definidas (Controller, Service, Repository, Model, DTO)
- ✅ Injeção de dependência via Spring
- ✅ DTOs de entrada e saída para transações
- ✅ Lombok para reduzir boilerplate
- ✅ Documentação Swagger automática
- ✅ Tratamento global de erros com `GlobalExceptionHandler`
- ✅ Testes unitários focados nas regras de negócio

---

## 📊 Dados de Exemplo

O projeto pode incluir dados de exemplo em `data.sql`:

- Categorias de receitas e despesas
- Transações de exemplo

Para iniciar com banco vazio, remova ou renomeie o arquivo `src/main/resources/data.sql`.

---

## 🔧 Configuração para PostgreSQL (Produção)

### 1. Criar banco de dados:

```sql
CREATE DATABASE gestao_financeira
    WITH 
    ENCODING = 'UTF8';
```

### 2. Alterar application.properties:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestao_financeira
spring.datasource.username=postgres
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

---

## 📈 Melhorias Futuras

- [ ] Autenticação e autorização (Spring Security + JWT)
- [ ] Múltiplos usuários
- [ ] Gráficos e dashboards
- [ ] Exportação de relatórios (PDF/Excel)
- [ ] Metas financeiras
- [ ] Notificações de gastos
- [ ] Mais testes unitários e de integração
- [ ] Deploy (Railway, Render, AWS)
- [ ] Frontend (React ou Angular)

---

## 👨‍💻 Autor

Fernanda Lima

📧 Email: [ffernandalima.ads@gmail.com](mailto:ffernandalima.ads@gmail.com)

🔗 LinkedIn: https://www.linkedin.com/in/fernandaliima/

💻 GitHub: [@efernandalima](https://github.com/efernandalima)

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

---

## ⭐ Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.
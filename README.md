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

### Banco de Dados
- **H2 Database** (desenvolvimento)
- **PostgreSQL** (produção)

### Documentação
- **Swagger/OpenAPI 3.0** (documentação interativa da API)

### Ferramentas
- **Maven** (gerenciamento de dependências)
- **Git** (controle de versão)

---

## 📁 Arquitetura do Projeto

```text
src/main/java/br/com/financeiro/
├── model/ # Entidades JPA
│   ├── Categoria.java
│   ├── Transacao.java
│   └── TipoTransacao.java
├── repository/ # Repositórios Spring Data
│   ├── CategoriaRepository.java
│   └── TransacaoRepository.java
├── service/ # Regras de negócio
│   ├── CategoriaService.java
│   ├── TransacaoService.java
│   └── RelatorioService.java
├── controller/ # Endpoints REST
│   ├── CategoriaController.java
│   ├── TransacaoController.java
│   └── RelatorioController.java
├── exception/ # Tratamento de erros
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
└── config/ # Configurações
    └── SwaggerConfig.java
```

**Padrão de arquitetura em camadas:**
- **Controller** → recebe requisições HTTP
- **Service** → aplica regras de negócio
- **Repository** → acessa banco de dados
- **Model** → representa entidades

---

## ⚙️ Como Executar

### **Pré-requisitos**
- Java 17 ou superior
- Maven 3.6+
- PostgreSQL 15+ (opcional - H2 já configurado)

### **1. Clonar o repositório**
```bash
git clone https://github.com/efernandalima/gestao-financeira.git
cd gestao-financeira
```

### **2. Configurar banco de dados (opcional)**

Para usar H2 (padrão):
Não precisa fazer nada! O projeto já vem configurado.

Para usar PostgreSQL:

```sql
CREATE DATABASE gestao_financeira;
```

Edite `src/main/resources/application.properties`:

```text
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
- **API:** http://localhost:8081
- **Swagger UI:** http://localhost:8081/swagger-ui.html
- **H2 Console:** http://localhost:8081/h2-console
    - **JDBC URL:** `jdbc:h2:mem:financeiro`
    - **User:** `sa`
    - **Password:** *(vazio)*

---

## 📚 Documentação da API

### Swagger UI
Acesse a documentação interativa completa em:

```text
http://localhost:8081/swagger-ui.html
```

### Endpoints Principais

#### Categorias
### Endpoints Principais

#### **Categorias**
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/categorias` | Listar todas as categorias |
| GET | `/api/categorias/{id}` | Buscar categoria por ID |
| GET | `/api/categorias/tipo/{tipo}` | Listar por tipo (RECEITA/DESPESA) |
| POST | `/api/categorias` | Criar nova categoria |
| PUT | `/api/categorias/{id}` | Atualizar categoria |
| DELETE | `/api/categorias/{id}` | Deletar categoria |

#### **Transações**
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/transacoes` | Listar todas as transações |
| GET | `/api/transacoes/{id}` | Buscar transação por ID |
| GET | `/api/transacoes/tipo/{tipo}` | Listar por tipo |
| GET | `/api/transacoes/periodo` | Listar por período |
| GET | `/api/transacoes/categoria/{id}` | Listar por categoria |
| GET | `/api/transacoes/saldo` | Calcular saldo |
| POST | `/api/transacoes` | Criar nova transação |
| PUT | `/api/transacoes/{id}` | Atualizar transação |
| DELETE | `/api/transacoes/{id}` | Deletar transação |

#### **Relatórios**
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/relatorios/periodo` | Gerar relatório completo |


---

## 🧪 Exemplos de Uso

### 1. Criar uma Categoria
```bash
POST http://localhost:8081/api/categorias
Content-Type: application/json

{
  "nome": "Salário",
  "descricao": "Salário mensal",
  "tipo": "RECEITA"
}
```

### 2. Criar uma Receita
```bash
POST http://localhost:8081/api/transacoes
Content-Type: application/json

{
  "descricao": "Salário Janeiro",
  "valor": 5000.00,
  "data": "2026-01-05",
  "tipo": "RECEITA",
  "categoria": {
    "id": 1
  },
  "observacao": "Salário do mês"
}
```

### 3. Gerar Relatório do Mês
```bash
GET http://localhost:8081/api/relatorios/periodo?inicio=2026-01-01&fim=2026-01-31
```

Resposta:

```json
{
  "periodo": {
    "inicio": "2026-01-01",
    "fim": "2026-01-31"
  },
  "totalReceitas": 6500.00,
  "totalDespesas": 1900.00,
  "saldo": 4600.00,
  "receitas": ["..."],
  "despesas": ["..."]
}
```

---

## 🎯 Funcionalidades Implementadas

### Regras de Negócio
✅ Validação de dados (Bean Validation)  
✅ Categoria compatível com tipo da transação  
✅ Tratamento de exceções personalizado  
✅ Cálculos financeiros (saldo, totais por período)  
✅ Queries otimizadas (JPQL)

### Boas Práticas
✅ Código limpo e organizado (Clean Code)  
✅ Separação em camadas (MVC)  
✅ Injeção de dependência  
✅ DTOs quando necessário  
✅ Lombok para reduzir boilerplate  
✅ Documentação Swagger automática  
✅ Tratamento global de erros

---

## 📊 Dados de Exemplo

O projeto inclui dados de exemplo em `data.sql`:

- 10 categorias (4 receitas, 6 despesas)
- 5 transações de exemplo

Para iniciar com banco vazio, remova ou renomeie o arquivo `src/main/resources/data.sql`

---

## 🔧 Configuração para PostgreSQL (Produção)

### 1. Criar banco de dados:
```sql
CREATE DATABASE gestao_financeira
    WITH 
    ENCODING = 'UTF8';
```

### 2. Alterar application.properties:
```text
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
- [ ] Testes unitários e de integração
- [ ] Deploy (Heroku, AWS, Railway)
- [ ] Frontend (React ou Angular)

---

## 👨‍💻 Autor

Fernanda Lima

📧 Email: ffernandalima.ads@gmail.com

🔗 LinkedIn:https://www.linkedin.com/in/fernandaliima/

💻 GitHub: @efernandalima

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

---

## ⭐ Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.

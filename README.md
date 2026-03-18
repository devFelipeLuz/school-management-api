# 📚 School Management API

API REST desenvolvida com Java e Spring Boot para gerenciamento escolar.

O sistema permite gerenciar alunos, turmas, matrículas, avaliações, presença e autenticação com segurança baseada em JWT.

---

## 🚀 Tecnologias

* Java 17+
* Spring Boot
* Spring Data JPA / Hibernate
* Spring Security + JWT
* PostgreSQL
* Maven
* Swagger / OpenAPI
* Testcontainers
* JUnit + MockMvc

---

## 📐 Arquitetura

O projeto segue uma arquitetura em camadas:

* **Controller** → exposição da API REST
* **Service** → regras de negócio (orquestração)
* **Entity** → regras de domínio
* **Repository** → acesso ao banco
* **DTO / Mapper** → comunicação externa
* **Security** → autenticação e autorização
* **Exception** → tratamento global de erros

---

## 🔐 Autenticação

A autenticação é feita via JWT.

### Fluxo:

1. Login com email e senha
2. Recebe:

   * Access Token (JWT)
   * Refresh Token
3. Usa o access token nas requisições protegidas
4. Pode renovar via `/auth/refresh`

---

## 🔎 Active vs Inactive Records

A API utiliza **soft delete**.

### ✔ Comportamento:

* Registros **não são removidos do banco**
* Registros podem estar:

  * `active = true`
  * `active = false`

### 📌 Importante:

* A API **retorna tanto registros ativos quanto inativos por padrão**
* Registros inativos **continuam acessíveis para consulta**
* Porém, **não podem participar de regras de negócio**

### Exemplos:

* ❌ aluno inativo não pode se matricular
* ❌ matrícula cancelada não pode receber nota

---

## 🔍 Filtros

É possível filtrar por status:

```http
GET /students?active=true
GET /students?active=false
```

---

## 📚 Principais Recursos

### 👨‍🎓 Students

* `POST /students` → criar aluno
* `GET /students` → listar alunos
* `GET /students/{id}` → buscar por id (ativo ou inativo)
* `PATCH /students/{id}` → atualizar
* `DELETE /students/{id}` → desativar (soft delete)

---

### 🏫 Classrooms

* Gerenciamento de turmas
* Controle de capacidade
* Contador de matrículas ativas

---

### 📝 Enrollments

* Matrícula de alunos em turmas

#### Regras:

* aluno deve estar ativo
* turma deve ter capacidade

#### Endpoints:

* `POST /enrollments`
* `PATCH /enrollments/{id}/cancel`
* `PATCH /enrollments/{id}/finish`

---

### 📊 Assessments & Grades

* Criação de avaliações
* Lançamento de notas

#### Validações:

* mesma turma
* matrícula ativa
* nota dentro do limite

---

### 📅 Attendance

* Registro de presença por sessão
* Controle por aluno e data

---

### 🔑 Auth

* `POST /auth/login`
* `POST /auth/refresh`
* `POST /auth/logout`
* `POST /auth/forgot-password`
* `POST /auth/reset-password`

---

## 🧪 Testes

O projeto possui:

### ✔ Testes de Integração

* Controllers testados com MockMvc
* Banco real via Testcontainers

Exemplos:

* StudentControllerIT
* EnrollmentControllerIT
* AuthControllerIT

---

### ✔ Testes Unitários

* Services testados isoladamente
* Uso de mocks para dependências

---

## ⚠️ Tratamento de Erros

A API possui tratamento global com respostas padronizadas:

* `400` → erro de negócio
* `404` → recurso não encontrado
* `422` → erro de validação
* `500` → erro inesperado

---

## 🐳 Rodando com Docker

O projeto disponibiliza um banco PostgreSQL via Docker.

### 📄 docker-compose.yml

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: school-db
    restart: always
    environment:
      POSTGRES_DB: school
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

---

### ▶️ Subir o banco

```bash
docker-compose up -d
```

---

### ⛔ Parar o banco

```bash
docker-compose down
```

---

### 📌 Configuração do banco

* Host: localhost
* Porta: 5432
* Database: school
* User: postgres
* Password: postgres

---

## 📄 Documentação

Swagger disponível em:

```
http://localhost:8080/swagger-ui.html
```

---

## 🧠 Regras de Negócio (Resumo)

* Aluno inativo não pode se matricular
* Turma não pode exceder capacidade
* Matrícula cancelada reduz contador da turma
* Nota deve respeitar limite da avaliação
* Tokens possuem expiração e podem ser revogados

---

## 🏁 Como rodar o projeto

```bash
# build
mvn clean install

# rodar aplicação
mvn spring-boot:run
```

---

## 👨‍💻 Autor

Projeto desenvolvido como prática de backend com foco em boas práticas, arquitetura limpa e DDD pragmático.

# Omnichannel Orders API

REST API para gestão de pedidos multicanal do restaurante **Raízes do Nordeste**, desenvolvida como projeto multidisciplinar.

---

## Requisitos

| Ferramenta | Versão mínima |
|---|---|
| Java (JDK) | 21 |
| Maven | 3.9+ (ou usar o wrapper `./mvnw`) |
| PostgreSQL | 15+ |

---

## Configuração de variáveis de ambiente

**1. Copie o arquivo de exemplo:**

```bash
cp src/main/resources/application-local.properties.example \
   src/main/resources/application-local.properties
```

**2. Edite o arquivo e preencha os valores:**

```properties
# Banco de dados
DB_HOST=localhost
DB_PORT=5432
DB_NAME=omnichannel_db
DB_USERNAME=postgres
DB_PASSWORD=sua_senha

# Conta admin criada automaticamente na primeira execução
ADMIN_EMAIL=admin@raizes.com
ADMIN_PASSWORD=SenhaSegura@123
ADMIN_NAME=System Admin

# JWT — mínimo 32 caracteres
JWT_SECRET=troque-por-uma-chave-secreta-longa-aqui!!

# Segredo do gateway de pagamento (webhook)
GATEWAY_SECRET=segredo-do-gateway-aqui
```

> Variáveis opcionais com padrão:
> - `JWT_EXPIRATION_MS` — padrão `86400000` (24h)
> - `SERVER_PORT` — padrão `8080`

---

## Criação do banco de dados

Conecte ao PostgreSQL e crie o banco:

```sql
CREATE DATABASE omnichannel_db;
```

As tabelas e o seed inicial são criados automaticamente pelo **Flyway** na primeira execução da aplicação. Não é necessário rodar scripts manualmente.

**Migrations executadas:**

| Versão | Descrição |
|---|---|
| V1 | Tabela `users` |
| V2 | Tabelas `categories` e `products` |
| V3 | Tabelas `units`, `stock`, `orders` e `order_items` |
| V4 | Tabela `payments` |
| V5 | Tabela `consents` (LGPD) |
| V6 | Tabela `audit_logs` |
| V7 | Seed: unidade, categorias, produtos e estoque iniciais |

---

## Instalação de dependências e execução

**Instalar dependências:**

```bash
./mvnw dependency:resolve
```

**Iniciar a aplicação:**

```bash
./mvnw spring-boot:run
```

Ou gerar o JAR e executar:

```bash
./mvnw clean package -DskipTests
java -jar target/omnichannel-orders-api-0.0.1-SNAPSHOT.jar
```

A API estará disponível em `http://localhost:8080`.

---

## Documentação da API (Swagger)

Com a aplicação rodando, acesse:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

Para testar endpoints protegidos no Swagger:
1. Faça login em `POST /auth/login` e copie o token retornado
2. Clique em **Authorize** (cadeado no topo)
3. Cole o token no campo `bearerAuth` e confirme

---

## Primeiros passos após subir a aplicação

A conta de administrador é criada automaticamente na primeira execução usando as variáveis `ADMIN_EMAIL`, `ADMIN_PASSWORD` e `ADMIN_NAME`. Com ela você pode:

- Criar contas de staff via `POST /admin/users` (roles: `ATTENDANT`, `KITCHEN`, `MANAGER`)
- Os dados de catálogo (unidade, categorias, produtos, estoque) já estão disponíveis via seed (V7)
- Clientes se cadastram via `POST /auth/register/customer`

---

## Estrutura do projeto

```
src/
├── main/
│   ├── java/.../
│   │   ├── api/            # Controllers, DTOs de request/response, exceções
│   │   ├── application/    # Services e DTOs de aplicação
│   │   ├── domain/         # Entidades, enums e models
│   │   └── infrastructure/ # Repositórios, configurações, segurança
│   └── resources/
│       ├── db/migration/   # Scripts Flyway (V1–V7)
│       ├── application.properties
│       └── application-local.properties.example
```

---

## Variáveis de ambiente — referência completa

| Variável | Obrigatória | Descrição |
|---|---|---|
| `DB_HOST` | ✅ | Host do PostgreSQL |
| `DB_PORT` | ✅ | Porta do PostgreSQL |
| `DB_NAME` | ✅ | Nome do banco de dados |
| `DB_USERNAME` | ✅ | Usuário do banco |
| `DB_PASSWORD` | ✅ | Senha do banco |
| `ADMIN_EMAIL` | ✅ | E-mail da conta admin inicial |
| `ADMIN_PASSWORD` | ✅ | Senha da conta admin inicial |
| `ADMIN_NAME` | ✅ | Nome da conta admin inicial |
| `JWT_SECRET` | ✅ | Chave de assinatura JWT (mín. 32 chars) |
| `GATEWAY_SECRET` | ✅ | Token de autenticação do webhook de pagamento |
| `JWT_EXPIRATION_MS` | ❌ | Expiração do JWT em ms (padrão: 86400000) |
| `SERVER_PORT` | ❌ | Porta da aplicação (padrão: 8080) |

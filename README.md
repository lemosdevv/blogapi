# BlogAPI

BlogAPI é uma API RESTful segura para uma plataforma de blog construída com Spring Boot. Ela oferece recursos como autenticação e autorização de usuários usando JWT, controle de acesso baseado em papéis, e gerenciamento de posts, categorias, tags, comentários, álbuns, fotos e tarefas (todos).

## Funcionalidades

- Registro e login de usuários com autenticação JWT
- Controle de acesso baseado em papéis (USER, ADMIN)
- Operações CRUD para posts de blog com paginação e filtragem por categoria e tag
- Gerenciamento de categorias, tags, comentários, álbuns, fotos e tarefas
- Integração com banco de dados MySQL usando Flyway para migrações
- Validação e tratamento de erros
- Armazenamento seguro de senhas com criptografia

## Pré-requisitos

- Java 21
- Maven 3.6+
- Servidor MySQL rodando localmente ou acessível remotamente

## Segurança

- Utiliza tokens JWT para autenticação sem estado
- Senhas armazenadas de forma segura com hash
- Controle de acesso baseado em papéis para restringir endpoints

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.4
- Spring Security com JWT
- Spring Data JPA com Hibernate
- MySQL
- Flyway para migrações de banco de dados
- Maven para gerenciamento de dependências e build
- ModelMapper para mapeamento DTO
- Lombok para redução de código boilerplate

## Instalação e Configuração

1. Clone o repositório:

   ```bash
   git clone <url-do-repositorio>
   cd blogapi
   ```

2. Configure a conexão com o banco de dados no arquivo `src/main/resources/application.yml`:

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/blogapi
       username: root
       password: sua_senha
       driver-class-name: com.mysql.cj.jdbc.Driver
   ```

3. Certifique-se que o servidor MySQL está rodando e crie o banco de dados `blogapi` caso não exista:

   ```sql
   CREATE DATABASE blogapi;
   ```

4. A aplicação usa Flyway para executar automaticamente as migrações do banco de dados na inicialização.

## Configuração

- O segredo JWT e o tempo de expiração podem ser configurados no `application.yml` na seção `app`:

  ```yaml
  app:
    jwtSecret: "sua_chave_secreta_jwt"
    jwtExpirationInMs: 3600000
  ```

- Ajuste outras configurações como porta do servidor ou logging conforme necessário.

## Migração do Banco de Dados

Flyway está configurado para gerenciar as migrações do esquema do banco de dados. Os scripts de migração estão localizados em `src/main/resources/migration_db`. Ao iniciar a aplicação, o Flyway aplicará as migrações pendentes no banco MySQL configurado.

## Executando a Aplicação

Compile e execute a aplicação usando Maven:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

## Visão Geral dos Endpoints da API

### Autenticação

- `POST /api/auth/signin` - Autentica usuário e retorna token JWT
- `POST /api/auth/signup` - Registra um novo usuário

### Posts

- `GET /api/post` - Lista todos os posts com paginação
- `GET /api/post/category/{id}` - Lista posts por categoria com paginação
- `GET /api/post/tag/{id}` - Lista posts por tag com paginação
- `GET /api/post/{id}` - Obtém um post pelo ID
- `POST /api/post` - Cria um novo post (requer papel USER)
- `PUT /api/post/{id}` - Atualiza um post (requer papel USER ou ADMIN)
- `DELETE /api/post/{id}` - Deleta um post (requer papel USER ou ADMIN)

### Outros Recursos

A API também suporta gerenciamento de categorias, tags, comentários, álbuns, fotos e tarefas através dos respectivos endpoints (consulte os controladores no código-fonte para detalhes).

## Exemplos de Requisições JSON Válidas

### Cadastro de Usuário - `/api/auth/signup`

```json
{
	"firstName": "Leanne",
	"lastName": "Graham",
	"username": "leanne",
	"password": "password",
	"email": "leanne.graham@gmail.com"
}
```

### Login - `/api/auth/signin`

```json
{
	"usernameOrEmail": "leanne",
	"password": "password"
}
```

### Criar Post - `/api/post`

```json
{
	"title": "Titulo do Seu post",
	"body": "Decrição do seu post",
}
```

### Atualizar Post - `/api/post/{id}`

```json
{
	"title": "POST ATUALIZADO",
	"body": "Conteúdo atualizado do post."
}
```

### Criar Comentário - `/api/posts/{postId}/comments`

```json
{
	"body": "Comentário de exemplo para o post."
}
```

### Criar Álbum - `/api/albums`

```json
{
	"title": "Meu Álbum"
}
```

### Criar Foto - `/api/photos`

```json
{
	"title": "Foto Exemplo",
	"url": "https://via.placeholder.com/600/92c952",
	"thumbnailUrl": "https://via.placeholder.com/150/92c952",
	"albumId": 2
}
```

### Criar Tarefa (Todo) - `/api/todos`

```json
{
	"title": "Tarefa Exemplo",
	"completed": false
}
```

## Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo LICENSE para detalhes.

## Contato

Para dúvidas ou suporte, entre em contato com o mantenedor do projeto..

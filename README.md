
# User Management System

Este projeto é uma aplicação Spring Boot para gerenciamento de usuários. Ele oferece endpoints para registro e autenticação de usuários com validações e criptografia de senhas.

Alunos:

- João Antônio de Lima Carrazzoni – 01523593 
- Luann Henrique Sousa de Lucas – 01356035 
- Roberto Henrique Cavalcanti Freitas - 01536220
---

## Recursos

1. **Registro de Usuários**
   - Validação de nome, e-mail e senha.
   - Criptografia de senha usando BCrypt.
   - Verificação de e-mails duplicados.

2. **Autenticação de Usuários**
   - Login via e-mail e senha.
   - Retorno de erros detalhados para credenciais inválidas.

3. **Configurações de Segurança**
   - Suporte a autenticação básica HTTP.
   - CSRF desativado para simplificação (não recomendado em produção).

4. **Banco de Dados**
   - Banco de dados H2 em memória para desenvolvimento.
   - Configuração via `application.properties`.

---

## Tecnologias Utilizadas

- **Java 17**: Linguagem principal.
- **Spring Boot 3**: Framework para desenvolvimento rápido.
  - Spring Security para autenticação e segurança.
  - Spring Data JPA para persistência.
- **H2 Database**: Banco de dados em memória para testes.
- **JUnit 5**: Framework de testes.
- **Mockito**: Mocking em testes unitários.

---

## Estrutura do Projeto

```plaintext
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── config/         # Configurações de segurança
│   │   ├── controller/     # Controladores REST
│   │   ├── dto/            # Objetos de Transferência de Dados (DTOs)
│   │   ├── exception/      # Classes de exceção personalizadas
│   │   ├── model/          # Entidades do banco de dados
│   │   ├── repository/     # Interfaces de repositório JPA
│   │   └── service/        # Lógica de negócios e serviços
│   └── resources/
│       ├── application.properties  # Configurações do Spring Boot
│       └── data.sql        # Dados iniciais (se necessário)
└── test/                   # Testes unitários
```

---

## Endpoints Disponíveis

### **Registro de Usuários**

- **URL**: `/api/users/register`
- **Método**: `POST`
- **Request Body**:
  ```json
  {
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password1"
  }
  ```
- **Response**: Retorna o usuário registrado ou erro de validação.

### **Login de Usuários**

- **URL**: `/api/users/login`
- **Método**: `POST`
- **Request Body**:
  ```json
  {
    "email": "john.doe@example.com",
    "password": "password1"
  }
  ```
- **Response**: Retorna os dados do usuário autenticado ou erro.

---

## Validações de Senha

- Mínimo de 8 caracteres.
- Deve conter pelo menos uma letra e um número.
- Exemplo de senha válida: `password1`.

---

## Configurações

### Banco de Dados
Arquivo `application.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

### Segurança
- **Autenticação básica** está habilitada.
- **CSRF** está desabilitado (não recomendado em produção).

---

## Testes

O projeto possui uma cobertura abrangente de testes, garantindo a funcionalidade e qualidade do código.

### Tipos de Testes

1. **Testes Unitários**:
   - Verificam o comportamento individual de métodos e classes.
   - Exemplos:
     - Validação de regras de negócio no `UserService`.
     - Exceções personalizadas como `EmailAlreadyExistsException` e `InvalidCredentialsException`.

2. **Testes de Controladores**:
   - Simulam requisições HTTP para os endpoints REST.
   - Validam a interação entre controladores e serviços.
   - Exemplos:
     - Testar o sucesso e falha no registro de usuários.
     - Testar autenticação de usuários.

3. **Testes de Integração** (sugerido como melhoria futura):
   - Verificar a integração completa do sistema, incluindo persistência e lógica de negócios.

### Ferramentas Utilizadas
- **JUnit 5**: Framework para criação de testes.
- **Mockito**: Criação de mocks para isolamento de dependências.

### Executando Testes
Use o comando abaixo para executar todos os testes:
```bash
./mvnw test
```

### Exemplos de Testes Implementados

#### Teste de Sucesso no Registro
```java
@Test
public void testRegisterSuccess() {
    User user = new User("John Doe", "john.doe@example.com", "password1");

    when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
    when(userRepository.save(any(User.class))).thenReturn(user);

    User registeredUser = userService.register(user);

    assertNotNull(registeredUser);
    assertEquals("John Doe", registeredUser.getName());
    verify(userRepository, times(1)).save(any(User.class));
}
```

#### Teste de Erro no Login
```java
@Test
public void testLoginInvalidPassword() {
    String email = "john.doe@example.com";
    String password = "wrongpassword";
    String encodedPassword = passwordEncoder.encode("password1");

    User user = new User("John Doe", email, encodedPassword);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
        userService.login(email, password);
    });

    assertEquals("Email ou senha incorreto.", exception.getMessage());
    verify(userRepository, times(1)).findByEmail(email);
}
```

---

## Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/heyluannlucas/qualidade.git
   ```
2. Entre no diretório do projeto:
   ```bash
   cd demo
   ```
3. Execute o projeto:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Acesse o console H2 (opcional):
   - URL: `http://localhost:8080/h2-console`
   - Usuário: `sa`
   - Senha: *(vazia)*

---

## Melhorias Futuras

- Implementar autenticação baseada em tokens (JWT).
- Melhorar as mensagens de erro para internacionalização (i18n).
- Adicionar testes de integração.





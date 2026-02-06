# DIP Exercise - Desacoplamento de Lógica de Reset de Senha

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-Project-blue.svg)](https://maven.apache.org/)
[![SOLID](https://img.shields.io/badge/SOLID-Principles-green.svg)](https://en.wikipedia.org/wiki/SOLID)

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Objetivo](#objetivo)
- [Princípios SOLID Implementados](#princípios-solid-implementados)
  - [Dependency Inversion Principle (DIP)](#dependency-inversion-principle-dip)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Como o DIP foi Implementado](#como-o-dip-foi-implementado)
- [Diagrama de Arquitetura](#diagrama-de-arquitetura)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Como Executar](#como-executar)
- [Contato](#contato)

## 🎯 Sobre o Projeto

Este repositório contém uma implementação prática dos princípios SOLID, com foco especial no **Dependency Inversion Principle (DIP)**, aplicado em um sistema de recuperação de senha. O projeto demonstra como desacoplar a lógica de negócio das implementações concretas de notificação, permitindo maior flexibilidade e manutenibilidade do código.

## 🎓 Objetivo

O objetivo principal deste exercício é demonstrar como aplicar o **Princípio da Inversão de Dependência** para:

- ✅ Desacoplar a lógica de reset de senha dos mecanismos de notificação
- ✅ Facilitar a adição de novos métodos de envio de notificações (email, SMS, push, etc.)
- ✅ Melhorar a testabilidade do código
- ✅ Reduzir o acoplamento entre as camadas da aplicação
- ✅ Aumentar a flexibilidade e manutenibilidade do sistema

## 🏗️ Princípios SOLID Implementados

### Dependency Inversion Principle (DIP)

O **DIP** estabelece que:

> *"Módulos de alto nível não devem depender de módulos de baixo nível. Ambos devem depender de abstrações. Abstrações não devem depender de detalhes. Detalhes devem depender de abstrações."*

#### 🔴 Antes do DIP (Código Acoplado)

```java
public class PasswordResetter {
    private EmailPasswordRecovery emailSender; // Dependência direta da implementação
    
    public void reset(User user) {
        // Lógica acoplada ao email
        emailSender.sendEmail(user.getEmail(), "Link de reset");
    }
}
```

**Problemas:**
- ❌ Difícil adicionar SMS ou outros canais
- ❌ Impossível testar sem enviar emails reais
- ❌ Alto acoplamento entre camadas
- ❌ Violação do DIP

#### 🟢 Depois do DIP (Código Desacoplado)

```java
// Interface (Abstração) no domínio
public interface NotificationSender {
    void send(String destiny, String message);
}

// Classe de aplicação depende da abstração
public class PasswordResetter {
    private NotificationSender sender; // Depende da abstração!
    
    public PasswordResetter(NotificationSender sender) {
        this.sender = sender;
    }
    
    public void reset(User user) {
        String link = "http://techstore.com/reset?token=123";
        sender.send(user.getEmail(), "Seu link: " + link);
    }
}

// Implementações concretas na infraestrutura
public class EmailPasswordRecovery implements NotificationSender {
    @Override
    public void send(String destiny, String message) {
        // Implementação de envio por email
    }
}

public class SmsPasswordRecovery implements NotificationSender {
    @Override
    public void send(String destiny, String message) {
        // Implementação de envio por SMS
    }
}
```

**Benefícios:**
- ✅ `PasswordResetter` não conhece implementações concretas
- ✅ Fácil adicionar novos canais (WhatsApp, Push, etc.)
- ✅ Testável com mocks/stubs
- ✅ Baixo acoplamento entre camadas
- ✅ Conformidade com DIP

### Outros Princípios SOLID

#### Single Responsibility Principle (SRP)
- `PasswordResetter`: responsável apenas pela lógica de reset
- `NotificationSender`: responsável apenas pelo envio de notificações
- `User`: responsável apenas pela representação de usuário

#### Open/Closed Principle (OCP)
- O sistema está **aberto para extensão** (novos senders podem ser adicionados)
- O sistema está **fechado para modificação** (não precisa alterar `PasswordResetter` para adicionar novos canais)

#### Liskov Substitution Principle (LSP)
- Qualquer implementação de `NotificationSender` pode substituir outra sem quebrar o sistema

#### Interface Segregation Principle (ISP)
- Interface `NotificationSender` é específica e coesa, contendo apenas o método necessário

## 📁 Estrutura do Projeto

```
dip-exercise/
├── src/
│   └── main/
│       └── java/
│           └── pablo/
│               └── tzeliks/
│                   ├── Main.java
│                   ├── application/
│                   │   └── PasswordResetter.java          # Lógica de aplicação
│                   ├── domain/
│                   │   ├── entity/
│                   │   │   ├── User.java                   # Entidade de domínio
│                   │   │   └── vo/
│                   │   │       └── Password.java           # Value Object
│                   │   └── sender/
│                   │       └── NotificationSender.java     # Interface (Abstração)
│                   └── infrastructure/
│                       └── sender/
│                           ├── emailPasswordRecovery.java  # Implementação Email
│                           └── smsPasswordRecovery.java    # Implementação SMS
├── pom.xml
└── README.md
```

### Camadas da Arquitetura

#### 🔷 Domain (Domínio)
- Contém as **abstrações** e regras de negócio
- **Não depende de nenhuma outra camada**
- Define a interface `NotificationSender`
- Contém entidades e value objects

#### 🔶 Application (Aplicação)
- Contém os casos de uso
- **Depende apenas do Domain** (abstrações)
- Implementa `PasswordResetter` usando `NotificationSender`

#### 🔴 Infrastructure (Infraestrutura)
- Contém implementações concretas
- **Depende do Domain** para implementar interfaces
- Implementa `EmailPasswordRecovery` e `SmsPasswordRecovery`

## 🔧 Como o DIP foi Implementado

### 1️⃣ Interface no Domínio

```java
// domain/sender/NotificationSender.java
public interface NotificationSender {
    void send(String destiny, String message);
}
```

A interface está no **domínio**, não na infraestrutura. Isso é crucial para o DIP!

### 2️⃣ Aplicação Depende da Abstração

```java
// application/PasswordResetter.java
public class PasswordResetter {
    private NotificationSender sender;
    
    public PasswordResetter(NotificationSender sender) {
        this.sender = sender;  // Injeção de dependência
    }
    
    public void reset(User user) {
        String link = "http://techstore.com/reset?token=123";
        sender.send(user.getEmail(), "Seu link: " + link);
    }
}
```

### 3️⃣ Infraestrutura Implementa a Abstração

```java
// infrastructure/sender/emailPasswordRecovery.java
public class emailPasswordRecovery implements NotificationSender {
    @Override
    public void send(String destiny, String message) {
        System.out.println("Your recovery Code was sent to " + destiny + ", message " + message);
    }
}

// infrastructure/sender/smsPasswordRecovery.java
public class smsPasswordRecovery implements NotificationSender {
    @Override
    public void send(String destiny, String message) {
        System.out.println("Your recovery Code was sent to " + destiny + ", message " + message);
    }
}
```

### 4️⃣ Injeção de Dependência em Tempo de Execução

```java
// Main.java (exemplo de uso)
public class Main {
    public static void main(String[] args) {
        // Escolha a implementação em tempo de execução
        NotificationSender emailSender = new emailPasswordRecovery();
        PasswordResetter resetter = new PasswordResetter(emailSender);
        
        User user = new User(1L, "user@example.com", "123456789", new Password("secret"));
        resetter.reset(user);
        
        // Ou troque para SMS sem modificar PasswordResetter
        NotificationSender smsSender = new smsPasswordRecovery();
        PasswordResetter smsResetter = new PasswordResetter(smsSender);
        smsResetter.reset(user);
    }
}
```

### ✨ Vantagens da Implementação

1. **Testabilidade**: Podemos criar mocks de `NotificationSender` para testar `PasswordResetter`
2. **Flexibilidade**: Novos canais podem ser adicionados sem alterar código existente
3. **Desacoplamento**: `PasswordResetter` não conhece detalhes de implementação
4. **Inversão de Dependência**: Dependência flui do concreto para o abstrato
5. **Manutenibilidade**: Mudanças em um canal não afetam outros ou a lógica de negócio

## 📐 Diagrama de Arquitetura

<!-- Espaço reservado para o diagrama de arquitetura -->
<!-- O diagrama será adicionado aqui -->

_Diagrama a ser adicionado mostrando a relação entre as camadas e a inversão de dependência._

## 🛠️ Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Maven** - Gerenciamento de dependências e build
- **Arquitetura em Camadas** - Domain, Application, Infrastructure
- **SOLID Principles** - Princípios de design orientado a objetos

## 🚀 Como Executar

### Pré-requisitos

- Java 21 ou superior
- Maven 3.6 ou superior

### Passos para Execução

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/PabloTzeliks/dip-exercise.git
   cd dip-exercise
   ```

2. **Compile o projeto:**
   ```bash
   mvn clean compile
   ```

3. **Execute a aplicação:**
   ```bash
   mvn exec:java -Dexec.mainClass="pablo.tzeliks.Main"
   ```

### Exemplo de Uso

```java
// Criar um usuário
User user = new User(1L, "user@example.com", "11987654321", new Password("myPassword123"));

// Usar notificação por email
NotificationSender emailSender = new emailPasswordRecovery();
PasswordResetter emailResetter = new PasswordResetter(emailSender);
emailResetter.reset(user);

// Usar notificação por SMS
NotificationSender smsSender = new smsPasswordRecovery();
PasswordResetter smsResetter = new PasswordResetter(smsSender);
smsResetter.reset(user);

// Adicionar WhatsApp no futuro?
// Basta criar: public class WhatsAppPasswordRecovery implements NotificationSender
// Sem modificar PasswordResetter!
```

## 📞 Contato

**Pablo Tzeliks**

- GitHub: [@PabloTzeliks](https://github.com/PabloTzeliks)
- Repository: [dip-exercise](https://github.com/PabloTzeliks/dip-exercise)

---

⭐ Se este projeto foi útil para você, considere dar uma estrela!

📚 Feito com o objetivo de demonstrar boas práticas de desenvolvimento com SOLID e DIP.

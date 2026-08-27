# Banking Challenge Java

A terminal-based banking application developed in Java as part of an Accenture programming challenge focused on Object-Oriented Programming, modular architecture, business validations, and software design best practices.

## Overview

The application simulates a banking system in memory. Users can create checking or savings accounts, authenticate with account number and password, perform banking operations, manage personal and card information, request financial products, invest money, and export transaction history to a CSV file.

The project applies separation of responsibilities across application, menu, service, entity, and exception packages. The `Program` class works as the application entry point, menu classes handle terminal interaction, services coordinate business operations, entities represent the banking domain, and custom exceptions describe validation failures.

## Features

### Account Management

- Create checking and savings accounts
- Register account holder, address, phone number, and email
- Validate duplicate account numbers
- Authenticate using account number and password
- Limit invalid password attempts
- Change password
- Change address
- Change phone number and email

### Banking Operations

- Deposit funds
- Withdraw funds
- Transfer funds between accounts
- Check account balance
- Check credit limit
- Request credit limit increase
- Reject zero or negative deposit, withdrawal, transfer, investment, and limit-increase values
- Prevent operations when available funds are insufficient

### Cards

- Automatically generate a card number when an account is created
- Automatically generate a CVV
- Automatically generate an expiration date five years from account creation
- Display card information
- Block a lost or stolen card
- Display card block status

> Card number, expiration date, and CVV are not included in the general account `toString()` output used during transfer confirmation.

### Loans

- Simulate a loan with installments, interest, and IOF calculation
- Display the effective loan cost and monthly payment
- Request a loan and deposit the requested amount into the account
- Register the loan deposit in transaction history
- Restrict the loan product to checking accounts

### Investments

- Save money in a CDI-based investment balance
- Restrict the investment product to checking accounts
- Validate the investment amount and available account balance
- Display the current invested balance
- Simulate the projected value after one year using the rate configured in the service
- Register the investment in transaction history

> The CDI projection is a simplified educational simulation and does not represent a real-time market rate or financial recommendation.

### Bill Payments

- Pay bills using a 47-character typeable line
- Extract the due-date factor and payment amount from the typeable line
- Apply overdue-payment adjustment
- Reject invalid or incomplete typeable lines
- Prevent payment when available funds are insufficient
- Register bill payments in transaction history

### Transactions

- Track deposits, withdrawals, transfers, loan deposits, bill payments, and CDI investments
- Timestamp every transaction
- Export account transaction history to a CSV file

### Transfer Restrictions

- Transfer amounts must be greater than zero
- The destination account must exist
- Transfers above R$ 1,000.00 are allowed only between 06:00 and 22:00
- The source account must have sufficient available funds

## Custom Exceptions

The project uses specific runtime exceptions instead of generic `IllegalArgumentException` handling:

- `AccountAlreadyExistsException`
- `AccountNotFoundException`
- `InsufficientFundsException`
- `InvalidAccountTypeException`
- `InvalidAddressException`
- `InvalidAmountException`
- `InvalidContactInfoException`
- `InvalidLoginException`
- `InvalidTypeableline`
- `TransferNotAllowedException`

Business validations are performed in services or domain entities. Menu classes catch the relevant exceptions and display their messages to the user.

## Technologies Used

- Java
- Eclipse IDE
- Git
- GitHub
- MermaidJS

## Programming Concepts Applied

- Object-Oriented Programming
- Inheritance
- Polymorphism
- Encapsulation
- Method overriding
- Enums
- Collections with `List` and `Map`
- Custom exception handling
- Service-layer separation
- Modular terminal menus
- File manipulation with CSV export
- Authentication
- Java Date and Time API
- In-memory data storage

## Project Structure

```text
src
├── application
│   └── Program.java
├── entities
│   ├── Account.java
│   ├── AccountType.java
│   ├── CheckingAccount.java
│   ├── SavingsAccount.java
│   └── Transaction.java
├── exceptions
│   ├── AccountAlreadyExistsException.java
│   ├── AccountNotFoundException.java
│   ├── InsufficientFundsException.java
│   ├── InvalidAccountTypeException.java
│   ├── InvalidAddressException.java
│   ├── InvalidAmountException.java
│   ├── InvalidContactInfoException.java
│   ├── InvalidLoginException.java
│   ├── InvalidTypeableline.java
│   └── TransferNotAllowedException.java
├── menu
│   ├── AccountMenu.java
│   ├── MainMenu.java
│   └── MenuUtils.java
└── services
    ├── AccountService.java
    └── LoanServices.java
```

## Architecture and Responsibilities

### `application`

Contains the application entry point. `Program` configures the locale, creates the in-memory account map and shared `Scanner`, and starts `MainMenu`.

### `menu`

Handles terminal input, navigation, confirmation prompts, success messages, and exception presentation.

- `MainMenu`: account creation, login, and initial navigation
- `AccountMenu`: authenticated banking operations
- `MenuUtils`: shared continuation behavior

### `services`

Coordinates business operations and validations.

- `AccountService`: account creation, login, deposits, withdrawals, transfers, credit limit requests, CSV export, bill payments, profile updates, and CDI investment
- `LoanServices`: loan simulation and loan request processing

### `entities`

Represents domain state and account-specific behavior.

- `Account`: common account, customer, card, balance, investment, and transaction data
- `CheckingAccount`: credit-limit behavior and withdrawal using balance plus credit limit
- `SavingsAccount`: interest-rate behavior and withdrawal limited to available balance
- `Transaction`: timestamped transaction record

### `exceptions`

Contains domain-specific runtime exceptions for invalid operations and business-rule violations.

## How to Run

### 1. Clone the repository

```bash
git clone https://github.com/ricardobfernandes/bank-challenge-java.git
```

### 2. Open the project

Import the project into Eclipse IDE as an existing Java project.

### 3. Execute

Run:

```text
application.Program
```

## How to Use

### Initial Menu

```text
1 - Create Account
2 - Log In To Your account
0 - Exit
```

### Create an Account

Provide:

- Account type
- Account holder name
- Initial balance
- Agency number
- Account number
- Password
- Address
- Cellphone number
- Email
- Credit limit for a checking account, or interest rate for a savings account

A card number, expiration date, CVV, and initial unblocked status are generated automatically.

### Authenticated Account Menu

```text
1  - Deposit
2  - Withdraw
3  - Transfer
4  - Check Balance
5  - Check Limit
6  - Request Limit
7  - Request Loan
8  - Transaction History
9  - Pay Bills
10 - Change Password
11 - Change Address
12 - Change Contact Information
13 - Block Card
14 - Show Card Information
15 - Save Money (CDI)
16 - Exit
```

## Transaction History

The CSV export uses the following columns:

```text
DateTime;Type;Amount
```

Example:

```text
17/08/2026 22:42:13;DEPOSIT;4500.0
17/08/2026 22:42:22;WITHDRAW;-4000.0
17/08/2026 22:44:01;LOAN_DEPOSIT;5000.0
17/08/2026 22:44:30;BILL_PAYMENT;-520.0
17/08/2026 22:45:10;CDI_INVESTMENT;-1000.0
```

The generated file follows this naming pattern:

```text
transactions_accountNumber_<account-number>.csv
```

## Bill Payment Examples

The typeable line must contain exactly 47 characters.

- Characters from position 33 to 36 represent the due-date factor used by the application
- The final 10 characters represent the payment amount in cents

Example 1:

```text
Value: R$ 500.00
Due date: 15/08/2026
75691305320108502503774915720010215390000050000
```

Example 2:

```text
Value: R$ 1,000.00
Due date: 25/08/2026
75691305320108502503774915720010215490000100000
```

## Class Diagram

```mermaid
classDiagram
    Account <|-- CheckingAccount
    Account <|-- SavingsAccount
    Account "1" *-- "*" Transaction

    Program --> MainMenu : starts
    MainMenu --> AccountMenu : opens after login
    MainMenu --> AccountService : uses
    AccountMenu --> AccountService : uses
    AccountMenu --> LoanServices : uses
    AccountService --> Account : manages
    LoanServices --> Account : manages loan

    class Program {
        +main(String[] args)
    }

    class MainMenu {
        -Scanner sc
        -Map~Integer, Account~ accounts
        -boolean running
        -boolean loggedIn
        -Account loggedAccount
        +start()
        -showOptions()
    }

    class AccountMenu {
        -Scanner sc
        -Map~Integer, Account~ accounts
        -Account loggedAccount
        -boolean running
        +start()
        -showMenu()
    }

    class MenuUtils {
        +Continuation(Scanner sc) boolean
    }

    class Account {
        -Integer agencyNumber
        -Integer accountNumber
        -String password
        -String address
        -String phoneNumber
        -String email
        -AccountType accountType
        -String accountHolder
        -String cardNumber
        -String cardExpirationDate
        -String cardCVV
        -boolean cardBlocked
        -Double investedBalance
        #Double balance
        #List~Transaction~ transactions
        +deposit(double amount)
        +withdraw(double amount)
        +addTransaction(String type, Double amount)
        +blockCard()
        +unblockCard()
    }

    class CheckingAccount {
        -Double creditLimit
        +increaseCreditLimit(Double amount)
        +withdraw(double amount)
    }

    class SavingsAccount {
        -Double interestRate
        +updateBalance()
        +withdraw(double amount)
    }

    class Transaction {
        -LocalDateTime dateTime
        -String type
        -Double amount
        +toString() String
    }

    class AccountService {
        +createAccount(Map accounts, Scanner sc) Account
        +login(Map accounts, Scanner sc) Account
        +deposit(Account account, double amount)
        +withdraw(Account account, double amount)
        +transfer(Account source, Map accounts, int destination, double amount)
        +requestLimit(Account account, double amount)
        +exportTransactions(Account account)
        +payBill(Account account, String typeableLine) double
        +changePassword(Account account, String currentPassword, String newPassword)
        +changeAddress(Account account, String newAddress)
        +changeContactInformation(Account account, String phone, String email)
        +investMoney(Account account, double amount) double
    }

    class LoanServices {
        +loanSimulation(Account account, double amount, int installments)
        +requestLoan(Account account, double amount)
    }
```

## Sequence Diagram: Transfer Operation

```mermaid
sequenceDiagram
    actor User
    participant AccountMenu
    participant AccountService
    participant SourceAccount
    participant DestinationAccount

    User->>AccountMenu: Select transfer and enter destination
    AccountMenu->>User: Request confirmation and amount
    User->>AccountMenu: Confirm and enter amount
    AccountMenu->>AccountService: transfer(source, accounts, destinationNumber, amount)

    alt Destination account does not exist
        AccountService-->>AccountMenu: AccountNotFoundException
        AccountMenu-->>User: Display validation message
    else Amount is zero or negative
        AccountService-->>AccountMenu: InvalidAmountException
        AccountMenu-->>User: Display validation message
    else Transfer violates time restriction
        AccountService-->>AccountMenu: TransferNotAllowedException
        AccountMenu-->>User: Display validation message
    else Insufficient available funds
        SourceAccount-->>AccountService: InsufficientFundsException
        AccountService-->>AccountMenu: Propagate exception
        AccountMenu-->>User: Display validation message
    else Valid transfer
        AccountService->>SourceAccount: withdraw(amount)
        AccountService->>DestinationAccount: deposit(amount)
        AccountService->>SourceAccount: addTransaction(TRANSFER_OUT)
        AccountService->>DestinationAccount: addTransaction(TRANSFER_IN)
        AccountService-->>AccountMenu: Transfer completed
        AccountMenu-->>User: Display success message
    end
```

## Sequence Diagram: CDI Investment

```mermaid
sequenceDiagram
    actor User
    participant AccountMenu
    participant AccountService
    participant Account

    User->>AccountMenu: Select Save Money (CDI)
    AccountMenu->>User: Request investment amount
    User->>AccountMenu: Enter amount
    AccountMenu->>AccountService: investMoney(account, amount)

    alt Savings account
        AccountService-->>AccountMenu: InvalidAccountTypeException
        AccountMenu-->>User: Display validation message
    else Invalid amount
        AccountService-->>AccountMenu: InvalidAmountException
        AccountMenu-->>User: Display validation message
    else Insufficient balance
        AccountService-->>AccountMenu: InsufficientFundsException
        AccountMenu-->>User: Display validation message
    else Valid investment
        AccountService->>Account: withdraw(amount)
        AccountService->>Account: update invested balance
        AccountService->>Account: addTransaction(CDI_INVESTMENT)
        AccountService-->>AccountMenu: Return one-year projected value
        AccountMenu-->>User: Display investment balance and projection
    end
```

## Design Improvements

The current version includes the following architectural improvements:

- The original main-class menu flow was separated into `MainMenu`, `AccountMenu`, and `MenuUtils`
- Business operations and validations were moved to service classes or the appropriate account entities
- Generic argument exceptions were replaced by domain-specific exceptions
- Zero and negative financial values are rejected before account state is changed
- Checking and savings accounts apply different withdrawal rules through method overriding
- Terminal interaction remains in menu classes while business rules remain in services and entities

## Author

**Ricardo Fernandes**  
Product Engineering Analyst

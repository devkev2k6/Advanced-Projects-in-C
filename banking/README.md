# Professional Bank Management System in C

A feature-rich, secure, and portfolio-ready **console-based Bank Management System** written in **pure C** — demonstrating clean code architecture, binary file persistence, input validation, transaction history, and basic security practices.


- C programming
- File I/O handling (binary files)
- Structured programming
- Input sanitization & error handling
- Real-world simulation logic

## ✨ Features

- **Unique auto-generated account numbers** (starting from 1001)
- **4-digit PIN protection** for all sensitive operations
- Full **CRUD** support:
  - Create new account
  - View account details & balance
  - Delete account (with confirmation via PIN)
- **Financial operations**:
  - Deposit
  - Withdraw (with sufficient balance check)
  - Transfer money between accounts
- **Complete transaction history** logging:
  - Timestamped entries
  - Type (Deposit / Withdrawal / Transfer In / Transfer Out / Account Created)
  - Amount & balance after each transaction
- Clean, formatted console UI with tables
- Persistent storage using binary files:
  - `account.dat` → account records
  - `transactions.dat` → audit trail
- Robust input validation & buffer clearing
- Professional error messages and user feedback

## 🛠️ Tech Stack

- Language: **C99 / C11**
- Standard Library only (no external dependencies)
- Binary file I/O (`fread`, `fwrite`, `fseek`)
- Time handling (`<time.h>`)
- String manipulation & input sanitizatiion.
### Prerequisites

- GCC or any standard C compiler
- Windows / Linux / macOS

### Build & Run

```bash
# Clone the repository
git clone https://github.com/devkev2k6/Advanced-Banking-System.git
cd bank-management-system-c

# Compile
gcc -o bank main.c

# Run
./bank          # Linux/macOS
bank.exe        # Windows

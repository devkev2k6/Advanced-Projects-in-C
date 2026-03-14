#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>

const char* ACCOUNT_FILE = "account.dat";
const char* TRANS_FILE = "transactions.dat";

typedef struct {
    char name[50];
    int acc_no;
    int pin;
    float balance;
} Account;

typedef struct {
    int acc_no;
    char type[20];
    float amount;
    char datetime[30];
    float balance_after;
} Transaction;

/* Helper to clear input buffer after scanf (prevents common C input issues) */
void clear_input_buffer(void) {
    int c;
    while ((c = getchar()) != '\n' && c != EOF);
}

/* Auto-generates unique account number (starts at 1001) */
int generate_account_number(void) {
    FILE *file = fopen(ACCOUNT_FILE, "rb");
    if (file == NULL) {
        return 1001;
    }
    Account acc;
    int max_acc = 1000;
    while (fread(&acc, sizeof(Account), 1, file) == 1) {
        if (acc.acc_no > max_acc) {
            max_acc = acc.acc_no;
        }
    }
    fclose(file);
    return max_acc + 1;
}

/* Logs every transaction with timestamp (append-only binary file) */
void log_transaction(int acc_no, const char* type, float amount, float balance_after) {
    FILE *file = fopen(TRANS_FILE, "ab");
    if (file == NULL) {
        return;
    }
    Transaction trans;
    trans.acc_no = acc_no;
    strcpy(trans.type, type);
    trans.amount = amount;
    time_t now = time(NULL);
    struct tm *tm_info = localtime(&now);
    strftime(trans.datetime, sizeof(trans.datetime), "%d-%m-%Y %H:%M:%S", tm_info);
    trans.balance_after = balance_after;
    fwrite(&trans, sizeof(Transaction), 1, file);
    fclose(file);
}

void create_account(void) {
    Account acc;
    acc.acc_no = generate_account_number();

    printf("\n=== Create New Account ===\n");
    clear_input_buffer();
    printf("Enter your full name: ");
    fgets(acc.name, sizeof(acc.name), stdin);
    acc.name[strcspn(acc.name, "\n")] = '\0';

    int pin;
    do {
        printf("Enter 4-digit PIN (1000-9999): ");
        scanf("%d", &pin);
        clear_input_buffer();
    } while (pin < 1000 || pin > 9999);
    acc.pin = pin;

    acc.balance = 0.0f;

    FILE *file = fopen(ACCOUNT_FILE, "ab");
    if (file == NULL) {
        printf("Error: Unable to open account file!\n");
        return;
    }
    fwrite(&acc, sizeof(acc), 1, file);
    fclose(file);

    log_transaction(acc.acc_no, "Account Created", 0.0f, 0.0f);

    printf("\nAccount created successfully!\n");
    printf("Account Number : %d\n", acc.acc_no);
    printf("PIN            : %d\n", acc.pin);
    printf("Initial Balance: Rs. 0.00\n");
}

void deposit_money(void) {
    int acc_no, pin;
    float amount;

    printf("\n=== Deposit Money ===\n");
    printf("Enter account number: ");
    scanf("%d", &acc_no);
    clear_input_buffer();
    printf("Enter PIN: ");
    scanf("%d", &pin);
    clear_input_buffer();
    printf("Enter deposit amount: ");
    scanf("%f", &amount);
    clear_input_buffer();

    if (amount <= 0.0f) {
        printf("Error: Amount must be positive!\n");
        return;
    }

    FILE *file = fopen(ACCOUNT_FILE, "rb+");
    if (file == NULL) {
        printf("Error: Unable to open account file!\n");
        return;
    }

    Account acc;
    bool found = false;
    bool pin_ok = false;

    while (fread(&acc, sizeof(acc), 1, file) == 1) {
        if (acc.acc_no == acc_no) {
            found = true;
            if (acc.pin == pin) {
                pin_ok = true;
                acc.balance += amount;
                fseek(file, -(long)sizeof(acc), SEEK_CUR);
                fwrite(&acc, sizeof(acc), 1, file);
                log_transaction(acc.acc_no, "Deposit", amount, acc.balance);
                printf("\nSuccess! Deposited Rs. %.2f\nNew balance: Rs. %.2f\n", amount, acc.balance);
                break;
            }
        }
    }
    fclose(file);

    if (!found) {
        printf("Error: Account %d not found.\n", acc_no);
    } else if (!pin_ok) {
        printf("Error: Invalid PIN!\n");
    }
}

void withdraw_money(void) {
    int acc_no, pin;
    float amount;

    printf("\n=== Withdraw Money ===\n");
    printf("Enter account number: ");
    scanf("%d", &acc_no);
    clear_input_buffer();
    printf("Enter PIN: ");
    scanf("%d", &pin);
    clear_input_buffer();
    printf("Enter withdrawal amount: ");
    scanf("%f", &amount);
    clear_input_buffer();

    if (amount <= 0.0f) {
        printf("Error: Amount must be positive!\n");
        return;
    }

    FILE *file = fopen(ACCOUNT_FILE, "rb+");
    if (file == NULL) {
        printf("Error: Unable to open account file!\n");
        return;
    }

    Account acc;
    bool found = false;
    bool pin_ok = false;

    while (fread(&acc, sizeof(acc), 1, file) == 1) {
        if (acc.acc_no == acc_no) {
            found = true;
            if (acc.pin == pin) {
                pin_ok = true;
                if (acc.balance >= amount) {
                    acc.balance -= amount;
                    fseek(file, -(long)sizeof(acc), SEEK_CUR);
                    fwrite(&acc, sizeof(acc), 1, file);
                    log_transaction(acc.acc_no, "Withdrawal", amount, acc.balance);
                    printf("\nSuccess! Withdrawn Rs. %.2f\nRemaining balance: Rs. %.2f\n", amount, acc.balance);
                } else {
                    printf("Error: Insufficient balance!\n");
                }
                break;
            }
        }
    }
    fclose(file);

    if (!found) {
        printf("Error: Account %d not found.\n", acc_no);
    } else if (!pin_ok) {
        printf("Error: Invalid PIN!\n");
    }
}

void transfer_money(void) {
    int from_acc, to_acc, pin;
    float amount;

    printf("\n=== Transfer Money ===\n");
    printf("From account number: ");
    scanf("%d", &from_acc);
    clear_input_buffer();
    printf("Enter PIN: ");
    scanf("%d", &pin);
    clear_input_buffer();
    printf("To account number  : ");
    scanf("%d", &to_acc);
    clear_input_buffer();
    printf("Transfer amount    : ");
    scanf("%f", &amount);
    clear_input_buffer();

    if (amount <= 0.0f) {
        printf("Error: Amount must be positive!\n");
        return;
    }

    /* Check if target account exists */
    bool to_exists = false;
    FILE *check = fopen(ACCOUNT_FILE, "rb");
    if (check) {
        Account tmp;
        while (fread(&tmp, sizeof(tmp), 1, check) == 1) {
            if (tmp.acc_no == to_acc) {
                to_exists = true;
                break;
            }
        }
        fclose(check);
    }
    if (!to_exists) {
        printf("Error: Target account %d not found.\n", to_acc);
        return;
    }

    /* Process withdrawal from source */
    FILE *file = fopen(ACCOUNT_FILE, "rb+");
    if (file == NULL) return;

    Account acc;
    bool from_found = false;
    bool pin_ok = false;
    bool withdrawn = false;

    while (fread(&acc, sizeof(acc), 1, file) == 1) {
        if (acc.acc_no == from_acc) {
            from_found = true;
            if (acc.pin == pin) {
                pin_ok = true;
                if (acc.balance >= amount) {
                    acc.balance -= amount;
                    fseek(file, -(long)sizeof(acc), SEEK_CUR);
                    fwrite(&acc, sizeof(acc), 1, file);
                    log_transaction(from_acc, "Transfer Out", amount, acc.balance);
                    withdrawn = true;
                } else {
                    printf("Error: Insufficient balance in source account!\n");
                    fclose(file);
                    return;
                }
                break;
            }
        }
    }
    fclose(file);

    if (!from_found) {
        printf("Error: Source account not found.\n");
        return;
    }
    if (!pin_ok) {
        printf("Error: Invalid PIN!\n");
        return;
    }
    if (!withdrawn) return;

    /* Deposit into target */
    FILE *file2 = fopen(ACCOUNT_FILE, "rb+");
    if (file2 == NULL) return;
    bool deposited = false;
    while (fread(&acc, sizeof(acc), 1, file2) == 1) {
        if (acc.acc_no == to_acc) {
            acc.balance += amount;
            fseek(file2, -(long)sizeof(acc), SEEK_CUR);
            fwrite(&acc, sizeof(acc), 1, file2);
            log_transaction(to_acc, "Transfer In", amount, acc.balance);
            deposited = true;
            break;
        }
    }
    fclose(file2);

    if (deposited) {
        printf("\nSuccess! Rs. %.2f transferred from %d to %d\n", amount, from_acc, to_acc);
    }
}

void check_balance(void) {
    int acc_no, pin;

    printf("\n=== Check Balance ===\n");
    printf("Enter account number: ");
    scanf("%d", &acc_no);
    clear_input_buffer();
    printf("Enter PIN: ");
    scanf("%d", &pin);
    clear_input_buffer();

    FILE *file = fopen(ACCOUNT_FILE, "rb");
    if (file == NULL) {
        printf("Error: No accounts found!\n");
        return;
    }

    Account acc;
    bool found = false;
    bool pin_ok = false;

    while (fread(&acc, sizeof(acc), 1, file) == 1) {
        if (acc.acc_no == acc_no) {
            found = true;
            if (acc.pin == pin) {
                pin_ok = true;
                printf("\nCurrent balance for Account %d: Rs. %.2f\n", acc_no, acc.balance);
                break;
            }
        }
    }
    fclose(file);

    if (!found) {
        printf("Error: Account %d not found.\n", acc_no);
    } else if (!pin_ok) {
        printf("Error: Invalid PIN!\n");
    }
}

void view_transaction_history(void) {
    int acc_no, pin;

    printf("\n=== View Transaction History ===\n");
    printf("Enter account number: ");
    scanf("%d", &acc_no);
    clear_input_buffer();
    printf("Enter PIN: ");
    scanf("%d", &pin);
    clear_input_buffer();

    /* Verify account + PIN first */
    FILE *af = fopen(ACCOUNT_FILE, "rb");
    if (!af) {
        printf("No accounts exist yet!\n");
        return;
    }
    Account acc;
    bool valid = false;
    while (fread(&acc, sizeof(acc), 1, af) == 1) {
        if (acc.acc_no == acc_no && acc.pin == pin) {
            valid = true;
            break;
        }
    }
    fclose(af);
    if (!valid) {
        printf("Error: Invalid account or PIN!\n");
        return;
    }

    FILE *tf = fopen(TRANS_FILE, "rb");
    if (tf == NULL) {
        printf("No transactions recorded yet for any account.\n");
        return;
    }

    Transaction t;
    printf("\nTransaction History for Account %d:\n", acc_no);
    printf("---------------------------------------------------------------------\n");
    printf("| Date & Time         | Type           | Amount     | Balance After  |\n");
    printf("---------------------------------------------------------------------\n");
    bool has_trans = false;
    while (fread(&t, sizeof(t), 1, tf) == 1) {
        if (t.acc_no == acc_no) {
            has_trans = true;
            printf("| %-19s | %-14s | Rs.%-8.2f | Rs.%-11.2f |\n",
                   t.datetime, t.type, t.amount, t.balance_after);
        }
    }
    fclose(tf);

    if (!has_trans) {
        printf("| No transactions found for this account.                          |\n");
    }
    printf("---------------------------------------------------------------------\n");
}

void list_all_accounts(void) {
    FILE *file = fopen(ACCOUNT_FILE, "rb");
    if (file == NULL) {
        printf("No accounts created yet!\n");
        return;
    }

    Account acc;
    printf("\n=== All Accounts ===\n");
    printf("---------------------------------------------------------------\n");
    printf("| Acc No | Name                     | Balance        | PIN    |\n");
    printf("---------------------------------------------------------------\n");
    while (fread(&acc, sizeof(acc), 1, file) == 1) {
        printf("| %-6d | %-24s | Rs.%-11.2f | ****   |\n",
               acc.acc_no, acc.name, acc.balance);
    }
    fclose(file);
    printf("---------------------------------------------------------------\n");
}

void delete_account(void) {
    int acc_no, pin;

    printf("\n=== Delete Account ===\n");
    printf("Enter account number: ");
    scanf("%d", &acc_no);
    clear_input_buffer();
    printf("Enter PIN: ");
    scanf("%d", &pin);
    clear_input_buffer();

    FILE *file = fopen(ACCOUNT_FILE, "rb");
    if (file == NULL) {
        printf("No accounts to delete!\n");
        return;
    }
    FILE *temp = fopen("temp.dat", "wb");
    if (temp == NULL) {
        fclose(file);
        printf("Error creating temporary file!\n");
        return;
    }

    Account acc;
    bool deleted = false;
    while (fread(&acc, sizeof(acc), 1, file) == 1) {
        if (acc.acc_no == acc_no && acc.pin == pin) {
            deleted = true;
            continue;  /* skip this record */
        }
        fwrite(&acc, sizeof(acc), 1, temp);
    }
    fclose(file);
    fclose(temp);

    if (deleted) {
        remove(ACCOUNT_FILE);
        rename("temp.dat", ACCOUNT_FILE);
        printf("Account %d has been permanently deleted.\n", acc_no);
    } else {
        remove("temp.dat");
        printf("Error: Account not found or invalid PIN!\n");
    }
}

int main(void) {
    printf("============================================================\n");
    printf("          PROFESSIONAL BANK MANAGEMENT SYSTEM\n");
    printf("      (Portfolio-ready: PIN security + Transaction log)\n");
    printf("============================================================\n");

    while (1) {
        int choice;
        printf("\n1. Create New Account\n");
        printf("2. Deposit Money\n");
        printf("3. Withdraw Money\n");
        printf("4. Transfer Money\n");
        printf("5. Check Balance\n");
        printf("6. View Transaction History\n");
        printf("7. List All Accounts\n");
        printf("8. Delete Account\n");
        printf("9. Exit\n");
        printf("------------------------------------------------------------\n");
        printf("Enter choice: ");

        if (scanf("%d", &choice) != 1) {
            clear_input_buffer();
            printf("Invalid input! Please enter a number.\n");
            continue;
        }
        clear_input_buffer();

        switch (choice) {
            case 1: create_account(); break;
            case 2: deposit_money(); break;
            case 3: withdraw_money(); break;
            case 4: transfer_money(); break;
            case 5: check_balance(); break;
            case 6: view_transaction_history(); break;
            case 7: list_all_accounts(); break;
            case 8: delete_account(); break;
            case 9:
                printf("\nThank you for using the Professional Bank System.\n");
                printf("Goodbye!\n");
                return 0;
            default:
                printf("Invalid choice! Please select 1-9.\n");
        }
    }
}

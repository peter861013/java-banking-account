# Banking Account System

用 Java 練習物件導向設計的銀行帳戶系統：透過抽象類別、介面、多型與自訂例外，模擬四種有不同利息與手續費規則的帳戶。

## 帳戶類型

| 類別 | 計息方式 | 提款規則 | 存款規則 |
|---|---|---|---|
| `CheckingAccount` 支票帳戶 | 按日計息 | 無手續費，餘額不得低於 $1000 | 不限 |
| `SavingAccount` 儲蓄帳戶 | 按月計息 | 每月前 3 筆免費，之後每筆收 $1 | 不限 |
| `CDAccount` 定存帳戶 | 開戶後 12 個月內按月計息 | 期滿前提款收 $250 手續費 | 期滿前不可存款 |
| `LoanAccount` 貸款帳戶 | 按月計息 | 不可提款 | 不限（用來還款） |

所有帳戶的年利率預設為 12%。

## 架構

```
BasicAccount                 name(), balance()
├── WithdrawableAccount      withdraw()
├── DepositableAccount       deposit()
└── InterestableAccount      computeInterest()
        └── FullFunctionalAccount（繼承以上三個介面）

Account（抽象類別）
├── CheckingAccount
├── SavingAccount
├── CDAccount
└── LoanAccount
```

- `Account`：保存帳戶名稱、餘額、利率、開戶日與上次計息日，提供共用的 `deposit()`，並把 `withdraw(amount, date)` 與 `computeInterest(date)` 交給子類別實作。
- `BankingException`：透支、日期不合法、定存期間存款等違規操作會丟出這個例外。

## 檔案

- `Account.java`：介面、抽象類別 `Account`、四種帳戶與 `BankingException`
- `Application.java`：示範程式，把四種帳戶放進同一個 `Account[]` 陣列，以多型方式依序計息、提款、存款，並印出結果或例外訊息

## 執行方式

需要 JDK 8 以上。

```bash
javac Account.java Application.java
java Application
```

輸出範例（節錄）：

```
Account <John Smith> now has $1000.0 balance

EXCEPTION: Banking system throws a class BankingException with message:
	MESSAGE: Underfraft from checking account name:John Smith
	Account balance remains $1000.0
```

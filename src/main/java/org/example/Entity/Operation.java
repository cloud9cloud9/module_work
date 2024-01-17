package org.example.Entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "operation")
@ToString(exclude = {"fromAccount", "toAccount"})
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "id")
    private long id;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    @CsvBindByName(column = "operation_type")
    @CsvBindByPosition(position = 2)
    private OperationType operationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "expense_category")
    @CsvBindByName(column = "expense_category")
    @CsvBindByPosition(position = 3)
    private OperationType.ExpenseCategory expenseCategory;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "created_at")
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "amount")
    @CsvNumber(value = "#,##0.00")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    @CsvBindByName(column = "from_account_id")
    @CsvBindByPosition(position = 6)
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    @CsvBindByName(column = "to_account_id")
    @CsvBindByPosition(position = 7)
    private Account toAccount;

}

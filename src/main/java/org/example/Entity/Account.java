package org.example.Entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvIgnore;
import com.opencsv.bean.CsvNumber;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account")
@ToString(exclude = {"user", "operationList"})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "user_id")
    private User user;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "balance")
    @CsvNumber(value = "#,##0.00")
    private long balance;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fromAccount")
    @CsvIgnore
    private List<Operation> operationList;

}

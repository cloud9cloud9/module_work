package org.example.Entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@ToString(exclude = "accountList")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "id")
    private long id;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "user_name")
    private String userName;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "password")
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @CsvIgnore
    private List<Account> accountList;

}

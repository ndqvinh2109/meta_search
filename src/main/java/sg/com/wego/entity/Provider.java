package sg.com.wego.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PROVIDER")
public class Provider implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name = "code", unique = true, length = 10)
    private String code;

    @Column(name = "name")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

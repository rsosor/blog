package com.rsosor.app.model.entity;

import com.rsosor.app.model.enums.OptionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Setting entity.
 *
 * @author RsosoR
 * @date 2021/9/12
 */
@Data
@Entity
@Table(name = "options")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Option extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "custom-id")
    @GenericGenerator(name = "custom-id",
            strategy = "run.halo.app.model.entity.support.CustomIdGenerator")
    private Integer id;

    /**
     * option type
     */
    @Column(name = "type")
    @ColumnDefault("0")
    private OptionType type;

    /**
     * option key
     */
    @Column(name = "option_key", length = 100, nullable = false)
    private String key;

    /**
     * option value
     */
    @Column(name = "option_value", nullable = false)
    @Lob
    private String value;

    public Option(String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    @Override
    public void prePersist() {
        super.prePersist();

        if (type == null) {
            type = OptionType.INTERNAL;
        }
    }
}

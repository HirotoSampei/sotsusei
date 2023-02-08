package jp.te4a.spring.boot.sotsusei.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ng_words")
@Data
public class NGWordBean {
    @Id
    Integer ng_word_id;
    String ng_word;
    
}

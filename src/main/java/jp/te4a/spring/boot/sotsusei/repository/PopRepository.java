package jp.te4a.spring.boot.sotsusei.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.PopBean;

public interface PopRepository extends JpaRepository<PopBean, Integer>, JpaSpecificationExecutor<PopBean>{
    
    @Query(value="SELECT user_id, REPLACE(REPLACE(note, CHAR(13), ''), CHAR(10), '') as note FROM users WHERE user_id = ?1",nativeQuery=true)
	PopBean findByUser(Integer user_id);
    
}

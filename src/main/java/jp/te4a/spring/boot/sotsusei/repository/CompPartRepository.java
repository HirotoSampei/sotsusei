package jp.te4a.spring.boot.sotsusei.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import javax.transaction.Transactional;

import jp.te4a.spring.boot.sotsusei.bean.CompPartBean;
import jp.te4a.spring.boot.sotsusei.bean.CompPartPrimaryKey;

public interface CompPartRepository extends JpaRepository<CompPartBean, CompPartPrimaryKey>{

    @Query("SELECT X FROM CompPartBean X WHERE X.comp_id = ?1")
	List<CompPartBean> findByComp_id(Integer comp_id);

    @Query("SELECT X.comp_id FROM CompPartBean X WHERE X.user_id = ?1")
	List<Integer> findByComp_idToUser_id(Integer user_id);

    @Query("SELECT X.user_id FROM CompPartBean X WHERE X.comp_id = ?1")
	List<Integer> findByUser_id(Integer comp_id);

    @Query("SELECT X.nickname FROM CompPartBean X WHERE X.comp_id = ?1 and X.user_id = ?2")
	String findByNickname(Integer comp_id, Integer user_id);

    @Query("SELECT COUNT(*) FROM CompPartBean X WHERE X.comp_id = ?1")
	Integer countByComp_id(Integer comp_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM CompPartBean X WHERE X.user_id = ?1")
    void deleteByuser_id(Integer user_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM CompPartBean X WHERE X.comp_id = ?1")
    void deleteBycomp_id(Integer comp_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM CompPartBean X WHERE X.user_id = ?1 and X.comp_id = ?2")
    void deleteByuser(Integer user_id, Integer comp_id);
}

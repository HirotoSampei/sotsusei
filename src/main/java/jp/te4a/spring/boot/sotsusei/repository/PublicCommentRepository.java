package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import jp.te4a.spring.boot.sotsusei.bean.PublicCommentBean;
import jp.te4a.spring.boot.sotsusei.bean.PublicCommentPrimaryKey;

public interface PublicCommentRepository extends JpaRepository<PublicCommentBean, PublicCommentPrimaryKey>{

    @Query("SELECT X FROM PublicCommentBean X WHERE X.comp_id = ?1 ORDER BY X.commented_date")
	List<PublicCommentBean> findByComp_id(Integer comp_id);

    @Query("SELECT X.user_id FROM PublicCommentBean X WHERE X.comp_id = ?1")
	List<Integer> findUser_id(Integer comp_id);
    
}

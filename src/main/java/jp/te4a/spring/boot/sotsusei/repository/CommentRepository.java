package jp.te4a.spring.boot.sotsusei.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.CommentBean;
import jp.te4a.spring.boot.sotsusei.bean.CommentPrimaryKey;

public interface CommentRepository extends JpaRepository<CommentBean, CommentPrimaryKey>{

    @Query("SELECT X FROM CommentBean X WHERE X.comp_id = ?1 ORDER BY X.commented_date")
	List<CommentBean> findByComp_id(Integer comp_id);

    @Query("SELECT X.user_id FROM CommentBean X WHERE X.comp_id = ?1")
	List<Integer> findUser_id(Integer comp_id);
    
}
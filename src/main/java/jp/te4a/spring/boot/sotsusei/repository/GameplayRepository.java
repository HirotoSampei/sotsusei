package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;
import jp.te4a.spring.boot.sotsusei.bean.GameplayPrimaryKey;

public interface GameplayRepository extends JpaRepository<GameplayBean, GameplayPrimaryKey>{
    @Query("SELECT X FROM GameplayBean X WHERE X.user_id = ?1 ORDER BY X.user_id")
    List<GameplayBean> findAllByGame_id(Integer user_id);

    @Query("SELECT X FROM GameplayBean X WHERE X.user_id = ?1 ORDER BY X.user_id")
    GameplayPrimaryKey findAllByGameplayPrimaryKey(Integer user_id);
}

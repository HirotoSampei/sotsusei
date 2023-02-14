package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.NGWordBean;

public interface NGWordRepository extends JpaRepository<NGWordBean, Integer>{
  @Query("SELECT X.ng_word FROM NGWordBean X")
  List<String> findNGWords();
}

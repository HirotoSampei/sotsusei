package jp.te4a.spring.boot.sotsusei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.te4a.spring.boot.sotsusei.bean.GameplayPrimaryKey;
import jp.te4a.spring.boot.sotsusei.repository.GameplayRepository;
@Service
public class GameplayService {
    
    @Autowired
    GameplayRepository gameplayRepository;

    public void delete(GameplayPrimaryKey id) { gameplayRepository.deleteById(id); }
}
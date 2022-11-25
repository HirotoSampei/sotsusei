package jp.te4a.spring.boot.sotsusei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;
import jp.te4a.spring.boot.sotsusei.repository.GameplayRepository;
@Service
public class GameplayService {
    
    @Autowired
    GameplayRepository gameplayRepository;

    public GameplayBean create(GameplayBean gameplayBean) {
        gameplayRepository.save(gameplayBean);
        return gameplayBean;
    }
}
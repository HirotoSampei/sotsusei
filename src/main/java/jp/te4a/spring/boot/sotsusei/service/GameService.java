package jp.te4a.spring.boot.sotsusei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.te4a.spring.boot.sotsusei.repository.GameRepository;

@Service
public class GameService {
    
@Autowired
GameRepository gameRepository;


}

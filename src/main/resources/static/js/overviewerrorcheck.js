function nicknameCheck(nickname){
  var nick = nickname.value;
  if(nick.length > 50){
    nickname.setCustomValidity("ニックネームは50桁以内で入力してください");
  }else{
    nickname.setCustomValidity('');
  }
}
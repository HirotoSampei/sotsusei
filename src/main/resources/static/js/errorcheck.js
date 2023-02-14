function nameCheck(user_name){
  // 入力値取得
  var name = user_name.value;
  // エラーチェック
  if(name.length >= 50){
      user_name.setCustomValidity("名前は50桁以内で入力してください");
  }else{
      user_name.setCustomValidity('');
  }
}

function passwordCheck(password){
	// 入力値取得
	var pass = password.value;
	// エラーチェック
	if(pass.length >= 100){
		password.setCustomValidity("パスワードは100桁以内で入力してください");
	}else if(pass.length < 8){
		password.setCustomValidity('パスワードは8桁以上で入力してください');
	}else{
		password.setCustomValidity('');
	}
}
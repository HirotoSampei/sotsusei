function compnameCheck(comp_name){
  var comp = comp_name.value;
  if(comp.length > 50){
    comp_name.setCustomValidity("大会名は50桁以内で入力してください");
  }else{
    comp_name.setCustomValidity('');
  }
}

function descriptionCheck(description){
  var desc = description.value;
  if(desc.length > 2000){
    description.setCustomValidity("大会概要は2000桁以内で入力してください");
  }else{
    description.setCustomValidity('');
  }
}

function nicknameCheck(host_nickname){
  var nickname = host_nickname.value;
  if(nickname.length > 50){
    host_nickname.setCustomValidity("主催者名は50桁以内で入力してください");
  }else{
    host_nickname.setCustomValidity('');
  }
}

function startdateCheck(start_date){
  var start = start_date.value;
  if(start < dateToStr24HPad0(new Date(), 'YYYY-MM-DD hh:mm:ss')){
    start_date.setCustomValidity("開始日時は現在の日付以降で入力してください");
  }else{
    start_date.setCustomValidity('');
  }
}

function enddateCheck(end_date){
  var end = end_date.value;
  var start = document.getElementById('start_date').value;
  if(end < dateToStr24HPad0(new Date(), 'YYYY-MM-DD hh:mm:ss')){
    end_date.setCustomValidity("終了日時は現在の日付以降で入力してください");
  }else if(start > end){
    end_date.setCustomValidity("終了日時は開始日時以降で入力してください");
  }else{
    end_date.setCustomValidity('');
  }
}

function limitCheck(limit_of_participants){
  var limit = limit_of_participants.value;
  if(limit < 2){
    limit_of_participants.setCustomValidity("参加者上限は2人以上で入力してください");
  }else if(limit > 200){
    limit_of_participants.setCustomValidity("参加者上限は200人以下で入力してください");
  }else{
    limit_of_participants.setCustomValidity('');
  }
}

function deadlineCheck(deadline){
  var dead = deadline.value;
  var start = document.getElementById('start_date').value;
  if(dead < dateToStr24HPad0(new Date(), 'YYYY-MM-DD hh:mm:ss')){
    deadline.setCustomValidity("締め切り日時は現在の日付以降で入力してください");
  }else if(start < dead){
    deadline.setCustomValidity("締め切り日時は開始日時前で入力してください");
  }else{
    deadline.setCustomValidity('');
  }
}

function dateToStr24HPad0(date, format) {
        
  if (!format) {
      // デフォルト値
      format = 'YYYY-MM-DD hh:mm:ss'
  }
  
  // フォーマット文字列内のキーワードを日付に置換する
  format = format.replace(/YYYY/g, date.getFullYear());
  format = format.replace(/MM/g, ('0' + (date.getMonth() + 1)).slice(-2));
  format = format.replace(/DD/g, ('0' + date.getDate()).slice(-2));
  format = format.replace(/hh/g, ('0' + date.getHours()).slice(-2));
  format = format.replace(/mm/g, ('0' + date.getMinutes()).slice(-2));
  format = format.replace(/ss/g, ('0' + date.getSeconds()).slice(-2));
  
  return format;
}
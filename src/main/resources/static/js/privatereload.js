$(function(){
    function reload(){
      const comp_id = document.getElementById("comp_id_for_comment").value;
      $.ajax({
        url: '/comp/privatereload',
        type: "POST",
        data: {
          comp_id: comp_id,
          _csrf: $("*[name=_csrf]").val()
        },
        dataType : "text",
      })
      .done(function(data){
        const commentDataList = JSON.parse(data);
        let i = 0;
        $("#commentBox").replaceWith('<div class="chat" id="commentBox"> </div>');
        for(i = 0; i < commentDataList.length; i++){
          let divTag = $("<div />");
          $("#commentBox").append($('<label for="c1" class="comment-log" id="'+decodeURI(commentDataList[i].user_id)+'" onclick="selectfunc('+"'"+commentDataList[i].comment_date+"'"+","+"'"+commentDataList[i].user_id+"'"+')"></label>').text(decodeURI(commentDataList[i].nickname) + ":" + decodeURI(commentDataList[i].comment)));
          $('#commentBox').append($('<input type="checkbox" id="c1">'));
          $('#commentBox').append($('<button class="delete hide" id="'+decodeURI(commentDataList[i].comment_date)+'" onclick="deletefunc('+"'"+commentDataList[i].comment_date+"'"+')">削除</button>'));
          $('#commentBox').append(divTag);
        }
       
      })
      .fail(function(XMLHttpRequest, status, e){
        alert(XMLHttpRequest,status,e);
      });
    };
  

  $(function(){
    setInterval(function(){
      reload();
    },10000);
  });

});
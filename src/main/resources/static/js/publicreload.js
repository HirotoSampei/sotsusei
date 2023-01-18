$(function(){
    function reload(){
      const comp_id = document.getElementById("comp_id_for_comment").value;
      $.ajax({
        url: '/comp/publicreload',
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
          $("#commentBox").append($('<label class="comment-log"></label>').text(decodeURI(commentDataList[i].user_name) + ":" + decodeURI(commentDataList[i].comment)));
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
    },5000);
  });

});